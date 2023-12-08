package com.github.javaservergroup.apiclient.exception;

import com.alibaba.fastjson2.JSON;
import com.github.javaservergroup.apiclient.Request;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString
@Getter
public class StatusCodeNot200Exception extends Exception {

    private final int statusCode;
    private final Request request;

    public StatusCodeNot200Exception(int statusCode, Request request) {
        this.statusCode = statusCode;
        this.request = request;
        log.error(this.getStatusCode() + "\t" + JSON.toJSONString(this.request), this);
    }

}
