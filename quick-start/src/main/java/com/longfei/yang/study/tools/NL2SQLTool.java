package com.longfei.yang.study.tools;

import com.aispace.supersql.builder.RagOptions;
import com.aispace.supersql.engine.SpringSqlEngine;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NL2SQLTool {

    @Resource
    private DashScopeChatModel dashScopeChatModel;
    @Resource
    private SpringSqlEngine sqlEngine;

    @Tool(
            name = "generateSqlAndQuery",
            description = "用户的查询是否涉及统计数据、求和、计数、平均值等聚合操作？"
    )
    public String generateSqlAndQuery(@ToolParam(description = "用户的问题") String message) {
        String sql = sqlEngine
                .setChatModel(dashScopeChatModel)
                .setOptions(RagOptions.builder()
                        .topN(5)
                        .rerank(false)
                        .limitScore(0.1)
                        .build())
                .generateSql(message);
        List<Map<String, Object>> result = sqlEngine.executeSql(sql);
        return JSON.toJSONString(result);
    }
}
