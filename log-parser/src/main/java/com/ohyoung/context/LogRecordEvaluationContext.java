package com.ohyoung.context;

import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author ouyb01
 * @date 2022/1/24 21:20
 */
public class LogRecordEvaluationContext extends MethodBasedEvaluationContext {

    private static final LocalVariableTableParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();


    public LogRecordEvaluationContext(Object rootObject, Method method, Object[] arguments, Object ret, String errorMsg) {
        // 把方法的参数都放到 SpEL 解析的 RootObject 中
        //把方法的参数都放到 SpEL 解析的 RootObject 中
        super(rootObject, method, arguments, PARAMETER_NAME_DISCOVERER);
        //把 LogRecordContext 中的变量都放到 RootObject 中
        Map<String, Object> variables = LogRecordContext.getVariables();
        if (variables != null && variables.size() > 0) {
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                setVariable(entry.getKey(), entry.getValue());
            }
        }
        //把方法的返回值和 ErrorMsg 都放到 RootObject 中
        setVariable("_ret", ret);
        setVariable("_errorMsg", errorMsg);
    }
}
