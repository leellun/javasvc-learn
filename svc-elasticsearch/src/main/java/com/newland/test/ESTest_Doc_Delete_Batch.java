package com.newland.test;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.DeleteOperation;
import com.newland.utils.ElasticsearchClientUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量删除文档
 */
public class ESTest_Doc_Delete_Batch {
    private static ElasticsearchClient client = ElasticsearchClientUtils.getClient();
    @Test
    public void test1() throws Exception {
        List<BulkOperation> bulkOperationList = new ArrayList<>();
        bulkOperationList.add(new BulkOperation.Builder().delete(
                new DeleteOperation.Builder().index("users").id("1001").build()).build());
        // 批量删除数据
        co.elastic.clients.elasticsearch.core.BulkRequest request = new co.elastic.clients.elasticsearch.core.BulkRequest.Builder().operations(bulkOperationList).build();
        BulkResponse response = client.bulk(request);
    }
    @Test
    public void test2() throws Exception {
        BulkRequest bulkRequest=BulkRequest.of(builder -> builder.operations(BulkOperation.of(operationBuilder->
                operationBuilder.index(i->i.index("users").id("1001"))
        )));
        // 批量删除数据
        BulkResponse response = client.bulk(bulkRequest);
    }
}
