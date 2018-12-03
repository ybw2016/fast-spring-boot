package com.fast.springcloud.provider.dto.response;

import java.util.List;

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
public class UserListQueryRsp implements Response {
    private List<UserQueryRsp> list;
}
