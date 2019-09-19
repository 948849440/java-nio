package com.summer.study.netty.base.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface MessageId {
    @AliasFor(
            annotation = Component.class)
    String value() default "";

    int id();
}
