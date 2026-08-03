"""订单操作工具 - 取消订单、支付等"""

from langchain.tools import tool
from loguru import logger

from ..core.http_client import LakemartClient

client = LakemartClient()


@tool
async def cancel_order(order_no: str) -> str:
    """
    取消指定的订单。
    当用户明确说"取消订单"、"把我的xxx订单退掉"时，应该调用此工具。
    注意：只有处于"待支付"或"已支付"状态的订单才能取消。

    Args:
        order_no: 订单号，如 "2076559530468573184"
    """
    try:
        # 第一步：根据订单号查找对应的主键ID
        list_data = await client.get("/api/order/list", params={"pageNum": 1, "pageSize": 100})

        if list_data.get("code") != 0:
            return "获取订单列表失败"

        records = list_data.get("data", {}).get("records", [])
        matched = None
        for order in records:
            if order.get("orderNo") == order_no:
                matched = order
                break

        if not matched:
            return f"未找到订单 {order_no}，请确认订单号是否正确。"

        order_id = matched.get("id")
        status = matched.get("status")

        # 检查订单状态是否可取消
        if status == 5:  # 假设 5 表示已取消
            return f"订单 {order_no} 已经是取消状态，无需重复取消。"

        if status not in [0, 1]:  # 待支付(0) 或 已支付(1)
            status_desc = matched.get("statusDesc", "当前状态")
            return f"订单 {order_no} 当前状态为 '{status_desc}'，无法取消。只有待支付或已支付的订单才能取消。"

        # 第二步：调用取消接口
        data = await client.post("/api/order/cancel", data={"orderId": order_id})

        if data.get("code") == 0:
            return f"✅ 订单 {order_no} 已成功取消。"
        else:
            return f"取消订单失败：{data.get('message', '未知错误')}"

    except Exception as e:
        logger.error(f"取消订单失败: {e}")
        return f"取消订单失败，请稍后重试。"


@tool
async def pay_order(order_no: str) -> str:
    """
    支付指定的订单。
    当用户说"支付订单"、"付款"时，应该调用此工具。
    注意：只有"待支付"状态的订单才能支付。

    Args:
        order_no: 订单号，如 "2076559530468573184"
    """
    try:
        # 第一步：根据订单号查找对应的主键ID
        list_data = await client.get("/api/order/list", params={"pageNum": 1, "pageSize": 100})

        if list_data.get("code") != 0:
            return "获取订单列表失败"

        records = list_data.get("data", {}).get("records", [])
        matched = None
        for order in records:
            if order.get("orderNo") == order_no:
                matched = order
                break

        if not matched:
            return f"未找到订单 {order_no}，请确认订单号是否正确。"

        order_id = matched.get("id")
        status = matched.get("status")

        # 检查订单状态是否可支付
        if status == 0:  # 待支付
            # 调用支付接口
            data = await client.post("/api/order/pay", data={"orderId": order_id})
            if data.get("code") == 0:
                return f"✅ 订单 {order_no} 支付成功！"
            else:
                return f"支付失败：{data.get('message', '未知错误')}"
        else:
            status_desc = matched.get("statusDesc", "当前状态")
            return f"订单 {order_no} 当前状态为 '{status_desc}'，只有待支付的订单才能支付。"

    except Exception as e:
        logger.error(f"支付订单失败: {e}")
        return f"支付订单失败，请稍后重试。"