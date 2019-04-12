package com.lance.es.eslearn;

import com.lance.es.eslearn.entity.DocumentItem;
import com.lance.es.eslearn.util.ObjectToJsonUtil;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.*;
public class EslearnApplicationTests {

	@Test
	public void test() throws Exception {
		Client transportClient;
		transportClient = getClient();
		DocumentItem item = new DocumentItem();
		item.setId(12211221);
		item.setName("12211221");

		Map<String, String> tags = new HashMap<>();
		tags.put("host", "localhost");
		item.setTags(tags);

		byte[] json = ObjectToJsonUtil.getByteJson(item);

		IndexRequestBuilder indexRequestBuilder = transportClient.prepareIndex("logmetric-testunit1", "log.Testunit")
				.setSource(json, XContentType.JSON);

		BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();
		bulkRequestBuilder.add(indexRequestBuilder);

		//IndexResponse indexResponse = indexRequestBuilder.execute().actionGet();
		BulkResponse indexResponse = bulkRequestBuilder.execute().actionGet();
		System.out.println(indexResponse.getItems().length);
	}

	public Client getClient() throws Exception {
		Settings settings = Settings.builder()
				.put("cluster.name", "gdslog")
				.put("client.transport.sniff", true)
				.put("xpack.security.user", "logmetric:logmetric.write.pass")
				.build();
		PreBuiltXPackTransportClient client = new PreBuiltXPackTransportClient(settings);
		InetSocketTransportAddress transportAddress = new InetSocketTransportAddress(new InetSocketAddress("10.114.22.147", 9300));
		client.addTransportAddress(transportAddress);
		return client;
	}

}
