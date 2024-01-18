package com.fast.springboot.basic.excel;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yanbowen
 * @date 2024-01-18
 */
@Data
public class ExcelData {
    private String userId;
    private Long bizOrderId;
    private BigDecimal amount;
}
