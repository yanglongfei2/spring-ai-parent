package com.longfei.yang.study.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.longfei.yang.study.tools.NL2SQLTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.nio.charset.Charset;

@RestController
public class NL2SQLController {

    private final ChatClient chatClient;

    public NL2SQLController(DashScopeChatModel dashScopeChatModel, 
                           ChatMemory chatMemory, 
                           NL2SQLTool nl2SQLTool,
                           @Value("classpath:prompts/data-analysis-prompt-template.txt") Resource systemPrompt) {
        this.chatClient = ChatClient.builder(dashScopeChatModel)
                .defaultSystem(systemPrompt, Charset.defaultCharset())
                .defaultTools(nl2SQLTool)
                .defaultAdvisors(
                        PromptChatMemoryAdvisor.builder(chatMemory).build(),
                        SimpleLoggerAdvisor.builder().build())
                .build();
    }

    @CrossOrigin
    @GetMapping(value = "/send", produces = "text/event-stream;charset=UTF-8")
    public Flux<String> generatePost(@RequestParam(value = "message", defaultValue = "你好") String message) {
        return chatClient.prompt().user(message).stream().content()
                .onErrorResume(e -> {
                    if (e instanceof java.io.IOException) {
                        return Flux.empty();
                    }
                    return Flux.error(e);
                });
    }

}