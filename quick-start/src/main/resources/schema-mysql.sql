CREATE TABLE IF NOT EXISTS SPRING_AI_CHAT_MEMORY (
    `conversation_id` VARCHAR(36) NOT NULL,
    `content` TEXT NOT NULL,
    `type` VARCHAR(10) NOT NULL,
    `timestamp` TIMESTAMP NOT NULL,

    INDEX `SPRING_AI_CHAT_MEMORY_CONVERSATION_ID_TIMESTAMP_IDX` (`conversation_id`, `timestamp`)
);

-- AI工具配置表
CREATE TABLE IF NOT EXISTS ai_tool_config (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `tool_name` VARCHAR(100) NOT NULL COMMENT '工具名称',
    `method_name` VARCHAR(100) NOT NULL COMMENT '方法名称',
    `description` VARCHAR(500) NOT NULL COMMENT '工具描述',
    `input_schema` TEXT NOT NULL COMMENT '输入参数JSON Schema',
    `enabled` BOOLEAN DEFAULT TRUE COMMENT '是否启用',
    `target_class` VARCHAR(255) NOT NULL COMMENT '目标类全限定名',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_tool_name` (`tool_name`)
) COMMENT='AI工具配置表';

-- 插入示例数据
INSERT INTO ai_tool_config (tool_name, method_name, description, input_schema, enabled, target_class) 
VALUES (
    'cancel',
    'cancel',
    '退票',
    '{
      "type": "object",
      "properties": {
        "ticketNumber": {
          "type": "string",
          "description": "预定号，可以是纯数字"
        },
        "name": {
          "type": "string",
          "description": "真实人名（必填，必须为人的真实姓名，严禁用其他信息代替；如缺失请传null）"
        }
      },
      "required": ["ticketNumber", "name"]
    }',
    true,
    'com.longfei.yang.study.tools.TicketTools'
) ON DUPLICATE KEY UPDATE 
    method_name = VALUES(method_name),
    description = VALUES(description),
    input_schema = VALUES(input_schema),
    enabled = VALUES(enabled),
    target_class = VALUES(target_class);