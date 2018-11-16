package com.fast.springboot.basic.util;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.Charset;

/**
 * @author bowen.yan
 * @date 2018-11-16
 */
public class Base64Util {
    public static Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    public static String encrypt(String input) {
        return new String(Base64.encodeBase64(input.getBytes(DEFAULT_CHARSET)), DEFAULT_CHARSET);
    }

    public static String decrypt(String input) {
        return new String(Base64.decodeBase64(input.getBytes(DEFAULT_CHARSET)), DEFAULT_CHARSET);
    }

    public static void main(String[] args) {
        String group = "lisi";
        System.out.println("encrypt name -> " + encrypt(group));
        System.out.println("decrypt name -> " + decrypt(encrypt(group)));
        /*
        // 运行结果
        encrypt name -> emhhbmdzYW4=
        decrypt name -> zhangsan
        */
    }
}
