package com.snow.dingtalk.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.snow.common.annotation.DingTalkLog;
import com.snow.common.constant.Constants;
import com.snow.common.enums.DingTalkLogType;
import com.snow.common.exception.SyncDataException;
import com.snow.common.utils.StringUtils;
import com.snow.common.utils.spring.SpringUtils;
import com.snow.dingtalk.common.BaseConstantUrl;
import com.snow.dingtalk.model.client.SnowDingTalkDefaultClient;
import com.snow.dingtalk.model.request.DingUserCreateRequest;
import com.snow.dingtalk.model.request.UserListRequest;
import com.snow.dingtalk.service.UserService;
import com.snow.system.domain.SysPost;
import com.snow.system.domain.SysUser;
import com.snow.system.service.ISysUserService;
import com.snow.system.service.impl.SysConfigServiceImpl;
import com.snow.system.service.impl.SysPostServiceImpl;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2020/11/9 10:51
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private SysPostServiceImpl sysPostService=SpringUtils.getBean(SysPostServiceImpl.class);

    private SysConfigServiceImpl isysConfigService=SpringUtils.getBean(SysConfigServiceImpl.class);

    private ISysUserService sysUserService=SpringUtils.getBean(ISysUserService.class);


    @Override
    @DingTalkLog(dingTalkLogType=DingTalkLogType.USER_CREATE,dingTalkUrl =BaseConstantUrl.USER_CREATE)
    public String createDingUser(DingUserCreateRequest createRequest) {
        OapiV2UserCreateRequest oapiV2UserCreateRequest = BeanUtil.toBean(createRequest, OapiV2UserCreateRequest.class);
        try {
            OapiV2UserCreateResponse rsp = new SnowDingTalkDefaultClient<OapiV2UserCreateResponse>().execute(oapiV2UserCreateRequest,BaseConstantUrl.USER_CREATE);
            if(rsp.getErrcode()!=0){
                log.error("@@创建钉钉用户信息返回异常：{}",rsp.getErrmsg());
                throw new SyncDataException(JSON.toJSONString(oapiV2UserCreateRequest),rsp.getErrmsg());
            }
            return rsp.getResult().getUserid();
        } catch (ApiException e) {
            log.error("@@创建钉钉用户信息返回异常：{}",e.getMessage());
            log.error(e.getMessage(),e);
            throw new SyncDataException(JSON.toJSONString(oapiV2UserCreateRequest),e.getErrMsg());
        }
    }

    public OapiSnsGetuserinfoBycodeResponse.UserInfo getUserInfoByCode(String authCode) {
        String appId= isysConfigService.selectConfigByKey("ding.login.appid");
        String appSecret= isysConfigService.selectConfigByKey("ding.login.appSecret");
        // 通过临时授权码获取授权用户的个人信息
        DefaultDingTalkClient client2 = new DefaultDingTalkClient(BaseConstantUrl.GET_USER_BY_CODE);
        OapiSnsGetuserinfoBycodeRequest req = new OapiSnsGetuserinfoBycodeRequest();
        // 通过扫描二维码，跳转指定的redirect_uri后，向url中追加的code临时授权码
        req.setTmpAuthCode(authCode);
        try {
            OapiSnsGetuserinfoBycodeResponse  response = client2.execute(req, appId,appSecret);
            if(response.getErrcode()==0){
                return response.getUserInfo();
            }else {
                throw new SyncDataException(JSON.toJSONString(req),response.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("@@通过临时授权码获取授权用户的个人信息异常：{}",e.getErrMsg());
            log.error(e.getMessage(),e);
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }
    /**
     * 添加钉钉用户
     * @param sysUser
     * @return
     */
    @Override
    @DingTalkLog(dingTalkLogType = DingTalkLogType.USER_CREATE,dingTalkUrl=BaseConstantUrl.USER_CREATE)
    public OapiV2UserCreateResponse.UserCreateResponse createUser(SysUser sysUser) {
        OapiV2UserCreateRequest req = new OapiV2UserCreateRequest();
        req.setUserid(String.valueOf(sysUser.getUserId()));
        req.setName(sysUser.getUserName());
        req.setMobile(sysUser.getPhonenumber());
        req.setHideMobile(false);
        req.setTelephone(sysUser.getPhonenumber());
        req.setJobNumber(sysUser.getJobnumber());
        req.setTitle(sysUser.getPosition());
        req.setEmail(sysUser.getEmail());
        req.setOrgEmail(sysUser.getOrgEmail());
        req.setWorkPlace(sysUser.getWorkPlace());
        req.setRemark(sysUser.getRemark());
        //部门list
        req.setDeptIdList(JSON.toJSONString(sysUser.getDeptId()));
        //员工在对应的部门中的职位
        List<OapiV2UserCreateRequest.DeptTitle> deptTitleList = new ArrayList<>();
        Long[] postIds = sysUser.getPostIds();
        if(StringUtils.isNotNull(postIds)){
            for(Long postId:postIds){
                SysPost sysPost = sysPostService.selectPostById(postId);
                OapiV2UserCreateRequest.DeptTitle deptTitle = new OapiV2UserCreateRequest.DeptTitle();
                deptTitle.setDeptId(sysUser.getDeptId());
                deptTitle.setTitle(sysPost.getPostName());
                deptTitleList.add(deptTitle);
            }
        }
        req.setSeniorMode(false);
        if(StringUtils.isNotNull(sysUser.getHiredDate())){
            Date hiredDate= sysUser.getHiredDate();
            req.setHiredDate(hiredDate.getTime());
        }
        try {
            OapiV2UserCreateResponse response = new SnowDingTalkDefaultClient<OapiV2UserCreateResponse>().execute(req,BaseConstantUrl.USER_CREATE);
            if(response.getErrcode()==0){
                return response.getResult();
            }else {
                throw new SyncDataException(JSON.toJSONString(req),response.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("@@钉钉createUser异常：{}",e.getErrMsg());
            log.error(e.getMessage(),e);
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }

    @Override
    @DingTalkLog(dingTalkLogType = DingTalkLogType.USER_UPDATE,dingTalkUrl=BaseConstantUrl.USER_UPDATE)
    public String updateUserV2(SysUser sysUser) {
        OapiV2UserUpdateRequest req = new OapiV2UserUpdateRequest();
        req.setUserid(sysUser.getDingUserId());
        req.setName(sysUser.getUserName());
        req.setMobile(sysUser.getPhonenumber());
        req.setHideMobile(false);
        req.setJobNumber(sysUser.getJobnumber());
        req.setTitle(sysUser.getPosition());
        req.setEmail(sysUser.getEmail());
        req.setOrgEmail(sysUser.getOrgEmail());
        req.setWorkPlace(sysUser.getWorkPlace());
        req.setRemark(sysUser.getRemark());
        req.setDeptIdList(JSON.toJSONString(sysUser.getDeptId()));
        req.setSeniorMode(false);
        if(ObjectUtil.isNotNull(sysUser.getHiredDate())){
            req.setHiredDate(sysUser.getHiredDate().getTime());
        }
        req.setLanguage(Constants.ZH_CN);
        try {
            OapiV2UserUpdateResponse response = new SnowDingTalkDefaultClient<OapiV2UserUpdateResponse>().execute(req,BaseConstantUrl.USER_UPDATE);
            if(response.getErrcode()==0){
                return response.getRequestId();
            }else {
                throw new SyncDataException(JSON.toJSONString(req),response.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("@@钉钉updateUser异常：{}",e.getErrMsg());
            log.error(e.getMessage(),e);
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }


    /**
     * 删除钉钉用户
     * @param ids
     */
    @Override
    @DingTalkLog(dingTalkLogType = DingTalkLogType.USER_DELETE,dingTalkUrl=BaseConstantUrl.USER_DELETE)
    public void deleteUser(String ids) {
        OapiV2UserDeleteRequest req = new OapiV2UserDeleteRequest();
        req.setUserid(ids);
        try {
            OapiV2UserDeleteResponse response = new SnowDingTalkDefaultClient<OapiV2UserDeleteResponse>().execute(req,BaseConstantUrl.USER_DELETE);
            if(response.getErrcode()==0){
                String requestId = response.getRequestId();
                log.info("dingTalk删除用户返回：{}",requestId);
            }else {
                throw new SyncDataException(JSON.toJSONString(req),response.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("钉钉deleteUser异常：{}",e.getErrMsg());
            e.printStackTrace();
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }

    @Override
    public OapiV2UserGetResponse.UserGetResponse  getUserByUserId(String userId) {
        OapiV2UserGetRequest req = new OapiV2UserGetRequest();
        req.setUserid(userId);
        req.setLanguage(Constants.ZH_CN);
        try {
            OapiV2UserGetResponse response = new SnowDingTalkDefaultClient<OapiV2UserGetResponse>().execute(req,BaseConstantUrl.GET_USER_BY_ID);
            if(response.getErrcode()==0){
                OapiV2UserGetResponse.UserGetResponse result = response.getResult();
                log.info("dingTalk根据用户id获取用户详细信息返回：{}",result);
                return result;
            }else {
                throw new SyncDataException(JSON.toJSONString(req),response.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("dingTalk根据用户id获取用户详细信息返回异常：{}",e.getErrMsg());
            log.error(e.getMessage(),e);
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }

    @Override
    public OapiV2UserListResponse.PageResult getUserInfoByDept(UserListRequest userListRequest) {
        OapiV2UserListRequest req = new OapiV2UserListRequest();
        req.setDeptId(userListRequest.getDeptId());
        req.setCursor(userListRequest.getCursor());
        req.setSize(userListRequest.getSize());
        req.setOrderField("modify_desc");
        req.setContainAccessLimit(false);
        req.setLanguage(Constants.ZH_CN);
        try {
            OapiV2UserListResponse response = new SnowDingTalkDefaultClient<OapiV2UserListResponse>().execute(req,BaseConstantUrl.GET_USERINFO_BY_DEPT);
            if(response.getErrcode()==0){
                OapiV2UserListResponse.PageResult result = response.getResult();
                log.info("dingTalk根据部门获取用户详细信息返回：{}",result);
                return result;
            }else {
                throw new SyncDataException(JSON.toJSONString(req),response.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("dingTalk根据部门获取用户详细信息返回异常：{}",e.getErrMsg());
            log.error(e.getMessage(),e);
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }


    @Override
    public OapiUserGetbyunionidResponse.UserGetByUnionIdResponse  getUserByUnionId(String unionId){
        OapiUserGetbyunionidRequest request = new OapiUserGetbyunionidRequest();
        request.setUnionid(unionId);
        try {
            OapiUserGetbyunionidResponse response = new SnowDingTalkDefaultClient<OapiUserGetbyunionidResponse>().execute(request,BaseConstantUrl.GET_USER_UNION_ID);
            if(response.getErrcode()==0){
                OapiUserGetbyunionidResponse.UserGetByUnionIdResponse result = response.getResult();
                log.info("@@UserServiceImpl->getUserByUnionId获取用户详细信息返回：{}",result);
                return result;
            }else {
                throw new SyncDataException(JSON.toJSONString(request),response.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("UserServiceImpl->getUserByUnionId获取用户详细信息返回异常：{}",e.getErrMsg());
            log.error(e.getMessage(),e);
            throw new SyncDataException(JSON.toJSONString(request),e.getErrMsg());
        }
    }

    @Override
    public String getUnionIdBySysUserId(Long sysUserId) {
        return getUserByUserId(sysUserService.selectUserById(sysUserId).getDingUserId()).getUnionid();
    }

    @Override
    public String getUserByPhone(String phone) {
        OapiV2UserGetbymobileRequest req = new OapiV2UserGetbymobileRequest();
        req.setMobile(phone);
        try {
            OapiV2UserGetbymobileResponse response = new SnowDingTalkDefaultClient<OapiV2UserGetbymobileResponse>().execute(req,BaseConstantUrl.GET_USERINFO_BY_MOBILE);
            if(response.getErrcode()==0){
                OapiV2UserGetbymobileResponse.UserGetByMobileResponse result = response.getResult();
                log.info("@@UserServiceImpl->getUserByPhone response:{}",JSON.toJSONString(result));
                return result.getUserid();
            }else {
                throw new SyncDataException(JSON.toJSONString(req),response.getErrmsg());
            }
        } catch (ApiException e) {
            log.error("UserServiceImpl->getUserByPhone error response:{}",e.getErrMsg());
            log.error(e.getMessage(),e);
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }

}
