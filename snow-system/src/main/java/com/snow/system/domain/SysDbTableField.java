package com.snow.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.snow.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;

/**
 * 数据库对象 sys_db_table_field
 * 
 * @author Agee
 * @date 2022-12-16
 */
@Data
public class SysDbTableField extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 数据库表id */
    @Excel(name = "数据库表id")
    private Long dbTableId;

    /** 字段名称 */
    @Excel(name = "字段名称")
    private String fieldName;

    /** 是否主键 */
    @Excel(name = "是否主键")
    private Boolean fieldIsPrimaryKey;

    /** 类型 */
    @Excel(name = "类型")
    private String fieldType;

    /** 长度 */
    @Excel(name = "长度")
    private Long fieldLength;

    /** 小数点 */
    @Excel(name = "小数点")
    private Long fieldDecimal;

    /** 是否为空 */
    @Excel(name = "是否为空")
    private Boolean fieldIsNull;

    /** 默认值 */
    @Excel(name = "默认值")
    private String fieldDefaultValue;

    /** 逻辑删除 */
    @Excel(name = "逻辑删除")
    private Long deleted;

    private String remark;

}
