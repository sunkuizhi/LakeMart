<template>
  <div class="chat-widget">
    <el-badge :value="unreadCount" :hidden="unreadCount === 0" class="chat-badge">
      <el-button
        class="chat-toggle-btn"
        :icon="isOpen ? ChatDotRound : ChatLineRound"
        circle
        size="large"
        @click="toggleChat"
      />
    </el-badge>

    <el-drawer
      v-model="isOpen"
      title="LakeMart 智能客服"
      direction="btt"
      size="65vh"
      :with-header="true"
      :close-on-click-modal="false"
      class="chat-drawer"
      @close="handleClose"
    >
      <div class="chat-container">
        <div class="message-list" ref="messageListRef">
          <div
            v-for="(msg, idx) in messages"
            :key="idx"
            class="message-item"
            :class="msg.role"
          >
            <div class="avatar">
              <el-avatar :size="36" :src="msg.role === 'user' ? userAvatar : botAvatar" />
            </div>
            <div class="content">
              <div class="bubble" v-html="formatMessage(msg.content)"></div>
              <div class="time">{{ msg.time }}</div>
            </div>
          </div>
          <div v-if="isLoading" class="message-item assistant">
            <div class="avatar">
              <el-avatar :size="36" :src="botAvatar" />
            </div>
            <div class="content">
              <div class="bubble typing-indicator">
                <span></span><span></span><span></span>
              </div>
            </div>
          </div>
        </div>

        <div class="input-area">
          <el-input
            v-model="inputText"
            placeholder="请输入您的问题..."
            @keyup.enter="sendMessage"
            :disabled="isLoading"
            clearable
            size="large"
          >
            <template #append>
              <el-button type="primary" @click="sendMessage" :loading="isLoading">
                发送
              </el-button>
            </template>
          </el-input>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import { ChatLineRound, ChatDotRound } from '@element-plus/icons-vue'
import { marked } from 'marked'

// 配置 marked 支持 GitHub 风格 Markdown（包括表格）
marked.setOptions({
  gfm: true,
  breaks: true,
})

// ---------- 状态 ----------
const isOpen = ref(false)
const inputText = ref('')
const isLoading = ref(false)
const messages = ref([])
const unreadCount = ref(0)
const sessionId = ref(null)
const messageListRef = ref(null)
let abortController = null

const userAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
const botAvatar = 'https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png'

// ---------- 工具函数 ----------
const getCurrentTime = () => {
  return new Date().toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
}

// 使用 marked 渲染 Markdown
const formatMessage = (text) => {
  if (!text) return ''
  return marked.parse(text)
}

// ---------- 核心：发送消息 ----------
const sendMessage = async () => {
  const query = inputText.value.trim()
  if (!query || isLoading.value) return
  inputText.value = ''

  messages.value.push({
    role: 'user',
    content: query,
    time: getCurrentTime()
  })
  scrollToBottom()

  isLoading.value = true
  unreadCount.value = 0

  const payload = {
    query: query,
    session_id: sessionId.value || undefined
  }

  const token = localStorage.getItem('token') || ''

  try {
    abortController = new AbortController()
    const response = await fetch('/api/v1/agent/chat/stream', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(payload),
      signal: abortController.signal
    })

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }

    const reader = response.body.getReader()
    const decoder = new TextDecoder()
    let buffer = ''
    let assistantMessage = { role: 'assistant', content: '', time: getCurrentTime() }
    let isFirstChunk = true

    while (true) {
      const { done, value } = await reader.read()
      if (done) break

      buffer += decoder.decode(value, { stream: true })
      const chunks = buffer.split('\n\n')
      buffer = chunks.pop() || ''

      for (const chunk of chunks) {
        if (!chunk.trim()) continue
        const lines = chunk.split('\n')
        let eventType = null
        let dataStr = null
        for (const line of lines) {
          if (line.startsWith('event: ')) {
            eventType = line.slice(7).trim()
          } else if (line.startsWith('data: ')) {
            dataStr = line.slice(6)
          }
        }
        if (!eventType || !dataStr) continue

        try {
          const data = JSON.parse(dataStr)
          if (eventType === 'session') {
            if (data.session_id) {
              sessionId.value = data.session_id
              localStorage.setItem('chatSessionId', data.session_id)
            }
          } else if (eventType === 'text') {
            if (data.delta) {
              if (isFirstChunk) {
                assistantMessage.time = getCurrentTime()
                isFirstChunk = false
              }
              assistantMessage.content += data.delta
              const lastIdx = messages.value.length - 1
              if (messages.value[lastIdx]?.role === 'assistant') {
                messages.value[lastIdx] = { ...assistantMessage }
              } else {
                messages.value.push({ ...assistantMessage })
              }
              scrollToBottom()
            }
            if (data.done) {
              // 消息结束
            }
          } else if (eventType === 'end') {
            isLoading.value = false
            if (!assistantMessage.content) {
              messages.value.push({
                role: 'assistant',
                content: '抱歉，我没有获取到有效回复。',
                time: getCurrentTime()
              })
            }
            scrollToBottom()
          } else if (eventType === 'error') {
            isLoading.value = false
            ElMessage.error(data.message || '服务异常，请稍后重试')
            messages.value.push({
              role: 'assistant',
              content: '服务异常，请稍后重试。',
              time: getCurrentTime()
            })
            scrollToBottom()
          }
        } catch (e) {
          console.warn('解析 SSE 数据失败:', e, dataStr)
        }
      }
    }
  } catch (error) {
    if (error.name === 'AbortError') {
      console.log('请求被取消')
    } else {
      console.error('发送消息失败:', error)
      ElMessage.error('网络异常，请稍后重试')
    }
    isLoading.value = false
  } finally {
    abortController = null
    if (!isLoading.value) {
      isLoading.value = false
      scrollToBottom()
    }
  }
}

// ---------- UI 辅助 ----------
const scrollToBottom = () => {
  nextTick(() => {
    if (messageListRef.value) {
      messageListRef.value.scrollTop = messageListRef.value.scrollHeight
    }
  })
}

const toggleChat = () => {
  isOpen.value = !isOpen.value
  if (isOpen.value) {
    unreadCount.value = 0
  }
}

const handleClose = () => {
  if (abortController) {
    abortController.abort()
    abortController = null
  }
  isLoading.value = false
}

onMounted(() => {
  const saved = localStorage.getItem('chatSessionId')
  if (saved) sessionId.value = saved
})

onUnmounted(() => {
  if (abortController) {
    abortController.abort()
    abortController = null
  }
})
</script>

<style scoped>
/* ---------- 悬浮按钮 ---------- */
.chat-widget {
  position: fixed;
  bottom: 30px;
  right: 30px;
  z-index: 9999;
}
.chat-toggle-btn {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: linear-gradient(135deg, #409EFF, #66b1ff);
  color: #fff;
  font-size: 28px;
  box-shadow: 0 4px 16px rgba(64, 158, 255, 0.4);
  transition: transform 0.3s, box-shadow 0.3s;
}
.chat-toggle-btn:hover {
  transform: scale(1.05);
  box-shadow: 0 6px 24px rgba(64, 158, 255, 0.6);
}
.chat-badge {
  display: inline-block;
}

/* ---------- 抽屉 ---------- */
.chat-drawer :deep(.el-drawer__header) {
  margin-bottom: 0;
  padding: 16px 24px;
  border-bottom: 1px solid #ebeef5;
  background: #f8faff;
}
.chat-drawer :deep(.el-drawer__body) {
  padding: 0;
  display: flex;
  flex-direction: column;
  height: 100%;
}

/* ---------- 聊天容器 ---------- */
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: #f0f2f5;
}
.message-list {
  flex: 1;
  overflow-y: auto;
  padding: 20px 24px;
  background: #f0f2f5;
}

/* ---------- 消息项 ---------- */
.message-item {
  display: flex;
  margin-bottom: 20px;
  animation: fadeInUp 0.3s ease;
}
.message-item.user {
  flex-direction: row-reverse;
}
.message-item .avatar {
  flex-shrink: 0;
}
.message-item.user .avatar {
  margin-left: 14px;
}
.message-item.assistant .avatar {
  margin-right: 14px;
}
.message-item .content {
  max-width: 78%;
  display: flex;
  flex-direction: column;
}
.message-item.user .content {
  align-items: flex-end;
}
.message-item .bubble {
  padding: 12px 18px;
  border-radius: 16px;
  font-size: 15px;
  line-height: 1.7;
  word-break: break-word;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  background: #ffffff;
  color: #303133;
}
.message-item.user .bubble {
  background: linear-gradient(135deg, #409EFF, #66b1ff);
  color: #fff;
  border-bottom-right-radius: 4px;
}
.message-item.assistant .bubble {
  background: #ffffff;
  border-bottom-left-radius: 4px;
}
.message-item .time {
  font-size: 12px;
  color: #909399;
  margin-top: 6px;
  padding: 0 6px;
}
.message-item.user .time {
  text-align: right;
}

/* ---------- 打字指示器 ---------- */
.typing-indicator {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 18px;
}
.typing-indicator span {
  display: inline-block;
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background: #909399;
  animation: typing 1.4s infinite both;
}
.typing-indicator span:nth-child(2) {
  animation-delay: 0.2s;
}
.typing-indicator span:nth-child(3) {
  animation-delay: 0.4s;
}
@keyframes typing {
  0%, 60%, 100% { transform: scale(1); opacity: 0.4; }
  30% { transform: scale(1.3); opacity: 1; }
}

/* ---------- 输入区 ---------- */
.input-area {
  padding: 16px 24px 20px;
  background: #fff;
  border-top: 1px solid #ebeef5;
  display: flex;
  gap: 12px;
  align-items: center;
}
.input-area .el-input {
  flex: 1;
}

/* ---------- 动画 ---------- */
@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(12px); }
  to { opacity: 1; transform: translateY(0); }
}

/* ---------- 消息内样式 ---------- */
.bubble :deep(strong) {
  font-weight: 600;
  color: #303133;
}
.bubble :deep(em) {
  font-style: italic;
}
.bubble :deep(ul) {
  margin: 8px 0 8px 20px;
  padding-left: 0;
  list-style: none;
}
.bubble :deep(li) {
  position: relative;
  padding-left: 20px;
  margin-bottom: 4px;
}
.bubble :deep(li::before) {
  content: "•";
  position: absolute;
  left: 0;
  color: #409EFF;
  font-weight: bold;
}
.bubble :deep(br) {
  display: block;
  content: "";
  margin: 6px 0;
}

.bubble :deep(table) {
  border-collapse: collapse;
  width: 100%;
  margin: 8px 0;
  font-size: 14px;
}
.bubble :deep(th),
.bubble :deep(td) {
  border: 1px solid #ddd;
  padding: 6px 10px;
  text-align: left;
}
.bubble :deep(th) {
  background-color: #f5f7fa;
  font-weight: 600;
}
.bubble :deep(tr:nth-child(even)) {
  background-color: #fafafa;
}
</style>
