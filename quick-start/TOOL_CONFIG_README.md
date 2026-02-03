# AI 工具动态配置说明

## 概述

本项目实现了从数据库动态加载 AI 工具配置的功能，无需修改代码即可添加、修改或禁用工具。

## 数据库表结构

### ai_tool_config 表

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键，自增 |
| tool_name | VARCHAR(100) | 工具名称（唯一） |
| method_name | VARCHAR(100) | 对应的方法名 |
| description | VARCHAR(500) | 工具描述 |
| input_schema | TEXT | 输入参数的 JSON Schema |
| enabled | BOOLEAN | 是否启用 |
| target_class | VARCHAR(255) | 目标类的全限定名 |
| created_at | TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | 更新时间 |

## 使用方法

### 1. 初始化数据库

执行 `schema-mysql.sql` 脚本，会自动创建表并插入示例数据。

### 2. 添加新工具

```sql
INSERT INTO ai_tool_config (tool_name, method_name, description, input_schema, enabled, target_class) 
VALUES (
    'query_ticket',
    'queryTicket',
    '查询票据信息',
    '{
      "type": "object",
      "properties": {
        "ticketNumber": {
          "type": "string",
          "description": "票据号码"
        }
      },
      "required": ["ticketNumber"]
    }',
    true,
    'com.longfei.yang.study.tools.TicketTools'
);
```

### 3. 在代码中使用

```java
@Autowired
private TicketTools ticketTools;

// 获取所有启用的工具
List<ToolCallback> tools = ticketTools.getToolCallList();

// 在 ChatClient 中使用
ChatClient chatClient = ChatClient.builder(chatModel)
    .defaultTools(tools.toArray(new ToolCallback[0]))
    .build();
```

### 4. 禁用工具

```sql
UPDATE ai_tool_config SET enabled = false WHERE tool_name = 'cancel';
```

### 5. 修改工具配置

```sql
UPDATE ai_tool_config 
SET description = '新的描述',
    input_schema = '新的 JSON Schema'
WHERE tool_name = 'cancel';
```

## 核心类说明

### ToolConfig
工具配置实体类，映射数据库表结构。

### ToolConfigRepository
数据访问层，提供查询工具配置的方法：
- `findAllEnabled()`: 查询所有启用的工具
- `findByToolName(String toolName)`: 根据工具名查询

### TicketTools
工具类，提供：
- `getToolCallList()`: 从数据库动态加载工具配置并构建 ToolCallback 列表
- 具体的工具方法实现（如 `cancel()`）

## 扩展说明

### 添加新的工具类

1. 创建新的工具类（如 `OrderTools`）
2. 在数据库中添加配置，`target_class` 指向新类
3. 确保新类被 Spring 管理（添加 `@Service` 或 `@Component` 注解）

### 支持更复杂的参数类型

当前实现通过方法名匹配，支持任意参数类型。如需更精确的匹配，可以在 `ToolConfig` 中添加参数类型信息。

## 注意事项

1. 确保 `target_class` 指向的类已被 Spring 容器管理
2. `input_schema` 必须是有效的 JSON Schema 格式
3. 方法名必须与实际方法名完全匹配
4. 修改配置后，需要重新调用 `getToolCallList()` 才能生效
