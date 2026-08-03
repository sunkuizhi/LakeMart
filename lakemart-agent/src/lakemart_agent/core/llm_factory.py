"""LLM 工厂 - 根据配置创建不同的 ChatModel 实例"""

from langchain_openai import ChatOpenAI
from langchain_core.language_models import BaseChatModel
from langchain_core.messages import AIMessage, HumanMessage, SystemMessage

from ..settings import settings


def get_llm() -> BaseChatModel:
    """
    根据 settings.llm_provider 返回对应的 LLM 实例。
    目前支持 'deepseek' 和 'openai'（使用 OpenAI 兼容接口）。
    """
    provider = settings.llm_provider.lower()

    if provider == "deepseek":
        if not settings.deepseek_api_key:
            raise ValueError("DEEPSEEK_API_KEY 未在 .env 中设置")
        return ChatOpenAI(
            model=settings.deepseek_model,
            api_key=settings.deepseek_api_key,
            base_url=settings.deepseek_base_url,
            temperature=0.3,
            max_tokens=4096,
        )
    elif provider == "openai":
        if not settings.openai_api_key:
            raise ValueError("OPENAI_API_KEY 未在 .env 中设置")
        return ChatOpenAI(
            model=settings.openai_model,
            api_key=settings.openai_api_key,
            temperature=0.3,
            max_tokens=4096,
        )
    else:
        raise ValueError(f"不支持的 LLM 提供商: {provider}")