package com.fast.springcloud.provider.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author bowen.yan
 * @date 2018-12-03
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserQueryRsp implements Response {
    private String username;
    private boolean gender;
    private int age;
    private String address;
}
