package com.ohyoung;

import java.util.List;
import java.util.Objects;

/**
 * 日志操作实体, 记录被@LogRecord标注的方法信息
 * @author ohYoung
 * @date 2022/4/4 21:29
 */
public class LogRecordMetaData {

    /**
     * @LogRecord注解 定义的具有业务意义的key, 例如success, fail等, 这里用key、value的形式接收
     */
    private String key;
    /**
     * @LogRecord注解 中的key对应的值, 目前考虑到只有String类型才会进行SPEL解析
     */
    private String value;

    /**
     * 当前表达式中所有的自定义函数名称
     */
    private List<String> functionNames;

    /**
     * 解析是否在方法执行前, 例如要记录删除操作中对象的某些信息, 需要在方法执行前执行自定义函数(例如根据id查询对象信息)
     */
    private boolean executeBefore;

    public LogRecordMetaData(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<String> getFunctionNames() {
        return functionNames;
    }

    public void setFunctionNames(List<String> functionNames) {
        this.functionNames = functionNames;
    }

    public boolean isNeedParse() {
        return Objects.nonNull(value) && (value.contains("{") || value.contains("}"));
    }

    public boolean getExecuteBefore() {
        // 如果表达式需要打印结果, 则必须在函数后执行
        if (value.contains("_ret")) {
            return false;
        }
        return executeBefore;
    }

    public void setExecuteBefore(boolean executeBefore) {
        this.executeBefore = executeBefore;
    }
}
