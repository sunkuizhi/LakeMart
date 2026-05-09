# LakeMart 电商平台

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.13-brightgreen)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-3.5-4FC08D)](https://vuejs.org/)
[![Docker](https://img.shields.io/badge/Docker-20.10+-2496ED)](https://docker.com/)

LakeMart 是一个功能完整、前后端分离的电商系统，包含用户端（H5）和管理端（后台），支持商品管理、购物车、订单、积分、轮播图、数据可视化仪表盘等核心功能，并采用 Spring Boot 3 + Vue 3 + Docker 一键部署。

---

## 📋 目录

- [技术栈](#技术栈)
- [主要功能](#主要功能)
- [快速开始（Docker）](#快速开始docker)
- [本地开发运行](#本地开发运行)
- [项目结构](#项目结构)
- [环境变量配置](#环境变量配置)
- [部分页面截图](#部分页面截图)
- [贡献指南](#贡献指南)
- [许可证](#许可证)

---

## 🛠 技术栈

### 后端
- **Spring Boot 3.5.13** - 主框架
- **JDK 21** - 运行环境
- **MyBatis-Plus 3.5.13** - ORM 框架
- **MySQL 8** - 关系型数据库
- **Redis 7** - 缓存与验证码存储
- **MinIO** - 对象存储（商品图片、轮播图、头像）
- **JWT** - 身份认证
- **Spring Security** - 权限控制
- **Spring Mail** - 邮件发送（验证码）
- **SpringDoc OpenAPI** - API 文档

### 前端（管理端 & 用户端）
- **Vue 3** + **Vite** - 前端框架
- **TypeScript** - 类型安全
- **Element Plus** - UI 组件库
- **ECharts** - 数据图表
- **Axios** - HTTP 客户端
- **Pinia** - 状态管理
- **Vue Router** - 路由

### 部署
- **Docker** + **Docker Compose** - 容器化一键启动

---

## ✨ 主要功能

### 管理端
- 登录 / 退出（JWT，角色区分）
- 商品管理（CRUD、上下架、MinIO 图片上传）
- 订单管理（列表、状态筛选、发货、完成、取消）
- 用户管理（列表、禁用/启用、重置密码、手动调整积分）
- 分类管理（树形结构、增删改查、状态切换）
- 轮播图管理（CRUD、图片上传、启用/禁用）
- 数据仪表盘（订单趋势、销售额趋势、热销商品排行）
- 个人中心（修改密码、头像上传、手机号、邮箱修改、昵称/简介）

### 用户端
- 注册 / 登录（邮箱验证码）
- 商品浏览（列表、分类筛选、关键字搜索、价格/销量排序）
- 商品详情
- 购物车（添加、修改数量、删除、清空）
- 收货地址管理（增删改查、设为默认）
- 下单（从购物车选择商品、选择地址、生成订单）
- 订单管理（列表、状态筛选、支付（模拟）、取消订单、确认收货）
- 积分明细（查看积分变动记录）
- 个人中心（修改密码、头像上传、手机号、邮箱修改、昵称/简介）
- 首页轮播图（从管理端配置）

---

## 🚀 快速开始（Docker）

### 前置要求
- Docker 20.10+
- Docker Compose 2.0+

### 一键启动

```bash
git clone https://github.com/sunkuizhi/LakeMart.git
cd LakeMart
docker-compose up -d
```
等待所有服务启动（约 30 秒），然后访问：

| 服务 | 地址 | 备注 |
|------|------|------|
| 用户端 | http://localhost:5174 | |
| 管理端 | http://localhost:5173 | |
| MinIO 控制台 | http://localhost:9001 | 账号/密码: minioadmin / minioadmin |

### 默认账号

- 管理员：邮箱 `admin2@example.com`，密码 `12345678`（需先注册后手动将数据库 `tb_user.role` 改为 `ROLE_ADMIN`）
- 或注册新用户后，在数据库中修改角色

> 说明：所有服务数据均通过 Docker 卷持久化（`mysql-data/`、`redis-data/`、`minio-data/`），重启不丢失。

---

## 💻 本地开发运行

### 后端（IDEA）

1. 安装 JDK 21、Maven、MySQL、Redis、MinIO。
2. 导入 `lakemart-server` 模块。
3. 修改 `application.yml` 中的数据库、Redis、MinIO 地址为本地地址。
4. 运行 `LakeMartApplication.main()`。

### 前端（VS Code）

```bash
cd lakemart-admin   # 或 lakemart-client
npm install
npm run dev
```

📁 项目结构
```
LakeMart/
├── docker-compose.yml # 一键部署配置
├── lakemart-server/ # Spring Boot 后端
│ ├── src/main/java/... # 控制器、服务、实体等
│ └── src/main/resources/ # 配置文件和 Mapper XML
├── lakemart-admin/ # 管理端（Vue 3）
├── lakemart-client/ # 用户端（Vue 3）
└── sql/ # 建表脚本（示例）
```

## 🔧 环境变量配置

生产环境建议使用环境变量覆盖默认配置，修改 `docker-compose.yml` 或运行参数：

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| DB_PASSWORD | MySQL 密码 | root |
| JWT_SECRET | JWT 密钥 | your-256-bit-secret... |
| MINIO_ACCESS_KEY | MinIO 访问密钥 | minioadmin |
| MAIL_USERNAME | 邮箱用户名 | your-email@qq.com |

详细配置见 `lakemart-server/src/main/resources/application-example.yml`。

## 📸 部分页面截图

> 请在此处添加项目截图（建议放入 `screenshots/` 文件夹，并在下方引用）

例如：
- 管理端仪表盘
- 用户端商品列表
- 购物车页面
- 订单详情

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request。请确保代码风格统一，并遵循现有目录结构。

## 📄 许可证

MIT License © 2026 [sunkuizhi](https://github.com/sunkuizhi)
