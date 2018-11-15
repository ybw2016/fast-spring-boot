package com.fast.springboot.basic.annotation.encrypt;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DesUtil {
    private static final String DES = "DES";

    /**
     * DES加密KEY.
     */
    private static final String DES_KEY = "des_key_123"; // 自行修改

    /**
     * 加密.
     *
     * @param src 数据源
     * @param key 密钥，长度必须是8的倍数
     * @return 返回加密后的数据
     */
    public static byte[] encrypt(byte[] src, byte[] key) {
        // DES算法要求有一个可信任的随机数源
        try {
            SecureRandom sr = new SecureRandom();
            // 从原始密匙数据创建DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key);
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成
            // 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey securekey = keyFactory.generateSecret(dks);
            // Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance(DES);
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
            // 现在，获取数据并加密
            // 正式执行加密操作
            return cipher.doFinal(src);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * DES数据加密.
     *
     * @param data origin String
     * @param key  密钥
     * @return encrypted String
     */
    public static String encrypt(String data, String key) {
        if (data != null) {
            try {
                return byte2hex(encrypt(data.getBytes(), key.getBytes()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    /**
     * 解密.
     *
     * @param src 数据源
     * @param key 密钥，长度必须是8的倍数
     * @return 返回解密后的原始数据
     */
    public static byte[] decrypt(byte[] src, byte[] key) {
        try {
            //DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            // 从原始密匙数据创建一个DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key);
            // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
            // 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey securekey = keyFactory.generateSecret(dks);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance(DES);
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
            // 现在，获取数据并解密
            // 正式执行解密操作
            return cipher.doFinal(src);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * DES数据解密.
     *
     * @param data encrypted String
     * @param key  密钥
     * @return origin String
     */
    public static String decrypt(String data, String key) {
        return new String(decrypt(hex2byte(data.getBytes()), key.getBytes()));
    }

    /**
     * 二行制转字符串.
     *
     * @param bytes byte array
     * @return String value
     */
    private static String byte2hex(byte[] bytes) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; bytes != null && n < bytes.length; n++) {
            stmp = Integer.toHexString(bytes[n] & 0XFF);
            if (stmp.length() == 1) {
                hs.append('0');
            }
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }

    private static byte[] hex2byte(byte[] bytes) {
        if ((bytes.length % 2) != 0) {
            throw new IllegalArgumentException();
        }
        byte[] b2 = new byte[bytes.length / 2];
        for (int n = 0; n < bytes.length; n += 2) {
            String item = new String(bytes, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    /**
     * for test.
     *
     * @param args args
     */
    public static void main(String[] args) {
        String test = "L201811150001";
        //DES加密
        test = encrypt(test, DES_KEY);
        System.out.println("加密后的数据：" + test);

        //DES解密
        test = decrypt(test, DES_KEY);
        System.out.println("解密后的数据：" + test);
    }
}
