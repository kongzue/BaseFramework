package com.kongzue.baseframework.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import com.kongzue.baseframework.BaseFrameworkSettings;

import java.util.Locale;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2018/12/24 16:40
 */
public class LanguageUtil {
    
    public static Context wrap(Context context) {
        
        if (BaseFrameworkSettings.selectLocale == null) {
            return context;
        } else {
            Locale selectLocale = BaseFrameworkSettings.selectLocale;
            
            Resources res = context.getResources();
            Configuration configuration = res.getConfiguration();
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                configuration.setLocale(selectLocale);
                LocaleList localeList = new LocaleList(selectLocale);
                LocaleList.setDefault(localeList);
                configuration.setLocales(localeList);
                context = context.createConfigurationContext(configuration);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                configuration.setLocale(selectLocale);
                context = context.createConfigurationContext(configuration);
            }
            
            return new ContextWrapper(context);
        }
    }
}
