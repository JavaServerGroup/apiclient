package com.github.javaservergroup.apiclient;

import com.github.javaservergroup.apiclient.exception.StatusCodeNot200Exception;
import com.github.javaservergroup.apiclient.model.ResponseWrapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {

    // http请求的header
    Map<String, String> header = new HashMap<>();

    // http请求参数
    Object param;

    // http请求的地址
    String url;

    // 默认使用全局超时时间配置
    int connectionTimeout = ApiClient.getDefaultConnectionTimeout();

    // 默认使用全局超时时间配置
    int readTimeout = ApiClient.getDefaultReadTimeout();

    // url上的请求字符串
    String paramsString;

    // 是否跟随跳转
    boolean isFollowRedirects = true;

    // 添加代理http的host
    String httpProxyHost;

    // 添加代理http的port
    Integer httpProxyPort;

    public Request() {
    }

    public Request followRedirects(boolean isFollowRedirects) {
        this.isFollowRedirects = isFollowRedirects;
        return this;
    }

    public Request httpProxy(String host, int port) {
        this.httpProxyHost = host;
        this.httpProxyPort = port;
        return this;
    }

    public Request header(Map<String, String> header) {
        this.header.putAll(header);
        return this;
    }

    public Request header(String key, String value) {
        this.header.put(key, value);
        return this;
    }

    public Request connectionTimeout(int connectionTimeout) {
        if (connectionTimeout < 1) {
            throw new IllegalArgumentException("超时时间必须大于0");
        }
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public Request readTimeout(int readTimeout) {
        if (connectionTimeout < 1) {
            throw new IllegalArgumentException("超时时间必须大于0");
        }
        this.readTimeout = readTimeout;
        return this;
    }

    public Request param(Object param) {
        //param方法应该只调用一次
        if (this.param != null) {
            throw new IllegalArgumentException("param方法应该只调用一次");
        }
        this.param = param;
        return this;
    }

    public String get(String url) throws IOException, StatusCodeNot200Exception {
        this.url = url;
        return getResponseWrapper(url).getResponseBody();
    }

    public <T> T get(String url, Class<T> clazz) throws IOException, StatusCodeNot200Exception {
        return getResponseWrapper(url).getResponseBody(clazz);
    }

    public String post(String url) throws IOException, StatusCodeNot200Exception {
        this.url = url;
        return postResponseWrapper(url).getResponseBody();
    }

    public <T> T post(String url, Class<T> clazz) throws IOException, StatusCodeNot200Exception {
        return postResponseWrapper(url).getResponseBody(clazz);
    }

    public String restPost(String url) throws IOException, StatusCodeNot200Exception {
        this.url = url;
        return restPostResponseWrapper(url).getResponseBody();
    }

    public <T> T restPost(String url, Class<T> clazz) throws IOException, StatusCodeNot200Exception {
        return restPostResponseWrapper(url).getResponseBody(clazz);
    }

    public String filePost(String url) throws IOException, StatusCodeNot200Exception {
        this.url = url;
        return filePostResponseWrapper(url).getResponseBody();
    }

    public <T> T filePost(String url, Class<T> clazz) throws IOException, StatusCodeNot200Exception {
        return filePostResponseWrapper(url).getResponseBody(clazz);
    }

    public Map<String, List<String>> head(String url) throws IOException, StatusCodeNot200Exception {
        this.url = url;
        HeadProcessor processor = new HeadProcessor(this);
        processor.process();
        return processor.getResponseHeader();
    }

    public ResponseWrapper getResponseWrapper(String url) throws IOException, StatusCodeNot200Exception {
        this.url = url;
        return new GetProcessor(this).process();
    }

    public ResponseWrapper postResponseWrapper(String url) throws IOException, StatusCodeNot200Exception {
        this.url = url;
        AbstractProcessor processor = new PostProcessor(this);
        return processor.process();
    }

    public ResponseWrapper restPostResponseWrapper(String url) throws IOException, StatusCodeNot200Exception {
        this.url = url;
        AbstractProcessor processor = new RestPostProcessor(this);
        return processor.process();
    }

    public ResponseWrapper filePostResponseWrapper(String url) throws IOException, StatusCodeNot200Exception {
        this.url = url;
        AbstractProcessor processor = new MultipartPostProcessor(this);
        return processor.process();
    }



}
