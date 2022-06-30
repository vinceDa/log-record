package com.ohyoung.example;

import com.ohyoung.LogRecord;
import com.ohyoung.Operator;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author ouyb01
 * @date 2022/6/21 10:29
 */
@RestController
@RequestMapping("/test")
public class UserController {

    /**
     * 基础用法 - 单个参数
     */
    @GetMapping("/base")
    @LogRecord(success = "这是个{#name}, 结果为{#_ret} ", fail = "{#id}的结果执行错误, 错误信息为 {#_errMsg}", bizNo = "bizNo2222")
    public String base(String name) {
        return "1234";
    }

    /**
     * 基础用法 - 参数为json
     */
    @PostMapping("/json")
    @LogRecord(success = "id: {id}, name: {name} ", bizNo = "bizNo2222")
    public String base(@RequestBody User user) {
        return "1234";
    }

    /**
     * 基础用法 - 带条件的日志记录
     */
    @GetMapping("/condition")
    @LogRecord(success = "这是个{#name} ", fail = "{#name}的结果执行错误, 错误信息为 {#_errorMsg}", bizNo = "bizNo2222", condition = "{#name=='ohyoung'}")
    public String condition(String name) {
        return "1234";
    }

    /**
     * 代码报错
     */
    @GetMapping("/withError")
    @LogRecord(success = "这是个{#name} ", fail = "{#name}的结果执行错误, 错误信息为 {#_errorMsg}", bizNo = "bizNo2222")
    public String withError(String name) {
        return 1 / 0 + "";
    }

    /**
     * 自定义函数
     */
    @GetMapping("/customFunction")
    @LogRecord(success = "这是个{#manDetail(#id)}, {#name}", bizNo = "bizNo2222")
    public String getOne(Long id, String name) {
        return "1234";
    }
}

