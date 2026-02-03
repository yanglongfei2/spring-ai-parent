package com.longfei.yang.study.tools;

import com.longfei.yang.study.service.TicketService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TicketTools {


    @Autowired
    private TicketService ticketService;



//    @Tool(description = "退票")
//    public String cancel(
//            // @ToolParam告诉大模型参数的描述
//            @ToolParam(description = "预定号，可以是纯数字") String ticketNumber,
//            @ToolParam(description = "真实人名（必填，必须为人的真实姓名，严禁用其他信息代替；如缺失请传null）") String name
//    ) {
//        return ticketService.cancel(ticketNumber, name);
//    }



    @Tool(description = "退票")
    @PreAuthorize("hasRole('ADMIN')")
    public String cancel(
            // @ToolParam告诉大模型参数的描述
            @ToolParam(description = "预定号，可以是纯数字") String ticketNumber,
            @ToolParam(description = "真实人名（必填，必须为人的真实姓名，严禁用其他信息代替；如缺失请传null）") String name
    ) {
        // 当前登录用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // 先查询 --->先校验
        ticketService.cancel(ticketNumber, name);
        return username+"退票成功！";
    }



}
