"""FastAPI 应用入口 - 启动智能客服服务"""

from contextlib import asynccontextmanager
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from loguru import logger

from .settings import settings


@asynccontextmanager
async def lifespan(app: FastAPI):
    logger.info(f"🚀 启动 {settings.app_name} 服务...")
    logger.info(f"📡 监听地址: http://{settings.app_host}:{settings.app_port}")
    logger.info(f"🔗 下游后端: {settings.lakemart_server_url}")
    yield
    logger.info("👋 服务正在优雅关闭...")


# 1. 创建 FastAPI 实例（必须先创建）
app = FastAPI(
    title="LakeMart AI Agent",
    description="智能客服助手 - 基于 LangGraph 的电商对话系统",
    version="0.1.0",
    lifespan=lifespan,
)

# 2. 注册路由（在 app 创建之后）
from .api.routes import chat
app.include_router(chat.router, prefix="/api/v1/agent", tags=["Chat"])

# 3. 添加 CORS 中间件
app.add_middleware(
    CORSMiddleware,
    allow_origins=[
        "http://localhost:5173",
        "http://localhost:5174",
        "http://127.0.0.1:5173",
        "http://127.0.0.1:5174",
    ],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


# 4. 健康检查接口
@app.get("/health")
async def health_check():
    return {"status": "ok", "service": settings.app_name}


@app.get("/")
async def root():
    return {
        "message": f"Welcome to {settings.app_name}",
        "docs": "/docs",
        "health": "/health"
    }


logger.info("✅ 应用配置加载完成")