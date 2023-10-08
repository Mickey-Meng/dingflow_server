package com.snow.dingtalk.stream.strategy;

import com.alibaba.fastjson.JSONObject;

/**
 * @author qimingjin
 * @date 2023-08-18 16:32
 * @Description:
 */
public class DefaultEventStrategy implements IEventStrategy{


    @Override
    public void handle(String eventId, String eventType, JSONObject bizData) {

    }
}
