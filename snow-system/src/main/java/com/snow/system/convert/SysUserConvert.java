package com.snow.system.convert;

import com.snow.system.domain.SysUser;
import com.snow.system.domain.response.SysUserResp;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author qimingjin
 * @date 2022-12-16 13:48
 * @Description: 系统用户对象转化
 */
@Mapper
public interface SysUserConvert {
    SysUserConvert INSTANCE = Mappers.getMapper(SysUserConvert.class);

    SysUserResp convert(SysUser sysUser);

    List<SysUserResp> convert(List<SysUser> sysUserList);

}
