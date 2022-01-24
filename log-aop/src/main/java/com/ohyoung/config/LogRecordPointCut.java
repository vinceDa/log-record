package com.ohyoung.config;

import com.ohyoung.LogRecordOperationSource;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 业务中的 AOP 逻辑大部分是使用 @Aspect 注解实现的，
 * 但是基于注解的 AOP 在 Spring boot 1.5 中兼容性是有问题的，
 * 组件为了兼容 Spring boot1.5 的版本我们手工实现 Spring 的 AOP 逻辑
 * @author ouyb01
 * @date 2022/1/10 12:29
 */
public class LogRecordPointCut extends StaticMethodMatcherPointcut implements Serializable {

    private LogRecordOperationSource logRecordOperationSource;

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        // 解析 这个 method 上有没有 @LogRecordAnnotation 注解，有的话会解析出来注解上的各个参数
        return false;
    }

    void setLogRecordOperationSource(LogRecordOperationSource logRecordOperationSource) {
        this.logRecordOperationSource = logRecordOperationSource;
    }

}
