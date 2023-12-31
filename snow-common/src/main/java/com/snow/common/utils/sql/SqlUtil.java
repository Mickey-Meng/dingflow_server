package com.snow.common.utils.sql;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.snow.common.core.page.TableDataInfo;
import com.snow.common.exception.UtilException;

import java.util.List;

/**
 * sql操作工具类
 *
 * @author ruoyi
 */
public class SqlUtil
{
    /**
     * 定义常用的 sql关键字
     */
    public static String SQL_REGEX = "select |insert |delete |update |drop |count |exec |chr |mid |master |truncate |char |and |declare ";

    /**
     * 仅支持字母、数字、下划线、空格、逗号、小数点（支持多个字段排序）
     */
    public static String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,\\.]+";

    /**
     * 检查字符，防止注入绕过
     */
    public static String escapeOrderBySql(String value)
    {
        if (StrUtil.isNotEmpty(value) && !isValidOrderBySql(value))
        {
            throw new UtilException("参数不符合规范，不能进行查询");
        }
        return value;
    }

    /**
     * 验证 order by 语法是否符合规范
     */
    public static boolean isValidOrderBySql(String value)
    {
        return value.matches(SQL_PATTERN);
    }

    /**
     * SQL关键字检查
     */
    public static void filterKeyword(String value) {
        if (StrUtil.isEmpty(value)) {
            return;
        }
        List<String> sqlKeywords = StrUtil.split(SQL_REGEX, "\\|");
        for (int i = 0; i < sqlKeywords.size(); i++) {
            if (StrUtil.indexOfIgnoreCase(value, sqlKeywords.get(i)) > -1) {
                throw new UtilException("参数存在SQL注入风险");
            }
        }
    }

    public static <R> TableDataInfo<R> getDataTable(List list, Class<R> clazz) {
        TableDataInfo<R> rspData = new TableDataInfo<>();
        rspData.setCode(0);
        rspData.setRows(BeanUtil.copyToList(list,clazz));
        rspData.setTotal(new PageInfo(list).getTotal());
        rspData.setPageIndex(new PageInfo(list).getPageNum());
        rspData.setPageSize(new PageInfo(list).getPageSize());
        return rspData;
    }
}
