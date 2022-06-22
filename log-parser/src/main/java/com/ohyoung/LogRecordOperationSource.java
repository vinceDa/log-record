package com.ohyoung;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 注解解析类
 * @author ouyb01
 * @date 2022/1/11 9:36
 */
@Component
public class LogRecordOperationSource {

    public boolean isLogRecordAnnotationExist(Class<?> targetClass) {
        // 获取@LogRecord中的方法
        Method[] declaredMethods = targetClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.isAnnotationPresent(LogRecord.class)) {
                return true;
            }
        }
        return false;
    }

    public List<LogRecordOperation> computeLogRecordOperations(Method method, Class<?> targetClass, Object[] args) {
        try {
            // 获取@LogRecord中的方法
            Method[] declaredMethods = targetClass.getDeclaredMethods();
            List<LogRecordOperation> operations = new ArrayList<>();
            for (Method declaredMethod : declaredMethods) {
                // 过滤掉不含@LogRecord注解的方法
                boolean isAnnotationExist = declaredMethod.isAnnotationPresent(LogRecord.class);
                if (isAnnotationExist) {
                    declaredMethod.setAccessible(true);
                    // 获取方法上的注解
                    LogRecord logRecord = declaredMethod.getAnnotation(LogRecord.class);
                    if (Objects.isNull(logRecord)) {
                        return new ArrayList<>();
                    }
                    // 解析注解上对应的信息
                    operations.addAll(parseAnnotation(logRecord, method, targetClass, args));
                }
            }
            return operations;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private List<LogRecordOperation> parseAnnotation(LogRecord logRecord, Method method, Class<?> targetClass, Object[] args) {
        Field[] fields = logRecord.getClass().getDeclaredFields();
        List<LogRecordOperation> operations = new ArrayList<>(fields.length);
        // 正常操作下, 反射不能获取到参数名称, 所以这里直接写死
        operations.add(new LogRecordOperation("success", logRecord.success()));
        operations.add(new LogRecordOperation("fail", logRecord.fail()));
        operations.add(new LogRecordOperation("operator", logRecord.operator()));
        operations.add(new LogRecordOperation("bizNo", logRecord.bizNo()));
        operations.add(new LogRecordOperation("category", logRecord.category()));
        operations.add(new LogRecordOperation("detail", logRecord.detail()));
        operations.add(new LogRecordOperation("condition", logRecord.condition()));
        return operations;
    }
}
