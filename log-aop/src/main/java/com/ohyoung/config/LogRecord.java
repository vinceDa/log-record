package com.ohyoung.config;

import java.lang.annotation.*;

/**
 * 记录操作日志相关信息
 * @author ouyb01
 * @date 2022/1/10 11:37
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LogRecord {
    /**
     * 操作日志的文本模板, 必填
     */
    String content();

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
    String remark() default "";

    /**
     * 记录日志的条件
     */
    String condition() default "";

}
