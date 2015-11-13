package com.jsbn.mgr.ui.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityFeature {
	boolean no_title() default true;
	boolean full_screen() default false;
	int layout() default 0;
	int statusBarColor() default -1;
}
