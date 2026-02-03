package com.longfei.yang.study;

import com.longfei.yang.study.advisor.ReReadingAdvisor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AdvisorTest {

    ChatClient chatClient;

    @BeforeEach
    void init(@Autowired
                     DeepSeekChatModel chatModel) {
        chatClient = ChatClient
                .builder(chatModel)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor()
                )
                .build();
    }

    @Test
    void testChatOptions() {
        String content = chatClient.prompt()
                .user("Hello")
                .call()
                .content();
        System.out.println(content);
    }


    @Test
    void testChatOptions2() {
        String content = chatClient.prompt()
                .user("中国有多大？")
                .advisors(new ReReadingAdvisor())
                .call()
                .content();
        System.out.println(content);
    }
}
