package com.ohyoung.config;

import java.lang.reflect.Method;

/**
 * @author ohYoung
 * @date 2022/1/26 19:20
 */
public class Parst {

    public static void main(String[] args) {
        try {
            // 获取@LogRecord中的方法
            Class<?> testCls = Class.forName("com.ohyoung.config.AnnotationTest");
            Method[] declaredMethods = testCls.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                boolean isLogRecordExist = declaredMethod.isAnnotationPresent(LogRecord.class);
                if (isLogRecordExist) {
                    declaredMethod.setAccessible(true);
                    LogRecord logRecord = declaredMethod.getAnnotation(LogRecord.class);
                    System.out.println(logRecord.toString());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
