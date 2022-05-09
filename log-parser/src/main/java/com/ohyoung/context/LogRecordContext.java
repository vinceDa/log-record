package com.ohyoung.context;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author ouyb01
 * @date 2022/1/24 21:18
 */
public class LogRecordContext {

    private static final InheritableThreadLocal<Stack<Map<String, Object>>> variableMapStack = new InheritableThreadLocal<>();

    public static void setVariables(Map<String, Object> variables) {
        variableMapStack.get().push(variables);
    }

    public static Map<String, Object> getVariables() {
        return variableMapStack.get().pop();
    }

    public static void clear() {
        variableMapStack.remove();
    }
}
