package com.snow.system.service.impl;

import java.util.List;
import java.util.ArrayList;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.snow.common.core.page.TableDataInfo;
import com.snow.common.constant.GenConstants;
import com.snow.common.enums.DbFieldTypeEnum;
import com.snow.common.utils.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snow.common.utils.FunUtils;
import com.snow.common.utils.sql.SqlUtil;
import com.snow.system.domain.SysDbTableField;
import com.snow.system.domain.response.SysDbTableResp;
import org.springframework.stereotype.Service;
import com.snow.system.mapper.SysDbTableMapper;
import com.snow.system.domain.SysDbTable;
import com.snow.system.service.ISysDbTableService;
import com.snow.common.core.text.Convert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import javax.annotation.Resource;

/**
 * 数据库Service业务层处理
 *
 * @author Agee
 * @date 2022-12-16
 */
@Service
public class SysDbTableServiceImpl extends ServiceImpl<SysDbTableMapper, SysDbTable> implements ISysDbTableService {
    @Resource
    private SysDbTableMapper sysDbTableMapper;


    @Resource
    private SysDbTableFieldServiceImpl sysDbTableFieldService;

    /**
     * 查询数据库
     *
     * @param id 数据库ID
     * @return 数据库
     */
    @Override
    public SysDbTable selectSysDbTableById(Long id) {
        SysDbTableField sysDbTableField =new SysDbTableField();
        sysDbTableField.setDbTableId(id);
        createTableSql(sysDbTableMapper.selectById(id),sysDbTableFieldService.selectSysDbTableFieldList(sysDbTableField));
        return sysDbTableMapper.selectById(id);
    }

    @Override
    public TableDataInfo<SysDbTableResp> selectSysDbTablePageList(SysDbTable sysDbTable) {
        LambdaQueryWrapper<SysDbTable> lambda = new QueryWrapper<SysDbTable>().lambda();
        lambda.like(ObjectUtil.isNotEmpty(sysDbTable.getDbName()),SysDbTable::getDbName,sysDbTable.getDbName());
        TableDataInfo<SysDbTableResp> dataTable = SqlUtil.getDataTable(sysDbTableMapper.selectList(lambda), SysDbTableResp.class);
        if(ObjectUtil.isEmpty(dataTable)){
            return dataTable;
        }
        dataTable.getRows().forEach(t->{
            int row= sysDbTableMapper.selectIsExistTableName(t.getDbName());
            t.setExist(row>0);
        });
        return dataTable;
    }

    /**
     * 查询数据库列表
     *
     * @param sysDbTable 数据库
     * @return 数据库
     */
    @Override
    public List<SysDbTable> selectSysDbTableList(SysDbTable sysDbTable) {
        LambdaQueryWrapper<SysDbTable> lambda = new QueryWrapper<SysDbTable>().lambda();
        lambda.like(ObjectUtil.isNotEmpty(sysDbTable.getDbName()),SysDbTable::getDbName,sysDbTable.getDbName());
        return sysDbTableMapper.selectList(lambda);
    }

    /**
     * 新增数据库
     *
     * @param sysDbTable 数据库
     * @return 结果
     */
    @Override
    public int insertSysDbTable(SysDbTable sysDbTable) {
        int row= sysDbTableMapper.selectIsExistTableName(sysDbTable.getDbName());
        FunUtils.isTure(row>0).throwMessage("数据表已存在");
        sysDbTable.setCreateTime(DateUtils.getNowDate());
        return sysDbTableMapper.insert(sysDbTable);
    }

    /**
     * 修改数据库
     *
     * @param sysDbTable 数据库
     * @return 结果
     */
    @Override
    public int updateSysDbTable(SysDbTable sysDbTable) {
        sysDbTable.setUpdateTime(DateUtils.getNowDate());
        return sysDbTableMapper.updateById(sysDbTable);
    }

    /**
     * 删除数据库对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
     @Override
     public int deleteSysDbTableByIds(String ids) {
        return sysDbTableMapper.deleteBatchIds(Convert.toStrList(ids));
     }

    /**
     * 删除数据库信息
     *
     * @param id 数据库ID
     * @return 结果
     */
    @Override
    public int deleteSysDbTableById(Long id) {
        return sysDbTableMapper.deleteById(id);
    }

    /**
     * 生成表
     * @param id
     */
    public void createDbTable(Long id){
        SysDbTable sysDbTable =this.getById(id);
        SysDbTableField sysDbTableField=new SysDbTableField();
        sysDbTableField.setDbTableId(id);
        List<SysDbTableField> sysDbTableFields = sysDbTableFieldService.selectSysDbTableFieldList(sysDbTableField);
        String tableSql = createTableSql(sysDbTable, sysDbTableFields);
        sysDbTableMapper.createTable(tableSql);
    }


    public String createTableSql(SysDbTable dbTable, List<SysDbTableField> tableFieldList){
        StringBuilder builder = StrUtil.builder(StrUtil.format("CREATE TABLE `{}`(", dbTable.getDbName()));
        StringBuilder primaryKey=StrUtil.builder();
        for (SysDbTableField sysDbTableField : tableFieldList) {
            builder.append(StrUtil.format("`{}` {}{} ", sysDbTableField.getFieldName(), sysDbTableField.getFieldType(),getFieldLength(sysDbTableField)));

            if(sysDbTableField.getFieldIsNull()){
                builder.append("DEFAULT ");
                if(StrUtil.isNotEmpty(sysDbTableField.getFieldDefaultValue())){
                    builder.append(sysDbTableField.getFieldDefaultValue());
                }else {
                    builder.append(" NULL");
                }
            }else {
                builder.append("NOT NULL");
            }
            if(sysDbTableField.getFieldIsPrimaryKey()){
                primaryKey.append(StrUtil.format("`{}`,",sysDbTableField.getFieldName()));
            }
            //拼接注释
            builder.append(StrUtil.format(" COMMENT '{}',",sysDbTableField.getRemark()));
        }
        builder.append(StrUtil.format(" PRIMARY KEY ({})", primaryKey.substring(0,primaryKey.length()-1)));
        builder.append(StrUtil.format(" )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='{}';",dbTable.getRemark()));

        return builder.toString();
    }

    private String getFieldLength(SysDbTableField dbTableField ){
        StringBuilder fieldLength=StrUtil.builder();
        String fieldType = dbTableField.getFieldType();
        if(ArrayUtil.contains(GenConstants.COLUMNTYPE_NUMBER,dbTableField.getFieldType())){
            if(ObjectUtil.equal(fieldType, DbFieldTypeEnum.DECIMAL.getName())){

                fieldLength.append(StrUtil.format("({},{})",
                        (ObjectUtil.isEmpty(dbTableField.getFieldLength())||dbTableField.getFieldLength()==0)?10:dbTableField.getFieldLength(),
                        dbTableField.getFieldDecimal())
                );
            }else {
                fieldLength.append(StrUtil.format("({})",
                        (ObjectUtil.isEmpty(dbTableField.getFieldLength())||dbTableField.getFieldLength()==0)?10:dbTableField.getFieldLength()
                        )
                );
            }
        }
        if(ArrayUtil.contains(GenConstants.COLUMNTYPE_STR,dbTableField.getFieldType())){
            fieldLength.append(StrUtil.format("({})",dbTableField.getFieldLength()));
        }

       return fieldLength.toString();
    }
}
