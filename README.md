# LakeMart 电商平台

基于 Spring Boot 3 + Vue 3 的全栈电商项目，支持用户端和管理端，包含完整的购物流程、数据分析、对象存储等。

## 技术栈

- **后端**: Spring Boot 3.5.13, JDK 21, MyBatis-Plus, MySQL 8, Redis 7, MinIO, JWT, Spring Security, Spring Mail
- **前端**: Vue 3, Vite, TypeScript, Element Plus, ECharts, Pinia
- **部署**: Docker + Docker Compose

## 功能特性

### 管理端
- 用户认证（JWT，角色区分）
- 商品管理（CRUD、上下架、MinIO 图片上传）
- 订单管理（列表、状态筛选、发货、完成、取消）
- 用户管理（列表、禁用/启用、重置密码、积分调整）
- 分类管理（树形展示、增删改查）
- 轮播图管理
- 数据仪表盘（订单趋势、销售额趋势、热销商品排行）

### 用户端
- 注册/登录（邮箱验证码）
- 商品浏览（分页、筛选、排序）
- 购物车
- 地址管理
- 下单、支付（模拟）、取消订单、确认收货
- 订单列表与详情
- 个人中心（修改密码、头像、邮箱、手机号、简介）
- 积分明细
- 首页轮播图

## 快速启动（Docker）

1. 克隆项目
   ```bash
   git clone https://github.com/你的用户名/LakeMart.git
   cd LakeMart