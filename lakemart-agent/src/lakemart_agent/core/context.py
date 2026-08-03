"""请求上下文 - 用于在工具中传递用户信息"""
from contextvars import ContextVar
from typing import Optional

# 存储当前请求的用户 JWT
current_token: ContextVar[Optional[str]] = ContextVar("current_token", default=None)
current_user_id: ContextVar[Optional[int]] = ContextVar("current_user_id", default=None)

def set_current_token(token: str):
    """设置当前请求的 JWT"""
    current_token.set(token)

def get_current_token() -> Optional[str]:
    """获取当前请求的 JWT"""
    return current_token.get()