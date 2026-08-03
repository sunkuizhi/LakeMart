FROM python:3.12-slim

WORKDIR /app

# 安装系统依赖（如果需要）
RUN apt-get update && apt-get install -y --no-install-recommends \
    curl \
    && rm -rf /var/lib/apt/lists/*

# 复制依赖文件并安装
COPY pyproject.toml ./
RUN pip install --no-cache-dir -e .

# 复制源代码
COPY src/ ./src/

# 暴露端口
EXPOSE 8081

# 启动命令
CMD ["uvicorn", "src.lakemart_agent.main:app", "--host", "0.0.0.0", "--port", "8081"]