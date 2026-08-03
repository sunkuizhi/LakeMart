"""API 依赖注入 - 用于提取请求上下文"""
from fastapi import Header, HTTPException
from typing import Optional


async def get_current_user_token(authorization: Optional[str] = Header(None)) -> str:
    """
    从 Authorization 头中提取 JWT Token。
    前端传递格式：Bearer <token>
    """
    if not authorization:
        raise HTTPException(status_code=401, detail="未提供认证信息")

    parts = authorization.split()
    if len(parts) != 2 or parts[0].lower() != "bearer":
        raise HTTPException(status_code=401, detail="认证格式错误，应为 Bearer <token>")

    return parts[1]