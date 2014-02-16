package com.sepage.franceterme.data.xml.util;

import java.util.List;

public class Util {

    public static String listToStringNoBreak (List<String> list) {
        StringBuilder builder = new StringBuilder();
        for (String obj : list) {
            builder.append(obj + ", ");
        }
        return builder.toString();
    }

    public static String listToString (List list) {
        StringBuilder builder = new StringBuilder();
        for (Object obj : list) {
            builder.append("[" + obj.toString() + "], ");
        }
        return builder.toString();
    }
}
