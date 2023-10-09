package com.snow.flowable.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.snow.common.core.page.PageModel;
import com.snow.common.enums.WorkRecordStatus;
import com.snow.common.utils.FunUtils;
import com.snow.flowable.common.constants.FlowConstants;
import com.snow.flowable.common.enums.FlowTypeEnum;
import com.snow.flowable.common.skipTask.TaskSkipService;
import com.snow.flowable.convert.TaskConvert;
import com.snow.flowable.domain.*;
import com.snow.flowable.domain.response.HistoricTaskInstanceResp;
import com.snow.flowable.domain.response.TaskResp;
import com.snow.flowable.service.FlowableService;
import com.snow.flowable.service.FlowableTaskService;
import com.snow.flowable.service.FlowableUserService;
import com.snow.system.domain.SysUser;
import com.snow.system.service.impl.SysUserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Attachment;
import org.flowable.engine.task.Comment;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.identitylink.api.history.HistoricIdentityLink;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/1/13 15:54
 */
@Slf4j
@Service
public class FlowableTaskServiceImpl implements FlowableTaskService {

    @Resource
    private TaskService taskService;

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private HistoryService historyService;

    @Resource
    private FlowableService flowableService;

    @Resource
    private FlowableUserService flowableUserService;

    @Resource
    private SysUserServiceImpl sysUserService;

    @Resource
    private AppFormServiceImpl appFormService;

    @Resource
    private TaskSkipService taskSkipService;

    @Override
    public PageModel<TaskResp> findTasksByUserId(String userId, TaskBaseDTO taskBaseDTO) {
        //根据用户ID获取角色
        Set<Long> sysRoles = flowableUserService.getFlowGroupByUserId(Long.parseLong(userId));

        TaskQuery taskQuery = taskService.createTaskQuery()
                .or()
                .taskCandidateOrAssigned(userId);
        //这个地方查询会去查询系统的用户组表，希望的是查询自己的用户表
        if(CollUtil.isNotEmpty(sysRoles)) {
            List<String> roleIds = sysRoles.stream().map(String::valueOf).collect(Collectors.toList());
            taskQuery.taskCandidateGroupIn(roleIds).endOr();
        }

        if(StrUtil.isNotEmpty(taskBaseDTO.getProcessInstanceId())){
            taskQuery.processInstanceId(taskBaseDTO.getProcessInstanceId());
        }
        if(StrUtil.isNotEmpty(taskBaseDTO.getTaskId())){
            taskQuery.taskId(taskBaseDTO.getTaskId());
        }

        if(StrUtil.isNotEmpty(taskBaseDTO.getTaskName())){
            taskQuery.taskNameLike(StrUtil.format("%{}%",taskBaseDTO.getTaskName()));
        }
        if(StrUtil.isNotEmpty(taskBaseDTO.getBusinessKey())){
            taskQuery.processInstanceBusinessKeyLike(StrUtil.format("%{}%",taskBaseDTO.getBusinessKey()));
        }
        if(StrUtil.isNotEmpty(taskBaseDTO.getDefinitionKey())){
            taskQuery.processDefinitionKey(taskBaseDTO.getDefinitionKey());
        }
        if(StrUtil.isNotEmpty(taskBaseDTO.getProcessDefinitionName())){
            taskQuery.processDefinitionNameLike(StrUtil.format("%{}%",taskBaseDTO.getProcessDefinitionName()));
        }

        long count = taskQuery
                .orderByTaskCreateTime()
                .desc()
                .count();
        List<Task> taskList = taskQuery
                .orderByTaskCreateTime()
                .desc()
                .listPage(taskBaseDTO.getPageNum(), taskBaseDTO.getPageSize());

        List<TaskResp> taskVoList = taskList.stream().map(t -> {
            TaskResp taskResp = TaskConvert.INSTANCE.convert(t);
            ProcessInstance historicProcessInstance = flowableService.getProcessInstanceById(t.getProcessInstanceId());
            taskResp.setProcessDefinitionName(historicProcessInstance.getProcessDefinitionName());
            String startUserId = historicProcessInstance.getStartUserId();
            SysUser sysUser = sysUserService.selectUserById(Long.parseLong(startUserId));
            taskResp.setStartUserId(startUserId);
            taskResp.setStartUserName(sysUser.getUserName());
            taskResp.setBusinessKey(historicProcessInstance.getBusinessKey());
            taskResp.setStartTime(historicProcessInstance.getStartTime());
            //设置流程类型
            Object hisVariable = flowableService.getHisVariable(t.getProcessInstanceId(), FlowConstants.PROCESS_TYPE);
            Object processType = Optional.ofNullable(hisVariable).orElse(FlowTypeEnum.API_PROCESS.getCode());
            taskResp.setProcessType(String.valueOf(processType));
            return taskResp;
        }).collect(Collectors.toList());
        PageModel<TaskResp> pageModel = new PageModel<> ();
        pageModel.setTotalCount((int)count);
        pageModel.setPagedRecords(taskVoList);
        return pageModel;
    }


    @Override
    public  void submitTask(FinishTaskDTO finishTaskDTO) {
        Task task = this.getTask(finishTaskDTO.getTaskId());
        FunUtils.isTure(ObjectUtil.isEmpty(task)).throwMessage(StrUtil.format("该任务ID:{}不存在",finishTaskDTO.getTaskId()));
        //获取表单
        AppForm appFrom = appFormService.getAppFrom(task.getProcessInstanceId());
        //设置审批人，若不设置则数据表userid字段为null
        Authentication.setAuthenticatedUserId(finishTaskDTO.getUserId());
        //添加评论
        taskService.addComment(task.getId(),task.getProcessInstanceId(),FlowConstants.OPINION,StrUtil.isNotEmpty(finishTaskDTO.getComment())?finishTaskDTO.getComment():"");
        //上传文件
        List<FileEntry> files = finishTaskDTO.getFiles();
        Optional.ofNullable(files).ifPresent(m->
            m.forEach(t->{
                if(ObjectUtil.isNull(t.getKey())) return;
                taskService.createAttachment(FlowConstants.URL,task.getId(),task.getProcessInstanceId(),t.getName(),t.getKey(),t.getUrl());
            })
        );
        //保存参数到流程
        Map<String, Object> paramMap = BeanUtil.beanToMap(finishTaskDTO);
        paramMap.remove(FinishTaskDTO.COMMENT);
        paramMap.remove(FinishTaskDTO.FILES);
        if(CollUtil.isNotEmpty(paramMap)){
            Set<Map.Entry<String, Object>> entries = paramMap.entrySet();
            entries.forEach(t-> runtimeService.setVariable(task.getExecutionId(),t.getKey(),t.getValue()));
        }
        //修改业务数据的时候流程业务数据重新赋值
        if(finishTaskDTO.getIsUpdateBus()&&appFrom!=null){
            BeanUtil.copyProperties(finishTaskDTO,appFrom);
            runtimeService.setVariable(task.getExecutionId(),FlowConstants.APP_FORM,appFrom);
        }

        FunUtils.isEmptyOrNoEmpty(task.getOwner())
                .presentOrElseHandle(t->{
                    //claim the task，当任务分配给了某一组人员时，需要该组人员进行抢占。抢到了就将该任务给谁处理，其他人不能处理。认领任务
                    FunUtils.isTureOrFalse(ObjectUtil.equals(task.getDelegationState() ,DelegationState.PENDING))
                            .trueOrFalseHandle(()->
                                            taskService.resolveTask(task.getId(), paramMap)
                                    ,()->{
                                        taskService.claim(task.getId(), finishTaskDTO.getUserId());
                                        taskService.complete(task.getId(), paramMap, true);
                                    });

                },()->{
                    taskService.claim(task.getId(),finishTaskDTO.getUserId());
                    taskService.complete(task.getId(),paramMap,true);
                });
        //推进自动推动的节点
        taskSkipService.autoSkip(task.getProcessInstanceId());
    }

    @Override
    public void automaticTask(String processInstanceId){
        FinishTaskDTO completeTaskDTO=new FinishTaskDTO();
        Task task=flowableService.getTaskProcessInstanceById(processInstanceId);
        completeTaskDTO.setTaskId(task.getId());
        completeTaskDTO.setIsStart(true);
        completeTaskDTO.setIsPass(true);
        completeTaskDTO.setUserId(task.getAssignee());
        this.submitTask(completeTaskDTO);
    }

    @Override
    public void transferTask(String taskId, String curUserId, String targetUserId) {
        try {
            //todo 转办记录
            taskService.setOwner(taskId, curUserId);
            taskService.setAssignee(taskId,targetUserId);
        }catch (Exception e) {
            log.error(e.getMessage(),e.getCause());
            throw new RuntimeException("转办任务失败，请联系管理员");
        }

    }

    /**
     * 委派：是将任务节点分给其他人处理，等其他人处理好之后，委派任务会自动回到委派人的任务中
     * @param taskId 任务ID
     * @param curUserId 当前人ID
     * @param targetUserId 目标人ID
     */
    @Override
    public void delegateTask(String taskId, String curUserId, String targetUserId) {
        try {
            taskService.setOwner(taskId, curUserId);
            taskService.delegateTask(taskId,targetUserId);
        }catch (Exception e) {
            log.error(e.getMessage(),e.getCause());
            throw new RuntimeException("转办任务失败，请联系管理员");
        }
    }

    @Override
    public PageModel<HistoricTaskInstanceResp> getHistoricTaskInstance(HistoricTaskInstanceDTO historicTaskInstanceDTO) {

        HistoricTaskInstanceQuery historicTaskInstanceQuery = buildHistoricTaskInstanceCondition(historicTaskInstanceDTO);
        long count = historicTaskInstanceQuery.orderByHistoricTaskInstanceEndTime().
                desc().
                count();

        List<HistoricTaskInstance> historicTaskInstances = historicTaskInstanceQuery.orderByHistoricTaskInstanceEndTime().
                desc().
                listPage(historicTaskInstanceDTO.getPageNum(), historicTaskInstanceDTO.getPageSize());


        List<HistoricTaskInstanceResp> historicTaskInstanceVOS = historicTaskInstances.stream().map(t -> {
            HistoricTaskInstanceResp historicTaskInstanceResp=TaskConvert.INSTANCE.convert(t);
            Map<String, Object> processVariables = t.getProcessVariables();
            String url= Optional.ofNullable(String.valueOf(processVariables.get(FlowConstants.BUS_VAR_URL))).orElse("");

            //设置返回详情页
            if(ObjectUtil.isNotEmpty(url)&&ObjectUtil.isNotEmpty(processVariables.get("id"))){
                historicTaskInstanceResp.setFromDetailUrl(StrUtil.format("{}/{}",url,processVariables.get("id")));
            }

            //转办的时候会出现到历史记录里，但是任务还没完结???
            if(ObjectUtil.isNotNull(t.getEndTime())){
                historicTaskInstanceResp.setHandleTaskTime(DateUtil.formatBetween(t.getCreateTime(), t.getEndTime(), BetweenFormatter.Level.SECOND));
            }
            //审批结果
            Optional.ofNullable(historicTaskInstanceResp.getTaskLocalVariables()).ifPresent(u->{
                historicTaskInstanceResp.setIsPass(String.valueOf(Optional.ofNullable(u.get(FlowConstants.IS_PASS)).orElse("")));
                historicTaskInstanceResp.setIsStart(String.valueOf(Optional.ofNullable(u.get(FlowConstants.IS_START)).orElse("")));
            });
            //设置流程类型
            Object hisVariable = flowableService.getHisVariable(t.getProcessInstanceId(), FlowConstants.PROCESS_TYPE);
            Object processType = Optional.ofNullable(hisVariable).orElse(FlowTypeEnum.API_PROCESS.getCode());
            historicTaskInstanceResp.setProcessType(String.valueOf(processType));

            ProcessInstanceVO processInstanceVo = flowableService.getProcessInstanceVoById(t.getProcessInstanceId());
            historicTaskInstanceResp.setProcessName(processInstanceVo.getProcessDefinitionName());
            historicTaskInstanceResp.setBusinessKey(processInstanceVo.getBusinessKey());
            historicTaskInstanceResp.setStartUserId(processInstanceVo.getStartUserId());
            historicTaskInstanceResp.setStartUserName(processInstanceVo.getStartUserName());
            return historicTaskInstanceResp;

        }).collect(Collectors.toList());

        PageModel<HistoricTaskInstanceResp> pageModel = new PageModel<> ();
        pageModel.setTotalCount((int)count);
        pageModel.setPagedRecords(historicTaskInstanceVOS);
        return pageModel;
    }


    @Override
    public List<HistoricTaskInstanceResp>  getHistoricTaskInstanceNoPage(HistoricTaskInstanceDTO historicTaskInstanceDTO){
        HistoricTaskInstanceQuery historicTaskInstanceQuery = buildHistoricTaskInstanceCondition(historicTaskInstanceDTO);
        List<HistoricTaskInstance> list = historicTaskInstanceQuery.orderByTaskCreateTime().asc().list();
        List<HistoricTaskInstanceResp> historicTaskInstanceVOS =TaskConvert.INSTANCE.convert(list);
        historicTaskInstanceVOS.forEach(this::warpTaskVoList);
        return historicTaskInstanceVOS;
    }

    /**
     * 构建历史任务查询条件
     * @param historicTaskInstanceDTO 传入的查询参数
     */
    private HistoricTaskInstanceQuery buildHistoricTaskInstanceCondition(HistoricTaskInstanceDTO historicTaskInstanceDTO){
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery();

        if(ObjectUtil.isNotEmpty(historicTaskInstanceDTO.getProcessInstanceId())){
            historicTaskInstanceQuery.processInstanceId(historicTaskInstanceDTO.getProcessInstanceId());
        }
        if(ObjectUtil.isNotEmpty(historicTaskInstanceDTO.getProcessDefinitionName())){
            historicTaskInstanceQuery.processDefinitionName(historicTaskInstanceDTO.getProcessDefinitionName());
        }
        if(ObjectUtil.isNotEmpty(historicTaskInstanceDTO.getUserId())){
            historicTaskInstanceQuery.taskAssignee(historicTaskInstanceDTO.getUserId());
        }
        if(ObjectUtil.isNotEmpty(historicTaskInstanceDTO.getBusinessKeyLike())){
            historicTaskInstanceQuery.processInstanceBusinessKeyLike("%"+historicTaskInstanceDTO.getBusinessKeyLike()+"%");
        }
        if(ObjectUtil.isNotEmpty(historicTaskInstanceDTO.getBusinessKey())){
            historicTaskInstanceQuery.processInstanceBusinessKey(historicTaskInstanceDTO.getBusinessKey());
        }
        Optional.ofNullable(historicTaskInstanceDTO.getTaskStatus()).ifPresent(m->{
            if(m.equals(WorkRecordStatus.NO_FINISHED)){
                historicTaskInstanceQuery.unfinished();
            }
            if(m.equals(WorkRecordStatus.FINISHED)){
                historicTaskInstanceQuery.finished();
            }
        });

        Optional.ofNullable(historicTaskInstanceDTO.getProcessStatus()).ifPresent(m->{
            if(m.equals(WorkRecordStatus.NO_FINISHED)){
                historicTaskInstanceQuery.processUnfinished();
            }
            if(m.equals(WorkRecordStatus.FINISHED)){
                historicTaskInstanceQuery.processFinished();
            }
        });
        historicTaskInstanceQuery.includeIdentityLinks()
                .includeProcessVariables()
                .includeTaskLocalVariables();
        return historicTaskInstanceQuery;
    }



    @Override
    public Set<SysUser> getIdentityLinksForTask(String taskId, String type) {
        Set<SysUser> userList=new HashSet<>();
        List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(taskId);
        if(CollUtil.isEmpty(identityLinksForTask)){
            return userList;
        }
        identityLinksForTask.stream().filter(identityLink -> StrUtil.isNotEmpty(identityLink.getGroupId()) &&identityLink.getType().equals(type))
                .forEach(identityLink -> {
                    String groupId = identityLink.getGroupId();
                    List<SysUser> sysUsers=flowableUserService.getUserByFlowGroupId(Long.parseLong(groupId));
                    userList.addAll(sysUsers);
                });
        identityLinksForTask.stream().filter(identityLink -> StrUtil.isNotEmpty(identityLink.getUserId()) &&identityLink.getType().equals(type))
                .forEach(identityLink -> {
                    String userId = identityLink.getUserId();
                    SysUser sysUsers = sysUserService.selectUserById(Long.parseLong(userId));
                    userList.add(sysUsers);
                });
        return userList;
    }

    /**
     * starter，USER_ID与PROC_INST_ID_，记录流程的发起者
     * candidate，USER_ID_ 或 GROUP_ID_ 其中一个必须有值、TASK_ID_有值，记录当前任务的指派人与指派组。
     * participant， USER_ID与PROC_INST_ID_有值，记录流程任务的参与者。
     * @param taskId
     * @return
     */
    @Override
    public Set<SysUser> getHistoricIdentityLinksForTask(String taskId){
        Set<SysUser> userList=new HashSet<>();
        List<HistoricIdentityLink> historicIdentityLinksForTask = historyService.getHistoricIdentityLinksForTask(taskId);
        if(CollUtil.isEmpty(historicIdentityLinksForTask)){
            return userList;
        }
        historicIdentityLinksForTask.stream().filter(identityLink -> StrUtil.isNotEmpty(identityLink.getGroupId()) &&identityLink.getType().equals("candidate"))
                .forEach(identityLink -> {
                    String groupId = identityLink.getGroupId();
                    List<SysUser> sysUsers=flowableUserService.getUserByFlowGroupId(Long.parseLong(groupId));
                    userList.addAll(sysUsers);
                });
        historicIdentityLinksForTask.stream().filter(identityLink -> StrUtil.isNotEmpty(identityLink.getUserId()) &&identityLink.getType().equals("candidate"))
                .forEach(identityLink -> {
                    String userId = identityLink.getUserId();
                    SysUser sysUsers = sysUserService.selectUserById(Long.parseLong(userId));
                    userList.add(sysUsers);
                });
        return userList;
    }

    @Override
    public Task getTask(String taskId) {
        return taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
    }

    @Override
    public HistoricTaskInstanceResp getHisTask(String taskId) {

        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery()
                .taskId(taskId)
                .includeIdentityLinks()
                .includeTaskLocalVariables()
                .includeProcessVariables()
                .singleResult();
        HistoricTaskInstanceResp historicTaskInstanceVO=TaskConvert.INSTANCE.convert(historicTaskInstance);
        warpTaskVoList(historicTaskInstanceVO);
        return historicTaskInstanceVO;
    }



    /**
     * 组装任务返回参数
     * @param historicTaskInstanceVO
     */
    private void warpTaskVoList(HistoricTaskInstanceResp historicTaskInstanceVO){
        //保存待办人
        Set<SysUser> identityLinksForTask = getHistoricIdentityLinksForTask(historicTaskInstanceVO.getTaskId());
        Optional.ofNullable(identityLinksForTask).ifPresent(m->{
            List<String> userNameList = identityLinksForTask.stream().map(SysUser::getUserName).collect(Collectors.toList());
            historicTaskInstanceVO.setHandleUserList(userNameList);
        });

        //办理人
        if (ObjectUtil.isNotEmpty(historicTaskInstanceVO.getAssignee())&&com.snow.common.utils.StringUtils.isNumeric(historicTaskInstanceVO.getAssignee())) {
            SysUser sysUser = sysUserService.selectUserById(Long.parseLong(historicTaskInstanceVO.getAssignee()));
            historicTaskInstanceVO.setAssigneeName(sysUser.getUserName());
        }

        //审批结果
        Optional.ofNullable(historicTaskInstanceVO.getTaskLocalVariables()).ifPresent(u->{
            historicTaskInstanceVO.setIsPass(String.valueOf(Optional.ofNullable(u.get(FlowConstants.IS_PASS)).orElse("")));
            historicTaskInstanceVO.setIsStart(String.valueOf(Optional.ofNullable(u.get(FlowConstants.IS_START)).orElse("")));
        });

        //获取评论
        List<Comment> comment = taskService.getTaskComments(historicTaskInstanceVO.getTaskId(), FlowConstants.OPINION);
        historicTaskInstanceVO.setCommentList(comment);

        //获取附件
        List<Attachment> taskAttachments = taskService.getTaskAttachments(historicTaskInstanceVO.getTaskId());
        historicTaskInstanceVO.setAttachmentList(taskAttachments);
        //计算任务历时
        if(ObjectUtil.isNotEmpty(historicTaskInstanceVO.getCompleteTime())){
            historicTaskInstanceVO.setHandleTaskTime(DateUtil.formatBetween(historicTaskInstanceVO.getStartTime(), historicTaskInstanceVO.getCompleteTime(), BetweenFormatter.Level.SECOND));
        }
        HistoricProcessInstance historicProcessInstance = flowableService.getHistoricProcessInstanceById(historicTaskInstanceVO.getProcessInstanceId());
        historicTaskInstanceVO.setProcessName(historicProcessInstance.getProcessDefinitionName());
        historicTaskInstanceVO.setBusinessKey(historicProcessInstance.getBusinessKey());
    }


}
