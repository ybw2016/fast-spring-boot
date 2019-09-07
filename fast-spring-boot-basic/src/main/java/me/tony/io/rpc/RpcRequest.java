package me.tony.io.rpc;

import lombok.Data;

import java.io.Serializable;

/**
 * 输入描述.
 *
 * @author : weibo
 * @version : v1.0
 * @since : 2019/8/14 16:22
 */
@Data
public class RpcRequest implements Serializable {

    private Class clz;
    private String methodName;
    private Class[] paramType;
    private Object[] args;
}
