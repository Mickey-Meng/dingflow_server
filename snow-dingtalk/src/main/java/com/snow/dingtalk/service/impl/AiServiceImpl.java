package com.snow.dingtalk.service.impl;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.request.OapiAiMtTranslateRequest;
import com.dingtalk.api.request.OapiOcrStructuredRecognizeRequest;
import com.dingtalk.api.response.OapiAiMtTranslateResponse;
import com.dingtalk.api.response.OapiOcrStructuredRecognizeResponse;
import com.snow.common.exception.SyncDataException;
import com.snow.dingtalk.common.BaseConstantUrl;
import com.snow.dingtalk.model.client.SnowDingTalkDefaultClient;
import com.snow.dingtalk.model.request.AiTranslateRequest;
import com.snow.dingtalk.model.request.OcrRecognizeRequest;
import com.snow.dingtalk.service.AiService;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @program: snow
 * @description
 * @author: 没用的阿吉
 * @create: 2021-03-29 17:16
 **/
@Service
@Slf4j
public class AiServiceImpl implements AiService {


    @Override
    public OapiOcrStructuredRecognizeResponse.OcrStructuredResult ocrRecognize(OcrRecognizeRequest ocrRecognizeRequest) {
        OapiOcrStructuredRecognizeRequest req = new OapiOcrStructuredRecognizeRequest();
        req.setType(ocrRecognizeRequest.getType());
        req.setImageUrl(ocrRecognizeRequest.getImageUrl());
        try {
            OapiOcrStructuredRecognizeResponse response = new SnowDingTalkDefaultClient<OapiOcrStructuredRecognizeResponse>().execute(req, BaseConstantUrl.AI_OCR_RECOGNIZE);
            if(response.getErrcode()!=0){
                throw new SyncDataException(JSON.toJSONString(req),response.getErrmsg());
            }
            return response.getResult();
        } catch (ApiException e) {
            log.error("AiServiceImpl->ocrRecognize error msg:{}",e.getMessage());
            log.error(e.getMessage(),e);
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }

    @Override
    public String aiTranslate(AiTranslateRequest aiTranslateRequest) {
        OapiAiMtTranslateRequest req = new OapiAiMtTranslateRequest();
        req.setQuery(aiTranslateRequest.getQuery());
        req.setSourceLanguage(aiTranslateRequest.getSourceLanguage());
        req.setTargetLanguage(aiTranslateRequest.getTargetLanguage());
        try {
            OapiAiMtTranslateResponse response = new SnowDingTalkDefaultClient<OapiAiMtTranslateResponse>().execute(req, BaseConstantUrl.AI_TRANSLATE);
            if(response.getErrcode()!=0){
                throw new SyncDataException(JSON.toJSONString(req),response.getErrmsg());
            }
            return response.getResult();
        } catch (ApiException e) {
            log.error("AiServiceImpl->aiTranslate error msg:{}",e.getMessage());
            log.error(e.getMessage(),e);
            throw new SyncDataException(JSON.toJSONString(req),e.getErrMsg());
        }
    }
}
