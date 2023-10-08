package com.snow.dingtalk.model.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qimingjin
 * @date 2023-08-23 13:38
 * @Description: 直播观看数据
 */
@Data
public class LiveWatchDetailResponse implements Serializable {
    private static final long serialVersionUID = 7274851718464778252L;


    /**
     * 观看直播人数
     */
    private Integer liveUv;

    /**
     * 消息数
     */
    private Integer msgCount;

    /**
     * 点赞数
     */
    private Integer praiseCount;

    /**
     * 观看次数
     */
    private Integer pv;

    /**
     * 观看总人数
     */
    private Integer uv;

    /**
     * 观看回放人数
     */
    private Integer playbackUv;

    /**
     * 观看总时长，单位毫秒
     */
    private Long totalWatchTime;

    /**
     * 平均观看时长，单位毫秒
     */
    private Long avgWatchTime;
}
