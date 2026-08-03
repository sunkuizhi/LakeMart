"""商品相关工具 - 搜索商品、查看详情"""

from langchain.tools import tool
from loguru import logger

from ..core.http_client import LakemartClient

client = LakemartClient()


@tool
async def search_products(keyword: str, page: int = 1, size: int = 10) -> str:
    """
    根据关键词搜索商品。
    当用户询问"有没有xxx"、"想找xxx"、"推荐xxx"时，应该调用此工具。

    Args:
        keyword: 搜索关键词，如"篮球"、"运动鞋"、"手机"
        page: 页码，默认为1
        size: 每页数量，默认为10
    """
    try:
        # 构建请求体（与后端 ProductQueryDTO 匹配）
        payload = {
            "keyword": keyword,
            "pageNum": page,
            "pageSize": size
        }
        data = await client.post("/api/product/list", data=payload)

        if data.get("code") != 0:
            return f"搜索失败：{data.get('message', '未知错误')}"

        records = data.get("data", {}).get("records", [])
        total = data.get("data", {}).get("total", 0)
        if not records:
            return f"没有找到与 '{keyword}' 相关的商品，试试其他关键词吧。"

        lines = [f"🔍 找到 {total} 个与 '{keyword}' 相关的商品（显示前 {len(records)} 个）："]
        for p in records:
            name = p.get("name", "未知商品")
            price = p.get("price", 0)
            stock = p.get("stock", 0)
            product_id = p.get("id", "无")
            lines.append(f"• {name} (ID: {product_id}) | ¥{price:.2f} | 库存: {stock}")

        return "\n".join(lines)

    except Exception as e:
        logger.error(f"搜索商品失败: {e}")
        return f"搜索商品失败，请稍后重试。"


@tool
async def get_product_detail(product_id: int) -> str:
    """
    获取商品详情。
    当用户问"商品xxx怎么样"、"xxx的详情"、"xxx多少钱"时，应该调用此工具。

    Args:
        product_id: 商品ID，如 761
    """
    try:
        data = await client.get(f"/api/product/detail/{product_id}")

        if data.get("code") != 0:
            return f"未找到商品 ID: {product_id}，可能已下架或不存在。"

        product = data.get("data", {})
        name = product.get("name", "未知商品")
        price = product.get("price", 0)
        stock = product.get("stock", 0)
        category_name = product.get("categoryName", "未分类")
        description = product.get("description", "暂无描述")
        sales_count = product.get("salesCount", 0)

        return f"""
📦 **{name}**
- 价格: ¥{price:.2f}
- 库存: {stock}
- 分类: {category_name}
- 销量: {sales_count}
- 描述: {description}
"""

    except Exception as e:
        logger.error(f"获取商品详情失败: {e}")
        return f"获取商品详情失败，请稍后重试。"