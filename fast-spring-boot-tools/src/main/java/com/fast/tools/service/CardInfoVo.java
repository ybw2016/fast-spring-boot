package com.fast.tools.service;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author bowen.yan
 * @since 2019-09-09
 */
@Data
public class CardInfoVo implements Serializable {
    //@NotBlank(message = "该字段不能为空")
    @Excel(name = "CARDBINKEY")
    private String CARDBINKEY;

    //@NotBlank(message = "该字段不能为空")
    @Excel(name = "ISSNAME")
    private String ISSNAME;
}
