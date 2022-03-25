package com.ohyoung.enums;

/**
 * @author ouyb01
 * @date 2022/1/11 9:38
 */
public enum LogResourceEnum {

    CUSTOMER_PROTOCOL_JOIN(1, "客户服务-客户入住协议-业主入伙");

    private Integer code;

    private String desc;

    LogResourceEnum(Integer code, String desc) {
        this.desc = desc;
        this.code = code;
    }
}
