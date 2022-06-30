package com.ohyoung.evaluate;

import com.ohyoung.context.LogRecordContext;
import com.ohyoung.context.LogRecordEvaluationContext;
import com.ohyoung.function.IFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.MapAccessor;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.MethodResolver;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.support.ReflectiveMethodResolver;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author ouyb01
 * @date 2022/1/24 21:18
 */
@Component
public class LogRecordValueParser {
    @Autowired
    private LogRecordExpressionEvaluator expressionEvaluator;

    public String parse(String expression, Object[] args, Method method, Class<?> targetClass, Object ret, String errorMsg) {
        try {
            AnnotatedElementKey methodKey = new AnnotatedElementKey(method, targetClass);
            Object rootObject = args.length == 1 ? args[0] : args;
            LogRecordEvaluationContext evaluationContext = expressionEvaluator.createEvaluationContext(rootObject, method, args, targetClass, ret, errorMsg);
            return expressionEvaluator.parseExpression(handleCustomFunction(expression), methodKey, evaluationContext);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    private Object getRootObject(Method method, Class<?> targetClass) {
//        LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
//        String[] params = discoverer.getParameterNames(method);
//        if (params.length != 1) {
//            return null;
//        }
//
//        if ((method.getName().startsWith("get")) && (method.getName().length() == (field.getName().length() + 3))) {
//            if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
//                // MZ: Method found, run it
//                try {
//                    return method.invoke(o);
//                } catch (IllegalAccessException e) {
//                    Logger.fatal("Could not determine method: " + method.getName());
//                } catch (InvocationTargetException e) {
//                    Logger.fatal("Could not determine method: " + method.getName());
//                }
//
//            }
//        }
//    }


    /**
     * 处理自定义函数为可执行的函数
     * 例如{#getById(#id) #getByName(#name)} -> {#functionService.apply('getById', #id) #functionService.apply('getByName', #name)}
     *  todo 后续需要做容错处理, 即不符合规则的表达式直接不解析
     */
    private static String handleCustomFunction(String template) {
        int start = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < template.length(); i++) {
            char c = template.charAt(i);
            if (c == '#') {
                start = i;
                sb.append(c);
            } else if (c == '(') {
                String functionName = template.substring(start + 1, i);
                int index = sb.lastIndexOf(functionName);
                sb.replace(index, sb.toString().length(), "functionService.apply");
                sb.append("('").append(functionName).append("', ");
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

}
