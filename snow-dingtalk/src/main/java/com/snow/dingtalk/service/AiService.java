package com.snow.dingtalk.service;

import com.dingtalk.api.response.OapiAiMtTranslateResponse;
import com.dingtalk.api.response.OapiOcrStructuredRecognizeResponse;
import com.snow.dingtalk.model.request.AiTranslateRequest;
import com.snow.dingtalk.model.request.OcrRecognizeRequest;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-03-29 17:16
 **/
public interface AiService {

    /**
     * 图片识别接口
     * @param ocrRecognizeRequest
     * @return
     */
    OapiOcrStructuredRecognizeResponse.OcrStructuredResult ocrRecognize(OcrRecognizeRequest ocrRecognizeRequest);

    /**
     * 钉钉文本翻译
     * @return
     */
    String aiTranslate(AiTranslateRequest aiTranslateRequest);
}
