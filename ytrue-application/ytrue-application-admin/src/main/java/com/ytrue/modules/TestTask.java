package com.ytrue.modules;

import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author ytrue
 * @description: TestTask
 * @date 2022/12/16 14:29
 */
@Component("testTask")
public class TestTask {

    public void testRun() throws InterruptedException {
        TimeUnit.SECONDS.sleep(20);
        System.out.println("测试方法");
    }
}
