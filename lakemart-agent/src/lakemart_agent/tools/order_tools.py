"""订单相关工具 - 通过 HTTP 调用 Spring Boot 后端"""

import httpx
from langchain.tools import tool
from loguru import logger
from typing import Optional

from ..settings import settings
from ..core.context import get_current_token


def _get_headers() -> dict:
    """获取带 JWT 的请求头"""
    token = get_current_token()
    if not token:
        return {}
    return {"Authorization": f"Bearer {token}"}


@tool
async def list_my_orders(page: int = 1, size: int = 10) -> str:
    """
    获取当前用户的订单列表。
    当用户询问"我的订单"、"有哪些订单"、"最近的订单"时，应该调用此工具。
    """
    token = get_current_token()
    if not token:
        return "无法获取您的登录信息，请先登录。"

    try:
        url = f"{settings.lakemart_server_url}/api/order/list"
        params = {"pageNum": page, "pageSize": size}
        headers = _get_headers()

        async with httpx.AsyncClient(timeout=10.0) as client:
            response = await client.get(url, headers=headers, params=params)
            response.raise_for_status()
            data = response.json()

        records = data.get("data", {}).get("records", [])
        if not records:
            return "您目前没有任何订单。"

        lines = ["📋 您的订单列表："]
        for order in records:
            order_id = order.get("id")
            order_no = order.get("orderNo")
            status_desc = order.get("statusDesc", "未知")
            total = order.get("totalAmount", 0)
            create_time = order.get("createTime", "").split("T")[0]
            lines.append(f"• 订单号: {order_no} (ID: {order_id}) | {status_desc} | ¥{total:.2f} | {create_time}")

        return "\n".join(lines)


    except httpx.HTTPStatusError as e:

        if e.response.status_code == 404:

            return f"未找到订单 {order_id}，请确认订单号是否正确，或尝试输入完整的订单号。"

        elif e.response.status_code == 401:

            return "您的登录已过期，请重新登录后再查询订单。"

        else:

            logger.error(f"调用订单接口失败: {e}")

            return f"查询订单 {order_id} 时服务暂时不可用，请稍后重试。"


@tool
async def query_order_status(order_id_input: str) -> str:
    """
    查询指定订单的详细状态。
    当用户输入了具体的订单号时，调用此工具。

    Args:
        order_id_input: 用户输入的订单号（字符串格式，如 "2076559530468573184"）
    """
    token = get_current_token()
    if not token:
        return "无法获取您的登录信息，请先登录。"

    try:
        # 第一步：获取订单列表，匹配对应的主键 ID
        list_url = f"{settings.lakemart_server_url}/api/order/list"
        params = {"pageNum": 1, "pageSize": 100}  # 最多 100 条
        headers = _get_headers()

        async with httpx.AsyncClient(timeout=10.0) as client:
            # 获取列表
            resp = await client.get(list_url, headers=headers, params=params)
            resp.raise_for_status()
            data = resp.json()

        records = data.get("data", {}).get("records", [])
        matched = None
        for order in records:
            # 匹配用户输入的订单号
            if order.get("orderNo") == order_id_input:
                matched = order
                break

        if not matched:
            return f"未找到订单号 {order_id_input}，请确认订单号是否正确。"

        # 第二步：用数据库主键查询详情
        detail_id = matched.get("id")
        detail_url = f"{settings.lakemart_server_url}/api/order/detail/{detail_id}"

        async with httpx.AsyncClient(timeout=10.0) as client:
            resp = await client.get(detail_url, headers=headers)
            resp.raise_for_status()
            detail_data = resp.json()

        order_data = detail_data.get("data", {})
        status_desc = order_data.get("statusDesc", "未知")
        total = order_data.get("totalAmount", 0)
        create_time = order_data.get("createTime", "")

        # 商品明细
        items = order_data.get("items", [])
        item_lines = []
        for item in items:
            name = item.get("productName", "商品")
            qty = item.get("quantity", 0)
            price = item.get("price", 0)
            item_lines.append(f"  • {name} x {qty} = ¥{price * qty:.2f}")

        result = f"订单 {order_id_input} 详情：\n"
        result += f"状态: {status_desc}\n"
        result += f"总金额: ¥{total:.2f}\n"
        result += f"下单时间: {create_time}\n"
        if item_lines:
            result += "商品清单:\n" + "\n".join(item_lines)

        return result

    except Exception as e:
        logger.error(f"查询订单详情失败: {e}")
        return f"查询订单 {order_id_input} 失败，请稍后重试。"