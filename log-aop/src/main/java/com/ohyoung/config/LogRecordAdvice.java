package com.ohyoung.config;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

import static org.springframework.aop.support.AopUtils.getTargetClass;

/**
 * @author ouyb01
 * @date 2022/1/24 15:12
 */
public class LogRecordAdvice implements MethodInterceptor {

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
//        MethodExecuteResult methodExecuteResult = new MethodExecuteResult(true, null, "");
//        LogRecordContext.putEmptySpan();
//        Collection<LogRecordOps> operations = new ArrayList<>();
//        Map<String, String> functionNameAndReturnMap = new HashMap<>();
//        try {
//            operations = logRecordOperationSource.computeLogRecordOperations(method, targetClass);
//            List<String> spElTemplates = getBeforeExecuteFunctionTemplate(operations);
//            //业务逻辑执行前的自定义函数解析
//            functionNameAndReturnMap = processBeforeExecuteFunctionTemplate(spElTemplates, targetClass, method, args);
//        } catch (Exception e) {
//            log.error("log record parse before function exception", e);
//        }
//        try {
//            ret = invoker.proceed();
//        } catch (Exception e) {
//            methodExecuteResult = new MethodExecuteResult(false, e, e.getMessage());
//        }
//        try {
//            if (!CollectionUtils.isEmpty(operations)) {
//                recordExecute(ret, method, args, operations, targetClass,
//                        methodExecuteResult.isSuccess(), methodExecuteResult.getErrorMsg(), functionNameAndReturnMap);
//            }
//        } catch (Exception t) {
//            //记录日志错误不要影响业务
//            log.error("log record parse exception", t);
//        } finally {
//            LogRecordContext.clear();
//        }
//        if (methodExecuteResult.throwable != null) {
//            throw methodExecuteResult.throwable;
//        }
        return ret;
    }
}
