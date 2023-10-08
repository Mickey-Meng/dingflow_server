package com.snow.dingtalk.stream.strategy;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.snow.common.enums.DingTalkListenerType;
import com.snow.dingtalk.sync.SyncFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author qimingjin
 * @date 2023-08-18 16:32
 * @Description:
 */
@Slf4j
@Component
public class BpmsEventStrategy implements IEventStrategy{
    private  final SyncFlowService flowService= SpringUtil.getBean(SyncFlowService.class);

    @Override
    public void handle(String eventId, String eventType, JSONObject bizData) {
        log.info("BpmsEventStrategy->handle param eventType:{},bizData:{}",eventType, JSON.toJSONString(bizData));
        switch (eventType){
            case "bpms_instance_change":
                flowService.SyncSysInfo(DingTalkListenerType.BPMS_INSTANCE_CHANGE,bizData);
                break;
            case "bpms_task_change" :
                flowService.SyncSysInfo(DingTalkListenerType.BPMS_TASK_CHANGE,bizData);
                break;
            default:
        }
    }
}
