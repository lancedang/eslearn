package com.lance.es.eslearn.config;

import lombok.Data;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 通过ESConfig构件ES client
 */
@Data
@Configuration
@EnableConfigurationProperties(ESConfig.class)
public class XpackESBuilder {

    @Autowired
    private ESConfig esConfig;

    @Bean
    @Primary
    public Client buildClient() {
        //common 配置
        Settings settings = Settings.builder()
                .put("cluster.name", esConfig.getClusterName())
                .put("xpack.security.user", esConfig.getPwd())
                .put("client.transport.sniff", esConfig.isSniff())
                .build();

        TransportClient client = new PreBuiltXPackTransportClient(settings);

        //添加es集群地址
        try {
            for (String item : esConfig.getNodeURL()) {
                System.out.println(item);
                String[] url = item.split(":");
                InetSocketTransportAddress transportAddress = new InetSocketTransportAddress(InetAddress.getByName(url[0]), Integer.valueOf(url[1]));
                client.addTransportAddress(transportAddress);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        // on shutdown
        //client.close();
        //client.connectedNodes();
        return client;
    }

}
