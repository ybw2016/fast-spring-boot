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
        System.out.println("encrypt name -> " + encrypt("zhangsan"));
        System.out.println("encrypt password -> " + encrypt("123456"));
        System.out.println("decrypt name -> " + decrypt(encrypt("zhangsan")));
        System.out.println("encrypt name lisi -> " + encrypt("lisi"));

        System.out.println();
        System.out.println("encrypt addressCode -> " + encrypt("110022"));
        System.out.println("decrypt addressCode -> " + decrypt(encrypt("110022")));
        System.out.println("encrypt addressCode -> " + encrypt("110033"));
        System.out.println("decrypt addressCode -> " + decrypt(encrypt("110033")));

        /*
        // 运行结果
            encrypt name -> emhhbmdzYW4=
            encrypt password -> MTIzNDU2
            decrypt name -> zhangsan
            encrypt name lisi -> bGlzaQ==

            encrypt addressCode -> MTEwMDIy
            decrypt addressCode -> 110022
            encrypt addressCode -> MTEwMDMz
            decrypt addressCode -> 110033
        */
    }
}
