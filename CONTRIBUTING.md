# 贡献指南 / Contributing Guide

感谢你考虑为本项目做出贡献！
Thank you for considering contributing to this project!

## 如何贡献 / How to Contribute

### 报告问题 / Report Issues

如果你发现了 bug 或有功能建议，请：
If you find a bug or have a feature suggestion:

1. 在 [Issues](https://github.com/fluentlc/claude-code-4j/issues) 中搜索是否已有相关问题
2. 如果没有，创建新的 Issue，详细描述问题或建议

### 提交代码 / Submit Code

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 确保代码符合项目风格（见下方代码规范）
4. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
5. 推送到分支 (`git push origin feature/AmazingFeature`)
6. 创建 Pull Request

### 代码规范 / Code Style

- **Java 17** — 项目最低要求 Java 17，可使用 records、var、text blocks 等现代特性
- **模块归属** — 纯业务逻辑和工具放 `claude-code-4j-service`，Spring 相关代码放 `claude-code-4j-start`
- **注释** — 重要代码保持中英双语注释，便于国际协作
- **依赖** — service 模块不引入 Spring，保持框架无关性
- **编译验证** — 提交前运行 `mvn compile` 确认两个子模块均能编译通过

---

## 扩展指南 / Extension Guide

### 扩展新工具 / Add New Tools

1. 在 `claude-code-4j-service` 中新建实现 `ToolProvider` 接口的类
2. 在 `AgentAssembler.buildProviders()` 中加一行注册
3. 运行 `mvn compile` 验证

### 扩展 Teammate 工具 / Add Teammate Tools

如需给 Teammate 增加工具，在 `TeammateLoop.buildDispatch()` 和 `TeammateLoop.buildToolDefs()` 中各加一项即可。Teammate 工具集与 Lead 刻意分开，以保持职责清晰。

### 扩展 SSE 事件 / Add SSE Events

SSE 事件管道由三层组成：
1. `AgentEventListener`（service 模块）— 定义新的 `default` 事件方法
2. `StreamService.SseAgentEventListener`（start 模块）— 实现事件方法，调用 `emitter.send()`
3. `index.html` JS — 在 `handleSseEvent` 的 `switch` 中增加对应 case

示例：新增一个 `tool_progress` 事件：

```java
// AgentEventListener.java
default void onToolProgress(String toolCallId, int percent) {}

// StreamService.java (SseAgentEventListener)
@Override
public void onToolProgress(String toolCallId, int percent) {
    send("tool_progress", Map.of("toolCallId", toolCallId, "percent", percent));
}
```

```javascript
// index.html
case 'tool_progress': {
  const el = toolBlocks[payload.toolCallId];
  if (el) el.querySelector('.progress').style.width = payload.percent + '%';
  break;
}
```

### 扩展 Web Playground / Extend the Web Playground

Web Playground 是单文件 SPA（`claude-code-4j-start/src/main/resources/static/index.html`），采用 Linear 设计语言，无构建步骤。

**关键函数说明：**

| 函数 | 职责 |
|------|------|
| `handleSseEvent(type, payload)` | SSE 事件分发入口 |
| `appendAiRow()` | 创建 AI 消息行（`.msg-ai` + `.ai-body`） |
| `getOrCreateTextEl()` | 创建/复用文字块（含头像，确保仅在文字回复时显示） |
| `openDrawer(type, data)` | 打开右侧工作空间抽屉 |
| `renderMiniMessages(container, msgs, onCompactClick)` | 渲染 mini 消息列表（抽屉内复用主对话流 CSS 类） |
| `buildTranscriptChain(latestFile)` | 沿 `_transcript_file` 链递归加载压缩历史 |
| `switchSession(sessionId)` | 恢复历史会话（含压缩卡、Teammate 卡正确还原） |
| `recordCompact(summary, file)` | 记录并渲染压缩卡（始终置于对话流顶部） |
| `updateTmFloat()` | 更新 Teammate 悬浮状态条 |

修改前端后直接刷新浏览器即可看到效果（Spring Boot 提供静态文件服务）。

---

## 许可证 / License

通过提交代码，你同意你的贡献将按照 [MIT License](LICENSE) 授权。
By submitting code, you agree your contributions will be licensed under the [MIT License](LICENSE).
