package com.snow.system.domain.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author qimingjin
 * @date 2022-12-19 16:13
 * @Description:
 */
@Data
public class SysDbTableResp implements Serializable {
    private static final long serialVersionUID = 1908434258857590645L;

    private Long id;

    /** 数据库表名称 */
    private String dbName;

    private String remark;

    private boolean isExist;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


}
