package com.github.javaservergroup.apiclient.processor;

import com.github.javaservergroup.apiclient.model.Request;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.net.HttpURLConnection;

import static com.github.javaservergroup.apiclient.util.HttpUtil.*;

@Slf4j
public class GetProcessor extends AbstractProcessor {

    public GetProcessor(Request request) {
        this.request = request;
    }

    @Override
    void processingParam() {
        val url = appendParamStrToUrl(request.getUrl(), params2paramsStr(request.getParam()));
        request.setUrl(url);
    }

    @Override
    HttpURLConnection doProcess(HttpURLConnection httpUrlConnection) {
        if (log.isDebugEnabled()) {
            log.debug("发送请求: curl {} '{}'", makeHeaderLogString(request.getHeader()),  request.getUrl());
        }
        return httpUrlConnection;
    }


}
