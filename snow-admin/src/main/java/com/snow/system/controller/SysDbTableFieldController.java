package com.snow.system.controller;

import java.util.List;
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
import com.snow.system.domain.SysDbTableField;
import com.snow.system.service.ISysDbTableFieldService;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.common.core.page.TableDataInfo;

/**
 * 数据库Controller
 * 
 * @author Agee
 * @date 2022-12-16
 */
@Controller
@RequestMapping("/system/field")
public class SysDbTableFieldController extends BaseController
{
    private String prefix = "system/field";

    @Autowired
    private ISysDbTableFieldService sysDbTableFieldService;

    @RequiresPermissions("system:field:view")
    @GetMapping()
    public String field()
    {
        return prefix + "/field";
    }

    /**
     * 查询数据库列表
     */
    @RequiresPermissions("system:field:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysDbTableField sysDbTableField)
    {
        startPage();
        List<SysDbTableField> list = sysDbTableFieldService.selectSysDbTableFieldList(sysDbTableField);
        return getDataTable(list);
    }

    /**
     * 导出数据库列表
     */
    @RequiresPermissions("system:field:export")
    @Log(title = "数据库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysDbTableField sysDbTableField)
    {
        List<SysDbTableField> list = sysDbTableFieldService.selectSysDbTableFieldList(sysDbTableField);
        ExcelUtil<SysDbTableField> util = new ExcelUtil<SysDbTableField>(SysDbTableField.class);
        return util.exportExcel(list, "field");
    }

    /**
     * 新增数据库
     */
    @GetMapping("/add/{dbTableId}")
    public String add(@PathVariable("dbTableId") Long dbTableId,ModelMap mmap)
    {
        mmap.put("dbTableId",dbTableId);
        return prefix + "/add";
    }

    /**
     * 新增保存数据库
     */
    @RequiresPermissions("system:field:add")
    @Log(title = "数据库", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysDbTableField sysDbTableField)
    {
        return toAjax(sysDbTableFieldService.insertSysDbTableField(sysDbTableField));
    }

    /**
     * 修改数据库
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysDbTableField sysDbTableField = sysDbTableFieldService.selectSysDbTableFieldById(id);
        mmap.put("sysDbTableField", sysDbTableField);
        return prefix + "/edit";
    }

    /**
     * 修改保存数据库
     */
    @RequiresPermissions("system:field:edit")
    @Log(title = "数据库", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysDbTableField sysDbTableField)
    {
        return toAjax(sysDbTableFieldService.updateSysDbTableField(sysDbTableField));
    }

    /**
     * 删除数据库
     */
    @RequiresPermissions("system:field:remove")
    @Log(title = "数据库", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysDbTableFieldService.deleteSysDbTableFieldByIds(ids));
    }
}
