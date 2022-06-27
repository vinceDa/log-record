package com.ohyoung;

import java.lang.annotation.*;

/**
 * 拦截器，针对 @LogRecord 注解分析出需要记录的操作日志，然后把操作日志持久化
 *
 * @author ouyb01
 * @date 2022/1/10 11:37
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LogRecord {
    /**
     * 操作日志的文本模板, 必填
     */
    String success();

    /**
     * 操作日志失败的文本模板
     */
    String fail() default "";

    /**
     * 操作日志的执行人
     */
    String operator() default "";

    /**
     * 操作日志绑定的业务对象标识, 必填
     */
    String bizNo();

    /**
     * 操作日志的种类
     */
    String category() default "";

    /**
     * 扩展参数, 记录操作日志的修改详情
     */
    String detail() default "";

    /**
     * 记录日志的条件
     */
    String condition() default "";

    boolean isSuccess() default true;

}
