# LakeMart 电商平台

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.13-brightgreen)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-3.5-4FC08D)](https://vuejs.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue)](https://www.docker.com/)
[![GitHub release](https://img.shields.io/github/v/release/sunkuizhi/LakeMart)](https://github.com/sunkuizhi/LakeMart/releases)

> 一套前后端分离的电商系统，包含用户端（H5）和管理端（后台），支持商品、订单、购物车、积分、轮播图、数据可视化等核心功能，基于 Spring Boot 3 + Vue 3 + Docker 实现一键部署。

## 📖 目录

- [技术栈](#-技术栈)
- [主要功能](#-主要功能)
- [快速开始（Docker）](#-快速开始docker)
- [本地开发运行](#-本地开发运行)
- [项目结构](#-项目结构)
- [环境变量配置](#-环境变量配置)
- [部分页面展示](#-部分页面展示)
- [贡献指南](#-贡献指南)
- [许可证](#-许可证)

## 🛠 技术栈

| 分类 | 技术 |
|------|------|
| 后端 | Spring Boot 3.5.13, JDK 21, MyBatis-Plus, MySQL 8, Redis 7, MinIO, JWT, Spring Security, Spring Mail |
| 前端（管理端/用户端） | Vue 3 + Vite, TypeScript, Element Plus, ECharts, Axios, Pinia, Vue Router |
| 部署 | Docker, Docker Compose |

## ✨ 主要功能

### 管理端 (http://localhost:5173)
- 登录 / 退出（JWT，角色区分）
- 商品管理（CRUD、上下架、MinIO 图片上传）
- 订单管理（列表、状态筛选、发货、完成、取消）
- 用户管理（列表、禁用/启用、重置密码、手动调整积分）
- 分类管理（树形结构、增删改查、状态切换）
- 轮播图管理（CRUD、图片上传、启用/禁用）
- 数据仪表盘（订单趋势、销售额趋势、热销商品排行）
- 个人中心（修改密码、头像上传、手机号、邮箱修改、昵称/简介）

### 用户端 (http://localhost:5174)
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

## 🚀 快速开始（Docker）

### 前置要求
- Docker 20.10+
- Docker Compose 2.0+

### 一键启动

```bash
git clone https://github.com/sunkuizhi/LakeMart.git
cd LakeMart
docker-compose up -d
```bash
git clone https://github.com/sunkuizhi/LakeMart.git
cd LakeMart
docker-compose up -d

### 等待所有服务启动（约 30 秒），然后访问：

服务	地址	备注
用户端	http://localhost:5174	
管理端	http://localhost:5173	
MinIO 控制台	http://localhost:9001	账号/密码: minioadmin / minioadmin

默认账号
管理端：请先通过用户端注册一个账号，然后在数据库中执行 UPDATE tb_user SET role = 'ROLE_ADMIN' WHERE email = '你的邮箱'; 获得管理员权限。

用户端：自行注册。

所有数据通过 Docker 卷持久化（mysql-data/、redis-data/、minio-data/），重启不丢失。

💻 本地开发运行
后端（IDEA）
安装 JDK 21、Maven、MySQL、Redis、MinIO。

导入 lakemart-server 模块。

修改 application.yml 中的数据库、Redis、MinIO 连接信息。

运行 LakeMartApplication.main()。

前端（VS Code）
bash
cd lakemart-admin   # 或 lakemart-client
npm install
npm run dev
📁 项目结构
text
LakeMart/
├── docker-compose.yml          # 一键部署配置
├── lakemart-server/            # Spring Boot 后端
│   ├── src/main/java/...       # 控制器、服务、实体等
│   └── src/main/resources/     # 配置文件和 Mapper XML
├── lakemart-admin/             # 管理端（Vue 3）
├── lakemart-client/            # 用户端（Vue 3）
└── sql/                        # 建表脚本（示例）
🔧 环境变量配置
生产环境建议使用环境变量覆盖默认配置，修改 docker-compose.yml 或运行参数：

变量名	说明	默认值
DB_PASSWORD	MySQL 密码	root
JWT_SECRET	JWT 密钥	your-256-bit-secret...
MINIO_ACCESS_KEY	MinIO 访问密钥	minioadmin
MAIL_USERNAME	邮箱用户名	your-email@qq.com
MAIL_PASSWORD	邮箱授权码	your-auth-code
详细配置见 lakemart-server/src/main/resources/application-example.yml。

📸 部分页面展示
请将你的截图放入 docs/screenshots/ 目录，并在下方替换路径。

管理端仪表盘
https://docs/screenshots/admin-dashboard.png

商品管理
https://docs/screenshots/admin-products.png

用户端首页
https://docs/screenshots/client-home.png

购物车
https://docs/screenshots/client-cart.png

🤝 贡献指南
欢迎提交 Issue 和 Pull Request。请确保代码风格统一，并遵循现有目录结构。

开发环境准备
后端：JDK 21 + Maven

前端：Node 20+ + npm

提交前检查
确保后端测试通过（mvn test）

确保前端无编译错误（npm run build）

📄 许可证
MIT License © 2026 sunkuizhi

text

### 补充说明
- 如果你没有 `docs/screenshots/` 文件夹和截图，可以先删除或注释掉“部分页面展示”部分。
- 徽章中的版本号可以根据实际情况调整（如 Spring Boot 版本、Vue 版本）。
- 如果将来创建了 release，徽章会自动显示最新版本号。

你可以直接复制以上内容到 `README.md` 并推送。如果还需要调整格式或添加其他内容，请随时告诉我。
