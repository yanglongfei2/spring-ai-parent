package com.longfei.yang.study;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.longfei.yang.study.tools.NameCountsTools;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ToolTest {
    ChatClient chatClient;

    @BeforeEach
    void init(@Autowired DashScopeChatModel chatModel,
                     @Autowired NameCountsTools nameCountsTools) {
        chatClient = ChatClient.builder(chatModel).defaultTools(nameCountsTools).build();
    }

    @Test
    void testChatOptions() {
        String content = chatClient.prompt()
                .user("长沙有多少个叫徐庶的/no_think")
                // .tools() 也可以单独绑定当前对话
                .call()
                .content();
        System.out.println(content);
    }
}
