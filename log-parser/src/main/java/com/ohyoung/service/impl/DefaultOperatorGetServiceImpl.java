package com.ohyoung.service.impl;

import com.ohyoung.Operator;
import com.ohyoung.service.IOperatorGetService;

/**
 * @author ouyb01
 * @date 2022/1/24 21:24
 */
public class DefaultOperatorGetServiceImpl implements IOperatorGetService {

    @Override
    public Operator getUser() {
        // 从注解种获取user的信息

        // 如果注解中获取不到就从系统中获取

        // 都获取不到则报错
        return null;
    }
}
