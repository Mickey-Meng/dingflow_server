package com.snow.system.service;

import java.util.List;

import com.snow.common.core.page.TableDataInfo;
import com.snow.system.domain.SysDbTable;
import com.baomidou.mybatisplus.extension.service.IService;
import com.snow.system.domain.response.SysDbTableResp;

/**
 * 数据库Service接口
 * 
 * @author Agee
 * @date 2022-12-16
 */
public interface ISysDbTableService extends IService<SysDbTable>
{
    /**
     * 查询数据库表
     * 
     * @param id 数据库表ID
     * @return 数据库表
     */
    public SysDbTable selectSysDbTableById(Long id);

    /**
     * 查询数据库表列表
     * 
     * @param sysDbTable 数据库表
     * @return 数据库表集合
     */
    public TableDataInfo<SysDbTableResp> selectSysDbTablePageList(SysDbTable sysDbTable);

    public List<SysDbTable> selectSysDbTableList(SysDbTable sysDbTable);

    /**
     * 新增数据库表
     * 
     * @param sysDbTable 数据库表
     * @return 结果
     */
    public int insertSysDbTable(SysDbTable sysDbTable);

    /**
     * 修改数据库表
     * 
     * @param sysDbTable 数据库表
     * @return 结果
     */
    public int updateSysDbTable(SysDbTable sysDbTable);

    /**
     * 批量删除数据库表
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysDbTableByIds(String ids);

    /**
     * 删除数据库表信息
     * 
     * @param id 数据库表ID
     * @return 结果
     */
    public int deleteSysDbTableById(Long id);

    public void createDbTable(Long id);
}
