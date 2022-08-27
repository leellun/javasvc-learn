package com.newland.test;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import com.newland.utils.ElasticsearchClientUtils;

public class ESTest_Index_Search {
    private static ElasticsearchClient client = ElasticsearchClientUtils.getClient();
    public static void main(String[] args) throws Exception {
        // 查询索引
        GetIndexResponse getIndexResponse=client.indices().get(g->g.index("users"));

        // 响应状态
        System.out.println(getIndexResponse.result().get("users").aliases());
        System.out.println(getIndexResponse.result().get("users").mappings());
        System.out.println(getIndexResponse.result().get("users").settings());
    }
}
