# Neon-Astro-Stack

霓虹占星栈 —— 一个以霓虹视觉美学为核心的占星服务平台。

## 技术栈

| 端 | 技术 |
|----|------|
| 前端 | Vue 3 + TypeScript + Vite + Pinia + Vue Router + TailwindCSS + Axios + ECharts |
| 后端 | Spring Boot 3 + Spring Security 6 + JWT + MyBatis-Plus + PostgreSQL + Redis |
| 部署 | Docker + Nginx + Prometheus（可选） |

## 项目结构

```
Neon-Astro-Stack/
├── backend/          # Spring Boot 后端（模块化单体）
│   └── src/main/java/com/neonastro/
│       ├── common/        # 统一响应 / 异常 / 工具
│       ├── config/        # Security / MyBatis-Plus / Redis / OpenAPI
│       ├── security/      # JWT 过滤器 / 认证入口点
│       └── modules/       # 业务模块（auth / user / chart / horoscope / content / order / admin）
├── frontend/         # Vue 3 前端
│   └── src/
│       ├── api/           # Axios 封装 + 模块 API
│       ├── components/    # Neon 通用组件 / 布局 / 星盘组件
│       ├── composables/   # useAuth / useTheme / useToast
│       ├── router/        # 路由 + 守卫
│       ├── stores/        # Pinia（auth / user / theme）
│       ├── styles/        # Tailwind + 霓虹主题
│       ├── types/         # TS 类型定义
│       ├── utils/         # ECharts / 格式化 / 存储
│       └── views/         # 页面
├── docker/           # docker-compose 编排
└── .env.example      # 环境变量模板
```

## 快速开始

### 1. 启动基础设施（PostgreSQL + Redis）

```bash
cd docker
docker compose -f docker-compose.dev.yml up -d
```

### 2. 启动后端

```bash
cd backend
mvn spring-boot:run
# 默认端口 8080，context-path /api/v1
```

### 3. 启动前端

```bash
cd frontend
npm install
npm run dev
# 默认端口 5173，自动代理 /api -> localhost:8080
```

### 4. 全栈 Docker 部署

```bash
cp .env.example .env   # 按需修改
cd docker
docker compose up -d --build
# 前端 http://localhost  后端 http://localhost:8080
```

## 默认账号

数据库初始化后通过注册接口创建首个用户，或参考 `backend/src/main/resources/db/migration/V1__init_schema.sql`。
