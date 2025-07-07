package com.keensense.search.utils;

import java.net.URI;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

/**
 * Created by zhanx xiaohui on 2019-08-22.
 */
public class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {

    public static final String METHOD_NAME = "DELETE";

    public String getMethod() {
        return METHOD_NAME;
    }

    public HttpDeleteWithBody(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    public HttpDeleteWithBody(final URI uri) {
        super();
        setURI(uri);
    }

    public HttpDeleteWithBody() {
        super();
    }
}

/**
 * @program: platform
 * @description:
 * @author: zhan xiaohui
 * @create: 2019-08-22 14:18
 **/