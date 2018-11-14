package com.fast.rxjava.controller;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author bowen.yan
 * @date 2018-11-13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {
    private String name;
    private Date createTime;
}
