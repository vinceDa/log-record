package com.ohyoung.evaluate;

import com.ohyoung.context.ExpressionRootObject;
import com.ohyoung.context.LogRecordEvaluationContext;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SpEL会解析成一个Expression表达式，然后根据传入的Object获取到对应的值
 *
 * @author ouyb01
 * @date 2022/1/24 17:26
 */
public class LogRecordExpressionEvaluator extends CachedExpressionEvaluator {

    /**
     * expressionCache 是为了缓存方法、表达式和 SpEL的Expression的对应关系，让方法注解上添加的 SpEL表达式只解析一次
     */
    private Map<ExpressionKey, Expression> expressionCache = new ConcurrentHashMap<>(64);
    /**
     * 为了缓存传入到Expression表达式的 Object
     */
    private final Map<AnnotatedElementKey, Method> targetMethodCache = new ConcurrentHashMap<>(64);

    public String parseExpression(String conditionExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        return getExpression(this.expressionCache, methodKey, conditionExpression).getValue(evalContext, String.class);
    }

    public EvaluationContext createEvaluationContext(Method method, Object[] args, Class<?> targetClass, Object ret, String errorMsg, BeanFactory beanFactory) {
        Method targetMethod = getTargetMethod(targetClass, method);
        ExpressionRootObject rootObject = new ExpressionRootObject(method.getParameters(), args);
        return new LogRecordEvaluationContext(rootObject, targetMethod, args, getParameterNameDiscoverer(), ret, errorMsg);
    }

    private Method getTargetMethod(Class targetClass, Method method) {
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, targetClass);
        Method targetMethod = this.targetMethodCache.get(methodKey);
        if (targetMethod == null) {
            targetMethod = AopUtils.getMostSpecificMethod(method, targetClass);
            this.targetMethodCache.put(methodKey, targetMethod);
        }
        return targetMethod;
    }

}
