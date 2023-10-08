package com.snow.system.mapper;

import java.util.List;
import com.snow.system.domain.SysDbTable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 数据库Mapper接口
 * 
 * @author Agee
 * @date 2022-12-16
 */
public interface SysDbTableMapper extends BaseMapper<SysDbTable>
{
    /**
     * 查询数据库
     * 
     * @param id 数据库ID
     * @return 数据库
     */
    public SysDbTable selectSysDbTableById(Long id);

    /**
     * 查询数据库列表
     * 
     * @param sysDbTable 数据库
     * @return 数据库集合
     */
    public List<SysDbTable> selectSysDbTableList(SysDbTable sysDbTable);

    /**
     * 新增数据库
     * 
     * @param sysDbTable 数据库
     * @return 结果
     */
    public int insertSysDbTable(SysDbTable sysDbTable);

    /**
     * 修改数据库
     * 
     * @param sysDbTable 数据库
     * @return 结果
     */
    public int updateSysDbTable(SysDbTable sysDbTable);

    /**
     * 删除数据库
     * 
     * @param id 数据库ID
     * @return 结果
     */
    public int deleteSysDbTableById(Long id);

    /**
     * 批量删除数据库
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysDbTableByIds(String[] ids);


    /**
     * 查询数据表是否存在
     * @param tableName 表名称
     * @return
     */
    public int selectIsExistTableName(String tableName);

    /**
     * 创建表
     *
     * @param sql
     * @return 结果
     */
    public int createTable(@Param("sql") String sql);
}
