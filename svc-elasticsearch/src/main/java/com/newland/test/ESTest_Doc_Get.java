package com.newland.test;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetRequest;
import co.elastic.clients.elasticsearch.core.GetResponse;
import com.newland.bean.User;
import com.newland.utils.ElasticsearchClientUtils;

public class ESTest_Doc_Get {
    private static ElasticsearchClient client = ElasticsearchClientUtils.getClient();
    public static void main(String[] args) throws Exception {
        // 查询数据
        GetRequest request = new GetRequest.Builder().index("users").id("1001").build();
        GetResponse response = client.get(request, User.class);
        System.out.println(response.source());
    }
}
