package com.longfei.yang.study;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
class SimpleVectorStoreTest {

    @BeforeEach
    void init( @Autowired VectorStore vectorStore) {
        // 1. 声明内容文档
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
        // 2. 将文本进行向量化，并且存入向量数据库（无需再手动向量化）
        vectorStore.add(Arrays.asList(doc,doc2));
    }
 

    @Test
    void chatRagTest(@Autowired VectorStore vectorStore, @Autowired DashScopeChatModel chatModel) {

        ChatClient chatClient = ChatClient.builder(chatModel).build();
        String message = "退费需要多少费用?";
        String content = chatClient.prompt().user(message)

                .advisors(
                        new SimpleLoggerAdvisor(),
                        QuestionAnswerAdvisor.builder(vectorStore)
                                .searchRequest(
                                        SearchRequest
                                                .builder().query(message)
                                                .topK(5)
                                                .similarityThreshold(0.3)
                                                .build())
                                .build()


                ).call().content();

        System.out.println(content);

    }



    @Test
    void testRag3(@Autowired VectorStore vectorStore,
                         @Autowired DashScopeChatModel dashScopeChatModel) {


        ChatClient chatClient = ChatClient.builder(dashScopeChatModel)
                .defaultAdvisors(SimpleLoggerAdvisor.builder().build())
                .build();

        // 增强多
        Advisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
                // 查 = QuestionAnswerAdvisor
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .similarityThreshold(0.50)
                        .vectorStore(vectorStore)
                        .build())
                // 检索为空时，返回提示
                .queryAugmenter(ContextualQueryAugmenter.builder()
                        .allowEmptyContext(false)
                        .emptyContextPromptTemplate(PromptTemplate.builder().template("用户查询位于知识库之外。礼貌地告知用户您无法回答").build())
                        .build())
                // 相似性查询内容转换
                .queryTransformers(RewriteQueryTransformer.builder()
                        .chatClientBuilder(ChatClient.builder(dashScopeChatModel))
                        .targetSearchSystem("航空票务助手")
                        .build())
                // 检索后文档监控、操作
                .documentPostProcessors((query, documents) -> {
                    System.out.println("Original query: " + query.text());
                    System.out.println("Retrieved documents: " + documents.size());
                    return documents;
                })
                .build();

        String answer = chatClient.prompt()
                .advisors(retrievalAugmentationAdvisor)
                .user("退一张票大概要多少费用？希望别扣太多啊")
                .call()
                .content();

        System.out.println(answer);
    }



}