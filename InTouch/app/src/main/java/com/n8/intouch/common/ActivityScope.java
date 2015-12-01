package com.n8.intouch.common;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by n8 on 11/22/15.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScope {}
