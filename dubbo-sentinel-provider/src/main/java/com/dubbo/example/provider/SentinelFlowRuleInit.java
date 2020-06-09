package com.dubbo.example.provider;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by ChangJie on 2020-06-05.
 */
@Component
public class SentinelFlowRuleInit implements InitializingBean {

    @Value("${project.name}")
    private String projectName;

    @Value("${nacos.server_addr}")
    private String nocosServerAddr;

    public static final String GROUP_ID = "SENTINEL_GROUP";

    public static final String FLOW_DATA_ID_POSTFIX = "-flow-rules";

    @Override
    public void afterPropertiesSet() throws Exception {
        try{
            //project.name 参数只能通过 JVM -D 参数方式配置，不能通过properties文件配置
            //设置sentinel-client的appName,databoard那边需要用到
            System.setProperty("project.name", projectName);
            System.setProperty("csp.sentinel.dashboard.server", "localhost:8899");

            String remoteAddress = nocosServerAddr;
            String groupId = GROUP_ID;
            String dataId = projectName + FLOW_DATA_ID_POSTFIX;

            loadFlowRulesFromNacos(remoteAddress, groupId, dataId);
            System.out.println("success load flow rules from nacos");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void loadFlowRulesFromNacos(String remoteAddress, String groupId, String dataId) {
        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new NacosDataSource<>(remoteAddress, groupId, dataId,
                source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
                }));
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
    }
}
