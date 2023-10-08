package com.snow.dingtalk.stream.factory;

import com.snow.common.enums.DingTalkListenerType;
import com.snow.dingtalk.enums.StreamEventTypeEnum;
import com.snow.dingtalk.stream.strategy.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author qimingjin
 * @date 2023-08-18 16:28
 * @Description:
 */
public class EventStreamStrategyFactory {

    private static Map<String, IEventStrategy> STRATEGY_MAP = new HashMap<>();

    static {
        synchronized (IEventStrategy.class){
            STRATEGY_MAP.put(StreamEventTypeEnum.ORG_DEPT_CREATE.getEventType(), new DepartmentEventStrategy());
            STRATEGY_MAP.put(StreamEventTypeEnum.ORG_DEPT_MODIFY.getEventType(), new DepartmentEventStrategy());
            STRATEGY_MAP.put(StreamEventTypeEnum.ORG_DEPT_REMOVE.getEventType(), new DepartmentEventStrategy());
            STRATEGY_MAP.put(StreamEventTypeEnum.BPMS_INSTANCE_CHANGE.getEventType(), new BpmsEventStrategy());
            STRATEGY_MAP.put(StreamEventTypeEnum.BPMS_TASK_CHANGE.getEventType(), new BpmsEventStrategy());
            STRATEGY_MAP.put(StreamEventTypeEnum.USER_ADD_ORG.getEventType(), new UserEventStrategy());
            STRATEGY_MAP.put(StreamEventTypeEnum.USER_LEAVE_ORG.getEventType(), new UserEventStrategy());
            STRATEGY_MAP.put(StreamEventTypeEnum.USER_MODIFY_ORG.getEventType(), new UserEventStrategy());
        }
    }

    private static final IEventStrategy EMPTY = new DefaultEventStrategy();

    public static synchronized IEventStrategy getEventStrategy(String key){
        IEventStrategy strategy = STRATEGY_MAP.get(key);
        return null == strategy ? EMPTY : strategy;
    }

    public static Set<String> getKeys(){
        return STRATEGY_MAP.keySet();
    }

}
