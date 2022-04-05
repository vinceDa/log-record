package com.ohyoung;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 注解解析类
 * @author ouyb01
 * @date 2022/1/11 9:36
 */
public class LogRecordOperationSource {

    public List<LogRecordOperation> computeLogRecordOperations(Method method, Class<?> targetClass) {
        try {
            // 获取@LogRecord中的方法
            Method[] declaredMethods = targetClass.getDeclaredMethods();
            List<LogRecordOperation> operations = new ArrayList<>(declaredMethods.length);
            for (Method declaredMethod : declaredMethods) {
                // 过滤掉不含@LogRecord注解的方法
                boolean isAnnotationExist = declaredMethod.isAnnotationPresent(LogRecord.class);
                if (isAnnotationExist) {
                    declaredMethod.setAccessible(true);
                    // 获取方法上的注解
                    LogRecord logRecord = declaredMethod.getAnnotation(LogRecord.class);
                    if (Objects.isNull(logRecord)) {
                        return null;
                    }
                    // 解析注解上对应的信息
                    operations.add(parseAnnotation(logRecord));
                }
                return operations;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private LogRecordOperation parseAnnotation(LogRecord logRecord) {
        LogRecordOperation operation = new LogRecordOperation();
        operation.setBizNo(logRecord.bizNo());
        operation.setSuccess(logRecord.success());
        operation.setCategory(logRecord.category());
        operation.setCondition(logRecord.condition());
        operation.setDetail(logRecord.detail());
        operation.setFail(logRecord.fail());
        operation.setOperator(logRecord.operator());
        return operation;
    }
}
