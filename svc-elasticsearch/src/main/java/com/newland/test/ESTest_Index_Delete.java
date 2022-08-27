package com.newland.test;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import com.newland.utils.ElasticsearchClientUtils;

public class ESTest_Index_Delete {
    private static ElasticsearchClient client = ElasticsearchClientUtils.getClient();

    public static void main(String[] args) throws Exception {
        DeleteResponse response = client.delete(d -> d.index("users"));
        // 响应状态
        System.out.println(response.result());
    }
}
