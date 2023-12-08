package com.github.javaservergroup.apiclient;

import lombok.Getter;
import lombok.Setter;

public class ApiClient {

    /**
     * 全局连接超时时间
     */
    @Getter
    @Setter
    private static int defaultConnectionTimeout = 30000;

    /**
     * 全局读超时时间
     */
    @Getter
    @Setter
    private static int defaultReadTimeout = 30000;

    @Getter
    private static final String charsetName = "UTF-8";

    private ApiClient() {
    }

    public static Request Api() {
        return new Request();
    }
}
