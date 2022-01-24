package com.ohyoung.function;

/**
 * @author ouyb01
 * @date 2022/1/24 21:28
 */
public interface IParseFunction {

    /**
     * 自定义函数是否在业务代码执行之前解析
     */
    default boolean executeBefore() {
        return false;
    }

    String functionName();

    String apply(String value);

}
