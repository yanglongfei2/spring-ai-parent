package com.longfei.yang.study;


import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LLMTest {

    @Test
    void testQwen(@Autowired DashScopeChatModel dashScopeChatModel) {

        String content = dashScopeChatModel.call("你好你是谁");
        System.out.println(content);
    }


}
