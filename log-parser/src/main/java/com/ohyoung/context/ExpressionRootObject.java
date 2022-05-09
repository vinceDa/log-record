package com.ohyoung.context;

/**
 * @author ohYoung
 * @date 2022/4/30 15:27
 */
public class ExpressionRootObject {

    private final Object object;
    private final Object[] args;

    public ExpressionRootObject(Object object, Object[] args) {
        this.object = object;
        this.args = args;
    }

    public Object getObject() {
        return object;
    }

    public Object[] getArgs() {
        return args;
    }

}
