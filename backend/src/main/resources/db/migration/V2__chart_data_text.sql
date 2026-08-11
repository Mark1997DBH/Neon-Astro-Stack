-- V2: chart 表 chart_data 从 JSONB 改为 TEXT
-- 原因：MyBatis-Plus String 字段直接映射 jsonb 列在 PostgreSQL 下需要显式类型转换，
-- 使用 TEXT 更便于实体以 JSON 字符串方式读写，计算逻辑保持在后端。
ALTER TABLE chart ALTER COLUMN chart_data TYPE TEXT USING chart_data::text;
COMMENT ON COLUMN chart.chart_data IS '星盘计算结果（JSON 字符串）';
