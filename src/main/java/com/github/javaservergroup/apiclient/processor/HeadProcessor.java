package com.github.javaservergroup.apiclient.processor;

import com.alibaba.fastjson2.JSON;
import com.github.javaservergroup.apiclient.model.Request;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import static com.github.javaservergroup.apiclient.util.HttpUtil.*;

@Getter
@Slf4j
public class HeadProcessor extends AbstractProcessor {

    private Map<String, List<String>> responseHeader;

    public HeadProcessor(Request request) {
        this.request = request;
    }

    @Override
    void processingParam() {
        val url = appendParamStrToUrl(request.getUrl(), params2paramsStr(request.getParam()));
        request.setUrl(url);
    }

    @Override
    HttpURLConnection doProcess(HttpURLConnection httpUrlConnection) {
        responseHeader = httpUrlConnection.getHeaderFields();
        if (log.isDebugEnabled()) {
            log.debug("发送请求: curl -X HEAD {} '{}'", makeHeaderLogString(request.getHeader()), request.getUrl());
        }
        if (log.isDebugEnabled()) {
            log.debug("获得header: {}", JSON.toJSON(responseHeader));
        }
        return httpUrlConnection;
    }

}
