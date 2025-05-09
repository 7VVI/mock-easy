-- 创建API分组表
CREATE TABLE IF NOT EXISTS `api_group` (
  `id` varchar(36) NOT NULL COMMENT '分组ID',
  `name` varchar(100) NOT NULL COMMENT '分组名称',
  `description` varchar(500) DEFAULT NULL COMMENT '分组描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='API分组表';

-- 创建API配置表
CREATE TABLE IF NOT EXISTS `api_config` (
  `id` varchar(36) NOT NULL COMMENT 'API配置ID',
  `path` varchar(255) NOT NULL COMMENT 'API路径',
  `method` varchar(20) NOT NULL COMMENT '请求方法',
  `description` varchar(500) DEFAULT NULL COMMENT 'API描述',
  `group_id` varchar(36) DEFAULT NULL COMMENT '所属分组ID',
  `response_fields` json DEFAULT NULL COMMENT '响应字段配置(JSON格式)',
  `response_count` int DEFAULT 1 COMMENT '响应数据条数',
  `headers` json DEFAULT NULL COMMENT '响应头(JSON格式)',
  `delay` int DEFAULT 0 COMMENT '模拟延迟(毫秒)',
  `status_code` int DEFAULT 200 COMMENT '响应状态码',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_path_method` (`path`, `method`),
  KEY `idx_group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='API配置表';