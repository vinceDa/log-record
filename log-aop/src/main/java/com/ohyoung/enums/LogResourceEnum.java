package com.ohyoung.enums;

/**
 * @author ouyb01
 * @date 2022/1/11 9:38
 */
public enum LogResourceEnum {

    CUSTOMER_PROTOCOL_JOIN(1, "客户服务-客户入住协议-业主入伙");

    private String text;

    private Integer value;

    LogResourceEnum(Integer value, String text) {
        this.text = text;
        this.value = value;
    }
}
