package com.andrei.host.domain.deps;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Andrei on 25/03/2018.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScope {
}
