package com.ohyoung.config;

/**
 * @author ohYoung
 * @date 2022/1/26 19:18
 */
public class AnnotationTest {

    @LogRecord(content = "succ1", bizNo = "biz1")
    public void test() {
        System.out.println("test");
    }

}
