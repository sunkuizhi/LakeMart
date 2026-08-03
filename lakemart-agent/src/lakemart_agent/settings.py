"""应用配置管理 - 使用 Pydantic Settings 读取 .env 文件"""

from pydantic_settings import BaseSettings
from typing import Optional


class Settings(BaseSettings):
    """应用配置类"""

    # 应用基础配置
    app_name: str = "lakemart-agent"
    app_host: str = "0.0.0.0"
    app_port: int = 8081
    log_level: str = "INFO"

    # 下游服务（Spring Boot 主后端）
    lakemart_server_url: str = "http://localhost:8080"

    # LLM 配置 (DeepSeek / OpenAI 兼容)
    llm_provider: str = "deepseek"  # 或 "openai"
    deepseek_api_key: Optional[str] = None
    deepseek_model: str = "deepseek-chat"
    deepseek_base_url: str = "https://api.deepseek.com/v1"

    # OpenAI 备选（如果用 OpenAI 的话）
    openai_api_key: Optional[str] = None
    openai_model: str = "gpt-4o-mini"

    # Redis 配置（用于对话记忆）
    redis_url: str = "redis://localhost:6379/0"

    # 会话配置
    session_ttl_seconds: int = 604800  # 7天

    class Config:
        # 指定 .env 文件路径（项目根目录）
        env_file = ".env"
        env_file_encoding = "utf-8"
        case_sensitive = False


# 创建全局单例配置对象
settings = Settings()