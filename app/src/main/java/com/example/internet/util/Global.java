package com.example.internet.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Global {
    public static final String API_URL = "http://10.0.2.2:5001";
//    public static final String API_URL = "http://129.211.216.10:5001";
//    public static final String API_URL = "http://183.173.147.116:5000";
    public static final String EMPTY_AVATAR_URL = API_URL + "/static/default.png";

    public static final String VIDEO_TEST_URL = "https://media.geeksforgeeks.org/wp-content/uploads/20201217192146/Screenrecorder-2020-12-17-19-17-36-828.mp4?_=1";
    public static final int POLLING_INTERVAL = 3;

    public static final HashMap<String, String> TAG_CODE2STR_MAP;

    static{
        TAG_CODE2STR_MAP = new HashMap<>();
        TAG_CODE2STR_MAP.put("xyzx", "校园资讯");
        TAG_CODE2STR_MAP.put("xxky", "学习科研");
        TAG_CODE2STR_MAP.put("esjy", "二手交易");
        TAG_CODE2STR_MAP.put("chwl", "吃喝玩乐");
    }
    public static final HashMap<String, String> TAG_STR2CODE_MAP;

    static{
        TAG_STR2CODE_MAP = new HashMap<>();
        TAG_STR2CODE_MAP.put("校园资讯", "xyzx");
        TAG_STR2CODE_MAP.put("学习科研", "xxky");
        TAG_STR2CODE_MAP.put("二手交易", "esjy");
        TAG_STR2CODE_MAP.put("吃喝玩乐", "chwl");
    }

    public static final List<String> TAG_LIST;

    static{
        TAG_LIST = new ArrayList<>();
        TAG_LIST.add("校园资讯");
        TAG_LIST.add("学习科研");
        TAG_LIST.add("二手交易");
        TAG_LIST.add("吃喝玩乐");
    }

    public static final List<String> FILTER_LIST;

    static {
        FILTER_LIST = new ArrayList<>();
        FILTER_LIST.add("time");
        FILTER_LIST.add("like");
        FILTER_LIST.add("comment");
    }
}
