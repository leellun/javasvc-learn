package com.newland.test;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import com.newland.bean.User;
import com.newland.utils.ElasticsearchClientUtils;

public class ESTest_Doc_Update {
    private static ElasticsearchClient client = ElasticsearchClientUtils.getClient();

    public static void main(String[] args) throws Exception {
        // 修改数据
        UpdateRequest request = new UpdateRequest.Builder().index("users").id("1001").doc(new User("1001", "男", 1)).build();
//        UpdateRequest request=UpdateRequest.of(builder -> builder.index("users").id("1001").doc(new User("1001", "男", 1)));

        UpdateResponse response = client.update(request, User.class);
    }
}
