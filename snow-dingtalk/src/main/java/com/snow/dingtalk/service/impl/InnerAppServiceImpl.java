package com.snow.dingtalk.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSON;
import com.aliyun.dingtalkmicro_app_1_0.Client;
import com.aliyun.dingtalkmicro_app_1_0.models.*;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.snow.common.exception.SyncDataException;
import com.snow.dingtalk.common.BaseService;
import com.snow.dingtalk.model.response.InnerAppResponse;
import com.snow.dingtalk.model.response.ListAllInnerAppResponse;
import com.snow.dingtalk.service.InnerAppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qimingjin
 * @date 2023-08-23 14:00
 * @Description: 应用内部实现类
 */
@Service
@Slf4j
public class InnerAppServiceImpl extends BaseService implements InnerAppService {

    /**
     * 创建企业内应用客户端
     * @return 客户端对象
     * @throws Exception
     */
    public Client createInnerAppClient() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new Client(config);
    }

    @Override
    public InnerAppResponse crateInnerApp(com.snow.dingtalk.model.request.CreateInnerAppRequest innerAppRequest) {
        CreateInnerAppHeaders createInnerAppHeaders = new CreateInnerAppHeaders();
        createInnerAppHeaders.xAcsDingtalkAccessToken = this.getDingTalkTokenV2();
        CreateInnerAppRequest createInnerAppRequest = BeanUtil.toBean(innerAppRequest, CreateInnerAppRequest.class);
        createInnerAppRequest.setScopeType("BASE");
        List<String> ipWhiteList = Arrays.stream(innerAppRequest.getIpWhiteList().split(",")).collect(Collectors.toList());
        createInnerAppRequest.setIpWhiteList(ipWhiteList);
        try {
            CreateInnerAppResponse innerAppWithOptions = this.createInnerAppClient().createInnerAppWithOptions(createInnerAppRequest, createInnerAppHeaders, new RuntimeOptions());
            return BeanUtil.toBean(innerAppWithOptions.getBody(),InnerAppResponse.class);
        } catch (TeaException err) {
            if (CharSequenceUtil.isNotBlank(err.code) &&CharSequenceUtil.isNotBlank(err.message)) {
                log.error("InnerAppServiceImpl->crateInnerApp error msg code:{},message:{}",err.code,err.message);
                log.error(err.getMessage(),err);
                throw new SyncDataException(JSON.toJSONString(createInnerAppRequest),err.message);
            }
            log.error(err.getMessage(),err);
            throw new SyncDataException(JSON.toJSONString(createInnerAppRequest),err.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new SyncDataException(JSON.toJSONString(createInnerAppRequest),e.getMessage());
        }
    }

    @Override
    public List<ListAllInnerAppResponse> listAllApp() {
        ListAllAppHeaders listAllAppHeaders = new ListAllAppHeaders();
        listAllAppHeaders.xAcsDingtalkAccessToken = this.getDingTalkTokenV2();
        try {
            ListAllAppResponse listAllAppResponse = this.createInnerAppClient().listAllAppWithOptions(listAllAppHeaders, new RuntimeOptions());
            return BeanUtil.copyToList(listAllAppResponse.getBody().getAppList(),ListAllInnerAppResponse.class);
        } catch (TeaException err) {
            if (CharSequenceUtil.isNotBlank(err.code) &&CharSequenceUtil.isNotBlank(err.message)) {
                log.error("InnerAppServiceImpl->listAllApp error msg code:{},message:{}",err.code,err.message);
                log.error(err.getMessage(),err);
                throw new SyncDataException(JSON.toJSONString(listAllAppHeaders),err.message);
            }
            log.error(err.getMessage(),err);
            throw new SyncDataException(JSON.toJSONString(listAllAppHeaders),err.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new SyncDataException(JSON.toJSONString(listAllAppHeaders),e.getMessage());
        }
    }

    @Override
    public boolean updateInnerApp(com.snow.dingtalk.model.request.UpdateInnerAppRequest innerAppRequest) {
        UpdateInnerAppHeaders updateInnerAppHeaders = new UpdateInnerAppHeaders();
        updateInnerAppHeaders.xAcsDingtalkAccessToken = this.getDingTalkTokenV2();
        UpdateInnerAppRequest updateInnerAppRequest =BeanUtil.toBean(innerAppRequest,UpdateInnerAppRequest.class);
        try {
            UpdateInnerAppResponse updateInnerAppResponse = this.createInnerAppClient().updateInnerAppWithOptions(innerAppRequest.getAgentId(), updateInnerAppRequest, updateInnerAppHeaders, new RuntimeOptions());
            return updateInnerAppResponse.getBody().getResult();
        } catch (TeaException err) {
            if (CharSequenceUtil.isNotBlank(err.code) &&CharSequenceUtil.isNotBlank(err.message)) {
                log.error("InnerAppServiceImpl->updateInnerApp error msg code:{},message:{}",err.code,err.message);
                log.error(err.getMessage(),err);
                throw new SyncDataException(JSON.toJSONString(updateInnerAppRequest),err.message);
            }
            log.error(err.getMessage(),err);
            throw new SyncDataException(JSON.toJSONString(updateInnerAppRequest),err.getMessage());

        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new SyncDataException(JSON.toJSONString(updateInnerAppRequest),e.getMessage());
        }
    }

    @Override
    public boolean deleteInnerApp(Long agentId, String opUnionId) {
        DeleteInnerAppHeaders deleteInnerAppHeaders = new DeleteInnerAppHeaders();
        deleteInnerAppHeaders.xAcsDingtalkAccessToken = this.getDingTalkTokenV2();
        DeleteInnerAppRequest deleteInnerAppRequest = new DeleteInnerAppRequest()
                .setOpUnionId(opUnionId);
        try {
            DeleteInnerAppResponse deleteInnerAppResponse = this.createInnerAppClient().deleteInnerAppWithOptions(String.valueOf(agentId), deleteInnerAppRequest, deleteInnerAppHeaders, new RuntimeOptions());
            return deleteInnerAppResponse.getBody().getResult();
        } catch (TeaException err) {
            if (CharSequenceUtil.isNotBlank(err.code) && CharSequenceUtil.isNotBlank(err.message)) {
                log.error("InnerAppServiceImpl->deleteInnerApp error msg code:{},message:{}", err.code, err.message);
                log.error(err.getMessage(), err);
                throw new SyncDataException(JSON.toJSONString(deleteInnerAppRequest), err.message);
            }
            log.error(err.getMessage(), err);
            throw new SyncDataException(JSON.toJSONString(deleteInnerAppRequest), err.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new SyncDataException(JSON.toJSONString(deleteInnerAppRequest), e.getMessage());
        }
    }
}
