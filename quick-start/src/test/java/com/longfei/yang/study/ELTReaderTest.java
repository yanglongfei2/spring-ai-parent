package com.longfei.yang.study;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.List;

@SpringBootTest
class ELTReaderTest {

    @Test
    void testReaderText(@Value("classpath:rag/terms-of-service.txt") Resource resource) {

        List<Document> docs = new TextReader(resource).read();
        for (Document doc : docs) {
            System.out.println(doc.getText());
        }

    }
}
