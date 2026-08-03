"""用户积分相关工具"""

from langchain.tools import tool
from loguru import logger

from ..core.http_client import LakemartClient

client = LakemartClient()


@tool
async def get_points_logs(page: int = 1, size: int = 10) -> str:
    """
    获取用户的积分变动记录。
    当用户问"我的积分明细"、"积分记录"、"积分怎么来的"时，应该调用此工具。

    Args:
        page: 页码，默认为1
        size: 每页数量，默认为10
    """
    try:
        data = await client.get("/api/user/points/logs", params={"pageNum": page, "pageSize": size})

        if data.get("code") != 0:
            return f"获取积分记录失败：{data.get('message', '未知错误')}"

        page_data = data.get("data", {})
        records = page_data.get("records", [])
        total = page_data.get("total", 0)

        if not records:
            return "您还没有积分记录。"

        lines = [f"📊 **积分记录**（共 {total} 条）："]
        for log in records:
            change = log.get("change", 0)
            balance = log.get("balance", 0)
            reason = log.get("reason", "")
            time = log.get("createTime", "")
            sign = "+" if change > 0 else ""
            lines.append(f"• {sign}{change} 分 | 余额: {balance} | {reason} | {time}")

        return "\n".join(lines)

    except Exception as e:
        logger.error(f"获取积分记录失败: {e}")
        return "获取积分记录失败，请稍后重试。"