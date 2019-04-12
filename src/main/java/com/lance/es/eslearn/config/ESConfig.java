package com.lance.es.eslearn.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 用于封装es配置属性
 */
@Data
@ConfigurationProperties(prefix = "es.client")
public class ESConfig {

    //默认绑定prefix+field的属性
    private List<String> nodeURL;

    private String clusterName;

    //绑定value指定属性
    //开启嗅探
    @Value("${client.transport.sniff}")
    private boolean sniff;

    @Value("${xpack.security.user}")
    private String pwd;

}
