package com.snow.dingtalk.service;

import com.snow.dingtalk.model.request.CreateLiveRequest;
import com.snow.dingtalk.model.request.UpdateLiveRequest;
import com.snow.dingtalk.model.response.LiveInfoResponse;
import com.snow.dingtalk.model.response.LiveWatchDetailResponse;

/**
 * @author qimingjin
 * @date 2023-08-23 09:49
 * @Description: 直播服务
 */
public interface LiveService {

   /**
    * 步骤一：调用本接口，获取直播ID。
    * 步骤二：使用直播ID，拼接以下链接： dingtalk://dingtalkclient/action/start_uniform_live?liveUuid=直播ID
    * 步骤三：员工访问该链接，可创建直播。
    * @param liveRequest 请求参数
    * @return 直播id
    */
   String createLive(CreateLiveRequest liveRequest);

   /**
    * 删除直播
    * @param liveId 直播id
    * @param unionId 用户unionId
    * @return 是否成功
    */
   Boolean deleteLive(String liveId,String unionId);

   /**
    * 修改直播
    * @param updateLiveRequest 请求参数
    * @return 是否修改成功
    */
   Boolean updateLive(UpdateLiveRequest updateLiveRequest);

   /**
    * 查询直播信息
    * @param liveId 直播id
    * @param unionId 用户unionId
    * @return 直播详情
    */
   LiveInfoResponse getLiveDetail(String liveId, String unionId);

   /**
    * 查询直播观看数据
    * @param liveId 直播id
    * @param unionId 用户unionId
    * @return 直播数据
    * @remark: 本文档将于 2023 年 9 月 1 日迁移至历史文档（不推荐）目录，且本接口仅保持现有功能，不再新增支持其他能力，说明如下：
    * 如果未使用本接口，推荐使用钉钉数据资产平台。
    * 如果已使用本接口，建议您根据自身实际情况评估是否切换至钉钉数据资产平台。
    */
   LiveWatchDetailResponse queryLiveWatchDetail(String liveId, String unionId);
}
