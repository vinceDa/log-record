package com.ohyoung.config;


import com.ohyoung.LogRecordMetaData;
import com.ohyoung.LogRecordAnnotationParser;
import com.ohyoung.context.LogRecordContext;
import com.ohyoung.context.LogRecordEvaluationContext;
import com.ohyoung.entity.LogRecordPO;
import com.ohyoung.evaluate.LogRecordValueParser;
import com.ohyoung.function.IFunctionService;
import com.ohyoung.function.IParseFunction;
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

    @Autowired
    private ParseFunctionFactory parseFunctionFactory;

//    @Autowired
    private LogRecordAnnotationParser logRecordAnnotationParser;

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
//        LogRecordContext.putEmptySpan();
        Collection<LogRecordMetaData> operations = new ArrayList<>();
        List<LogRecordMetaData> executeBeforeFunctionMetaDataList = new ArrayList<>();
        List<LogRecordMetaData> executeAfterFunctionMetaDataList = new ArrayList<>();
        AnnotatedElementKey targetMethodKey = new AnnotatedElementKey(method, targetClass);
        LogRecordEvaluationContext evaluationContext = new LogRecordEvaluationContext(method, args);
        logRecordValueParser.setLogRecordEvaluationContext(evaluationContext);
        try {
            // 解析注解将数据存入LogRecordOperation
            operations = logRecordAnnotationParser.computeLogRecordOperations(method, targetClass, args);
//            List<String> spElTemplates = getBeforeExecuteFunctionTemplate(operations, targetMethodKey);
            // 业务逻辑执行前的自定义函数解析
            executeBeforeFunctionMetaDataList = operations.stream().filter(LogRecordMetaData::getExecuteBefore).collect(Collectors.toList());
            executeAfterFunctionMetaDataList = operations.stream().filter(o -> !o.getExecuteBefore()).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("@LogRecord parse error", e);
        }
        try {
            // 解析方法执行前的SpEL表达式
            processFunctionTemplate(evaluationContext, executeBeforeFunctionMetaDataList, targetMethodKey);
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
            evaluationContext.setRetAndErrMsg(ret, methodExecuteResult.getErrorMsg());
            // 解析方法执行后的SpEL表达式
            processFunctionTemplate(evaluationContext, executeAfterFunctionMetaDataList, targetMethodKey);
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
     * @param evaluationContext 上下文参数
     * @param operations 解析@LogRecord后的元数据集合
     */
    private void processFunctionTemplate(LogRecordEvaluationContext evaluationContext, Collection<LogRecordMetaData> operations, AnnotatedElementKey methodKey) {
        // 注册自定义函数
        operations.forEach(o -> registerCustomizeFunction(o.getFunctionNames(), evaluationContext));
        for (LogRecordMetaData operation : operations) {
            if (!operation.isNeedParse()) {
                continue;
            }
            operation.setValue(logRecordValueParser.parse(operation.getValue(), methodKey));
        }
    }

    private void registerCustomizeFunction(List<String> functionNames, LogRecordEvaluationContext evaluationContext) {
        functionNames.forEach(functionName -> {
            IParseFunction function = parseFunctionFactory.getFunction(functionName);

            if (Objects.nonNull(function)) {
                evaluationContext.registerFunction(functionName, function.functionMethod());
            }
        });
    }

    public void setLogRecordValueParser(LogRecordValueParser logRecordValueParser) {
        this.logRecordValueParser = logRecordValueParser;
    }

    public void setLogRecordAnnotationParser(LogRecordAnnotationParser logRecordAnnotationParser) {
        this.logRecordAnnotationParser = logRecordAnnotationParser;
    }

    public void setFunctionService(IFunctionService functionService) {
        this.functionService = functionService;
    }

    public void setLogRecordService(ILogRecordService logRecordService) {
        this.logRecordService = logRecordService;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }
}
