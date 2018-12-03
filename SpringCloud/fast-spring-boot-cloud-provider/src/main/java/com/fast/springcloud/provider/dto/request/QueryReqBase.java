package com.fast.springcloud.provider.dto.request;

import lombok.Data;

/**
 * 公共请求报文
 *
 * @author bowen.yan
 * @date 2018-12-03
 */
@Data
public class QueryReqBase {
    private String serialId;
    private String fromSystem;
    private Long timestamp;
    private Integer pageNo;
    private Integer pageSize;
}
