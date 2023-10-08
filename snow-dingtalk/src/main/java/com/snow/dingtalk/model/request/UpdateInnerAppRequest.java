package com.snow.dingtalk.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * @author qimingjin
 * @date 2023-08-23 14:05
 * @Description: 创建应用
 */
@Data
public class UpdateInnerAppRequest implements Serializable {


    private static final long serialVersionUID = -3047692763849771895L;

    /**
     * 应用的agentId，请参考基础概念-AgentId
     */
    @NotBlank(message = "agentId不能为空")
    private String agentId;
    /**
     * 操作人的unionId，该用户必须是拥有应用管理权限的管理员，可调用查询用户详情接口获取unionid参数值
     */
    @NotBlank(message = "opUnionId不能为空")
    private String opUnionId;
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
     * 服务器出口IP白名单列表，最大值50
     */
    private List<String> ipWhiteList;

    /**
     * 应用管理后台地址
     */
    private String ompLink;

    /**
     * 应用PC端地址
     */
    private String pcHomepageLink;
}
