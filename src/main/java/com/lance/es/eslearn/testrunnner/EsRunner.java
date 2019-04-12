package com.lance.es.eslearn.testrunnner;

import com.alibaba.fastjson.JSON;
import com.carrotsearch.hppc.cursors.ObjectCursor;
import com.lance.es.eslearn.config.NormalEsClient;
import com.lance.es.eslearn.config.XpackESBuilder;
import com.lance.es.eslearn.entity.DocumentItem;
import com.lance.es.eslearn.util.ObjectToJsonUtil;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;
import java.util.*;

@Component
public class EsRunner implements CommandLineRunner {

    @Autowired
    private XpackESBuilder xpackEsBuilder;

    @Override
    public void run(String... strings) throws Exception {

        //indexDocumentToEs();

        //写入
        //indexDocumentToEsByNormalClient();

        //查
        searchDocuments();

    }

    //插入单条document
    public void indexDocumentToEs() {
        DocumentItem item = new DocumentItem();
        long id = System.currentTimeMillis();
        item.setId(id);
        item.setName(id + "-name");

        item.setDate(new Date());

        String json = ObjectToJsonUtil.getStringJson(item);

        Client transportClient = xpackEsBuilder.buildClient();
        IndexRequestBuilder requestBuilder = transportClient.prepareIndex("logmetric-test", "single", id + "").setSource(json, XContentType.JSON);

        //insert
        requestBuilder.execute();

        //find
        GetResponse getResponse = transportClient.prepareGet("logmetric-test", "single", id + "").get();
        if (getResponse.isExists()) {
            System.out.println(getResponse.getSourceAsString());
        } else {
            System.out.println("not exist");
        }
    }

    public void indexDocumentToEsByNormalClient() throws UnknownHostException {
        DocumentItem item = new DocumentItem();
        long id = System.currentTimeMillis();
        item.setId(id);
        item.setName(id + "-name");
        item.setDate(new Date());
        Map<String, String> tags = new HashMap<>();
        tags.put("host", "localhost");
        item.setTags(tags);

        String json = JSON.toJSONStringWithDateFormat(item, "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        System.out.println("json string: " + json);
        Client normalClient = NormalEsClient.createNormalClient();

        IndexRequestBuilder requestBuilder = normalClient.prepareIndex("logmetric-2018.10.24", "single", id + "").setSource(json, XContentType.JSON);
        requestBuilder.execute();

    }


    public void searchDocuments() {
        Client transportClient = xpackEsBuilder.buildClient();

        //查看es cluster下所有index
        ImmutableOpenMap<String, IndexMetaData> indices = transportClient.admin().cluster().prepareState().get().getState().getMetaData().getIndices();
        Iterator<ObjectCursor<IndexMetaData>> iterator = indices.values().iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next().value.getIndex().getName());
        }

        //找到某条id的document
        //getResponse=transportClient.prepareGet().setIndex("logmetric-2019.04.12").setType("log").get();
        GetResponse getResponse = transportClient.prepareGet("logmetric-2019.04.12", "log", "AWoRXI_BbbT998VYq2uO").get();
        if (getResponse.isExists()) {
            System.out.println("exist");
            System.out.println(getResponse.getSourceAsString());
        } else {
            System.out.println("not exist");
        }

        //获取某个index/type下所有记录
        SearchResponse searchResponse = transportClient.prepareSearch("mau-log-2019.04.13")
        //SearchResponse searchResponse = transportClient.prepareSearch("logmetric-2019.04.11")
                //.setTypes("log")
                .setSearchType(SearchType.QUERY_AND_FETCH)
                //.setQuery(QueryBuilders.matchQuery("name", field))
                .get();
        List<SearchHit> searchHits = Arrays.asList(searchResponse.getHits().getHits());
        Iterator<SearchHit> searchHitIterator = searchHits.iterator();
        while (searchHitIterator.hasNext()) {
            System.out.println(searchHitIterator.next().getSource());
        }

    }

}
