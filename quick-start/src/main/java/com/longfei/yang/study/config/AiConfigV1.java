package com.longfei.yang.study.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.ai.deepseek.api.DeepSeekApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 公众号：程序员徐庶
 */
@Configuration
public class AiConfigV1 {

    @Bean
    public ChatClient deepseekR1() {

        DeepSeekApi deepSeekApi = DeepSeekApi.builder()
                .apiKey(System.getenv("DEEP_SEEK_API_KEY"))
                .build();


        DeepSeekChatModel deepSeekChatModel = DeepSeekChatModel.builder()
                .deepSeekApi(deepSeekApi)
                .defaultOptions(DeepSeekChatOptions.builder().model(DeepSeekApi.ChatModel.DEEPSEEK_REASONER).build())
                .build();

        return ChatClient.builder(deepSeekChatModel).build();
    }

    @Bean
    public ChatClient deepseekV3() {

        DeepSeekApi deepSeekApi = DeepSeekApi.builder()
                .apiKey(System.getenv("DEEP_SEEK_API_KEY"))
                .build();


        DeepSeekChatModel deepSeekChatModel = DeepSeekChatModel.builder()
                .deepSeekApi(deepSeekApi)
                .defaultOptions(
                        DeepSeekChatOptions.builder()
                                .model(DeepSeekApi.ChatModel.DEEPSEEK_CHAT)
                                .build()
                )
                .build();

        return ChatClient.builder(deepSeekChatModel).build();
    }



}
