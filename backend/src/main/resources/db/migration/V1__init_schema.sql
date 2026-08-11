-- =============================================================
-- Neon Astro Stack 初始化数据库脚本 V1
-- 数据库: PostgreSQL
-- 说明: 创建认证相关核心表 + chart/horoscope 占位表
-- =============================================================

-- ------------------------------
-- 系统用户表
-- ------------------------------
CREATE TABLE sys_user (
    id          BIGSERIAL    PRIMARY KEY,
    username    VARCHAR(64)  NOT NULL UNIQUE,                 -- 用户名
    email       VARCHAR(128) NOT NULL UNIQUE,                 -- 邮箱
    password    VARCHAR(255) NOT NULL,                        -- 密码（BCrypt 加密）
    nickname    VARCHAR(64),                                  -- 昵称
    avatar      VARCHAR(255),                                 -- 头像 URL
    status      SMALLINT      NOT NULL DEFAULT 1,             -- 状态：1 启用，0 禁用
    create_time TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted     SMALLINT      NOT NULL DEFAULT 0              -- 逻辑删除：0 未删除，1 已删除
);
COMMENT ON TABLE sys_user IS '系统用户表';
COMMENT ON COLUMN sys_user.id IS '主键 ID';
COMMENT ON COLUMN sys_user.username IS '用户名';
COMMENT ON COLUMN sys_user.email IS '邮箱';
COMMENT ON COLUMN sys_user.password IS '密码（BCrypt 加密）';
COMMENT ON COLUMN sys_user.nickname IS '昵称';
COMMENT ON COLUMN sys_user.avatar IS '头像 URL';
COMMENT ON COLUMN sys_user.status IS '状态：1 启用，0 禁用';
COMMENT ON COLUMN sys_user.create_time IS '创建时间';
COMMENT ON COLUMN sys_user.update_time IS '更新时间';
COMMENT ON COLUMN sys_user.deleted IS '逻辑删除标识：0 未删除，1 已删除';

-- ------------------------------
-- 系统角色表
-- ------------------------------
CREATE TABLE sys_role (
    id          BIGSERIAL    PRIMARY KEY,
    role_code   VARCHAR(64)  NOT NULL UNIQUE,                 -- 角色编码
    role_name   VARCHAR(64)  NOT NULL,                        -- 角色名称
    description VARCHAR(255),                                 -- 描述
    create_time TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted     SMALLINT      NOT NULL DEFAULT 0
);
COMMENT ON TABLE sys_role IS '系统角色表';

-- ------------------------------
-- 用户-角色关联表
-- ------------------------------
CREATE TABLE sys_user_role (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT     NOT NULL,                          -- 用户 ID
    role_id     BIGINT     NOT NULL,                          -- 角色 ID
    create_time TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sur_user FOREIGN KEY (user_id) REFERENCES sys_user (id),
    CONSTRAINT fk_sur_role FOREIGN KEY (role_id) REFERENCES sys_role (id),
    UNIQUE (user_id, role_id)
);
COMMENT ON TABLE sys_user_role IS '用户-角色关联表';

-- ------------------------------
-- 用户资料表（扩展用户信息）
-- ------------------------------
CREATE TABLE user_profile (
    id            BIGSERIAL    PRIMARY KEY,
    user_id       BIGINT       NOT NULL UNIQUE,               -- 关联用户 ID
    birth_date    DATE,                                       -- 出生日期
    birth_time    TIME,                                       -- 出生时间
    birth_place   VARCHAR(255),                               -- 出生地点
    gender        SMALLINT,                                   -- 性别
    bio           TEXT,                                       -- 个人简介
    create_time   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted       SMALLINT    NOT NULL DEFAULT 0,
    CONSTRAINT fk_up_user FOREIGN KEY (user_id) REFERENCES sys_user (id)
);
COMMENT ON TABLE user_profile IS '用户资料表';

-- ------------------------------
-- 星盘表（占位，后续扩展）
-- ------------------------------
CREATE TABLE chart (
    id          BIGSERIAL    PRIMARY KEY,
    user_id     BIGINT       NOT NULL,                       -- 所属用户 ID
    name        VARCHAR(128),                                 -- 星盘名称
    chart_type  VARCHAR(32),                                  -- 星盘类型（natal/transit/synastry）
    birth_date  DATE,                                         -- 盘主出生日期
    birth_time  TIME,                                         -- 盘主出生时间
    birth_place VARCHAR(255),                                 -- 盘主出生地点
    latitude    DOUBLE PRECISION,                             -- 纬度
    longitude   DOUBLE PRECISION,                             -- 经度
    chart_data  JSONB,                                        -- 星盘计算结果（JSON）
    create_time TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted     SMALLINT     NOT NULL DEFAULT 0
);
COMMENT ON TABLE chart IS '星盘表';
COMMENT ON COLUMN chart.chart_type IS '星盘类型：natal 本命盘，transit 推运，synastry 合盘';
COMMENT ON COLUMN chart.chart_data IS '星盘计算结果（JSON）';

-- ------------------------------
-- 每日运势表（占位，后续扩展）
-- ------------------------------
CREATE TABLE horoscope_daily (
    id           BIGSERIAL    PRIMARY KEY,
    user_id      BIGINT,                                     -- 关联用户（可空，表示通用运势）
    zodiac_sign  VARCHAR(16)  NOT NULL,                       -- 星座
    forecast_date DATE        NOT NULL,                       -- 运势日期
    overall      VARCHAR(32),                                 -- 综合运势指数
    love         VARCHAR(32),                                 -- 爱情运势指数
    career       VARCHAR(32),                                 -- 事业运势指数
    wealth       VARCHAR(32),                                 -- 财运指数
    health       VARCHAR(32),                                 -- 健康指数
    summary      TEXT,                                        -- 运势概述
    lucky_color  VARCHAR(32),                                 -- 幸运色
    lucky_number VARCHAR(32),                                -- 幸运数字
    create_time  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted      SMALLINT     NOT NULL DEFAULT 0,
    UNIQUE (zodiac_sign, forecast_date)
);
COMMENT ON TABLE horoscope_daily IS '每日运势表';
COMMENT ON COLUMN horoscope_daily.zodiac_sign IS '星座';
COMMENT ON COLUMN horoscope_daily.forecast_date IS '运势日期';

-- ------------------------------
-- 初始化默认角色
-- ------------------------------
INSERT INTO sys_role (role_code, role_name, description) VALUES
    ('ROLE_USER',  '普通用户', '注册用户默认角色'),
    ('ROLE_ADMIN', '管理员',   '系统管理员');
