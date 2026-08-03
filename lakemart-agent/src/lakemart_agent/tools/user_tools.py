"""用户相关工具 - 获取个人信息、积分、等级等"""

from langchain.tools import tool
from loguru import logger

from ..core.http_client import LakemartClient

client = LakemartClient()


@tool
async def get_user_profile() -> str:
    """
    获取当前用户的基本信息和画像。
    当用户问"我的信息"、"我的等级"、"我的积分"、"我是谁"时，应该调用此工具。
    """
    try:
        data = await client.get("/api/client/user/profile")

        if data.get("code") != 0:
            return f"获取用户信息失败：{data.get('message', '未知错误')}"

        user = data.get("data", {})
        username = user.get("username", "未知")
        email = user.get("email", "未绑定")
        phone = user.get("phone", "未绑定")
        points = user.get("points", 0)
        role = user.get("role", "普通用户")
        status = user.get("status", 1)

        # 用户等级映射
        level_map = {
            0: "普通用户",
            1: "青铜会员",
            2: "白银会员",
            3: "黄金会员",
            4: "钻石会员"
        }
        level = level_map.get(user.get("level", 0), "普通用户")

        return f"""
👤 **用户信息**
- 用户名: {username}
- 邮箱: {email}
- 电话: {phone}
- 角色: {role}
- 等级: {level}
- 积分: {points}
- 状态: {'正常' if status == 1 else '异常'}
"""

    except Exception as e:
        logger.error(f"获取用户信息失败: {e}")
        return "获取用户信息失败，请稍后重试。"


@tool
async def get_user_statistics() -> str:
    """
    获取用户的统计信息（订单数、总消费等）。
    当用户问"我买了多少东西"、"我花了多少钱"、"我的订单统计"时，应该调用此工具。
    """
    try:
        # 假设后端有统计接口，如果没有可以用订单列表自己算
        data = await client.get("/api/order/list", params={"pageNum": 1, "pageSize": 100})

        if data.get("code") != 0:
            return "获取订单统计失败"

        orders = data.get("data", {}).get("records", [])
        total_orders = len(orders)
        total_spent = sum(o.get("totalAmount", 0) for o in orders)

        # 按状态统计
        status_stats = {}
        for o in orders:
            status = o.get("statusDesc", "未知")
            status_stats[status] = status_stats.get(status, 0) + 1

        stats_lines = ["📊 **您的订单统计**"]
        stats_lines.append(f"- 总订单数: {total_orders}")
        stats_lines.append(f"- 总消费金额: ¥{total_spent:.2f}")
        stats_lines.append("- 各状态分布:")
        for status, count in status_stats.items():
            stats_lines.append(f"  • {status}: {count} 单")

        return "\n".join(stats_lines)

    except Exception as e:
        logger.error(f"获取用户统计失败: {e}")
        return "获取用户统计失败，请稍后重试。"