package com.snow.web.controller.dingtalk;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.core.page.TableDataInfo;
import com.snow.dingtalk.model.request.CreateInnerAppRequest;
import com.snow.dingtalk.model.request.UpdateInnerAppRequest;
import com.snow.dingtalk.model.response.InnerAppResponse;
import com.snow.dingtalk.model.response.ListAllInnerAppResponse;
import com.snow.dingtalk.service.InnerAppService;
import com.snow.dingtalk.service.impl.UserServiceImpl;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysUser;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author qimingjin
 * @date 2023-08-23 14:54
 * @Description: 企业应用APP
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/ding/innerApp")
public class InnerAppController extends BaseController {

    private final InnerAppService innerAppService;

    private final UserServiceImpl userService;
    private final String prefix = "ding/innerApp";

    @GetMapping()
    @RequiresPermissions("ding:innerApp:listAllApp")
    public String toInnerApp(){
        return CharSequenceUtil.format("{}/{}",prefix,"list");
    }

    @PostMapping("list")
    @ResponseBody
    @RequiresPermissions("ding:innerApp:listAllApp")
    public TableDataInfo<ListAllInnerAppResponse> listAllApp(){
        return this.pageBySubList(innerAppService.listAllApp());
    }

    @GetMapping("add")
    @RequiresPermissions("ding:innerApp:add")
    public String toCrateInnerApp(){
        return CharSequenceUtil.format("{}/{}",prefix,"add");
    }

    @PostMapping("crateInnerApp")
    @RequiresPermissions("ding:innerApp:add")
    public AjaxResult crateInnerApp(CreateInnerAppRequest createInnerAppRequest){
        String unionId = userService.getUnionIdBySysUserId(ShiroUtils.getUserId());
        createInnerAppRequest.setOpUnionId(unionId);
        return AjaxResult.success(innerAppService.crateInnerApp(createInnerAppRequest));
    }

    @GetMapping("updateInnerApp")
    public String toUpdateInnerApp(){
        return CharSequenceUtil.format("{}/{}",prefix,"update");
    }

    @PostMapping("updateInnerApp")
    public AjaxResult updateInnerApp(UpdateInnerAppRequest updateInnerAppRequest){
        return toAjax(innerAppService.updateInnerApp(updateInnerAppRequest));
    }


    public AjaxResult deleteInnerApp(Long agentId,String opUnionId){
        return toAjax(innerAppService.deleteInnerApp(agentId,opUnionId));
    }
}
