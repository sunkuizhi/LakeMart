# 在 src/lakemart_agent/models/requests.py 中写入
from pydantic import BaseModel
from typing import Optional

class ChatRequest(BaseModel):
    session_id: Optional[str] = None
    query: str
    channel: str = "client"