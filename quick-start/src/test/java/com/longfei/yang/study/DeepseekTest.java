package com.longfei.yang.study;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

@SpringBootTest
public class DeepseekTest {


    @Test
    void testChat(@Autowired
                         DeepSeekChatModel chatModel) {
        String call = chatModel.call("你是谁");
        System.out.println(call);
    }

    @Test
    void testChat2(@Autowired
                          DeepSeekChatModel chatModel) {

        Flux<String> stream = chatModel.stream("你是谁");
        // 阻塞输出
        stream.toIterable().forEach(System.out::print);
    }


    @Test
    void testChatOptions(@Autowired
                                DeepSeekChatModel chatModel) {
        DeepSeekChatOptions options = DeepSeekChatOptions.builder().temperature(2d).build();
        ChatResponse res = chatModel.call(new Prompt("请写一句诗描述清晨。", options));
        System.out.println(res.getResult().getOutput().getText());
    }
}
