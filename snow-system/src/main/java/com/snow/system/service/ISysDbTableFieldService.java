package com.snow.system.service;

import java.util.List;
import com.snow.system.domain.SysDbTableField;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 数据库Service接口
 * 
 * @author Agee
 * @date 2022-12-16
 */
public interface ISysDbTableFieldService extends IService<SysDbTableField>
{
    /**
     * 查询数据库
     * 
     * @param id 数据库ID
     * @return 数据库
     */
    public SysDbTableField selectSysDbTableFieldById(Long id);

    /**
     * 查询数据库列表
     * 
     * @param sysDbTableField 数据库
     * @return 数据库集合
     */
    public List<SysDbTableField> selectSysDbTableFieldList(SysDbTableField sysDbTableField);

    /**
     * 新增数据库
     * 
     * @param sysDbTableField 数据库
     * @return 结果
     */
    public int insertSysDbTableField(SysDbTableField sysDbTableField);

    /**
     * 修改数据库
     * 
     * @param sysDbTableField 数据库
     * @return 结果
     */
    public int updateSysDbTableField(SysDbTableField sysDbTableField);

    /**
     * 批量删除数据库
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysDbTableFieldByIds(String ids);

    /**
     * 删除数据库信息
     * 
     * @param id 数据库ID
     * @return 结果
     */
    public int deleteSysDbTableFieldById(Long id);
}
