package com.ohyoung.entity;

/**
 * @author ouyb01
 * @date 2022/6/23 19:09
 */
public class LogRecordPO {

    /**
     * 操作日志的文本模板, 必填
     */
    private String success;

    /**
     * 操作日志失败的文本模板
     */
    private String fail;

    /**
     * 操作日志的执行人
     */
    private String operator;

    /**
     * 操作日志绑定的业务对象标识, 必填
     */
    private String bizNo;

    /**
     * 操作日志的种类
     */
    private String category;

    /**
     * 扩展参数, 记录操作日志的修改详情
     */
    private String detail;

    /**
     * 记录日志的条件
     */
    private String condition;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getFail() {
        return fail;
    }

    public void setFail(String fail) {
        this.fail = fail;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "LogRecordPO{" +
                "success='" + success + '\'' +
                ", fail='" + fail + '\'' +
                ", operator='" + operator + '\'' +
                ", bizNo='" + bizNo + '\'' +
                ", category='" + category + '\'' +
                ", detail='" + detail + '\'' +
                ", condition='" + condition + '\'' +
                '}';
    }
}
