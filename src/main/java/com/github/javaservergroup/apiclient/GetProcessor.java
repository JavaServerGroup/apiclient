package com.github.javaservergroup.apiclient;

import lombok.extern.slf4j.Slf4j;

import java.net.HttpURLConnection;

import static com.github.javaservergroup.apiclient.util.HttpUtil.*;

@Slf4j
public class GetProcessor extends AbstractProcessor {

    public GetProcessor(Request request) {
        this.request = request;
    }

    @Override
    void processingParam() {
        request.url = appendParamStrToUrl(request.url, params2paramsStr(request.param));
    }

    @Override
    HttpURLConnection doProcess(HttpURLConnection httpUrlConnection) {
        if (log.isDebugEnabled()) {
            log.debug("发送请求: curl {} '{}'", makeHeaderLogString(request.header), request.url);
        }
        return httpUrlConnection;
    }


}
