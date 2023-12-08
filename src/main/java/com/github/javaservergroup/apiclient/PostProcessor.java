package com.github.javaservergroup.apiclient;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.HttpURLConnection;

import static com.github.javaservergroup.apiclient.util.HttpUtil.*;

@Slf4j
public class PostProcessor extends AbstractProcessor {

    public PostProcessor(Request request) {
        this.request = request;
    }

    @Override
    void processingParam() {
        request.paramsString = params2paramsStr(request.param);
    }

    @Override
    HttpURLConnection doProcess(HttpURLConnection httpUrlConnection) throws IOException {
        if (log.isDebugEnabled()) {
            if (request.paramsString == null || request.paramsString.isEmpty()) {
                log.debug("发送请求: curl '{}' {}-X POST", request.url, makeHeaderLogString(request.header));
            } else {
                log.debug("发送请求: curl '{}' {}-X POST -d '{}'", request.url, makeHeaderLogString(request.header), request.paramsString);
            }
        }
        httpUrlConnection.setDoOutput(true);
        if (!"".equals(request.paramsString)) {
            httpUrlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            byte[] data = request.paramsString.getBytes(ApiClient.getCharsetName());
            httpUrlConnection.setFixedLengthStreamingMode(data.length);

            writeAndCloseStream(httpUrlConnection.getOutputStream(), data);
        } else {
            httpUrlConnection.setFixedLengthStreamingMode(0);
        }
        return httpUrlConnection;
    }

}
