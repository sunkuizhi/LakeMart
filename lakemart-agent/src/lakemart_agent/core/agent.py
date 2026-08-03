"""LangGraph Agent 构建 - 包含状态、节点、边和编译"""

from typing import Annotated, Literal
from langgraph.graph import StateGraph, MessagesState, END
from langgraph.prebuilt import ToolNode
from langgraph.checkpoint.redis.aio import AsyncRedisSaver
from langchain_core.messages import HumanMessage, AIMessage, SystemMessage
from langchain_core.tools import BaseTool
from redis import Redis
from loguru import logger

from .llm_factory import get_llm
from ..settings import settings
from ..tools.order_tools import query_order_status, list_my_orders
from ..tools.product_tools import search_products, get_product_detail
from ..tools.user_tools import get_user_profile, get_user_statistics
from ..tools.order_actions import cancel_order, pay_order
from ..tools.cart_tools import get_cart_list, add_to_cart, remove_from_cart, clear_cart
from ..tools.address_tools import get_address_list, get_default_address
from ..tools.points_tools import get_points_logs


# 定义扩展状态
class AgentState(MessagesState):
    pass


# 所有工具列表
tools: list[BaseTool] = [
    query_order_status,
    list_my_orders,
    search_products,
    get_product_detail,
    get_user_profile,
    get_user_statistics,
    get_cart_list,
    add_to_cart,
    remove_from_cart,
    clear_cart,
    get_address_list,
    get_default_address,
    get_points_logs,
    cancel_order,
    pay_order,
]

# 创建 LLM 并绑定工具
llm = get_llm()
llm_with_tools = llm.bind_tools(tools)

# 系统提示词（优化版）
# 系统提示词（完整版）
SYSTEM_PROMPT = """你是一个智能电商客服助手，名为 LakeMart 小助手。你的目标是准确、快速地解决用户的问题。

## 用户意图识别
在调用工具前，先判断用户意图：
- 如果用户提到"订单"、"物流"、"退货" → 优先使用订单相关工具。
- 如果用户提到"商品"、"搜索"、"多少钱" → 优先使用商品相关工具。
- 如果用户提到"我的"、"个人信息"、"积分" → 优先使用用户相关工具。
- 如果用户提到"购物车" → 优先使用购物车相关工具。
- 如果用户提到"地址" → 优先使用地址相关工具。

## 工具选择指南
**订单工具：**
- list_my_orders: 当用户询问"我的订单"、"有哪些订单"时调用。
- query_order_status: 当用户提供了具体订单号（如数字或"订单号xxx"）时调用。
- cancel_order: 只有用户明确说"取消订单"并确认订单号后才调用。
- pay_order: 只有用户明确说"支付"并确认订单号后才调用。

**商品工具：**
- search_products: 当用户提供关键词（如"篮球"、"手机"）时调用。
- get_product_detail: 当用户提供商品ID（如数字）或说"这个商品"时调用。

**用户工具：**
- get_user_profile: 当用户问"我的信息"、"我的等级"时调用。
- get_user_statistics: 当用户问"我花了多少钱"、"统计"时调用。
- get_points_logs: 当用户问"积分记录"时调用。

**购物车工具：**
- get_cart_list: 当用户问"购物车有什么"时调用。
- add_to_cart: 当用户说"加入购物车"时，需先确认商品ID和数量。
- remove_from_cart: 当用户说"移除"时，需确认购物车项ID。
- clear_cart: 必须用户明确说"清空"且二次确认后调用。

**地址工具：**
- get_address_list: 当用户问"地址列表"时调用。
- get_default_address: 当用户问"默认地址"时调用。

## 回答风格
- 回答要简洁、清晰，使用中文。
- 如果用户的问题模糊，先反问澄清（如"请问您要查询哪个订单？请提供订单号"）。
- 如果工具返回空数据或错误，友好告知用户并提供替代建议（如"未找到该订单，请检查订单号是否正确，或尝试其他订单"）。
- 禁止编造数据，所有信息必须来自工具返回。
- 在回复中适当使用 Markdown 格式（如 **粗体**、列表、表格）提升可读性。

## 示例
- 用户："我的订单" → 调用 list_my_orders，返回订单列表。
- 用户："查一下订单 12345" → 调用 query_order_status(12345)，返回详情。
- 用户："推荐篮球" → 调用 search_products("篮球")，返回商品列表。
"""

def call_model(state: AgentState):
    messages = state["messages"]
    system_msg = SystemMessage(content=SYSTEM_PROMPT)
    response = llm_with_tools.invoke([system_msg] + messages)
    return {"messages": [response]}


def should_continue(state: AgentState) -> Literal["tools", "__end__"]:
    last_message = state["messages"][-1]
    if hasattr(last_message, "tool_calls") and last_message.tool_calls:
        return "tools"
    return "__end__"


# 构建图
builder = StateGraph(AgentState)
builder.add_node("agent", call_model)
builder.add_node("tools", ToolNode(tools))
builder.set_entry_point("agent")
builder.add_conditional_edges("agent", should_continue)
builder.add_edge("tools", "agent")


# -------------------- Redis 持久化 --------------------
try:
    redis_client = Redis.from_url(settings.redis_url, decode_responses=False)
    checkpointer = AsyncRedisSaver(redis_client)
    checkpointer.setup()
    logger.info("✅ Redis 异步检查点已初始化，对话历史将持久化保存")
except Exception as e:
    logger.warning(f"⚠️ Redis 连接失败，使用内存存储: {e}")
    from langgraph.checkpoint.memory import MemorySaver
    checkpointer = MemorySaver()



agent_graph = builder.compile(
    checkpointer=checkpointer
)

logger.info(f"✅ LangGraph Agent 已编译完成，工具数: {len(tools)}")