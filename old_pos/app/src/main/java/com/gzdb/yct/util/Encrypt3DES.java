package com.gzdb.yct.util;

/**
 * Created by 40303 on 2017/9/15.
 */

public class Encrypt3DES {

    static {
        System.loadLibrary("native-lib");
    }

    /**
     * 3DES加密
     * @param sour 测试数据
     * @param sourLen 测试数据长度
     * @param key 加密密钥
     * @param keyLen 加密密钥长度
     * @param flg 加密1，解密0
     * @return
     */
    private native byte[] encrypt3DES(byte[] sour,int sourLen,byte[] key,int keyLen,int flg);

    public byte[] encrypt(byte[] sour,int sourLen,byte[] key,int keyLen,int flg){
        return encrypt3DES(sour,sourLen,key,keyLen,flg);
    }
}
