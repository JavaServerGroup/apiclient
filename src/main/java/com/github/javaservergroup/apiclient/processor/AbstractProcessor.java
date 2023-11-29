package com.github.javaservergroup.apiclient.processor;

import com.github.javaservergroup.apiclient.exception.StatusCodeNot200Exception;
import com.github.javaservergroup.apiclient.model.Request;
import com.github.javaservergroup.apiclient.model.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import static com.github.javaservergroup.apiclient.util.HttpUtil.*;

@Slf4j
public abstract class AbstractProcessor {

    private HttpURLConnection httpUrlConnection;

    protected Request request;

    /**
     * 执行http请求
     */
    public ResponseWrapper process() throws IOException, StatusCodeNot200Exception {
        processingParam();
        commonPreProcess();
        doProcess(httpUrlConnection);
        return loadResponseWrapper(httpUrlConnection);
    }

    // 处理请求参数
    abstract void processingParam();

    // 通用的请求预处理
    private void commonPreProcess() throws IOException {
        final URL url = new URL(request.getUrl());

        httpUrlConnection = (HttpURLConnection) url.openConnection();
        httpUrlConnection.setRequestProperty("Charset", "UTF-8");
        httpUrlConnection.setConnectTimeout(request.getConnectionTimeout());
        httpUrlConnection.setReadTimeout(request.getReadTimeout());
        httpUrlConnection.setInstanceFollowRedirects(request.isFollowRedirects());
        httpUrlConnection.setRequestProperty("Accept-Encoding", "gzip");

        addHeaderToHttpUrlConnection(request.getHeader(), httpUrlConnection);
    }

    // 执行实际的请求
    abstract HttpURLConnection doProcess(HttpURLConnection httpUrlConnection) throws IOException;

    private InputStream getInputStreamByConnection(HttpURLConnection httpURLConnection) throws IOException {
        if ("gzip".equals(httpURLConnection.getContentEncoding())) {
            return new GZIPInputStream(httpURLConnection.getInputStream());
        } else {
            return httpURLConnection.getInputStream();
        }
    }

    /**
     * 处理responseBody输入流
     *
     * @param httpUrlConnection
     * @return
     * @throws IOException
     */
    private String handleResponseString(HttpURLConnection httpUrlConnection)
            throws IOException {
        try (InputStream is = getInputStreamByConnection(httpUrlConnection)) {
            final String result = readAndCloseStream(is);
            log.debug("返回: {}", result);
            return result;
        }
    }

    /**
     * 处理response获取wrapper
     *
     * @param httpUrlConnection
     * @return
     * @throws IOException
     */
    private ResponseWrapper loadResponseWrapper(HttpURLConnection httpUrlConnection) throws IOException, StatusCodeNot200Exception {
        ResponseWrapper wrapper = new ResponseWrapper();
        try {
            int responseCode = httpUrlConnection.getResponseCode();
            wrapper.setResponseCode(responseCode);
            wrapper.setResponseHeader(httpUrlConnection.getHeaderFields());

            if (is2XX(responseCode) || isRedirect(responseCode)) {
                wrapper.setResponseBody(handleResponseString(httpUrlConnection));
                return wrapper;
            } else {
                logHttpUrlConnectionErrorStream(httpUrlConnection);
                throw new StatusCodeNot200Exception(responseCode, request);
            }

        } catch (IOException e) {
            logHttpUrlConnectionErrorStream(httpUrlConnection);
            log.error("请求返回时发生IOException", e);
            throw e;
        } finally {
            httpUrlConnection.disconnect();
        }
    }

    private boolean isRedirect(int responseCode) {
        return responseCode >= 300 && responseCode <= 399 && !request.isFollowRedirects();
    }

    private boolean is2XX(int responseCode) {
        return responseCode >= 200 && responseCode <= 299;
    }


}
