package com.ohyoung.evaluate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;

import java.lang.reflect.Method;

/**
 * @author ouyb01
 * @date 2022/1/24 21:18
 */
public class LogRecordValueParser {

    @Autowired
    private LogRecordExpressionEvaluator expressionEvaluator;

    public void parse(Method method,  Object[] args, Class<?> targetClass, Object target) {
        EvaluationContext evaluationContext = expressionEvaluator.createEvaluationContext(method, args, targetClass, ret, errorMsg, beanFactory);
    }
}
