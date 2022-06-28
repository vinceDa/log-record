package com.ohyoung.evaluate;

import com.ohyoung.context.LogRecordContext;
import com.ohyoung.context.LogRecordEvaluationContext;
import com.ohyoung.function.IFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.MethodResolver;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author ouyb01
 * @date 2022/1/24 21:18
 */
@Component
public class LogRecordValueParser {

    private LogRecordEvaluationContext logRecordEvaluationContext;

    private IFunctionService functionService;
    @Autowired
    private LogRecordExpressionEvaluator expressionEvaluator;

    public String parse(String expression, Object[] args, Method method, Class<?> targetClass, Object ret, String errorMsg) {
        try {
            AnnotatedElementKey methodKey = new AnnotatedElementKey(method, targetClass);
            LogRecordEvaluationContext evaluationContext = expressionEvaluator.createEvaluationContext(method, args, targetClass, ret, errorMsg);
            List<String> functionNames = listFunctionInAnnotation(expression);
            for (String functionName : functionNames) {
                evaluationContext.registerFunction(functionName, functionService.getClass().getDeclaredMethod("apply", String.class, String.class));
                evaluationContext.setVariable(functionName, functionName);
            }
            return expressionEvaluator.parseExpression(expression, methodKey, evaluationContext);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



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

    public String parse(String expression, AnnotatedElementKey methodKey) {
        return expressionEvaluator.parseExpression(expression, methodKey, logRecordEvaluationContext);
    }


    public void setLogRecordEvaluationContext(LogRecordEvaluationContext logRecordEvaluationContext) {
        this.logRecordEvaluationContext = logRecordEvaluationContext;
    }

}
