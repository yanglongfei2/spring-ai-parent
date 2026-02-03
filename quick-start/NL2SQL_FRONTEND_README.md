# NL2SQL 前端页面使用说明

## 概述

为 NL2SQLController 创建的现代化对话界面，支持实时流式响应和自然语言到 SQL 的智能转换。

## 功能特性

### ✨ 核心功能
- 💬 实时对话界面
- 🌊 流式响应（SSE）
- 📱 响应式设计，支持移动端
- 🎨 现代化 UI 设计
- ⚡ 打字动画效果
- 📝 示例查询快速输入
- 📝 **完整的 Markdown 格式支持**
- 🎨 **SQL 代码语法高亮**
- 📋 **代码一键复制功能**
- 📊 **表格美化显示**

### 🎯 用户体验
- 消息渐入动画
- 自动滚动到最新消息
- 打字指示器
- 发送状态反馈
- 键盘快捷键（Enter 发送）

## 访问方式

### 启动应用
```bash
mvn spring-boot:run
```

### 访问页面
```
http://localhost:8082/nl2sql-chat.html
```

## 页面结构

```
nl2sql-chat.html
├── 头部区域 - 标题和品牌
├── 消息区域 - 对话历史
│   ├── 欢迎消息
│   ├── 示例查询按钮
│   ├── 用户消息（右侧，紫色）
│   └── AI 回复（左侧，白色）
└── 输入区域 - 消息输入和发送
```

## 使用示例

### 示例查询
页面提供了三个示例查询按钮：
1. "统计所有用户的数量"
2. "查询销售额最高的前10个产品"
3. "计算本月的平均订单金额"

### 自定义查询
用户可以输入任何自然语言查询，例如：
- "查询今天的订单总数"
- "统计每个城市的用户数量"
- "计算上个月的平均销售额"
- "你好"（非 SQL 查询，会得到礼貌回复）

## 技术实现

### 前端技术
- 纯 HTML + CSS + JavaScript
- 无需任何框架或库
- 使用 EventSource API 处理 SSE
- CSS3 动画和渐变

### 后端对接
```javascript
// SSE 连接
const url = `/send?message=${encodeURIComponent(message)}`;
const eventSource = new EventSource(url);

eventSource.onmessage = function(event) {
    // 处理流式响应
    const chunk = event.data;
    // 更新 UI
};
```

### 响应流程
1. 用户输入消息并点击发送
2. 前端显示用户消息和打字指示器
3. 建立 SSE 连接到 `/send` 接口
4. 实时接收并显示 AI 响应
5. 连接关闭，恢复输入状态

## 样式定制

### 主题色
```css
/* 渐变主题色 */
background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
```

### 修改主题色
在 `<style>` 标签中搜索 `#667eea` 和 `#764ba2`，替换为您喜欢的颜色。

### 消息气泡
- 用户消息：右侧，紫色渐变背景
- AI 消息：左侧，白色背景，带阴影

## 浏览器兼容性

### 支持的浏览器
- ✅ Chrome 60+
- ✅ Firefox 55+
- ✅ Safari 12+
- ✅ Edge 79+

### 核心依赖
- EventSource API（SSE 支持）
- CSS Grid 和 Flexbox
- CSS 动画

## 常见问题

### Q: 消息发送后没有响应？
A: 检查：
1. 后端服务是否正常运行（端口 8082）
2. 浏览器控制台是否有错误
3. 网络连接是否正常

### Q: 如何修改端口？
A: 修改 `application.yml` 中的 `server.port`，同时确保前端请求的 URL 匹配。

### Q: 如何添加更多示例查询？
A: 在 HTML 中找到 `.example-queries` 部分，添加：
```html
<div class="example-query" onclick="sendExample('您的查询')">
    您的查询
</div>
```

### Q: 如何禁用流式响应？
A: 需要修改后端接口，将 `Flux<String>` 改为 `String`，并相应修改前端代码使用普通 AJAX 请求。

## 扩展建议

### 功能增强
1. **历史记录** - 使用 localStorage 保存对话历史
2. **多会话** - 支持创建和切换多个对话
3. **导出功能** - 导出对话记录为文本或 PDF
4. **语音输入** - 集成 Web Speech API
5. **代码高亮** - 对 SQL 语句进行语法高亮

### 示例代码：添加历史记录
```javascript
// 保存消息
function saveMessage(message, role) {
    const history = JSON.parse(localStorage.getItem('chatHistory') || '[]');
    history.push({ message, role, timestamp: Date.now() });
    localStorage.setItem('chatHistory', JSON.stringify(history));
}

// 加载历史
function loadHistory() {
    const history = JSON.parse(localStorage.getItem('chatHistory') || '[]');
    history.forEach(item => addMessage(item.message, item.role));
}
```

## 性能优化

### 建议
1. 限制消息历史数量（如最多显示 100 条）
2. 使用虚拟滚动处理大量消息
3. 添加消息分页加载
4. 压缩和缓存静态资源

## 安全考虑

### 当前实现
- ✅ URL 参数编码（防止注入）
- ✅ 内容转义（使用 textContent）

### 建议增强
- 添加 CSRF 保护
- 实现用户认证
- 添加请求频率限制
- 输入内容验证和过滤

## 部署说明

### 开发环境
直接通过 Spring Boot 的静态资源服务访问。

### 生产环境
1. 打包到 JAR 文件中
2. 或部署到独立的 Web 服务器（Nginx）
3. 配置 HTTPS
4. 启用 Gzip 压缩

## 联系支持

如有问题或建议，请联系开发团队。
