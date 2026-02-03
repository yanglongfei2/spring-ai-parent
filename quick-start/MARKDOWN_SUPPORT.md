# 📝 Markdown 格式支持说明

## 概述

NL2SQL 对话页面现已支持完整的 Markdown 格式渲染，包括代码高亮、表格、列表等丰富的格式。

## ✨ 支持的 Markdown 特性

### 1. 标题
```markdown
# 一级标题
## 二级标题
### 三级标题
```

### 2. 文本格式
```markdown
**粗体文本**
*斜体文本*
~~删除线~~
`行内代码`
```

### 3. 列表
```markdown
- 无序列表项 1
- 无序列表项 2

1. 有序列表项 1
2. 有序列表项 2
```

### 4. 代码块（带语法高亮）
````markdown
```sql
SELECT * FROM users WHERE age > 18;
```

```python
def hello():
    print("Hello, World!")
```

```javascript
const greeting = "Hello, World!";
console.log(greeting);
```
````

### 5. 表格
```markdown
| 列1 | 列2 | 列3 |
|-----|-----|-----|
| 数据1 | 数据2 | 数据3 |
| 数据4 | 数据5 | 数据6 |
```

### 6. 引用
```markdown
> 这是一段引用文本
> 可以跨多行
```

### 7. 链接和图片
```markdown
[链接文本](https://example.com)
![图片描述](image-url.jpg)
```

### 8. 分隔线
```markdown
---
```

## 🎨 特殊样式

### SQL 代码高亮
后端返回的 SQL 查询会自动高亮显示：

```sql
SELECT 
    d.department_name,
    COUNT(u.user_id) as user_count
FROM departments d
LEFT JOIN users u ON d.department_id = u.department_id
GROUP BY d.department_id;
```

### 表格样式
表格会自动应用美化样式：
- 表头：紫色背景
- 偶数行：浅灰色背景
- 边框：清晰的分隔线

### 代码块复制功能
每个代码块右上角都有"复制"按钮，点击即可复制代码到剪贴板。

## 🔧 技术实现

### 使用的库
- **marked.js** (v11.1.1) - Markdown 解析
- **highlight.js** (v11.9.0) - 代码语法高亮

### CDN 资源
```html
<!-- Markdown 解析 -->
<script src="https://cdn.jsdelivr.net/npm/marked@11.1.1/marked.min.js"></script>

<!-- 代码高亮 -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/highlight.js@11.9.0/styles/github-dark.min.css">
<script src="https://cdn.jsdelivr.net/npm/highlight.js@11.9.0/highlight.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/highlight.js@11.9.0/languages/sql.min.js"></script>
```

## 📋 后端响应格式建议

### 推荐的响应格式

为了获得最佳显示效果，后端返回的内容建议使用以下格式：

#### 1. 带 SQL 查询的响应
```markdown
### 查询结果

根据您的问题"XXX"，我执行了以下 SQL 查询：

\`\`\`sql
SELECT column1, column2 
FROM table_name 
WHERE condition;
\`\`\`

**查询结果：**
- 结果项 1
- 结果项 2
```

#### 2. 带表格的响应
```markdown
### 统计信息

| 部门名称 | 用户数量 | 平均年龄 |
|---------|---------|---------|
| 技术部 | 45 | 28.5 |
| 销售部 | 32 | 30.2 |

**关键发现：**
1. 发现 1
2. 发现 2
```

#### 3. 带数据分析的响应
```markdown
# 📊 数据分析结果

## 1. 执行的查询

\`\`\`sql
SELECT * FROM users;
\`\`\`

## 2. 结果统计

| 指标 | 数值 |
|-----|------|
| 总数 | 1000 |
| 平均值 | 25.5 |

## 3. 建议

> **重要提示：** 基于当前数据的分析建议

- 建议 1
- 建议 2
```

## 🧪 测试页面

访问测试页面查看各种 Markdown 格式的渲染效果：

```
http://localhost:8082/markdown-test.html
```

测试页面包含：
1. SQL 查询示例
2. 表格和列表
3. 代码块和引用
4. 完整的 NL2SQL 响应示例

## 💡 使用示例

### 示例 1：简单查询
**用户输入：** "用户表有多少用户"

**AI 响应（Markdown 格式）：**
```markdown
根据查询，用户表共有 **1,234** 个用户。

执行的 SQL：
\`\`\`sql
SELECT COUNT(*) FROM users;
\`\`\`
```

### 示例 2：复杂统计
**用户输入：** "查询各部门的用户数量"

**AI 响应（Markdown 格式）：**
```markdown
### 部门用户统计

| 部门 | 用户数 | 占比 |
|-----|-------|------|
| 技术部 | 156 | 45% |
| 销售部 | 89 | 26% |
| 市场部 | 67 | 19% |

**SQL 查询：**
\`\`\`sql
SELECT 
    department_name,
    COUNT(*) as user_count,
    ROUND(COUNT(*) * 100.0 / SUM(COUNT(*)) OVER(), 2) as percentage
FROM users
GROUP BY department_name;
\`\`\`
```

## 🎯 最佳实践

### 1. 结构化响应
使用标题、列表和表格组织内容，使其更易读。

### 2. 突出重点
使用 **粗体** 和 `代码` 格式突出关键信息。

### 3. 提供上下文
在 SQL 查询前后添加说明文字，帮助用户理解。

### 4. 使用表格展示数据
对于结构化数据，优先使用表格而不是纯文本。

### 5. 添加视觉元素
使用 emoji 和引用块增强视觉效果：
- 📊 数据统计
- 💡 提示信息
- ⚠️ 警告信息
- ✅ 成功信息

## 🔍 故障排除

### 问题 1：Markdown 不渲染
**原因：** CDN 资源加载失败

**解决方案：**
1. 检查网络连接
2. 查看浏览器控制台是否有错误
3. 尝试刷新页面

### 问题 2：代码高亮不显示
**原因：** highlight.js 未正确加载

**解决方案：**
1. 确认 highlight.js 的 CDN 链接可访问
2. 检查是否加载了对应语言的支持文件

### 问题 3：复制按钮不工作
**原因：** 浏览器不支持 Clipboard API 或权限被拒绝

**解决方案：**
1. 使用 HTTPS 协议访问（localhost 除外）
2. 检查浏览器权限设置
3. 使用现代浏览器（Chrome 63+, Firefox 53+）

## 📚 参考资源

- [Marked.js 文档](https://marked.js.org/)
- [Highlight.js 文档](https://highlightjs.org/)
- [Markdown 语法指南](https://www.markdownguide.org/)
- [GitHub Flavored Markdown](https://github.github.com/gfm/)

## 🚀 未来增强

可能的功能扩展：
1. ✅ 支持数学公式（KaTeX）
2. ✅ 支持流程图（Mermaid）
3. ✅ 支持更多代码语言高亮
4. ✅ 自定义主题切换
5. ✅ 导出为 PDF 或 Markdown 文件

---

**提示：** 所有 AI 响应都会自动渲染 Markdown 格式，无需额外配置！
