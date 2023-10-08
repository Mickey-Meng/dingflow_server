package com.snow.flowable.domain.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author qimingjin
 * @date 2022-12-20 11:00
 * @Description: 任务响应实体
 */
@Data
public class TaskResp implements Serializable {
    private static final long serialVersionUID = -5251950654633617164L;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 流程定义名称
     */
    private String processDefinitionName;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 委托人
     */
    private String owner;

    /**
     * 父任务ID
     */
    private String parentTaskId;

    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 任务处理人
     */
    private String Assignee;

    /**
     * 任务创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 分类
     */
    private String category;

    /**
     * fromKey
     */
    private String formKey;

    /**
     * 流程类型
     */
    private String processType;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 流程发起人id
     */
    private String startUserId;

    /**
     * 发起人名称
     */
    private String startUserName;

    /**
     * 业务主键
     */
    private String businessKey;
    /**
     * 任务发起时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 任务完成时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date completeTime;

    /**
     * 处理任务时间
     */
    private String handleTaskTime;
    /**
     * 任务状态（0--待处理，1--已处理）
     */
    private Integer taskStatus=0;
    /**
     * 下个节点
     */
    private String nextTaskName;
    /**
     * 任务定义key
     */
    private String taskDefinitionKey;
    /**
     * 任务待处理人
     */
    private List<String> handleUserList;
}
