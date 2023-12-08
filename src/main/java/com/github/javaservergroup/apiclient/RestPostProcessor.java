package com.github.javaservergroup.apiclient;

import com.alibaba.fastjson2.JSON;
import com.github.javaservergroup.apiclient.exception.RestPostNotSupportFileException;
import com.github.javaservergroup.apiclient.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static com.github.javaservergroup.apiclient.util.HttpUtil.makeHeaderLogString;
import static com.github.javaservergroup.apiclient.util.HttpUtil.writeAndCloseStream;

@Slf4j
public class RestPostProcessor extends AbstractProcessor {

    public RestPostProcessor(Request request) {
        this.request = request;
    }

    @Override
    void processingParam() {
        checkIsNotPostFile(HttpUtil.obj2Map(request.param));
        if (request.param != null) {
            request.paramsString = JSON.toJSONString(request.param);
        }
        request.header("Content-Type", "application/json");
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
        if (request.paramsString != null) {
            byte[] data = request.paramsString.getBytes(StandardCharsets.UTF_8);
            httpUrlConnection.setFixedLengthStreamingMode(data.length);

            writeAndCloseStream(httpUrlConnection.getOutputStream(), data);
        } else {
            httpUrlConnection.setFixedLengthStreamingMode(0);
        }
        return httpUrlConnection;
    }

    private void checkIsNotPostFile(Map<String, Object> params) {
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                checkIsNotPostFileByEntry(entry);
            }
        }
    }

    private void checkIsNotPostFileByEntry(Map.Entry<String, Object> entry) {
        if (entry.getValue() instanceof File) {
            throw new RestPostNotSupportFileException();
        }
        if (entry.getValue() instanceof List) {

            for (Object obj : (List) entry.getValue()) {
                if (obj instanceof File) {
                    throw new RestPostNotSupportFileException();
                }
            }
        }
    }

}
