package com.ohyoung.context;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * 使用InheritableThreadLocal存储上下文避免嵌套注解时, 上下文的值被覆盖
 * @author ouyb01
 * @date 2022/1/24 21:18
 */
public class LogRecordContext {

    /**
     * 具备继承特性线程局部变量，子线程继承父线程的线程局部变量
     */
    private static final InheritableThreadLocal<Stack<Map<String, Object>>> VARIABLE_MAP_STACK;

    static {
        VARIABLE_MAP_STACK = new InheritableThreadLocal<Stack<Map<String, Object>>>() {
            @Override
            protected Stack<Map<String, Object>> initialValue() {
                return new Stack<>();
            }

            @Override
            protected Stack<Map<String, Object>> childValue(Stack<Map<String, Object>> parentValue) {
                Stack<Map<String, Object>> copiedStack = new Stack<>();
                // 仅共享父线程方法上下文中的变量
                if (!parentValue.isEmpty()) {
                    copiedStack.push(parentValue.peek());
                }
                return copiedStack;
            }
        };
    }

    /**
     * 入栈一个 map。在 @LogRecord 嵌套使用时，即将入栈的 map 包含栈顶 map 的数据。
     */
    public static void putEmptySpan() {
        if (VARIABLE_MAP_STACK.get().isEmpty()) {
            VARIABLE_MAP_STACK.get().push(new HashMap<>());
        } else {
            // 内层方法共享外层方法 Context Variables
            Map<String, Object> copiedMap = new HashMap<>(VARIABLE_MAP_STACK.get().peek());
            VARIABLE_MAP_STACK.get().push(copiedMap);
        }
    }

    public static void putVariable(String key, Object value) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(key, value);
        VARIABLE_MAP_STACK.get().push(map);
    }

    public static void putVariables(Map<String, Object> variables) {
        VARIABLE_MAP_STACK.get().push(variables);
    }

    public static Map<String, Object> getVariables() {
        if (VARIABLE_MAP_STACK.get().isEmpty()) {
            VARIABLE_MAP_STACK.get().push(new HashMap<>());
        }
        return VARIABLE_MAP_STACK.get().pop();
    }

    public static void clear() {
        VARIABLE_MAP_STACK.remove();
    }
}
