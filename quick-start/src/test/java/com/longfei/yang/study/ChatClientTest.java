package com.longfei.yang.study;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

@SpringBootTest
class ChatClientTest {
    @Test
    void testChatClient(@Autowired ChatClient.Builder builder) {

        ChatClient chatClient =builder.build();
        String content = chatClient.prompt()
                .user("Hello")
                .call()
                .content();
        System.out.println(content);
    }


    @Test
    void testChatClient2(@Autowired DashScopeChatModel dashScopeChatModel) {

        ChatClient chatClient = ChatClient.builder(dashScopeChatModel).build();
        String content = chatClient.prompt()
                .user("Hello")
                .call()
                .content();
        System.out.println(content);
    }


    @Test
    void testChatStream(@Autowired DeepSeekChatModel chatModel) {

        ChatClient chatClient = ChatClient.builder(chatModel).build();
        Flux<String> content = chatClient.prompt()
                .user("Hello")
                .stream()
                .content();
        // 阻塞输出
        content.toIterable().forEach(System.out::println);
    }
}