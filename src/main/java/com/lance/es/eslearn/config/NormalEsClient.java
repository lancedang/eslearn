package com.lance.es.eslearn.config;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Setting;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NormalEsClient {
    public static Client createNormalClient() throws UnknownHostException {
        String esUrl = "10.114.26.233";
        Settings settings = Settings.builder()
                .put("cluster.name", "gdslog")
                .build();


        TransportClient transportClient = new PreBuiltTransportClient(settings);
        InetSocketTransportAddress transportAddress = new InetSocketTransportAddress(InetAddress.getByName(esUrl), 9300);
        transportClient.addTransportAddress(transportAddress);
        return transportClient;
    }
}
