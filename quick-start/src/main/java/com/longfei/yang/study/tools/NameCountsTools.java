package com.longfei.yang.study.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class NameCountsTools {

    @Tool(description = "长沙有多少名字的数量")
    String LocationNameCounts(@ToolParam(description = "名字") String name) {
        return "10个";
    }


    @Tool(description = "获取指定位置天气,根据位置自动推算经纬度")
    public String getAirQuality(@ToolParam(description = "纬度") double latitude,
                                @ToolParam(description = "经度") double longitude) {
        System.out.println("纬度：" + latitude + "经度：" + longitude);
        return "天晴";
    }


}