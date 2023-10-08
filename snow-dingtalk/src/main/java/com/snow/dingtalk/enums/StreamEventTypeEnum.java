package com.snow.dingtalk.enums;

import lombok.Getter;

/**
 * @author qimingjin
 * @date 2023-08-18 16:46
 * @Description: 事件订阅枚举类，详情见 https://open.dingtalk.com/document/orgapp/stream
 */
@Getter
public enum StreamEventTypeEnum {

    /**
     *通讯录企业部门创建
     */
    ORG_DEPT_CREATE("org_dept_create","通讯录企业部门创建"),

    ORG_DEPT_MODIFY("org_dept_modify","通讯录企业部门修改"),

    ORG_DEPT_REMOVE("org_dept_remove","通讯录企业部门删除"),

    /**
     * 用户相关
     */
    USER_ADD_ORG("user_add_org","通讯录用户增加"),
    USER_MODIFY_ORG("user_modify_org","通讯录用户更改"),
    USER_LEAVE_ORG("user_leave_org","通讯录用户离职"),

    /**
     * 流程相关
     */
    BPMS_TASK_CHANGE("bpms_task_change","审批任务开始，结束，转交"),
    BPMS_INSTANCE_CHANGE("bpms_instance_change","审批实例开始，结束"),

    /**
     * 角色相关
     */
    LABEL_CONF_ADD("label_conf_add","增加角色或者角色组"),

    LABEL_CONF_MODIFY("label_conf_modify","修改角色或者角色组"),
    LABEL_CONF_DEL("label_conf_del","删除角色或者角色组"),
    LABEL_USER_CHANGE("label_user_change","员工角色信息发生变更"),

    /**
     * 考勤相关
     */
    ATTENDANCE_CHECK_RECORD("attendance_check_record","员工打卡事件"),
    ATTENDANCE_OVERTIME_DURATION("attendance_overtime_duration","员工加班事件"),
    ATTEND_BOSSCHECK_CHANGE("attend_bossCheck_change","考勤结果变更"),
    ATTEND_GROUP_CHANGE("attend_group_change","考勤组变更"),
    ATTEND_SHIFT_CHANGE("attend_shift_change","班次变更"),
    ATTENDANCE_SCHEDULE_CHANGE("attend_shift_change","员工排班变更事件"),
    CHECK_IN("check_in","用户签到")

    ;

    /**
     * 事件类型
     */
    private final String eventType;

    /**
     * 事件中文名称
     */
    private final String eventName;

    StreamEventTypeEnum(String eventType, String eventName){
        this.eventType=eventType;
        this.eventName=eventName;
    }
}
