package com.s3groupinc.gateway.api.util;

import java.util.List;

public class ArrayListOfStringsToStringArray {
    public static String[] Convert(List<String> list) {
        int size = list.size();
        String result[] = new String[size];

        for (int j = 0; j < size; j++) {
            result[j] = list.get(j);
        }
        return result;
    }
}