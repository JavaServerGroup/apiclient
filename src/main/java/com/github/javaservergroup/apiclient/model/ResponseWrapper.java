package com.github.javaservergroup.apiclient.model;

import com.alibaba.fastjson2.JSON;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ResponseWrapper {

    private int responseCode;

    private String responseBody;

    private Map<String, List<String>> responseHeader;

    public <T> T getResponseBody(Class<T> clazz) {
        return JSON.parseObject(responseBody, clazz);
    }

}
