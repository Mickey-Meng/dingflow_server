package com.snow.dingtalk.stream;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.open.app.api.GenericEventListener;
import com.dingtalk.open.app.api.OpenDingTalkStreamClientBuilder;
import com.dingtalk.open.app.api.message.GenericOpenDingTalkEvent;
import com.dingtalk.open.app.api.security.AuthClientCredential;
import com.dingtalk.open.app.stream.protocol.event.EventAckStatus;
import com.snow.common.constant.CacheConstants;
import com.snow.common.utils.CacheUtils;
import com.snow.dingtalk.stream.factory.EventStreamStrategyFactory;
import com.snow.dingtalk.stream.strategy.IEventStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author qimingjin
 * @date 2023-08-18 14:30
 * @Description: 与钉钉建立Websocket 通信
 */
@Slf4j
@Component
public class EventStreamClient implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
       log.info("EventStreamClient 开始将于与钉钉平台建立Websocket 链接");
        OpenDingTalkStreamClientBuilder
                .custom()
                .credential(new AuthClientCredential(CacheUtils.getSysConfig(CacheConstants.ENTERPRICE_APP_KEY).toString(), CacheUtils.getSysConfig(CacheConstants.ENTERPRICE_APP_SECRET).toString()))
                //注册事件监听
                .registerAllEventListener(new GenericEventListener() {
                    public EventAckStatus onEvent(GenericOpenDingTalkEvent event) {
                        try {
                            //事件唯一Id
                            String eventId = event.getEventId();
                            //事件类型
                            String eventType = event.getEventType();
                            //事件产生时间
                            Long bornTime = event.getEventBornTime();
                            //获取事件体
                            JSONObject bizData = event.getData();
                            log.info("EventStreamClient->run onEvent接收到的数据:{},bornTime:{}", JSONUtil.toJsonStr(bizData),bornTime);
                            //处理事件
                            IEventStrategy eventStrategy = EventStreamStrategyFactory.getEventStrategy(eventType);
                            eventStrategy.handle(eventId,eventType,bizData);
                            //消费成功
                            return EventAckStatus.SUCCESS;
                        } catch (Exception e) {
                            log.error("EventStreamClient->run error msg:{}",e.getMessage());
                            log.error(e.getMessage(),e);
                            //消费失败
                            return EventAckStatus.LATER;
                        }
                    }
                })
                .build()
                .start();
    }
}
