package com.jiao.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 18067
 * @Date 2021/9/17 9:52
 */
@Component
@ConfigurationProperties(prefix = "jdwx.sms")
public class JdwsSmsConfig {
    private String url;
    private String content;
    private String appkey;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
