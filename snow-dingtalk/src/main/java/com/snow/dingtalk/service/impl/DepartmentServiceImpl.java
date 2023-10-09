package com.snow.dingtalk.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.snow.common.annotation.DingTalkLog;
import com.snow.common.constant.Constants;
import com.snow.common.enums.DingTalkLogType;
import com.snow.common.exception.SyncDataException;
import com.snow.dingtalk.common.BaseConstantUrl;
import com.snow.dingtalk.model.client.SnowDingTalkDefaultClient;
import com.snow.dingtalk.model.request.DepartmentCreateRequest;
import com.snow.dingtalk.service.DepartmentService;
import com.snow.system.domain.SysDept;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2020-10-31 12:59
 **/
@Service(value = "departmentServiceImpl")
@Slf4j
public class DepartmentServiceImpl implements DepartmentService  {

    @Override
    @DingTalkLog(dingTalkLogType = DingTalkLogType.DEPARTMENT_CREATE,dingTalkUrl=BaseConstantUrl.DEPARTMENT_CREATE)
    public Long createDepartment(DepartmentCreateRequest departmentDTO){
        OapiDepartmentCreateRequest request = new OapiDepartmentCreateRequest();
        request.setParentid(departmentDTO.getParentid());
        request.setCreateDeptGroup(departmentDTO.getCreateDeptGroup());
        request.setOrder(departmentDTO.getOrder());
        request.setName(departmentDTO.getName());
        request.setHttpMethod(com.taobao.api.Constants.METHOD_POST);
        request.setSourceIdentifier(departmentDTO.getSourceIdentifier());
        try {
            OapiDepartmentCreateResponse response = new SnowDingTalkDefaultClient<OapiDepartmentCreateResponse>().execute(request,BaseConstantUrl.DEPARTMENT_CREATE);
            if(response.getErrcode()==0){
                return response.getId();
            }else {
                throw new SyncDataException(JSON.toJSONString(request),response.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("@@钉钉创建部门createDepartment异常：{}",e.getMessage());
            throw new SyncDataException(JSON.toJSONString(request),e.getErrMsg());
        }
    }

    @Override
    @DingTalkLog(dingTalkLogType = DingTalkLogType.DEPARTMENT_CREATE,dingTalkUrl=BaseConstantUrl.DEPARTMENT_CREATE_V2)
    public Long createDepartmentV2(DepartmentCreateRequest departmentDTO){
        OapiV2DepartmentCreateRequest request = new OapiV2DepartmentCreateRequest();
        request.setParentId(Long.parseLong(departmentDTO.getParentid()));
        request.setCreateDeptGroup(departmentDTO.getCreateDeptGroup());
        request.setOrder(Long.parseLong(departmentDTO.getOrder()));
        request.setSourceIdentifier(departmentDTO.getSourceIdentifier());
        request.setName(departmentDTO.getName());
        try {
            OapiV2DepartmentCreateResponse response = new SnowDingTalkDefaultClient<OapiV2DepartmentCreateResponse>().execute(request,BaseConstantUrl.DEPARTMENT_CREATE_V2);
            if(response.getErrcode()==0){
                return response.getResult().getDeptId();
            }else {
                throw new SyncDataException(JSON.toJSONString(request),response.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("@@钉钉创建部门createDepartment异常：{}",e.getMessage());
            throw new SyncDataException(JSON.toJSONString(request),e.getErrMsg());
        }
    }

    @Override
    @DingTalkLog(dingTalkLogType = DingTalkLogType.DEPARTMENT_UPDATE,dingTalkUrl=BaseConstantUrl.DEPARTMENT_UPDATE)
    public String updateDepartment(SysDept sysDept) {
        OapiV2DepartmentUpdateRequest req = new OapiV2DepartmentUpdateRequest();
        req.setDeptId(sysDept.getDeptId());
        req.setParentId(sysDept.getParentId());
        //是否限制本部门成员查看通讯录：true：开启限制。开启后本部门成员只能看到限定范围内的通讯录 false：不限制
        req.setOuterDept(false);
        req.setHideDept(false);
        //是否创建群
        req.setCreateDeptGroup(true);
        req.setOrder(Long.parseLong(sysDept.getOrderNum()));
        req.setName(sysDept.getDeptName());
        req.setLanguage(Constants.ZH_CN);
        req.setAutoAddUser(false);
        //部门主管列表
        if(CollUtil.isNotEmpty(sysDept.getLeader())){
            req.setDeptManagerUseridList(sysDept.getLeader().stream().map(String::valueOf).collect(Collectors.joining(",")));
        }
        try {
            OapiV2DepartmentUpdateResponse rsp = new SnowDingTalkDefaultClient<OapiV2DepartmentUpdateResponse>().execute(req,BaseConstantUrl.DEPARTMENT_UPDATE);
            if(rsp.getErrcode()==0){
               return rsp.getRequestId();
            }else {
                throw new SyncDataException(JSON.toJSONString(req),rsp.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("@@更新钉钉部门updateDepartment异常：{}",e.getMessage());
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }

    @Override
    @DingTalkLog(dingTalkLogType = DingTalkLogType.DEPARTMENT_DELETED,dingTalkUrl=BaseConstantUrl.DEPARTMENT_DELETE)
    public String deleteDepartment(Long id) {
        OapiV2DepartmentDeleteRequest req = new OapiV2DepartmentDeleteRequest();
        req.setDeptId(id);
        try {
            OapiV2DepartmentDeleteResponse rsp = new SnowDingTalkDefaultClient<OapiV2DepartmentDeleteResponse>().execute(req,BaseConstantUrl.DEPARTMENT_DELETE);
            if(rsp.getErrcode()==0){
                return rsp.getRequestId();
            }else {
                throw new SyncDataException(JSON.toJSONString(req),rsp.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("@@删除钉钉部门deleteDepartment异常：{}",e.getMessage());
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }

    }

    @Override
    @DingTalkLog(dingTalkLogType = DingTalkLogType.DEPARTMENT_QUERY,dingTalkUrl=BaseConstantUrl.GET_DEPARTMENT_BY_ID)
    public OapiV2DepartmentGetResponse.DeptGetResponse getDepartmentDetail(long id) {
        OapiV2DepartmentGetRequest req = new OapiV2DepartmentGetRequest();
        req.setDeptId(id);
        req.setLanguage(Constants.ZH_CN);
        try {
            OapiV2DepartmentGetResponse rsp = new SnowDingTalkDefaultClient<OapiV2DepartmentGetResponse>().execute(req,BaseConstantUrl.GET_DEPARTMENT_BY_ID);
            if(rsp.getErrcode()==0){
                return rsp.getResult();
            }else {
                throw new SyncDataException(JSON.toJSONString(req),rsp.getErrmsg());
            }
        } catch (ApiException e) {
            throw new SyncDataException(JSON.toJSONString(req),e.getMessage());
        }
    }


    @Override
    @DingTalkLog(dingTalkLogType = DingTalkLogType.DEPARTMENT_QUERY,dingTalkUrl=BaseConstantUrl.DEPARTMENT_LIST)
    public List<OapiDepartmentListResponse.Department> getDingTalkDepartmentList(){
        OapiDepartmentListRequest request = new OapiDepartmentListRequest();
        request.setHttpMethod("GET");
        request.setFetchChild(true);
        try {
            OapiDepartmentListResponse response = new SnowDingTalkDefaultClient<OapiDepartmentListResponse>().execute(request,BaseConstantUrl.DEPARTMENT_LIST);
            if(response.getErrcode()==0){
                return response.getDepartment();
            }else {
                throw new SyncDataException(JSON.toJSONString(request),response.getErrmsg());
            }
        } catch (ApiException e) {
            throw new SyncDataException(JSON.toJSONString(request),e.getMessage());
        }
    }


    @Override
    @DingTalkLog(dingTalkLogType = DingTalkLogType.DEPARTMENT_QUERY,dingTalkUrl=BaseConstantUrl.DEPARTMENT_LIST_V2)
    public List<OapiV2DepartmentListsubResponse.DeptBaseResponse> getDingTalkDepartmentListV2(){
        OapiV2DepartmentListsubRequest request = new OapiV2DepartmentListsubRequest();
        request.setLanguage(Constants.ZH_CN);
        try {
            OapiV2DepartmentListsubResponse response = new SnowDingTalkDefaultClient<OapiV2DepartmentListsubResponse>().execute(request,BaseConstantUrl.DEPARTMENT_LIST_V2);
            if(response.getErrcode()==0){
                return response.getResult();
            }else {
                throw new SyncDataException(JSON.toJSONString(request),response.getErrmsg());
            }
        } catch (ApiException e) {
            throw new SyncDataException(JSON.toJSONString(request),e.getMessage());
        }
    }
}
