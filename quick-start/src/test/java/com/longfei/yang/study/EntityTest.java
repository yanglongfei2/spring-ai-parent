package com.longfei.yang.study;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.longfei.yang.study.bean.ActorsFilms;
import com.longfei.yang.study.bean.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class EntityTest {

    ChatClient chatClient;

    @BeforeEach
    void init(@Autowired DashScopeChatModel chatModel) {
        chatClient = ChatClient.builder(chatModel).build();
    }

    @Test
    void testBoolOut() {
        Boolean isComplain = chatClient
                .prompt()
                .system("""
                        请判断用户信息是否表达了投诉意图?
                        只能用 true 或 false 回答，不要输出多余内容
                        """)
                .user("你们家的快递迟迟不到,我要退货！")
                .call()
                .entity(Boolean.class);

        // 分支逻辑
        if (Boolean.TRUE.equals(isComplain)) {
            System.out.println("用户是投诉，转接人工客服！");
        } else {
            System.out.println("用户不是投诉，自动流转客服机器人。");
            // todo 继续调用 客服ChatClient进行对话
        }
    }


    @Test
    void testEntityOut() {
        Address address = chatClient.prompt()
                .system("""
                        请从下面这条文本中提取收货信息
                        """)
                .user("收货人：张三，电话13588888888，地址：浙江省杭州市西湖区文一西路100号8幢202室")
                .call()
                .entity(Address.class);
        System.out.println(address);
    }

    @Test
    void testLowEntityOut(@Autowired DashScopeChatModel chatModel) {
        BeanOutputConverter<ActorsFilms> beanOutputConverter =
                new BeanOutputConverter<>(ActorsFilms.class);

        String format = beanOutputConverter.getFormat();

        String actor = "周星驰";

        String template = """
        提供5部{actor}导演的电影.
        {format}
        """;

        PromptTemplate promptTemplate = PromptTemplate.builder().template(template).variables(Map.of("actor", actor, "format", format)).build();
        ChatResponse response = chatModel.call(
                promptTemplate.create()
        );

        ActorsFilms actorsFilms = beanOutputConverter.convert(response.getResult().getOutput().getText());
        System.out.println(actorsFilms);
    }
}
