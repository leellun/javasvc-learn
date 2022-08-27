package com.newland.test;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.CreateOperation;
import com.newland.bean.User;
import com.newland.utils.ElasticsearchClientUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量插入
 */
public class ESTest_Doc_Insert_Batch {
    private static ElasticsearchClient client = ElasticsearchClientUtils.getClient();

    public static void main1(String[] args) throws Exception {
        List<BulkOperation> bulkOperationList = new ArrayList<>();
        //方式一
        bulkOperationList.add(new BulkOperation.Builder().create(
                new CreateOperation.Builder<User>().index("users").id("1001")
                        .document(new User("zhangsan", "男", 30)).build()).build());
        bulkOperationList.add(new BulkOperation.Builder().create(
                new CreateOperation.Builder<User>().index("users").id("1002")
                        .document(new User("zhangsan2", "男", 30)).build()).build());


        // 批量插入数据
        co.elastic.clients.elasticsearch.core.BulkRequest request = new co.elastic.clients.elasticsearch.core.BulkRequest.Builder().index("users").operations(bulkOperationList).build();

        BulkResponse response = client.bulk(request);
        System.out.println(response.items());
    }

    public static void main(String[] args) throws Exception {
        //方式二
        BulkRequest bulkRequest = BulkRequest.of(
                builder -> builder
                        .operations(
                                op -> op.index(
                                        i -> i.index("users").id("1001").document(new User("zhangsan", "男", 30))))
                        .operations(
                                op -> op.index(
                                        i -> i.index("users").id("1001").document(new User("zhangsan2", "男", 30)))
                        )
        );

        BulkResponse response = client.bulk(bulkRequest);
        System.out.println(response.items());
    }
}
