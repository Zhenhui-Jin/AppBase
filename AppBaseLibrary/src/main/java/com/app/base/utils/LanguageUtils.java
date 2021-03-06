package com.app.base.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import com.app.base.bus.RxBus;
import com.app.base.bus.event.LanguageChangeEvent;

import java.util.Locale;

public class LanguageUtils {

    public static final String LANG_TYPE_KEY = "LangType";

    public enum LangType {
        FOLLOW_SYSTEM, //跟随系统
        SIMPLE_CHINESE, //简体
        TRADITION_CHINESE, //繁体
        ENGLISH //英文
    }

    /**
     * 获取语言类型
     *
     * @return
     */
    public static LangType getCurrentLangType() {
        LangType langType = null;
        try {
            langType = DataCache.get(LANG_TYPE_KEY,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (langType == null) {

            String region = Locale.getDefault().getCountry();
            if (region.equals("CN")) {
                langType = LangType.SIMPLE_CHINESE;
            } else if (region.equals("TW") || region.equals("HK")|| region.equals("MO")) {
                langType = LangType.TRADITION_CHINESE;
            } else {
                langType = LangType.ENGLISH;
            }

            DataCache.put(LANG_TYPE_KEY, langType);

        }
        return langType;
    }

    /**
     * 获取语言名称
     *
     * @return
     */
    public static String getCurrentLocaleName() {
        Locale locale = getLocale(getCurrentLangType());
        String language = locale.getLanguage();
        return language;
    }

    /**
     * 设置语言类型
     *
     * @param langType
     */
    public static void setCurrentLangType(LangType langType) {
        //存储语言类型
        DataCache.put(LANG_TYPE_KEY, langType);
        //发送广播
        RxBus.get().post(new LanguageChangeEvent());
    }

    public static Locale getLocale(LangType type) {
        switch (type) {
            case FOLLOW_SYSTEM:
                return Locale.getDefault();
            case SIMPLE_CHINESE:
                return Locale.SIMPLIFIED_CHINESE;
            case TRADITION_CHINESE:
                return Locale.TRADITIONAL_CHINESE;
            case ENGLISH:
                return Locale.ENGLISH;
            default:
                return Locale.SIMPLIFIED_CHINESE;
        }
    }

    /**
     * 返回修改语言后的上下文
     *
     * @param context
     * @return
     */
    public static Context getAttachBaseContext(Context context) {
        return getAttachBaseContext(context, getCurrentLangType());
    }

    private static Context getAttachBaseContext(Context context, LangType langType) {
        //Android 7.0之后需要用另一种方式更改res语言,即配置进context中
        Log.d("LanguageUtils", "配置语言...默认locale=" + Locale.getDefault() + ";用户设置的为=" + getLocale(langType));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context = updateResources(context, langType);
        } else {
            //7.0之前的更新语言资源方式
            changeResLanguage(context, langType);
        }
        return context;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, LangType type) {
        Resources resources = context.getResources();
        Locale locale = getLocale(type);
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }

    private static void changeResLanguage(Context context, LangType type) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = getLocale(type);
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

}
