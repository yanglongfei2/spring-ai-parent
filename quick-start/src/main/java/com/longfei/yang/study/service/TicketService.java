package com.longfei.yang.study.service;

import org.springframework.stereotype.Service;

@Service
public class TicketService {


    public String cancel(String ticketNumber, String name) {
       return name + "票据单号：" + ticketNumber + ", 退票成功！";
    }
}
