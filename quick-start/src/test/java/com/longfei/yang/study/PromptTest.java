package com.longfei.yang.study;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Map;

@SpringBootTest
class PromptTest {


    @Test
    void test01(@Autowired DashScopeChatModel dashScopeChatModel) {
        String userText = """
                请告诉我三位著名的海盗，他们的黄金时代和他们的动机。
                每位海盗至少写一句话。
                """;

        Message userMessage = new UserMessage(userText);

        String systemText = """
                你是一个友好的 AI 助手，帮助人们寻找信息。
                你的名字是 {name}。
                你应该用你的名字回复用户的请求，并以一种 {voice} 的风格进行回复。
                """;

        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemText);
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("name", "Tom", "voice", "幽默"));

        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));

        List<Generation> response = dashScopeChatModel.call(prompt).getResults();
        System.out.println(response);
        String answer = ChatClient.create(dashScopeChatModel).prompt()
                .user(u -> u
                        .text("告诉我5部{composer}的电影.")
                        .param("composer", "周星驰"))
                .call()
                .content();
        System.out.println(answer);
    }

    @Test
    void testPrompt(@Autowired DeepSeekChatModel chatModel,
                           @Value("classpath:/prompts/system-message.st")
                           Resource systemResource) {
        ChatClient  chatClient = ChatClient.builder(chatModel)
                .defaultSystem(systemResource)
                .build();

        String content = chatClient.prompt()
                .system(p -> p.param("composer","周星驰"))
                .call()
                .content();

        System.out.println(content);
    }
}
