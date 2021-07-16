package com.example.userinfodemo.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UserInfoUtil {

    public static String mapToJson(Object map) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        return gson.toJson(map);
    }
}
