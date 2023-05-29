package com.example.internet.util;

import java.util.HashMap;

public class Global {
    public static final String API_URL = "http://10.0.2.2:5001";
    public static final String EMPTY_AVATAR_URL = API_URL + "/static/default.png";

    public static final HashMap<String, String> TAG_MAP;

    static{
        TAG_MAP = new HashMap<>();
        TAG_MAP.put("esjy", "二手交易");
        TAG_MAP.put("xyzx", "校园资讯");
        TAG_MAP.put("xxky", "学习科研");
        TAG_MAP.put("chwl", "吃喝玩乐");
        TAG_MAP.put("", "default");
    }
}
