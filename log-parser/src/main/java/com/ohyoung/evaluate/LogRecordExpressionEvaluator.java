package com.ohyoung.evaluate;

import com.ohyoung.context.LogRecordEvaluationContext;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 真正解析SpEL表达式的地方
 * SpEL会解析成一个Expression表达式，然后根据传入的Object获取到对应的值
 * @author ouyb01
 * @date 2022/1/24 17:26
 */
@Component
public class LogRecordExpressionEvaluator extends CachedExpressionEvaluator {

    private static final SpelExpressionParser EXPRESSION_PARSER = new CustomSpelExpressionParser();
    public LogRecordExpressionEvaluator() {
        super(EXPRESSION_PARSER);
    }

    /**
     * expressionCache 是为了缓存方法、表达式和 SpEL的Expression的对应关系，让方法注解上添加的 SpEL表达式只解析一次
     */
    private final Map<ExpressionKey, Expression> EXPRESSION_CACHE = new ConcurrentHashMap<>(64);
    /**
     * 为了缓存传入到Expression表达式的 Object
     */
    private final Map<AnnotatedElementKey, Method> targetMethodCache = new ConcurrentHashMap<>(64);

    public String parseExpression(String conditionExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        String value = "";
        try {
            value = getExpression(this.EXPRESSION_CACHE, methodKey, conditionExpression).getValue(evalContext, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public LogRecordEvaluationContext createEvaluationContext(Object rootObject, Method method, Object[] args, Class<?> targetClass, Object ret, String errorMsg) {
        return new LogRecordEvaluationContext(rootObject, method, args, ret, errorMsg);
    }

}
