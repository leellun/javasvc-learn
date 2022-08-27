package com.newland.test;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.Alias;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import com.newland.utils.ElasticsearchClientUtils;

public class ESTest_Index_Create {
    private static ElasticsearchClient client = ElasticsearchClientUtils.getClient();

    public static void main(String[] args) throws Exception {
        CreateIndexResponse createResponse = client.indices().create(c -> c
                .index("users")
                .aliases("name", aa -> aa
                        .isWriteIndex(true))
                .aliases("age", aa -> aa.isHidden(true))
                .aliases("sex", aa -> aa.isHidden(true)));
        System.out.println(createResponse);
    }
}
