package com.snow.dingtalk.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author qimingjin
 * @date 2023-08-23 10:28
 * @Description:
 */
@Data
public class UpdateLiveRequest implements Serializable {

    private static final long serialVersionUID = -1217900551707605038L;

    /**
     * 直播id
     */
    @NotBlank(message = "直播id不能为空")
    private String liveId;
    /**
     * 发起直播的主播unionId
     *  必传
     */
    @NotBlank(message = "主播unionId不能为空")
    private String unionId;
    /**
     * 直播标题
     */
    private String title;

    /**
     * 预计开播时间戳，单位毫秒
     */
    private Long preStartTime;
    /**
     * 预计结束时间戳，单位毫秒。
     */
    private Long preEndTime;
    /**
     * 直播的封面地址
     */
    private String coverUrl;
    /**
     * 直播简介
     */
    private String introduction;

}
