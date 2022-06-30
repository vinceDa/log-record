package com.ohyoung.function;

/**
 * @author ouyb01
 * @date 2022/1/24 21:28
 */
public interface IParseFunction {

    /**
     * 自定义函数是否在业务代码执行之前解析
     * 例如用户【xx】已被删除, 这里的【xx】需要在被删除之前查出来, 不然删除后就查不到了
     * @return 默认为否
     */
    default boolean executeBefore() {
        return false;
    }
    /**
     * 获取方法名
     * @return 返回函数名称
     */
    String functionName();
    /**
     * 执行
     * @param value 值
     */
    String apply(String value);
}
