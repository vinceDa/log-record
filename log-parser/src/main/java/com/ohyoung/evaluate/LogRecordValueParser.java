package com.ohyoung.evaluate;

import com.ohyoung.LogRecordOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;

/**
 * @author ouyb01
 * @date 2022/1/24 21:18
 */
public class LogRecordValueParser {

    @Autowired
    private LogRecordExpressionEvaluator expressionEvaluator;

    public String parse(LogRecordOperation logRecordOperation) {
        EvaluationContext evaluationContext = expressionEvaluator.createEvaluationContext(logRecordOperation.getMethod(), logRecordOperation.getArgs(),
                logRecordOperation.getTargetClass(), null, null, null);
        return expressionEvaluator.parseExpression(logRecordOperation.getSuccess(), null, evaluationContext);
    }
}
