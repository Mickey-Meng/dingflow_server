package com.snow.dingtalk.model.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qimingjin
 * @date 2023-08-23 14:13
 * @Description:
 */
@Data
public class InnerAppResponse implements Serializable {
    private static final long serialVersionUID = 6436231702944648118L;

    /**
     * 应用的AgentId
     */
    private Long agentId;

    /**
     * 应用的AppKey
     */
    private String appKey;

    /**
     * 应用的AppSecret
     */
    private String appSecret;

}
