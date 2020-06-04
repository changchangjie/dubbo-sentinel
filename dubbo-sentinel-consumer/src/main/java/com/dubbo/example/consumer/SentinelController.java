package com.dubbo.example.consumer;

import com.dubbo.example.DemoService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ChangJie on 2020-05-29.
 */
@RestController
public class SentinelController {

    @Reference(timeout = 3000,check = true,version = "1.0.0")
    DemoService demoService;//proxy$0

    @GetMapping("/say")
    public String sayHello(){
        RpcContext.getContext().setAttachment("dubboApplication","sentinel-web");
        return demoService.sayHello("test");
    }

    @GetMapping("/say2")
    public String sayHello2(){
        return demoService.sayHello("test2");
    }

    public static void main(String[] args) throws InterruptedException {
        int windowLength = 500;
        int arrayLength = 2;
        calculate(windowLength,arrayLength);

        Thread.sleep(100);
        calculate(windowLength,arrayLength);

        Thread.sleep(200);
        calculate(windowLength,arrayLength);

        Thread.sleep(200);
        calculate(windowLength,arrayLength);

        Thread.sleep(500);
        calculate(windowLength,arrayLength);

        Thread.sleep(500);
        calculate(windowLength,arrayLength);

        Thread.sleep(500);
        calculate(windowLength,arrayLength);

        Thread.sleep(500);
        calculate(windowLength,arrayLength);

        Thread.sleep(500);
        calculate(windowLength,arrayLength);
    }

    private static void calculate(int windowLength,int arrayLength){
        long time = System.currentTimeMillis();
        long timeId = time/windowLength;
        long currentWindowStart = time - time % windowLength;
        int idx = (int)(timeId % arrayLength);
        System.out.println("time="+time+",currentWindowStart="+currentWindowStart+",timeId="+timeId+",idx="+idx);
    }




}
