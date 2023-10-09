package com.snow.dingtalk.common;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.text.CharSequenceUtil;
import com.alibaba.fastjson.JSON;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.snow.common.annotation.DingTalkLog;
import com.snow.common.constant.CacheConstants;
import com.snow.common.constant.Constants;
import com.snow.common.enums.DingTalkLogType;
import com.snow.common.exception.SyncDataException;
import com.snow.common.utils.CacheUtils;
import com.snow.common.utils.spring.SpringUtils;
import com.snow.system.service.impl.SysConfigServiceImpl;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/9/16 10:46
 */
@Slf4j
public class BaseService {

    public static final String TOKEN="dingtalk_token";

    public static final String TOKENV2="dingtalk_token_V2";

    private final SysConfigServiceImpl sysConfigService=SpringUtils.getBean(SysConfigServiceImpl.class);


    /**
     * 获取token
     * @return
     */
    @DingTalkLog(dingTalkLogType = DingTalkLogType.GET_TOKEN,dingTalkUrl=BaseConstantUrl.GET_TOKEN_URL)
    public String getDingTalkToken(){
        //创建缓存，缓存默认是7100S
        TimedCache<String, String> timedCache = CacheUtil.newTimedCache(7100);
        if(CharSequenceUtil.isNotBlank(timedCache.get(TOKEN))){
            return timedCache.get(TOKEN);
        }
        DefaultDingTalkClient client = new DefaultDingTalkClient(BaseConstantUrl.GET_TOKEN_URL);
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey(CacheUtils.getSysConfig(CacheConstants.ENTERPRICE_APP_KEY,sysConfigService.selectConfigByKey(Constants.ENTERPRICE_APP_KEY)).toString());
        request.setAppsecret(CacheUtils.getSysConfig(CacheConstants.ENTERPRICE_APP_SECRET,sysConfigService.selectConfigByKey(Constants.ENTERPRICE_APP_SECRET)).toString());
        request.setHttpMethod(Constants.GET);
        try {
            OapiGettokenResponse response = client.execute(request);
            if(response.getErrcode()==0){
                timedCache.put(TOKEN,response.getAccessToken());
                return response.getAccessToken();
            }else {
                throw new SyncDataException(JSON.toJSONString(request),response.getErrmsg());
            }
        } catch (ApiException e) {
            log.error(e.getMessage(),e);
            throw new SyncDataException(JSON.toJSONString(request),e.getMessage());
        }
    }

    /**
     * 钉钉新版服务费token获取
     * @return token值
     */
    @DingTalkLog(dingTalkLogType = DingTalkLogType.GET_TOKEN_V2)
    public String getDingTalkTokenV2(){
        //创建缓存，缓存默认是7100S
        TimedCache<String, String> timedCache = CacheUtil.newTimedCache(7100);
        if(CharSequenceUtil.isNotBlank(timedCache.get(TOKENV2))){
            return timedCache.get(TOKENV2);
        }
        GetAccessTokenRequest getAccessTokenRequest=new GetAccessTokenRequest()
                .setAppKey(String.valueOf(CacheUtils.getSysConfig(CacheConstants.ENTERPRICE_APP_KEY,sysConfigService.selectConfigByKey(Constants.ENTERPRICE_APP_KEY))))
                .setAppSecret(String.valueOf(CacheUtils.getSysConfig(CacheConstants.ENTERPRICE_APP_SECRET,sysConfigService.selectConfigByKey(Constants.ENTERPRICE_APP_SECRET))));
        try {
            GetAccessTokenResponse accessToken = createClient().getAccessToken(getAccessTokenRequest);
            timedCache.put(TOKENV2,accessToken.getBody().getAccessToken());
            return accessToken.getBody().getAccessToken();
        } catch (TeaException err) {
            if (CharSequenceUtil.isNotBlank(err.code) &&CharSequenceUtil.isNotBlank(err.message)) {
                log.error("BaseService->getDingTalkTokenV2 error msg code:{},message:{}",err.code,err.message);
                log.error(err.getMessage(),err);
                throw new SyncDataException(JSON.toJSONString(getAccessTokenRequest),err.message);
            }
            log.error(err.getMessage(),err);
            throw new SyncDataException(JSON.toJSONString(getAccessTokenRequest),err.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new SyncDataException(JSON.toJSONString(getAccessTokenRequest),e.getMessage());
        }
    }

    /**
     * 创建createClient
     * @return
     * @throws Exception
     */

    public static com.aliyun.dingtalkoauth2_1_0.Client createClient() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkoauth2_1_0.Client(config);
    }

    /**
     * 创建流程client 对象
     * @return
     * @throws Exception
     */
    public static com.aliyun.dingtalkworkflow_1_0.Client createWorkFlowClient() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkworkflow_1_0.Client(config);
    }
}
