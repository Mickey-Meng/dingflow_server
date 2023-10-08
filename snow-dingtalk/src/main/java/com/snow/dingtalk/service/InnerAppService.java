package com.snow.dingtalk.service;

import com.snow.dingtalk.model.request.CreateInnerAppRequest;
import com.snow.dingtalk.model.request.UpdateInnerAppRequest;
import com.snow.dingtalk.model.response.InnerAppResponse;
import com.snow.dingtalk.model.response.ListAllInnerAppResponse;

import java.util.List;

/**
 * @author qimingjin
 * @date 2023-08-23 13:59
 * @Description: 企业内部应用管理
 */
public interface InnerAppService {

    /**
     * 创建内部应用 参考接口: https://open.dingtalk.com/document/orgapp/create-an-h5-application-for-your-enterprise
     * @param createInnerAppRequest 请求参数
     * @return 响应结果
     */
    InnerAppResponse crateInnerApp(CreateInnerAppRequest createInnerAppRequest);

    /**
     * 获取所有的应用 详情参考接口: https://open.dingtalk.com/document/orgapp/obtains-a-list-of-all-enterprise-applications
     * @return 应用对象
     */
    List<ListAllInnerAppResponse> listAllApp();

    /**
     * 更新企业内部应用 详情参考接口: https://open.dingtalk.com/document/orgapp/update-internal-h5-applications
     * @param updateInnerAppRequest 更新参数
     * @return 是否成功
     */
    boolean updateInnerApp(UpdateInnerAppRequest updateInnerAppRequest);

    /**
     * 删除企业内部应用  详情参考接口: https://open.dingtalk.com/document/orgapp/delete-an-internal-h5-application
     * @param agentId 应用的agentId
     * @param opUnionId 操作人的unionId
     * @return 是否成功
     */
    boolean deleteInnerApp(Long agentId,String opUnionId);
}
