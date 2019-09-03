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

    public static LangType getCurrentLangType() {
        LangType langType = null;
        try {
            langType = DataCache.get(LANG_TYPE_KEY, LangType.ENGLISH);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (langType == null) {
            langType = LangType.ENGLISH;
        }
        return langType;
    }

    public enum LangType {
        FOLLOW_SYSTEM, SIMPLE_CHINESE, TRADITION_CHINESE, ENGLISH
    }

    public static void setCurrentLangType(LangType langType) {
        DataCache.put(LANG_TYPE_KEY, langType);
        RxBus.get().post(new LanguageChangeEvent());
    }

    private static Locale getCurrentLang(LangType type) {
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

    public static Context getAttachBaseContext(Context context) {
        return getAttachBaseContext(context, getCurrentLangType());
    }

    private static Context getAttachBaseContext(Context context, LangType langType) {
        //Android 7.0之后需要用另一种方式更改res语言,即配置进context中
        Log.d("pigdreams", "配置语言...默认locale=" + Locale.getDefault() + ";用户设置的为=" + getCurrentLang(langType));
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
        Locale locale = getCurrentLang(type);
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }

    private static void changeResLanguage(Context context, LangType type) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = getCurrentLang(type);
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

}
