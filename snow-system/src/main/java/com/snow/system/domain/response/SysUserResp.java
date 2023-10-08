package com.snow.system.domain.response;

import com.snow.common.annotation.Excel;
import com.snow.common.annotation.Excel.ColumnType;
import com.snow.common.annotation.Excel.Type;
import com.snow.common.annotation.Excels;
import com.snow.common.core.domain.BaseEntity;
import com.snow.system.domain.SysDept;
import com.snow.system.domain.SysRole;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * 用户对象 sys_user
 * 
 * @author snow
 */
@Data
public class SysUserResp extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @Excel(name = "用户序号", cellType = ColumnType.NUMERIC, prompt = "用户编号")
    private Long userId;

    /** 部门ID */
    @Excel(name = "部门编号", type = Type.IMPORT)
    private Long deptId;

    /** 部门父ID */
    private Long parentId;

    /** 角色ID */
    private Long roleId;

    /** 登录名称 */
    @Excel(name = "登录名称")
    @NotBlank(message = "登录账号不能为空")
    @Size(min = 0, max = 30, message = "登录账号长度不能超过30个字符")
    private String loginName;

    /** 用户名称 */
    @Excel(name = "用户名称")
    @Size(min = 0, max = 30, message = "用户昵称长度不能超过30个字符")
    private String userName;

    /** 用户类型 */
    private String userType;

    /** 用户邮箱 */
    @Excel(name = "用户邮箱")
    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    private String email;

    /** 手机号码 */
    @Excel(name = "手机号码")
    @Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
    private String phonenumber;

    /** 用户性别 */
    @Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    private String sex;

    /** 用户头像 */
    private String avatar;

    /** 密码 */
    private String password;

    /** 盐加密 */
    private String salt;

    /** 帐号状态（0正常 1停用） */
    @Excel(name = "帐号状态", readConverterExp = "0=正常,1=停用")
    private String status;


    /** 最后登陆IP */
    @Excel(name = "最后登陆IP", type = Type.EXPORT)
    private String loginIp;

    /** 最后登陆时间 */
    @Excel(name = "最后登陆时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    private Date loginDate;

    /** 密码最后更新时间 */
    private Date pwdUpdateDate;

    /** 部门对象 */
    @Excels({
        @Excel(name = "部门名称", targetAttr = "deptName", type = Type.EXPORT),
        @Excel(name = "部门负责人", targetAttr = "leader", type = Type.EXPORT)
    })
    private SysDept dept;

    private List<SysRole> roles;

    /** 角色组 */
    private Long[] roleIds;

    /** 岗位组 */
    private Long[] postIds;
    /**
     * 	是否开启高管模式：
     * true：开启。开启后，手机号码对所有员工隐藏。普通员工无法对其发DING、发起钉钉免费商务电话。高管之间不受影响。
     * false：不开启。
     */
    private Boolean isSenior;

    /**
     * 	是否号码隐藏：
     * true：隐藏隐藏手机号后，手机号在个人资料页隐藏，但仍可对其发DING、发起钉钉免费商务电话。
     * false：不隐藏
     */
    private Boolean isHide;
    /**
     * 	员工工号，对应显示到OA后台和客户端个人资料的工号栏目。长度为0~64个字符。
     */
    private String jobnumber;

    /**
     * 	办公地点。长度为0~50个字符。
     */
    private String workPlace;

    /**
     * 分机号
     */
    private String tel;
    /**
     * 岗位信息
     */
    private String position;
    /**
     * 入职时间，时间戳
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date hiredDate;
    /**
     * 组织邮箱
     */
    private String orgEmail;

    /**
     * 钉钉用户ID
     */
    private String dingUserId;

}
