package com.ohyoung.config;


import com.ohyoung.LogRecordOperation;
import com.ohyoung.LogRecordOperationSource;
import com.ohyoung.context.LogRecordContext;
import com.ohyoung.evaluate.LogRecordValueParser;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.*;

import static org.springframework.aop.support.AopUtils.getTargetClass;

/**
 * 通过实现 MethodInterceptor 接口实现操作日志的增强逻辑
 * @author ouyb01
 * @date 2022/1/24 15:12
 */
public class LogRecordAdvice implements MethodInterceptor {

    @Autowired
    private LogRecordOperationSource logRecordOperationSource;

    @Autowired
    private LogRecordValueParser logRecordValueParser;

    private static Logger log = LoggerFactory.getLogger(LogRecordAdvice.class);

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        // 记录日志
        return execute(invocation, invocation.getThis(), method, invocation.getArguments());
    }

    private Object execute(MethodInvocation invoker, Object target, Method method, Object[] args) throws Throwable {
        Class<?> targetClass = getTargetClass(target);
        Object ret = null;
        MethodExecuteResult methodExecuteResult = new MethodExecuteResult(true, null, "");
//        LogRecordContext.putEmptySpan();
        Collection<LogRecordOperation> operations = new ArrayList<>();
        Map<String, String> functionNameAndReturnMap = new HashMap<>();
        try {
            operations = logRecordOperationSource.computeLogRecordOperations(method, targetClass);
            List<String> spElTemplates = getBeforeExecuteFunctionTemplate(operations);
            // 业务逻辑执行前的自定义函数解析
            functionNameAndReturnMap = processBeforeExecuteFunctionTemplate(spElTemplates, targetClass, method, args);
        } catch (Exception e) {
            log.error("log record parse before function exception", e);
        }
        try {
            ret = invoker.proceed();
        } catch (Exception e) {
            methodExecuteResult = new MethodExecuteResult(false, e, e.getMessage());
        }
        try {
            if (!CollectionUtils.isEmpty(operations)) {
                recordExecute(ret, method, args, operations, targetClass,
                        methodExecuteResult.isSuccess(), methodExecuteResult.getErrorMsg(), functionNameAndReturnMap);
            }
        } catch (Exception t) {
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

    private void recordExecute(Object ret, Method method, Object[] args, Collection<LogRecordOperation> operations, Class<?> targetClass, boolean success, String errorMsg, Map<String, String> functionNameAndReturnMap) {

    }

    /**
     * 解析@LogRecord中的自定义函数
     * @param spElTemplates
     * @param targetClass
     * @param method
     * @param args
     * @return
     */
    private Map<String, String> processBeforeExecuteFunctionTemplate(List<String> spElTemplates, Class<?> targetClass, Method method, Object[] args) {

        return null;
    }

    /**
     * 在方法执行之前获取模板
     * @LogRecord(content = "修改了订单的配送员：从“{queryOldUser{#request.deliveryOrderNo()}}”, 修改到“{deliveryUser{#request.userId}}”"
     * @param operations
     * @return
     */
    private List<String> getBeforeExecuteFunctionTemplate(Collection<LogRecordOperation> operations) {
        List<String> sqELTemplates = new ArrayList<>(operations.size());
        for (LogRecordOperation operation : operations) {
            String success = operation.getSuccess();
            // 不包含{{和}}则表示没有spEL表达式, 不需要解析
            if (!success.contains("{{") || !success.contains("}}") ) {
                sqELTemplates.add(success);
                continue;
            }
            sqELTemplates.add(logRecordValueParser.parse(operation));
        }
        return sqELTemplates;
    }


}
