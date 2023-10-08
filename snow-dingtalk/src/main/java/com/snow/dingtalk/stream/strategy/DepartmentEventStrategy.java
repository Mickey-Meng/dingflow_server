package com.snow.dingtalk.stream.strategy;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.response.OapiV2DepartmentGetResponse;
import com.snow.dingtalk.service.DepartmentService;
import com.snow.system.domain.SysDept;
import com.snow.system.service.ISysDeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author qimingjin
 * @date 2023-08-18 16:32
 * @Description: 部门事件监听策略
 */
@Slf4j
@Component
public class DepartmentEventStrategy implements IEventStrategy{
    private final DepartmentService departmentService= SpringUtil.getBean(DepartmentService.class);

    private final ISysDeptService sysDeptService= SpringUtil.getBean(ISysDeptService.class);
    private static final String DEPT_ID="deptId";

    @Override
    public void handle(String eventId, String eventType, JSONObject bizData) {
        log.info("DepartmentEventStrategy->handle param eventType:{},bizData:{}",eventType, JSON.toJSONString(bizData));
        switch (eventType){
            case "org_dept_create":
                JSONArray createDeptId = bizData.getJSONArray(DEPT_ID);
                this.createDepartment(createDeptId);
                break;
            case "org_dept_modify" :
                JSONArray modifyDeptId = bizData.getJSONArray(DEPT_ID);
                this.modifyDepartment(modifyDeptId);
                break;
            case "org_dept_remove":
                JSONArray removeDeptId = bizData.getJSONArray(DEPT_ID);
                this.removeDepartment(removeDeptId);
                break;
            default:
        }
    }


    private void createDepartment(JSONArray deptIds){
        for (Object deptId : deptIds) {
            //根据回调deptId，查询详情
            SysDept sysDept = warpSysDept(Long.parseLong(String.valueOf(deptId)));
            //判断是更新还是插入
            SysDept dept = sysDeptService.selectDeptById(Long.parseLong(String.valueOf(deptId)));
            if(ObjectUtil.isNotNull(dept)){
                sysDeptService.updateDept(sysDept);
            }else {
                sysDeptService.insertDept(sysDept);
            }
        }
    }

    private void modifyDepartment(JSONArray deptIds){
        for (Object deptId : deptIds) {
            //判断是更新还是插入
            SysDept dept = sysDeptService.selectDeptById(Long.parseLong(String.valueOf(deptId)));
            if(ObjectUtil.isEmpty(dept)) {
                return;
            }
            sysDeptService.updateDept(warpSysDept((long) deptId));
        }
    }

    private void removeDepartment(JSONArray deptIds){
        for (Object deptId : deptIds) {
            //判断是更新还是插入
            SysDept dept = sysDeptService.selectDeptById(Long.parseLong(String.valueOf(deptId)));
            if(ObjectUtil.isEmpty(dept)) {
                return;
            }
            sysDeptService.deleteDeptById(Long.parseLong(String.valueOf(deptId)));
        }
    }


    private SysDept warpSysDept(Long deptId){
        OapiV2DepartmentGetResponse.DeptGetResponse departmentDetail = departmentService.getDepartmentDetail(deptId);
        SysDept sysDept = new SysDept();
        sysDept.setDeptId(departmentDetail.getDeptId());
        sysDept.setDeptName(departmentDetail.getName());
        sysDept.setOrderNum(String.valueOf(departmentDetail.getOrder()));
        sysDept.setParentId(departmentDetail.getParentId());
        sysDept.setLeader(departmentDetail.getDeptManagerUseridList());
        sysDept.setIsSyncDingTalk(false);
        sysDept.setPhone(departmentDetail.getTelephone());
        return sysDept;
    }


}
