package com.newland.test;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.newland.bean.User;
import com.newland.utils.ElasticsearchClientUtils;

public class ESTest_Doc_Insert {
    private static ElasticsearchClient client = ElasticsearchClientUtils.getClient();

    public static void main(String[] args) throws Exception {
        User user = new User();
        user.setName("zhangsan");
        user.setAge(30);
        user.setSex("男");
        // 插入数据
        IndexRequest request = new IndexRequest.Builder<User>().index("users").id("1001").document(user).build();

        IndexResponse response = client.index(request);
        System.out.println(response.result());
    }
}
