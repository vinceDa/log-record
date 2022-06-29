package com.ohyoung.config;


import com.ohyoung.LogRecordMetaData;
import com.ohyoung.LogRecordAnnotationParser;
import com.ohyoung.context.LogRecordContext;
import com.ohyoung.context.LogRecordEvaluationContext;
import com.ohyoung.entity.LogRecordPO;
import com.ohyoung.evaluate.LogRecordValueParser;
import com.ohyoung.function.IFunctionService;
import com.ohyoung.function.ParseFunctionFactory;
import com.ohyoung.service.ILogRecordService;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.AnnotatedElementKey;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.aop.support.AopUtils.getTargetClass;

/**
 * 通过实现 MethodInterceptor 接口实现操作日志的增强逻辑
 * @author ouyb01
 * @date 2022/1/24 15:12
 */
public class LogRecordInterceptor implements MethodInterceptor {
    @Autowired
    private LogRecordValueParser logRecordValueParser;

    private LogRecordAnnotationParser logRecordAnnotationParser;

    @Autowired
    private IFunctionService functionService;

    @Autowired
    private ILogRecordService logRecordService;

    private String tenant;

    private static Logger log = LoggerFactory.getLogger(LogRecordInterceptor.class);

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        // 记录日志
        return execute(invocation, invocation.getThis(), method, invocation.getArguments());
    }

    private Object execute(MethodInvocation invoker, Object target, Method method, Object[] args) throws Throwable {
        Class<?> targetClass = getTargetClass(target);
        Object ret = null;
        MethodExecuteResult methodExecuteResult = new MethodExecuteResult();
        LogRecordContext.putEmptySpan();
        //todo 用于调用自定义函数, 暂时没想好更好的位置
        LogRecordContext.putVariable("functionService", functionService);
        Collection<LogRecordMetaData> operations = new ArrayList<>();
        List<LogRecordMetaData> executeBeforeFunctionMetaDataList = new ArrayList<>();
        List<LogRecordMetaData> executeAfterFunctionMetaDataList = new ArrayList<>();
        try {
            // 解析注解将数据存入LogRecordOperation
            operations = logRecordAnnotationParser.computeLogRecordOperations(targetClass);
            executeBeforeFunctionMetaDataList = operations.stream().filter(LogRecordMetaData::getExecuteBefore).collect(Collectors.toList());
            executeAfterFunctionMetaDataList = operations.stream().filter(o -> !o.getExecuteBefore()).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("@LogRecord parse error", e);
        }
        try {
            // 解析方法执行前的SpEL表达式
            processFunctionTemplate(executeBeforeFunctionMetaDataList, args, method, targetClass, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("log record parse before function exception", e);
        }
        try {
            ret = invoker.proceed();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("business error", e);
            methodExecuteResult = new MethodExecuteResult(false, e, e.getMessage());
        }
        try {
            // 解析方法执行后的SpEL表达式
            processFunctionTemplate(executeAfterFunctionMetaDataList, args, method, targetClass, ret, methodExecuteResult.getErrorMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("log record parse after function exception", e);
        }
        operations.clear();
        operations.addAll(executeBeforeFunctionMetaDataList);
        operations.addAll(executeAfterFunctionMetaDataList);
        try {
            LogRecordPO logRecordPO = new LogRecordPO();
            Class<? extends LogRecordPO> logRecordPOClass = logRecordPO.getClass();
            for (LogRecordMetaData operationMetaData : operations) {
                Field field = logRecordPOClass.getDeclaredField(operationMetaData.getKey());
                field.setAccessible(true);
                field.set(logRecordPO, operationMetaData.getValue());
            }
            if (!"false".equals(logRecordPO.getCondition())) {
                logRecordService.record(logRecordPO);
            }
        } catch (Exception t) {
            t.printStackTrace();
            //记录日志错误不要影响业务
            log.error("log record parse exception", t);
        } finally {
            LogRecordContext.clear();
        }
        if (Objects.nonNull(methodExecuteResult.getThrowable())) {
            throw methodExecuteResult.getThrowable();
        }
        return ret;
    }

    /**
     * 解析SpEL表达式并处理其中的自定义函数
     */
    private void processFunctionTemplate(Collection<LogRecordMetaData> operations, Object[] args, Method method, Class<?> targetClass, Object ret, String errorMsg) {
        for (LogRecordMetaData logRecordMetaData : operations) {
            if (logRecordMetaData.isNeedParse()) {
                logRecordMetaData.setValue(logRecordValueParser.parse(logRecordMetaData.getValue(), args, method, targetClass, ret, errorMsg));
            }
        }
    }

    public void setLogRecordAnnotationParser(LogRecordAnnotationParser logRecordAnnotationParser) {
        this.logRecordAnnotationParser = logRecordAnnotationParser;
    }

    public void setFunctionService(IFunctionService functionService) {
        this.functionService = functionService;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }
}
