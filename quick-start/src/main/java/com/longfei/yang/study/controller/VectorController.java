package com.longfei.yang.study.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

public class VectorController {


    private final VectorStore vectorStore;
    private final ChatClient chatClient;

    public VectorController(VectorStore vectorStore, ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
        this.vectorStore = vectorStore;

        this.chatClient = chatClientBuilder
                .defaultSystem("""
					   您是“图灵”航空公司的客户聊天支持代理。请以友好、乐于助人且愉快的方式来回复。
                       您正在通过在线聊天系统与客户互动。
                       
                        在提供有关预订或取消预订的信息之前，您必须始终
                        从用户处获取以下信息：预订号、客户姓名。
                        在询问用户之前，请检查消息历史记录以获取此信息。
                        在更改或退订之前，请先获取预订信息待用户回复确定之后才进行更改或退订的function-call。 
                       请讲中文。
                       今天的日期是 {current_date}.
					""")
                .defaultAdvisors(
                        PromptChatMemoryAdvisor.builder(chatMemory).build(),
                        new QuestionAnswerAdvisor(vectorStore))
                .defaultTools("getBookingDetails", "changeBooking", "cancelBooking") // FUNCTION CALLING
                .build();
    }

    /**
     * 嵌入文件
     *
     * @return 是否成功
     */
    @PostMapping("embedding")
    public Boolean embedding() {
        Document doc = Document.builder()
                .text("""
                预订航班:
                - 通过我们的网站或移动应用程序预订。
                - 预订时需要全额付款。
                - 确保个人信息（姓名、ID 等）的准确性，因为更正可能会产生 25 的费用。
                """)
                .build();
        Document doc2 = Document.builder()
                .text("""
                取消预订:
                - 最晚在航班起飞前 48 小时取消。
                - 取消费用：经济舱 75 美元，豪华经济舱 50 美元，商务舱 25 美元。
                - 退款将在 7 个工作日内处理。
                """)
                .build();
        vectorStore.add(Arrays.asList(doc, doc2));
        return true;
    }

    /**
     * 查询向量数据库
     *
     * @param query 用户的提问
     * @return 匹配到的文档
     */

    @GetMapping("query")
    public List<Document> query(@RequestParam String query) {
        return vectorStore.similaritySearch(query);
    }
}
