package com.longfei.yang.study.config;

import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class VectorStoreConfig {


//    @Bean
//    public VectorStore vectorStore(DashScopeEmbeddingModel embeddingModel) {
//        SimpleVectorStore.SimpleVectorStoreBuilder builder = SimpleVectorStore.builder(embeddingModel);
//        return builder.build();
//    }
//
//
//    @Bean
//    CommandLineRunner ingestTermOfServiceToVectorStore(DashScopeEmbeddingModel embeddingModel, VectorStore vectorStore,
//                                                       @Value("classpath:rag/terms-of-service.txt") Resource termsOfServiceDocs) {
//
//        return args -> {
//            vectorStore.write(                                  // 3.写入
//                    new TokenTextSplitter().transform(          // 2.转换
//                            new TextReader(termsOfServiceDocs).read())  // 1.读取
//            );
//
//        };
//    }
}
