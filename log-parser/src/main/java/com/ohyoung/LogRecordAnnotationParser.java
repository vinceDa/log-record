package com.ohyoung;

import com.ohyoung.function.ParseFunctionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注解解析类
 * @author ouyb01
 * @date 2022/1/11 9:36
 */
public class LogRecordAnnotationParser {

    @Autowired
    private ParseFunctionFactory parseFunctionFactory;

    private final ConcurrentHashMap<LogRecord, List<LogRecordMetaData>> LOG_RECORD_CACHE = new ConcurrentHashMap<>();

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

    public List<LogRecordMetaData> computeLogRecordOperations(Class<?> targetClass) {
        try {
            // 获取@LogRecord中的方法
            Method[] declaredMethods = targetClass.getDeclaredMethods();
            List<LogRecordMetaData> operations = new ArrayList<>();
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
                    List<LogRecordMetaData> metaDataList = parseAnnotation(logRecord);
                    operations.addAll(metaDataList);
                    LOG_RECORD_CACHE.put(logRecord, metaDataList);
//                    if (Objects.nonNull(LOG_RECORD_CACHE.get(logRecord))) {
//                        operations.addAll(LOG_RECORD_CACHE.get(logRecord));
//                    } else {
//                        // 解析注解上对应的信息
//                        List<LogRecordMetaData> metaDataList = parseAnnotation(logRecord);
//                        operations.addAll(metaDataList);
//                        LOG_RECORD_CACHE.put(logRecord, metaDataList);
//                    }
                }
            }
            return operations;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private List<LogRecordMetaData> parseAnnotation(LogRecord logRecord) {
        Field[] fields = logRecord.getClass().getDeclaredFields();
        List<LogRecordMetaData> operations = new ArrayList<>(fields.length);
        // 正常操作下, 反射不能获取到参数名称, 所以这里直接写死
        operations.add(new LogRecordMetaData("success", logRecord.success()));
        operations.add(new LogRecordMetaData("fail", logRecord.fail()));
        operations.add(new LogRecordMetaData("operator", logRecord.operator()));
        operations.add(new LogRecordMetaData("bizNo", logRecord.bizNo()));
        operations.add(new LogRecordMetaData("category", logRecord.category()));
        operations.add(new LogRecordMetaData("detail", logRecord.detail()));
        operations.add(new LogRecordMetaData("condition", logRecord.condition()));
        // 解析表达式中所有的自定义函数
        for (LogRecordMetaData operation : operations) {
            operation.setFunctionNames(listFunctionInAnnotation(operation.getValue()));
            operation.setExecuteBefore(operation.getFunctionNames().stream().anyMatch(s -> parseFunctionFactory.isBeforeFunction(s)));
        }
        return operations;
    }

    /**
     * 获取表达式中所有的自定义函数, 按照定义的规则'#'和'('中间的字符串就是方法名
     * todo 检测不符合规则的template
     */
    private List<String> listFunctionInAnnotation(String template) {
        if (template.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> functionNames = new ArrayList<>();
        int start = 0;
        for (int i = 0; i < template.length(); i++) {
            if (template.charAt(i) == '#') {
                start = i;
            }
            if (template.charAt(i) == '(') {
                functionNames.add(template.substring(start + 1, i));
            }
        }
        return functionNames;
    }

    public void setParseFunctionFactory(ParseFunctionFactory parseFunctionFactory) {
        this.parseFunctionFactory = parseFunctionFactory;
    }
}
