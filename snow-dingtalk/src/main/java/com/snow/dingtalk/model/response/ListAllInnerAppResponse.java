package com.snow.dingtalk.model.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author qimingjin
 * @date 2023-08-23 14:28
 * @Description: 内部应用
 */
@Data
public class ListAllInnerAppResponse implements Serializable {
    private static final long serialVersionUID = 1739849345725971762L;

    /**
     * 应用agentId
     */
    private String agentId;

    /**
     * 应用APPid
     */
    private String appId;
    /**
     * 应用名称
     */
    private String name;


    /**
     * 应用描述
     */
    private String desc;

    /**
     * 应用图标media，调用上传媒体文件接口获取media_id参数值
     */
    private String icon;

    /**
     * 创建的内部应用类型：【默认为0】
     * 0：创建企业内部H5微应用
     * 1：创建企业内部小程序
     */
    private Integer developType;
    /**
     * 应用首页地址
     * 当developType=0，即创建企业内部H5微应用，该参数必传。
     * 当developType=1，即创建企业内部小程序，该参数无需透传。
     */
    private String homepageLink;

    /**
     * 应用状态，取值：
     * 0：停用
     * 1：启用
     * 3：过期
     */
    private String appStatus;

    /**
     * 应用管理后台地址
     */
    private String ompLink;

    /**
     * 应用PC端地址
     */
    private String pcHomepageLink;
}
