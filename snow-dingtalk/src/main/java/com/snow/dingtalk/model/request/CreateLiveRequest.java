package com.snow.dingtalk.model.request;

import com.aliyun.tea.NameInMap;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author qimingjin
 * @date 2023-08-23 10:28
 * @Description:
 */
@Data
public class CreateLiveRequest implements Serializable {
    private static final long serialVersionUID = 3314224165048196023L;
    /**
     * 直播标题
     */
    @NotBlank(message = "直播标题不能为空")
    private String title;
    /**
     * 发起直播的主播unionId
     *  必传
     */
    @NotBlank(message = "主播unionId不能为空")
    private String unionId;
    /**
     * 预计开播时间戳，单位毫秒
     */
    @NotNull(message = "预计开播时间不能为空")
    private Long preStartTime;
    /**
     * 预计结束时间戳，单位毫秒。
     */
    @NotNull(message = "预计结束时间不能为空")
    private Long preEndTime;
    /**
     * 直播的封面地址
     */
    private String coverUrl;
    /**
     * 直播简介
     */
    private String introduction;

    /**
     * 直播分享范围：
     * 0:不公开
     * 1:全面公开
     * 2:组织内公开
     */
    private Long publicType;

}
