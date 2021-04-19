package com.kongzue.baseframework.interfaces;

import android.graphics.Color;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NavigationBarBackgroundColor {
    int a() default 255;
    int r() default 0;
    int g() default 0;
    int b() default 0;
}