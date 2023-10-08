package com.snow.dingtalk.model.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qimingjin
 * @date 2023-08-23 13:45
 * @Description: 直播信息
 */
@Data
public class LiveInfoResponse implements Serializable {
    private static final long serialVersionUID = 2772886995506344698L;

    /**
     * 直播ID
     */
    private String liveId;

    /**
     * 直播标题
     */
    private String title;

    /**
     * 主播的unionId
     */
    private String unionId;

    /**
     * 直播的封面地址
     */
    private String coverUrl;

    /**
     * 直播时长，单位毫秒
     */
    private Long duration;

    /**
     * 直播实际的结束时间戳
     */
    private Long endTime;

    /**
     * 直播简介
     */
    private String introduction;

    /**
     * 直播观看地址
     */
    private String livePlayUrl;

    /**
     * 直播状态。
     * 0：预告态
     * 1：直播态
     * 3：结束态
     */
    private Integer liveStatus;

    /**
     * 直播回放时长，单位毫秒
     */
    private Long playbackDuration;

    /**
     * 直播实际的开始时间戳，单位毫秒
     */
    private Long startTime;

    /**
     * 预约人数
     */
    private Integer subscribeCount;

    /**
     * 观看人数
     */
    private Integer uv;
}
