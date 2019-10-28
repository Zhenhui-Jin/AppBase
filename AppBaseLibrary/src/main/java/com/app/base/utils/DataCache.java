package com.app.base.utils;

import android.content.Context;

import com.orhanobut.hawk.Hawk;

/**
 * @Description 数据缓存  https://github.com/orhanobut/hawk
 * @Author Zhenhui
 * @Time 2019/8/15 18:28
 */
public class DataCache {

    public static void init(Context context) {
        Hawk.init(context).build();
    }

    public static <T> void put(String key, T value) {
        Hawk.put(key, value);
    }

    public static <T> T get(String key) {
        return Hawk.get(key);
    }

    public static <T> T get(String key, T defaultValue) {
        return Hawk.get(key, defaultValue);
    }

    public static void delete(String key) {
        Hawk.delete(key);
    }
}
