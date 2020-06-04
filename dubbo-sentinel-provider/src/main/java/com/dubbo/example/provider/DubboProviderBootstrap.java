package com.dubbo.example.provider;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * -Dcsp.sentinel.dashboard.server=localhost:8888
 * http:localhost:8888
 * sentinel
 * sentinel
 */
//@EnableAutoConfiguration
@SpringBootApplication
public class DubboProviderBootstrap {

    // nacos server ip 默认端口8848
    private static final String remoteAddress = "localhost";
    // nacos group
    private static final String groupId = "Sentinel:Demo";
    // nacos dataId
    private static final String dataId = "com.alibaba.csp.sentinel.demo.flow.rule";

    /**
     * nacosJSON格式的配置
     * [
     *     {
     *         "resource": "com.dubbo.example.DemoService:sayHello(java.lang.String)",
     *         "limitApp": "default",
     *         "grade": 1,
     *         "count": 2,
     *         "strategy": 0,
     *         "controlBehavior": 0,
     *         "clusterMode": false
     *     }
     * ]
     * @param args
     */
    public static void main(String[] args) {

//        initFlowRules();
        loadRules();

        new SpringApplicationBuilder(DubboProviderBootstrap.class)
                .run(args);
    }

    //初始化规则
    private static void initFlowRules() {
        List<FlowRule> rules = new ArrayList<>(); //限流规则的集合
        FlowRule flowRule = new FlowRule();
        flowRule.setResource("com.dubbo.example.DemoService:sayHello(java.lang.String)");//资源(方法名称、接口）
        flowRule.setCount(10);//限流阈值 qps=10
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);//限流阈值类型（QPS 或并发线程数）
        //流量控制手段（直接拒绝、Warm Up、匀速排队）
        flowRule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT);
        flowRule.setLimitApp("sentinel-web");//流控针对的调用来源，若为 default 则不区分调用来源
        rules.add(flowRule);
        FlowRuleManager.loadRules(rules);
    }

    private static void loadRules() {
        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new NacosDataSource<>(remoteAddress, groupId, dataId,
                source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
                }));
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
    }
}