package com.snow.dingtalk.service;

import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.dingtalk.api.response.OapiV2DepartmentGetResponse;
import com.dingtalk.api.response.OapiV2DepartmentListsubResponse;
import com.snow.dingtalk.model.request.DepartmentCreateRequest;
import com.snow.system.domain.SysDept;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/9/17 17:02
 */
@Service
public interface DepartmentService {
    /**
     * 创建部门
     * @param departmentDTO
     * @return
     */
    @Deprecated
    Long createDepartment(DepartmentCreateRequest departmentDTO);

    /**
     * 部门创建V2
     * @param departmentDTO 参数
     * @return 部门id
     */
    Long createDepartmentV2(DepartmentCreateRequest departmentDTO);

    /**
     * 更新部门
     * @param sysDept
     */
    String updateDepartment(SysDept sysDept);
    /**
     * 删除部门
     * @param
     */
    String deleteDepartment(Long id);

    /**
     * 根据ID获取钉钉部门详情
     * @param id
     */
    OapiV2DepartmentGetResponse.DeptGetResponse getDepartmentDetail(long id);

    /**
     * v1 获取部门列表
     * @return 部门列表
     */
    @Deprecated
    List<OapiDepartmentListResponse.Department> getDingTalkDepartmentList();

    /**
     * v2 版本获取部门列表
     * @return 部门列表
     */
    List<OapiV2DepartmentListsubResponse.DeptBaseResponse> getDingTalkDepartmentListV2();
}
