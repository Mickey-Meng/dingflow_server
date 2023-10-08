package com.snow.dingtalk.stream.strategy;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.snow.common.enums.DingTalkListenerType;
import com.snow.dingtalk.sync.SyncSysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author qimingjin
 * @date 2023-08-18 16:32
 * @Description: 用户监听策略，请参考com.snow.dingtalk.stream.strategy.DepartmentEventStrategy 自行实现
 */
@Slf4j
@Component
public class UserEventStrategy implements IEventStrategy{

    private final SyncSysUserService userService= SpringUtil.getBean(SyncSysUserService.class);
    
    @Override
    public void handle(String eventId, String eventType, JSONObject bizData) {
        log.info("UserEventStrategy->handle param eventType:{},bizData:{}",eventType, JSON.toJSONString(bizData));
        // 用户监听处理
        switch (eventType){
            case "user_add_org":
                userService.SyncSysInfo(DingTalkListenerType.USER_CREATE,bizData);
                break;
            case "user_modify_org":
                userService.SyncSysInfo(DingTalkListenerType.USER_UPDATE,bizData);
                break;
            case "user_leave_org":
                userService.SyncSysInfo(DingTalkListenerType.USER_DELETE,bizData);
                break;
            default:
        }

    }
}
