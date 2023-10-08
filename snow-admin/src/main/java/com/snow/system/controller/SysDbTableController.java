package com.snow.system.controller;

import java.util.List;

import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.response.SysDbTableResp;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.snow.common.annotation.Log;
import com.snow.common.enums.BusinessType;
import org.springframework.stereotype.Controller;
import com.snow.system.domain.SysDbTable;
import com.snow.system.service.ISysDbTableService;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.common.core.page.TableDataInfo;

/**
 * 数据库表Controller
 * 
 * @author Agee
 * @date 2022-12-16
 */
@Controller
@RequestMapping("/system/dbTable")
public class SysDbTableController extends BaseController
{
    private String prefix = "system/dbTable";

    @Autowired
    private ISysDbTableService sysDbTableService;

    @RequiresPermissions("system:dbTable:view")
    @GetMapping()
    public String dbTable()
    {
        return prefix + "/dbTable";
    }

    /**
     * 查询数据库表列表
     */
    @RequiresPermissions("system:dbTable:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo<SysDbTableResp> list(SysDbTable sysDbTable)
    {
        startPage();
        return sysDbTableService.selectSysDbTablePageList(sysDbTable);
    }

    /**
     * 导出数据库表表列表
     */
    @RequiresPermissions("system:dbTable:export")
    @Log(title = "数据库表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysDbTable sysDbTable)
    {
        List<SysDbTable> list = sysDbTableService.selectSysDbTableList(sysDbTable);
        ExcelUtil<SysDbTable> util = new ExcelUtil<SysDbTable>(SysDbTable.class);
        return util.exportExcel(list, "dbTable");
    }

    /**
     * 新增数据库表
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存数据库表
     */
    @RequiresPermissions("system:dbTable:add")
    @Log(title = "数据库表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysDbTable sysDbTable)
    {
        sysDbTable.setCreateBy(ShiroUtils.getUserId().toString());
        return toAjax(sysDbTableService.insertSysDbTable(sysDbTable));
    }

    /**
     * 修改数据库表
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysDbTable sysDbTable = sysDbTableService.selectSysDbTableById(id);
        mmap.put("sysDbTable", sysDbTable);
        return prefix + "/edit";
    }

    /**
     * 修改保存数据库表
     */
    @RequiresPermissions("system:dbTable:edit")
    @Log(title = "数据库表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysDbTable sysDbTable)
    {
        sysDbTable.setUpdateBy(ShiroUtils.getUserId().toString());
        return toAjax(sysDbTableService.updateSysDbTable(sysDbTable));
    }

    /**
     * 删除数据库表
     */
    @RequiresPermissions("system:dbTable:remove")
    @Log(title = "数据库表", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysDbTableService.deleteSysDbTableByIds(ids));
    }

    @RequiresPermissions("system:field:list")
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, ModelMap mmap) {
        mmap.put("tableId", id);
        return "system/field/field";
    }

    @RequiresPermissions("system:dbTable:createTable")
    @GetMapping("/createTable/{id}")
    @ResponseBody
    public AjaxResult createTable(@PathVariable("id") Long id) {

        sysDbTableService.createDbTable(id);
        return AjaxResult.success();
    }

}
