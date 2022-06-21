package com.ohyoung.evaluate;

import com.ohyoung.LogRecordOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;

/**
 * @author ouyb01
 * @date 2022/1/24 21:18
 */
@Component
public class LogRecordValueParser {

    @Autowired
    private LogRecordExpressionEvaluator expressionEvaluator;

    public String parse(LogRecordOperation logRecordOperation) {
        EvaluationContext evaluationContext = expressionEvaluator.createEvaluationContext(logRecordOperation.getMethod(), logRecordOperation.getArgs(),
                logRecordOperation.getTargetClass(), null, null);
        AnnotatedElementKey methodKey = new AnnotatedElementKey(logRecordOperation.getMethod(), logRecordOperation.getTargetClass());
        return expressionEvaluator.parseExpression(logRecordOperation.getSuccess(), methodKey, evaluationContext);
    }
}
