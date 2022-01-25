package com.ohyoung;

import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

/**
 * 注解解析类
 * @author ouyb01
 * @date 2022/1/11 9:36
 */
public class LogRecordOperationSource {

    public void parse(Method method, Class<?> targetClass) {
        try {
            // 获取@LogRecord中的方法
            Method[] declaredMethods = targetClass.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                // 过滤掉不含@LogRecord注解的方法
                boolean isAnnotationExist = declaredMethod.isAnnotationPresent(LogRecord.class);
                if (isAnnotationExist) {
                    declaredMethod.setAccessible(true);
                    // 获取方法上的注解
                    LogRecord logRecord = declaredMethod.getAnnotation(LogRecord.class);
                    if (logRecord == null) {
                        return;
                    }
                    // 解析注解上对应的信息

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
