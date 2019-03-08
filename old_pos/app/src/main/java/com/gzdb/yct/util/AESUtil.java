package com.gzdb.yct.util;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {

    /**
     * 算法/模式/填充                 16字节加密后数据长度       不满16字节加密后长度
     * AES/CBC/NoPadding                   16                          不支持
     * AES/CBC/PKCS5Padding                32                          16
     * AES/CBC/ISO10126Padding             32                          16
     * AES/CFB/NoPadding                   16                          原始数据长度
     * AES/CFB/PKCS5Padding                32                          16
     * AES/CFB/ISO10126Padding             32                          16
     * AES/ECB/NoPadding                   16                          不支持
     * AES/ECB/PKCS5Padding                32                          16
     * AES/ECB/ISO10126Padding             32                          16
     * AES/OFB/NoPadding                   16                          原始数据长度
     * AES/OFB/PKCS5Padding                32                          16
     * AES/OFB/ISO10126Padding             32                          16
     * AES/PCBC/NoPadding                  16                          不支持
     * AES/PCBC/PKCS5Padding               32                          16
     * AES/PCBC/ISO10126Padding            32                          16
     */

    // /** 算法/模式/填充 **/
    private static final String CipherMode = "AES/ECB/NoPadding";

    // /** 创建密钥 **/
    private static SecretKeySpec createKey(String key) {
        byte[] data = null;
        if (key == null) {
            key = "";
        }
        StringBuffer sb = new StringBuffer(16);
        sb.append(key);
        while (sb.length() < 16) {
            sb.append("0");
        }
        if (sb.length() > 16) {
            sb.setLength(16);
        }

        try {
            data = sb.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new SecretKeySpec(data, "AES");
    }

    private static IvParameterSpec createIV(String password) {
        byte[] data = null;
        if (password == null) {
            password = "";
        }
        StringBuffer sb = new StringBuffer(16);
        sb.append(password);
        while (sb.length() < 16) {
            sb.append("0");
        }
        if (sb.length() > 16) {
            sb.setLength(16);
        }

        try {
            data = sb.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new IvParameterSpec(data);
    }

    // /** 加密字节数据 **/
    public static byte[] encrypt(byte[] content, String password, String iv) {
        try {
            SecretKeySpec key = createKey(password);
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.ENCRYPT_MODE, key, createIV(iv));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // /** 加密字节数据 **/
    public static byte[] encrypt(byte[] content, byte[] key) {
        try {
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // /** 加密(结果为16进制字符串) **/
    public static String encrypt(String content, String password, String iv) {
        byte[] data = null;
        try {
            data = content.getBytes("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        data = encrypt(data, password, iv);
        String result = TUtil.bytes2HexString(data);
        return result;
    }

    // /** 解密字节数组 **/
    public static byte[] decrypt(byte[] content, byte[] key) {
        try {
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // /** 解密字节数组 **/
    public static byte[] decrypt(byte[] content, String password, String iv) {
        try {
            SecretKeySpec key = createKey(password);
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.DECRYPT_MODE, key, createIV(iv));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // /** 解密 **/
    public static String decrypt(String content, String password, String iv) {
        byte[] data = null;
        try {
            data = TUtil.hexString2Bytes(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        data = decrypt(data, password, iv);
        if (data == null)
            return null;
        String result = null;
        try {
            result = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
