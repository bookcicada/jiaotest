package com.jiao.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 18067
 * @Date 2021/9/19 19:13
 */
@Component
@ConfigurationProperties(prefix = "jdwx.realname")
public class JdwxRealNameConfig {
    private String url;
    private String appkey;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }
}
