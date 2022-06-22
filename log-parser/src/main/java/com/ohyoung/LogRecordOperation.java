package com.ohyoung;

import java.lang.reflect.Method;

/**
 * 日志操作实体, 记录被@LogRecord标注的方法信息
 * Method method,  Object[] args, Class<?> targetClass, Object ret, String errorMsg, BeanFactory beanFactory
 * @author ohYoung
 * @date 2022/4/4 21:29
 */
public class LogRecordOperation {

    /**
     * @LogRecord注解 定义的具有业务意义的key, 例如success, fail等, 这里用key、value的形式接收
     */
    private String key;
    /**
     * @LogRecord注解 中的key对应的值, 目前考虑到只有String类型才会进行SPEL解析
     */
    private String value;

    public LogRecordOperation(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
