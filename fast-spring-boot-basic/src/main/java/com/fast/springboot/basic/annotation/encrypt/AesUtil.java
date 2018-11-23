package com.fast.springboot.basic.annotation.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import java.nio.charset.Charset;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author bowen.yan
 * @date 2018-11-15
 */
public class AesUtil {
    private static final String SECRET = "tshsro3wasfdblesdf";
    private static final String CHARSET = "UTF-8";

    private static Cipher createAesCipher() throws Exception {
        return Cipher.getInstance("AES");
    }

    public static String encryptByAes(String input) {
        try {
            Cipher aesCipher = createAesCipher();
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(input.getBytes());
            keyGenerator.init(128, random);
            SecretKey secretKey = keyGenerator.generateKey();
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
            aesCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] bytes = aesCipher.doFinal(input.getBytes(CHARSET));
            return Hex.encodeHexString(bytes);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("加密失败！-> input: " + input);
        }
    }

    public static String decryptByAes(String encrypt) {
        try {
            byte[] bytes = Hex.decodeHex(encrypt);
            Cipher aesCipher = createAesCipher();
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(encrypt.getBytes());
            keyGenerator.init(128, random);
            SecretKey secretKey = keyGenerator.generateKey();
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
            aesCipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            return new String(aesCipher.doFinal(bytes), CHARSET);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("解密失败！-> encrypt: " + encrypt);
        }
    }

    public static void main(String[] args) {
        String orderId = "L201811150001aabbdsfsf";
        String encryptStr = encryptByAes(orderId);
        System.out.println("encryptStr -> " + encryptStr);
        String rawOrderId = decryptByAes(encryptStr);
        System.out.println("decryptStr -> " + rawOrderId);

//        try {
//            String data1 = new String(Base64.encodeBase64(orderId.getBytes(Charset.forName("utf-8"))));
//            String data2 = new String(Base64.decodeBase64(data1.getBytes(Charset.forName("utf-8"))));
//            System.out.println(data1);
//            System.out.println(data2);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
    }
}
