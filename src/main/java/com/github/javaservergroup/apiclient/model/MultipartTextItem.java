package com.github.javaservergroup.apiclient.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;

import static com.github.javaservergroup.apiclient.MultipartPostProcessor.LINE_END;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by jialechan on 2017/2/22.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class MultipartTextItem extends MultipartItem {

    private String value;

    @Override
    public String genContentDispositionStr() {
        return "Content-Disposition: form-data;name=\"" + getKey() + "\"" + LINE_END;
    }

    @Override
    public String genContentType() {
        return "Content-Type: text/plain; charset=" + UTF_8 + LINE_END;
    }

    @Override
    public void genBody(OutputStream out) {
        try {
            out.write(value.getBytes(UTF_8));
        } catch (IOException e) {
            log.error("写入multipart的body遇到错误", e);
        }
    }
}
