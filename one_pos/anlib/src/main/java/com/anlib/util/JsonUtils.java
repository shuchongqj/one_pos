package com.anlib.util;


import com.alibaba.fastjson.JSON;

import java.util.ArrayList;

/**
 * Created by zhumg on 3/15.
 */
public class JsonUtils {


    public static <T> T fromJson(String json, Class<T> type) {
        return JSON.parseObject(json, type);
    }

    private static String getToken(String json) {
        StringBuilder buf = new StringBuilder();
        boolean isInYinHao = false;
        while (json.length() > 0) {
            String token = json.substring(0, 1);
            json = json.substring(1);

            if ((!isInYinHao) && (
                    (token
                            .equals(":")) ||
                            (token.equals("{")) || (token.equals("}")) ||
                            (token
                                    .equals("[")) ||
                            (token.equals("]")) ||
                            (token
                                    .equals(",")))) {
                if (buf.toString().trim().length() != 0) {
                    break;
                }
                buf.append(token);
                break;
            }

            if (token.equals("\\")) {
                buf.append(token);
                buf.append(json.substring(0, 1));
                json = json.substring(1);
                continue;
            }
            if (token.equals("\"")) {
                buf.append(token);
                if (isInYinHao) {
                    break;
                }
                isInYinHao = true;
                continue;
            }

            buf.append(token);
        }
        return buf.toString();
    }

    private static void doFill(StringBuilder buf, int count, String fillStringUnit) {
        buf.append("\n");
        for (int i = 0; i < count; i++) {
            buf.append(fillStringUnit);
        }
    }

    public static String formatJson(String json) {
        String fillStringUnit = "\t";
        if ((json == null) || (json.trim().length() == 0)) {
            return "";
        }

        int fixedLenth = 0;
        ArrayList tokenList = new ArrayList();

        String jsonTemp = json;

        while (jsonTemp.length() > 0) {
            String token = getToken(jsonTemp);
            jsonTemp = jsonTemp.substring(token.length());
            token = token.trim();
            tokenList.add(token);
        }

        for (int i = 0; i < tokenList.size(); i++) {
            String token = (String) tokenList.get(i);
            int length = token.getBytes().length;
            if ((length > fixedLenth) && (i < tokenList.size() - 1) && (((String) tokenList.get(i + 1)).equals(":"))) {
                fixedLenth = length;
            }
        }

        StringBuilder buf = new StringBuilder();
        int count = 0;
        for (int i = 0; i < tokenList.size(); i++) {
            String token = (String) tokenList.get(i);

            if (token.equals(",")) {
                buf.append(token);
                doFill(buf, count, fillStringUnit);
            } else if (token.equals(":")) {
                buf.append(" ").append(token).append(" ");
            } else if (token.equals("{")) {
                String nextToken = (String) tokenList.get(i + 1);
                if (nextToken.equals("}")) {
                    i++;
                    buf.append("{ }");
                } else {
                    count++;
                    buf.append(token);
                    doFill(buf, count, fillStringUnit);
                }

            } else if (token.equals("}")) {
                count--;
                doFill(buf, count, fillStringUnit);
                buf.append(token);
            } else if (token.equals("[")) {
                String nextToken = (String) tokenList.get(i + 1);
                if (nextToken.equals("]")) {
                    i++;
                    buf.append("[ ]");
                } else {
                    count++;
                    buf.append(token);
                    doFill(buf, count, fillStringUnit);
                }

            } else if (token.equals("]")) {
                count--;
                doFill(buf, count, fillStringUnit);
                buf.append(token);
            } else {
                buf.append(token);

                if ((i < tokenList.size() - 1) && (((String) tokenList.get(i + 1)).equals(":"))) {
                    int fillLength = fixedLenth - token.getBytes().length;
                    if (fillLength > 0) {
                        for (int j = 0; j < fillLength; j++) {
                            buf.append(" ");
                        }
                    }
                }
            }
        }
        return buf.toString();
    }
}
