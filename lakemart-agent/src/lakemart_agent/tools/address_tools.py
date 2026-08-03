"""收货地址相关工具"""

from langchain.tools import tool
from loguru import logger

from ..core.http_client import LakemartClient

client = LakemartClient()


@tool
async def get_address_list() -> str:
    """
    获取当前用户的收货地址列表。
    当用户问"我的地址"、"收货地址"、"有哪些地址"时，应该调用此工具。
    """
    try:
        data = await client.get("/api/address/list")

        if data.get("code") != 0:
            return f"获取地址列表失败：{data.get('message', '未知错误')}"

        addresses = data.get("data", [])
        if not addresses:
            return "您还没有添加收货地址，请先添加一个地址。"

        lines = ["📍 **您的收货地址**："]
        for addr in addresses:
            is_default = "⭐ 默认" if addr.get("isDefault") else ""
            name = addr.get("receiverName", "未知")
            phone = addr.get("receiverPhone", "无")
            province = addr.get("province", "")
            city = addr.get("city", "")
            district = addr.get("district", "")
            detail = addr.get("detailAddress", "")
            full_address = f"{province}{city}{district}{detail}"
            addr_id = addr.get("id")
            lines.append(f"• [{addr_id}] {name} {phone} | {full_address} {is_default}".strip())

        return "\n".join(lines)

    except Exception as e:
        logger.error(f"获取地址列表失败: {e}")
        return "获取地址列表失败，请稍后重试。"


@tool
async def get_default_address() -> str:
    """
    获取当前用户的默认收货地址。
    当用户问"默认地址"、"常用地址"时，应该调用此工具。
    """
    try:
        data = await client.get("/api/address/default")

        if data.get("code") != 0:
            return "您还没有设置默认地址。"

        addr = data.get("data")
        if not addr:
            return "您还没有设置默认地址。"

        name = addr.get("receiverName", "未知")
        phone = addr.get("receiverPhone", "无")
        province = addr.get("province", "")
        city = addr.get("city", "")
        district = addr.get("district", "")
        detail = addr.get("detailAddress", "")
        full_address = f"{province}{city}{district}{detail}"

        return f"📍 默认地址：{name} {phone} | {full_address}"

    except Exception as e:
        logger.error(f"获取默认地址失败: {e}")
        return "获取默认地址失败，请稍后重试。"