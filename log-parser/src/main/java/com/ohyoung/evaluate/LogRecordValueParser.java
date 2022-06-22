package com.ohyoung.evaluate;

import com.ohyoung.context.LogRecordEvaluationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.stereotype.Component;

/**
 * @author ouyb01
 * @date 2022/1/24 21:18
 */
@Component
public class LogRecordValueParser {

    private LogRecordEvaluationContext logRecordEvaluationContext;
    @Autowired
    private LogRecordExpressionEvaluator expressionEvaluator;

    public String parse(String expression, AnnotatedElementKey methodKey) {
        return expressionEvaluator.parseExpression(expression, methodKey, logRecordEvaluationContext);
    }


    public void setLogRecordEvaluationContext(LogRecordEvaluationContext logRecordEvaluationContext) {
        this.logRecordEvaluationContext = logRecordEvaluationContext;
    }

}
