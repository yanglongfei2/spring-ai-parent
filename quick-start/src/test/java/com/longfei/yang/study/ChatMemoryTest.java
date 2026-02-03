package com.longfei.yang.study;

import com.longfei.yang.study.advisor.ReReadingAdvisor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChatMemoryTest {
    ChatClient chatClient;
    @BeforeEach
    void init(@Autowired DeepSeekChatModel chatModel,
                      @Autowired ChatMemory chatMemory) {
        chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(
                        PromptChatMemoryAdvisor.builder(chatMemory).build()
                ).build();
    }
    @Test
    void testChatOptions() {
        String content = chatClient.prompt()
                .user("我叫徐庶 ？")
                .advisors(new ReReadingAdvisor())
                .call()
                .content();
        System.out.println(content);
        System.out.println("--------------------------------------------------------------------------");

        content = chatClient.prompt()
                .user("我叫什么 ？")
                .advisors(new ReReadingAdvisor())
                .call()
                .content();
        System.out.println(content);
    }
}

