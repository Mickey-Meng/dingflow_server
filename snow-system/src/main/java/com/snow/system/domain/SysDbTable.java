package com.snow.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.snow.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;

/**
 * 数据库对象 sys_db_table
 * 
 * @author Agee
 * @date 2022-12-16
 */
@Data
public class SysDbTable extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 数据库表名称 */
    @Excel(name = "数据库表名称")
    private String dbName;

    private String remark;

    /** 逻辑删除 */
    @Excel(name = "逻辑删除")
    private Long deleted;



}
