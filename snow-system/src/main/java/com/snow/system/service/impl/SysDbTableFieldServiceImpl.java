package com.snow.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.common.core.text.Convert;
import com.snow.common.utils.DateUtils;
import com.snow.common.utils.FunUtils;
import com.snow.system.domain.SysDbTableField;
import com.snow.system.mapper.SysDbTableFieldMapper;
import com.snow.system.service.ISysDbTableFieldService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据库Service业务层处理
 *
 * @author Agee
 * @date 2022-12-16
 */
@Service
public class SysDbTableFieldServiceImpl extends ServiceImpl<SysDbTableFieldMapper, SysDbTableField> implements ISysDbTableFieldService {
    @Resource
    private SysDbTableFieldMapper sysDbTableFieldMapper;

    /**
     * 查询数据库
     *
     * @param id 数据库ID
     * @return 数据库
     */
    @Override
    public SysDbTableField selectSysDbTableFieldById(Long id) {
        return sysDbTableFieldMapper.selectById(id);
    }

    /**
     * 查询数据库列表
     *
     * @param sysDbTableField 数据库
     * @return 数据库
     */
    @Override
    public List<SysDbTableField> selectSysDbTableFieldList(SysDbTableField sysDbTableField) {
        LambdaQueryWrapper<SysDbTableField> lambda = new QueryWrapper<SysDbTableField>().lambda();
        lambda.eq(ObjectUtil.isNotEmpty(sysDbTableField.getDbTableId()),SysDbTableField::getDbTableId,sysDbTableField.getDbTableId());
        lambda.like(ObjectUtil.isNotEmpty(sysDbTableField.getFieldName()),SysDbTableField::getFieldName,sysDbTableField.getFieldName());
        lambda.eq(ObjectUtil.isNotEmpty(sysDbTableField.getFieldIsPrimaryKey()),SysDbTableField::getFieldIsPrimaryKey,sysDbTableField.getFieldIsPrimaryKey());
        lambda.eq(ObjectUtil.isNotEmpty(sysDbTableField.getFieldType()),SysDbTableField::getFieldType,sysDbTableField.getFieldType());
        lambda.eq(ObjectUtil.isNotEmpty(sysDbTableField.getFieldIsNull()),SysDbTableField::getFieldIsNull,sysDbTableField.getFieldIsNull());
        lambda.eq(ObjectUtil.isNotEmpty(sysDbTableField.getFieldDefaultValue()),SysDbTableField::getFieldDefaultValue,sysDbTableField.getFieldDefaultValue());
        return sysDbTableFieldMapper.selectList(lambda);
    }

    /**
     * 新增数据库
     *
     * @param sysDbTableField 数据库
     * @return 结果
     */
    @Override
    public int insertSysDbTableField(SysDbTableField sysDbTableField) {
        FunUtils.isTureOrFalse(sysDbTableField.getFieldIsPrimaryKey())
                .trueOrFalseHandle(()->sysDbTableField.setFieldIsNull(false),()->{});
        sysDbTableField.setCreateTime(DateUtils.getNowDate());
        return sysDbTableFieldMapper.insert(sysDbTableField);
    }

    /**
     * 修改数据库
     *
     * @param sysDbTableField 数据库
     * @return 结果
     */
    @Override
    public int updateSysDbTableField(SysDbTableField sysDbTableField) {
        FunUtils.isTureOrFalse(sysDbTableField.getFieldIsPrimaryKey())
                .trueOrFalseHandle(()->sysDbTableField.setFieldIsNull(false),()->{});
        sysDbTableField.setUpdateTime(DateUtils.getNowDate());
        return sysDbTableFieldMapper.updateById(sysDbTableField);
    }

    /**
     * 删除数据库对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
     @Override
     public int deleteSysDbTableFieldByIds(String ids) {
        return sysDbTableFieldMapper.deleteBatchIds(Convert.toStrList(ids));
     }

    /**
     * 删除数据库信息
     *
     * @param id 数据库ID
     * @return 结果
     */
    @Override
    public int deleteSysDbTableFieldById(Long id) {
        return sysDbTableFieldMapper.deleteById(id);
    }
}
