package com.anlib.util;

import java.util.List;

public class StringUtils {

    public static boolean isEmpty(String info) {
        return info == null || info.trim().length() < 1;
    }

    public static boolean isListEmpty(List list) {
        return list == null || list.size() < 1;
    }
}
