package com.snow.flowable.domain.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.utils.bean.BeanUtils;
import com.snow.flowable.domain.AppForm;
import lombok.Data;
import org.flowable.engine.task.Attachment;
import org.flowable.engine.task.Comment;
import org.flowable.task.api.history.HistoricTaskInstance;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author qimingjin
 * @date 2022-12-20 13:54
 * @Description:
 */
@Data
public class HistoricTaskInstanceResp extends TaskResp implements Serializable {
    private static final long serialVersionUID = -4019046936599749166L;

    /**
     * 流程执行ID
     */
    private String executionId;

    /**
     *流程名称
     */
    private String processName;

    /**
     * 任务定义ID
     */
    private String taskDefinitionId;

    /**
     * 用户名称
     */
    private String assigneeName;

    /**
     * 是否通过驳回
     */
    private String isPass;

    /**
     * 是否发起
     */
    private String isStart;

    /**
     * 评论
     */
    private List<Comment> commentList;

    /**
     *附件
     */
    private  List<Attachment> attachmentList;

    /**
     * 任务节点变量
     */
    private Map<String, Object> taskLocalVariables;

    /**
     * 流程变量
     */
    private Map<String, Object> processVariables;


    private String deleteReason;

    /**
     * 流程类型
     */
    private String processType;

    private String fromDetailUrl;

    private AppForm appForm;


    private int priority;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dueDate;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date claimTime;


    public static List<HistoricTaskInstanceResp> warpList(List<HistoricTaskInstance> historicTaskInstanceList){
        return historicTaskInstanceList.stream().map(t->{
            HistoricTaskInstanceResp historicTaskInstanceVO=new HistoricTaskInstanceResp();
            BeanUtils.copyProperties(t,historicTaskInstanceVO);
            historicTaskInstanceVO.setTaskLocalVariables(t.getTaskLocalVariables());
            historicTaskInstanceVO.setTaskId(t.getId());
            historicTaskInstanceVO.setTaskName(t.getName());
            //老的createTime底层换成了time getCreateTime
            historicTaskInstanceVO.setStartTime(t.getTime());
            historicTaskInstanceVO.setCompleteTime(t.getEndTime());
            historicTaskInstanceVO.setProcessVariables(t.getProcessVariables());
            return historicTaskInstanceVO;
        }).collect(Collectors.toList());
    }
}
