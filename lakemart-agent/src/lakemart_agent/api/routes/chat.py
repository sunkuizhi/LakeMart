"""对话路由 - 提供 SSE 流式聊天接口"""

import json
import uuid
import asyncio
from fastapi import APIRouter, Depends
from fastapi.responses import StreamingResponse
from langchain_core.messages import HumanMessage, AIMessage
from loguru import logger

from ...core.agent import agent_graph
from ...core.context import set_current_token
from ...models.requests import ChatRequest
from ..dependencies import get_current_user_token

router = APIRouter()


@router.post("/chat/stream")
async def chat_stream(
    request: ChatRequest,
    token: str = Depends(get_current_user_token)
):
    """流式对话接口，带用户认证和逐字输出"""
    session_id = request.session_id or str(uuid.uuid4())
    config = {"configurable": {"thread_id": session_id}}

    async def event_generator():
        yield f"event: thinking\ndata: {json.dumps({'content': '正在思考...'})}\n\n"
        set_current_token(token)
        yield f"event: session\ndata: {json.dumps({'session_id': session_id})}\n\n"

        try:
            final_content = None
            # 流式执行 Agent
            async for event in agent_graph.astream(
                {"messages": [HumanMessage(content=request.query)]},
                config=config,
                stream_mode="values"
            ):
                if "messages" in event and event["messages"]:
                    last_msg = event["messages"][-1]
                    if isinstance(last_msg, AIMessage) and last_msg.content:
                        final_content = last_msg.content

            if not final_content:
                yield f"event: text\ndata: {json.dumps({'delta': '抱歉，我没有获取到有效回复。', 'done': True})}\n\n"
                yield f"event: end\ndata: {json.dumps({'status': 'completed'})}\n\n"
                return

            # 逐字输出（打字机效果）
            # 按字符分割，每 50ms 发送一个字符（可调整速度）
            delay = 0.05  # 50ms/字符
            for i, char in enumerate(final_content):
                is_last = (i == len(final_content) - 1)
                yield f"event: text\ndata: {json.dumps({'delta': char, 'done': is_last})}\n\n"
                await asyncio.sleep(delay)

            # 发送结束事件
            yield f"event: end\ndata: {json.dumps({'status': 'completed'})}\n\n"

        except Exception as e:
            import traceback
            logger.error(f"Agent 执行失败: {traceback.format_exc()}")
            yield f"event: error\ndata: {json.dumps({'code': 5001, 'message': f'服务处理异常：{str(e)}'})}\n\n"

    return StreamingResponse(event_generator(), media_type="text/event-stream")