package com.core.util;


import com.gzdb.supermarket.util.Arith;

import java.util.Random;

/**
 * 邀请码生成器，基本原理：<br/>
 * 1）入参用户ID：1 <br/>
 * 2）使用自定义进制转换之后为：V <br/>
 * 3）转换未字符串，并在后面添加'A'：VA <br/>
 * 4）在VA后面再随机补足4位，得到：VAHKHE <br/>
 * 5）反向转换时以'A'为分界线，'A'后面的不再解析 <br/>
 *
 * @author chinahuangxc
 */
public class ShareCodeUtils {

    /**
     * 自定义进制(0,1没有加入,容易与o,l混淆)，数组顺序可进行调整增加反推难度，A用来补位因此此数组不包含A，共31个字符。
     */
    private static final char[] BASE = new char[]{
            'U', 'J', 'M', 'F', 'H', 'R', 'E', '8', 'S', '2', 'V', '4', 'W', 'Y', 'L', 'T',
            'N', '6', 'B', 'G', 'D', 'Z', 'X', '9', 'C', '7', 'P', '5', 'K', 'I', '3', 'Q'};

    /**
     * A补位字符，不能与自定义重复
     */
    private static final char SUFFIX_CHAR = 'A';
    /**
     * 进制长度
     */
    private static final int BIN_LEN = BASE.length;

    /**
     * 生成邀请码最小长度
     */
    //private static final int CODE_LEN = 6;

    /**
     * ID转换为邀请码
     */
    public static String grantInvitationCode(long key, int codeLen) {
        char[] buf = new char[BIN_LEN];
        int charPos = BIN_LEN;

        // 当id除以数组长度结果大于0，则进行取模操作，并以取模的值作为数组的坐标获得对应的字符
        while (key / BIN_LEN > 0) {
            int index = (int) (key % BIN_LEN);
            buf[--charPos] = BASE[index];
            key /= BIN_LEN;
        }
        buf[--charPos] = BASE[(int) (key % BIN_LEN)];
        // 将字符数组转化为字符串
        String result = new String(buf, charPos, BIN_LEN - charPos);

        // 长度不足指定长度则随机补全
        int len = result.length();
        if (len < codeLen) {
            StringBuilder sb = new StringBuilder();
            sb.append(SUFFIX_CHAR);
            Random random = new Random();
            // 去除SUFFIX_CHAR本身占位之后需要补齐的位数
            for (int i = 0; i < codeLen - len - 1; i++) {
                sb.append(BASE[random.nextInt(BIN_LEN)]);
            }
            result += sb.toString();
        }
        return result;
    }

    /**
     * 邀请码解析出ID<br/>
     * 基本操作思路恰好与idToCode反向操作。
     */
    public static Long codeToKey(String code) {
        char[] charArray = code.toCharArray();
        long result = 0L;
        for (int i = 0; i < charArray.length; i++) {
            int index = 0;
            for (int j = 0; j < BIN_LEN; j++) {
                if (charArray[i] == BASE[j]) {
                    index = j;
                    break;
                }
            }
            if (charArray[i] == SUFFIX_CHAR) {
                break;
            }
            if (i > 0) {
                result = result * BIN_LEN + index;
            } else {
                result = index;
            }
        }
        return result;
    }

    /**
     * 创建 打印 的商品条码
     *
     * @param itemId   商品ID
     * @param weight   这个单位是千克，有 0.88 的情况
     * @param discount
     */
    public static String createPrintItemBarcode(String itemId, String weight, double discount) {
        itemId = "9" + itemId;
        weight = changeString(String.valueOf((int) Arith.mul(Double.valueOf(weight), 100)), 4);
        String discountStr = changeString(String.valueOf((int) Arith.mul(discount, 100)), 3);
        //合并
        return itemId + weight + discountStr;
    }

    /**
     * 切换字符串，补位
     *
     * @param s
     * @return
     */
    public static String changeString(String s, int count) {
        int c = count - s.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < c; i++) {
            sb.append("0");
        }
        sb.append(s);
        return sb.toString();
    }

    /**
     * 解释 打印的商品条码
     *
     * @return 如果是标品商品，则返回 [商品条码]，如果是非标品打印出来的称重商品，则返回 [商品条码，重量，折扣]
     */
    public static String[] explainPrintItemBarcode(String itemBarcode) {
        try {
            int length = itemBarcode.length();
            //非标品，称重类
            String discount = itemBarcode.substring(length - 3, length);
            double discountDouble = Arith.div(Double.parseDouble(discount), 100);
            //这里打印，不可能
            discount = String.valueOf(discountDouble);
            String weight = itemBarcode.substring(length - 7, length - 3);
            double weightDouble = Arith.div(Double.parseDouble(weight), 100);
            weight = String.valueOf(weightDouble);
            String itemId = itemBarcode.substring(1, length - 7);
            //如果是原的，则barcode还需要解一次
            return new String[]{itemId, weight, discount};
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 称重商品条码生成时，passportId + 19001 表达是商品条码
     * 商品条码打印时，
     * <p>
     * 商品条码 重量(4位，比喻 0.88千克=88，1千克=100*10克) 折扣(3位表达，比喻，1折=001，1倍=010)
     *
     * @param args
     */
    public static void main(String[] args) {
        String barcode = createPrintItemBarcode("1223212", "0.88", 0.5);
        System.err.println(barcode);
        String strs[] = explainPrintItemBarcode(barcode);
        System.err.println("----------");
        for (int i = 0; i < strs.length; i++) {
            System.err.println(strs[i]);
        }
    }

}