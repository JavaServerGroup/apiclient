package com.github.javaservergroup.apiclient;

import com.alibaba.fastjson2.JSON;
import com.github.javaservergroup.apiclient.AbstractProcessor;
import com.github.javaservergroup.apiclient.Request;
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
        request.url = appendParamStrToUrl(request.url, params2paramsStr(request.param));
    }

    @Override
    HttpURLConnection doProcess(HttpURLConnection httpUrlConnection) {
        responseHeader = httpUrlConnection.getHeaderFields();
        if (log.isDebugEnabled()) {
            log.debug("发送请求: curl -X HEAD {} '{}'", makeHeaderLogString(request.header), request.url);
        }
        if (log.isDebugEnabled()) {
            log.debug("获得header: {}", JSON.toJSON(responseHeader));
        }
        return httpUrlConnection;
    }

}
