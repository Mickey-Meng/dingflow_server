package com.snow.dingtalk.stream.strategy;

import com.alibaba.fastjson.JSONObject;

/**
 * @author qimingjin
 * @date 2023-08-18 16:29
 * @Description:
 */
public interface IEventStrategy {


    /**
     * 处理事件
     * @param eventId 事件id
     * @param eventType 事件类型
     * @param bizData 业务数据
     */
    void handle(String eventId ,String eventType, JSONObject bizData);
}
