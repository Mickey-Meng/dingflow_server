package com.snow.dingtalk.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qimingjin
 * @date 2023-08-22 15:36
 * @Description:
 */
@Data
public class AiTranslateRequest implements Serializable {
    private static final long serialVersionUID = 444902174232076637L;

    /*
     *	翻译源文字符串
     */
    private String query;

    /**
     * 翻译源语言类型
     */
    private String sourceLanguage;

    /**
     * 翻译目标语言类型
     */
    private String targetLanguage;
}
