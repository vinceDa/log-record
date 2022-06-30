# log-record
## 介绍

log-record是一款简单好用的记录操作日志的插件。



log-record通过注解+aop+SpEL+自定义函数的方式实现了日志从解析到持久化的一整套方案，支持各种操作日志的适用场景。



log-record提供的能力包括但不限于：

- 通过表达式解析生成简洁明了的表达式，支持解析入参、自定义函数、返回值(_ret)、错误信息(_errMsg)等
- 可以通过条件控制是否记录日志
- 提供自定义函数扩展
- 提供自定义获取操作人扩展
- 提供自定义持久化扩展

- 支持嵌套、支持多线程使用情景



设计思路戳[这里](https://www.yuque.com/yigenranshaodexiongmao/fgx0oh/ph5p43)

## 快速开始

示例代码如下：

```java
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
```

包含的功能有：

1. 基础用法 - 单个参数

2. 基础用法 - 参数为json

3. 基础用法 - 带条件的日志记录

4. 代码报错

5. 自定义函数，实现IParseFunction接口：

   ```java
   @Component
   public class ManFunction implements IParseFunction {
       @Override
       public boolean executeBefore() {
           return true;
       }
   
       @Override
       public String functionName() {
           return "manDetail";
       }
   
       @Override
       public String apply(String value) {
           return "apply: 【value : " + value + "姓名: 欧阳帅奔, 家产: 0】";
       }
   }
   ```

   

6. 自定义持久化， 实现ILogRecordService

   ```java
   @Component
   public class TestLogRecordServiceImpl implements ILogRecordService {
   
       private static final Logger log = LoggerFactory.getLogger(TestLogRecordServiceImpl.class);
   
       @Override
       public void record(LogRecordPO logRecord, boolean isSuccess) {
           log.info("【logRecord custom implement】log={}", logRecord);
       }
   }
   ```



### 快速测试

```xml
curl --location --request GET 'http://localhost:8080/test/base?name=ohyoung'
curl --location --request POST 'http://localhost:8080/test/json' --header 'Content-Type: application/json' --data-raw '{"id": "id1", "name": "name1"}'
curl --location --request GET 'http://localhost:8080/test/condition?name=oh'
curl --location --request GET 'http://localhost:8080/test/withError?name=ohhhh'
curl --location --request GET 'http://localhost:8080/test/customFunction?id=1&name=2'
```

