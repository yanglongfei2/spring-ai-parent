package com.longfei.yang.study.controller;

import com.aispace.supersql.builder.RagOptions;
import com.aispace.supersql.engine.SpringSqlEngine;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Objects;

@RestController
public class NL2SQLController {

    private final DashScopeChatModel dashScopeChatModel;
    private final ChatClient chatClient;
    private final SpringSqlEngine sqlEngine;



    public NL2SQLController(DashScopeChatModel dashScopeChatModel, SpringSqlEngine sqlEngine) {
        this.sqlEngine = sqlEngine;
        this.dashScopeChatModel = dashScopeChatModel;
        this.chatClient = ChatClient.builder(dashScopeChatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @CrossOrigin
    @GetMapping(value = "/send", produces = "text/event-stream;charset=UTF-8")
    public Flux<String> generatePost(@RequestParam(value = "message", defaultValue = "你好") String message) {

        // 检查是否是聚合查询
        boolean isSql = chatClient.prompt()
                .system("用户的查询是否涉及统计数据、求和、计数、平均值等聚合操作？")
                .user(message)
                .call()
                .entity(Boolean.class);

        // 2. 非 SQL，直接礼貌回复
        if (!Boolean.TRUE.equals(isSql)) {
            return politeReply(message);
        }

        // 3. 生成 SQL
        String sql = sqlEngine
                .setChatModel(dashScopeChatModel)
                .setOptions(RagOptions.builder()
                        .topN(5)
                        .rerank(false)
                        .limitScore(0.1)
                        .build())
                .generateSql(message);

        // 4. SQL 生成失败，兜底回复
        if (sql == null || sql.isBlank()) {
            return politeReply(message);
        }

        // 5. 执行 SQL + 总结
        Object result = sqlEngine.executeSql(sql);
        return sqlEngine.generateSummary(message, String.valueOf(result))
                .flatMap(chatResponse ->
                     Flux.fromIterable(chatResponse.getResults())
                        .map(r -> r.getOutput().getText())
                        .filter(Objects::nonNull)
        );
    }

    private Flux<String> politeReply(String message) {
        return chatClient.prompt()
                .system("礼貌性回复用户的提问题")
                .user(message)
                .stream()
                .content();
    }
}