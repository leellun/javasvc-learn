package com.newland.test;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import com.newland.utils.ElasticsearchClientUtils;

public class ESTest_Doc_Delete {
    private static ElasticsearchClient client = ElasticsearchClientUtils.getClient();
    public static void main(String[] args) throws Exception {

        DeleteRequest request = new DeleteRequest.Builder().index("users").id("1001").build();
        DeleteResponse response = client.delete(request);
        System.out.println(response.toString());
    }
}
