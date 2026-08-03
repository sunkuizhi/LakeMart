"""购物车相关工具"""

from langchain.tools import tool
from loguru import logger

from ..core.http_client import LakemartClient

client = LakemartClient()


@tool
async def get_cart_list() -> str:
    """
    获取当前用户的购物车列表。
    当用户问"我的购物车"、"购物车有哪些商品"、"看看购物车"时，应该调用此工具。
    """
    try:
        data = await client.get("/api/cart/list")

        if data.get("code") != 0:
            return f"获取购物车失败：{data.get('message', '未知错误')}"

        items = data.get("data", [])
        if not items:
            return "您的购物车是空的，快去逛逛吧！🛒"

        lines = ["🛒 **您的购物车**："]
        total = 0
        for item in items:
            product_name = item.get("productName", "未知商品")
            price = item.get("price", 0)
            quantity = item.get("quantity", 0)
            subtotal = price * quantity
            total += subtotal
            lines.append(f"• {product_name} x {quantity} = ¥{subtotal:.2f}")

        lines.append(f"\n💰 **合计：¥{total:.2f}**")
        return "\n".join(lines)

    except Exception as e:
        logger.error(f"获取购物车失败: {e}")
        return "获取购物车失败，请稍后重试。"


@tool
async def add_to_cart(product_id: int, quantity: int = 1) -> str:
    """
    将商品添加到购物车。
    当用户说"加入购物车"、"买这个"、"加购"时，应该调用此工具。

    Args:
        product_id: 商品ID
        quantity: 数量，默认为1
    """
    try:
        payload = {
            "productId": product_id,
            "quantity": quantity
        }
        data = await client.post("/api/cart/add", data=payload)

        if data.get("code") == 0:
            return f"✅ 已成功将商品 {product_id} 加入购物车，数量：{quantity}。"
        else:
            return f"加入购物车失败：{data.get('message', '未知错误')}"

    except Exception as e:
        logger.error(f"加入购物车失败: {e}")
        return "加入购物车失败，请稍后重试。"


@tool
async def remove_from_cart(cart_item_id: int) -> str:
    """
    从购物车中移除商品。
    当用户说"删除购物车中的xxx"、"移除xxx"时，应该调用此工具。

    Args:
        cart_item_id: 购物车项ID（不是商品ID）
    """
    try:
        data = await client.delete(f"/api/cart/remove/{cart_item_id}")

        if data.get("code") == 0:
            return f"✅ 已从购物车中移除该商品。"
        else:
            return f"移除失败：{data.get('message', '未知错误')}"

    except Exception as e:
        logger.error(f"移除购物车商品失败: {e}")
        return "移除失败，请稍后重试。"


@tool
async def clear_cart() -> str:
    """
    清空购物车。
    当用户说"清空购物车"、"全部删除"时，应该调用此工具。
    注意：此操作不可撤销，需要用户明确确认。
    """
    try:
        data = await client.delete("/api/cart/clear")

        if data.get("code") == 0:
            return "✅ 购物车已清空。"
        else:
            return f"清空购物车失败：{data.get('message', '未知错误')}"

    except Exception as e:
        logger.error(f"清空购物车失败: {e}")
        return "清空购物车失败，请稍后重试。"