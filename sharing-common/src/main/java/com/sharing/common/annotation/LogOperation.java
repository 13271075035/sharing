package com.sharing.common.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 *
 * @author tecsmile@outlook.com
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogOperation {

    String apiType() default "";

    String apiPath() default "";

    String value() default "";
}
