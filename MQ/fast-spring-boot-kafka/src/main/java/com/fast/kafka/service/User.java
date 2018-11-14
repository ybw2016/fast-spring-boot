package com.fast.kafka.service;

import java.util.Date;

import lombok.Data;

/**
 * @author bowen.yan
 * @date 2018-11-12
 */
@Data
public class User {
    private Long id;
    private String msg;
    private Date sendTime;
}
