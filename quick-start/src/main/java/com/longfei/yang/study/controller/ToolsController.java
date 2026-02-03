package com.longfei.yang.study.controller;

import ch.qos.logback.core.net.server.Client;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.longfei.yang.study.tools.NameCountsTools;
import com.longfei.yang.study.tools.TicketTools;
import com.longfei.yang.study.tools.ToolsUtil;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ToolsController {


    private final ChatClient chatClient;


//    public ToolsController(DashScopeChatModel dashScopeChatModel, TicketTools ticketTools,
//                           NameCountsTools nameCountsTools) {
//        this.chatClient = ChatClient.builder(dashScopeChatModel)
//                .defaultTools(ticketTools, nameCountsTools)
//                .defaultAdvisors(new SimpleLoggerAdvisor())
//                .build();
//    }

    public ToolsController(DashScopeChatModel dashScopeChatModel, ToolsUtil toolsUtil) {
        this.chatClient = ChatClient.builder(dashScopeChatModel)
                .defaultToolCallbacks(toolsUtil.getToolCallList())
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }


    @GetMapping("/tools")
    public String tools (@RequestParam(value = "message", defaultValue = "讲个笑话") String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
