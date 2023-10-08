package com.snow.dingtalk.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.alibaba.fastjson.JSON;
import com.aliyun.dingtalklive_1_0.Client;
import com.aliyun.dingtalklive_1_0.models.*;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.snow.common.exception.SyncDataException;
import com.snow.dingtalk.common.BaseService;
import com.snow.dingtalk.model.response.LiveInfoResponse;
import com.snow.dingtalk.model.response.LiveWatchDetailResponse;
import com.snow.dingtalk.service.LiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author qimingjin
 * @date 2023-08-23 09:52
 * @Description: 钉钉直播服务实现类
 */
@Slf4j
@Service
public class LiveServiceImpl extends BaseService implements LiveService {

    /**
     * 创建直播客户端
     * @return 客户端
     * @throws Exception
     */
    public Client createLiveClient() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new Client(config);
    }


    @Override
    public String createLive(com.snow.dingtalk.model.request.CreateLiveRequest liveRequest) {
        CreateLiveHeaders createLiveHeaders = new CreateLiveHeaders();
        createLiveHeaders.xAcsDingtalkAccessToken = this.getDingTalkTokenV2();
        CreateLiveRequest createLiveRequest = BeanUtil.toBean(liveRequest, CreateLiveRequest.class);
        try {
            CreateLiveResponse liveWithOptions = this.createLiveClient().createLiveWithOptions(createLiveRequest, createLiveHeaders, new RuntimeOptions());
            return liveWithOptions.getBody().getResult().getLiveId();
        }catch (TeaException err) {
            if (CharSequenceUtil.isNotBlank(err.code) &&CharSequenceUtil.isNotBlank(err.message)) {
                log.error("LiveServiceImpl->createLive error msg code:{},message:{}",err.code,err.message);
                log.error(err.getMessage(),err);
                throw new SyncDataException(JSON.toJSONString(createLiveRequest),err.message);
            }
            log.error(err.getMessage(),err);
            throw new SyncDataException(JSON.toJSONString(createLiveRequest),err.getMessage());
        } catch (Exception err) {
            log.error(err.getMessage(),err);
            throw new SyncDataException(JSON.toJSONString(createLiveRequest),err.getMessage());
        }
    }

    @Override
    public Boolean deleteLive(String liveId, String unionId) {
        DeleteLiveHeaders deleteLiveHeaders = new DeleteLiveHeaders();
        deleteLiveHeaders.xAcsDingtalkAccessToken = this.getDingTalkTokenV2();
        DeleteLiveRequest deleteLiveRequest = new DeleteLiveRequest()

                .setLiveId(liveId)
                .setUnionId(unionId);
        try {
            DeleteLiveResponse deleteLiveResponse = this.createLiveClient().deleteLiveWithOptions(deleteLiveRequest, deleteLiveHeaders, new RuntimeOptions());
            return deleteLiveResponse.getBody().getResult().getSuccess();
        } catch (TeaException err) {
            if (CharSequenceUtil.isNotBlank(err.code) &&CharSequenceUtil.isNotBlank(err.message)) {
                log.error("LiveServiceImpl->deleteLive error msg code:{},message:{}",err.code,err.message);
                log.error(err.getMessage(),err);
                throw new SyncDataException(JSON.toJSONString(deleteLiveRequest),err.message);
            }
            log.error(err.getMessage(),err);
            throw new SyncDataException(JSON.toJSONString(deleteLiveRequest),err.getMessage());

        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new SyncDataException(JSON.toJSONString(deleteLiveRequest),e.getMessage());
        }
    }

    @Override
    public Boolean updateLive(com.snow.dingtalk.model.request.UpdateLiveRequest liveRequest) {
        UpdateLiveHeaders updateLiveHeaders = new UpdateLiveHeaders();
        updateLiveHeaders.xAcsDingtalkAccessToken =this.getDingTalkTokenV2();
        UpdateLiveRequest updateLiveRequest = BeanUtil.toBean(liveRequest, UpdateLiveRequest.class);
        try {
            UpdateLiveResponse updateLiveResponse = this.createLiveClient().updateLiveWithOptions(updateLiveRequest, updateLiveHeaders, new RuntimeOptions());
            return updateLiveResponse.getBody().getResult().getSuccess();
        } catch (TeaException err) {
            if (CharSequenceUtil.isNotBlank(err.code) &&CharSequenceUtil.isNotBlank(err.message)) {
                log.error("LiveServiceImpl->updateLive error msg code:{},message:{}",err.code,err.message);
                log.error(err.getMessage(),err);
                throw new SyncDataException(JSON.toJSONString(updateLiveRequest),err.message);
            }
            log.error(err.getMessage(),err);
            throw new SyncDataException(JSON.toJSONString(updateLiveRequest),err.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new SyncDataException(JSON.toJSONString(updateLiveRequest),e.getMessage());
        }
    }

    @Override
    public LiveInfoResponse getLiveDetail(String liveId, String unionId) {
        QueryLiveInfoHeaders queryLiveInfoHeaders = new QueryLiveInfoHeaders();
        queryLiveInfoHeaders.xAcsDingtalkAccessToken = this.getDingTalkTokenV2();
        QueryLiveInfoRequest queryLiveInfoRequest = new QueryLiveInfoRequest()
                .setLiveId(liveId)
                .setUnionId(unionId);
        try {
            QueryLiveInfoResponse queryLiveInfoResponse = this.createLiveClient().queryLiveInfoWithOptions(queryLiveInfoRequest, queryLiveInfoHeaders, new RuntimeOptions());
            return BeanUtil.toBean(queryLiveInfoResponse.getBody().getResult().getLiveInfo(),LiveInfoResponse.class);
        } catch (TeaException err) {
            if (CharSequenceUtil.isNotBlank(err.code) &&CharSequenceUtil.isNotBlank(err.message)) {
                log.error("LiveServiceImpl->getLiveDetail error msg code:{},message:{}",err.code,err.message);
                log.error(err.getMessage(),err);
                throw new SyncDataException(JSON.toJSONString(queryLiveInfoRequest),err.message);
            }
            log.error(err.getMessage(),err);
            throw new SyncDataException(JSON.toJSONString(queryLiveInfoRequest),err.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new SyncDataException(JSON.toJSONString(queryLiveInfoRequest),e.getMessage());
        }
    }

    @Override
    public LiveWatchDetailResponse queryLiveWatchDetail(String liveId, String unionId) {
        QueryLiveWatchDetailHeaders queryLiveWatchDetailHeaders = new QueryLiveWatchDetailHeaders();
        queryLiveWatchDetailHeaders.xAcsDingtalkAccessToken = this.getDingTalkTokenV2();
        QueryLiveWatchDetailRequest queryLiveWatchDetailRequest = new QueryLiveWatchDetailRequest()
                .setLiveId(liveId)
                .setUnionId(unionId);
        try {
            QueryLiveWatchDetailResponse queryLiveWatchDetailResponse = this.createLiveClient().queryLiveWatchDetailWithOptions(queryLiveWatchDetailRequest, queryLiveWatchDetailHeaders, new RuntimeOptions());
            return BeanUtil.toBean(queryLiveWatchDetailResponse.getBody().getResult(),LiveWatchDetailResponse.class);
        } catch (TeaException err) {
            if (CharSequenceUtil.isNotBlank(err.code) &&CharSequenceUtil.isNotBlank(err.message)) {
                log.error("LiveServiceImpl->queryLiveWatchDetail error msg code:{},message:{}",err.code,err.message);
                log.error(err.getMessage(),err);
                throw new SyncDataException(JSON.toJSONString(queryLiveWatchDetailRequest),err.message);
            }
            log.error(err.getMessage(),err);
            throw new SyncDataException(JSON.toJSONString(queryLiveWatchDetailRequest),err.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new SyncDataException(JSON.toJSONString(queryLiveWatchDetailRequest),e.getMessage());
        }
    }
}
