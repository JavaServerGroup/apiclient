package com.github.javaservergroup.apiclient.model;

import lombok.Data;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by jialechan on 2017/2/22.
 */
@Data
public abstract class MultipartItem {

    private String key;

    public abstract String genContentDispositionStr();

    public abstract String genContentType();

    public abstract void genBody(OutputStream out);

}