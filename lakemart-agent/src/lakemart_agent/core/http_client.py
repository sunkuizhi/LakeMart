"""通用 HTTP 客户端 - 统一处理 JWT 透传和错误"""

import httpx
from typing import Optional, Any
from loguru import logger

from ..settings import settings
from .context import get_current_token


class LakemartClient:
    """LakeMart 后端 HTTP 客户端"""

    def __init__(self, timeout: float = 10.0):
        self.timeout = timeout
        self.base_url = settings.lakemart_server_url

    def _get_headers(self) -> dict:
        """获取带 JWT 的请求头"""
        token = get_current_token()
        if not token:
            return {}
        return {"Authorization": f"Bearer {token}"}

    async def get(self, path: str, params: Optional[dict] = None) -> dict:
        """发送 GET 请求"""
        url = f"{self.base_url}{path}"
        headers = self._get_headers()

        async with httpx.AsyncClient(timeout=self.timeout) as client:
            response = await client.get(url, headers=headers, params=params)
            response.raise_for_status()
            return response.json()

    async def post(self, path: str, data: Optional[dict] = None) -> dict:
        """发送 POST 请求"""
        url = f"{self.base_url}{path}"
        headers = self._get_headers()
        headers["Content-Type"] = "application/json"

        async with httpx.AsyncClient(timeout=self.timeout) as client:
            response = await client.post(url, headers=headers, json=data)
            response.raise_for_status()
            return response.json()

    async def put(self, path: str, data: Optional[dict] = None) -> dict:
        """发送 PUT 请求"""
        url = f"{self.base_url}{path}"
        headers = self._get_headers()
        headers["Content-Type"] = "application/json"

        async with httpx.AsyncClient(timeout=self.timeout) as client:
            response = await client.put(url, headers=headers, json=data)
            response.raise_for_status()
            return response.json()