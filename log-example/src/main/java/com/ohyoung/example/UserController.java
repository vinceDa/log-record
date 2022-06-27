package com.ohyoung.example;

import com.ohyoung.LogRecord;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ouyb01
 * @date 2022/6/21 10:29
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/getOne")
    @LogRecord(success = "这是个{#manDetail(#id)}", fail = "{#id}的结果是{#_ret}", bizNo = "bizNo2222", condition = "{#id==1}")
    public String getOne(Long id, String name) {
        return 1/0 + "";
    }
}

