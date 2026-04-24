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
