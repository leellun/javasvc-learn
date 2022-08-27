package com.newland;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.AcknowledgedResponse;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.CreateOperation;
import co.elastic.clients.elasticsearch.core.bulk.DeleteOperation;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.*;
import com.newland.bean.User;
import com.newland.utils.ElasticsearchClientUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ElasticOptionTest{
    private ElasticsearchClient client= ElasticsearchClientUtils.getClient();

    @Test
    public void createType() throws IOException {
        CreateIndexResponse createResponse = client.indices().create(
                new CreateIndexRequest.Builder()
                        .index("users")
                        .aliases("name",
                                new Alias.Builder().isWriteIndex(true).build()
                        ).aliases("age", new Alias.Builder().isHidden(true).build())
                        .aliases("sex", new Alias.Builder().isHidden(true).build())
                        .build()
        );
        System.out.println(createResponse);
    }

    @Test
    public void test() throws IOException {
        SearchResponse<User> search = client.search(s -> s
                        .index("users")
                        .query(q -> q
                                .term(t -> t
                                        .field("name")
                                        .value(v -> v.stringValue("zhangsan"))
                                )),
                User.class);

        for (Hit<User> hit : search.hits().hits()) {
            System.out.println(hit.source());
        }
    }

    /**
     * 插入文档
     */
    @Test
    public void testDocInsert() throws IOException {
        com.newland.bean.User user = new com.newland.bean.User();
        user.setName("zhangsan");
        user.setAge(30);
        user.setSex("男");
        // 插入数据
        IndexRequest request = new IndexRequest.Builder<User>().index("users").id("1001").document(user).build();

        IndexResponse response = client.index(request);
        System.out.println(response.result());
    }

    /**
     * 批量插入文档
     */
    @Test
    public void testDocInsertBatch() throws IOException {
        List<BulkOperation> bulkOperationList = new ArrayList<>();
        bulkOperationList.add(new BulkOperation.Builder().create(
                new CreateOperation.Builder<User>().index("users").id("1001")
                        .document(new User("zhangsan", "男", 30)).build()).build());
        // 批量插入数据
        co.elastic.clients.elasticsearch.core.BulkRequest request = new co.elastic.clients.elasticsearch.core.BulkRequest.Builder().index("users").operations(bulkOperationList).build();

        BulkResponse response = client.bulk(request);
        System.out.println(response.items());
    }

    /**
     * 获取文档
     */
    @Test
    public void testDocGet() throws IOException {
        // 查询数据
        GetRequest request = new GetRequest.Builder().index("users").id("1001").build();
        GetResponse response = client.get(request, User.class);
        System.out.println(response.source());
    }

    /**
     * 删除文档
     */
    @Test
    public void testDocDelete() throws IOException {
        DeleteRequest request = new DeleteRequest.Builder().index("users").id("1001").build();
        DeleteResponse response = client.delete(request);
        System.out.println(response.toString());
    }

    /**
     * 批量删除文档
     */
    @Test
    public void testDocDeleteBatch() throws IOException {
        List<BulkOperation> bulkOperationList = new ArrayList<>();
        bulkOperationList.add(new BulkOperation.Builder().delete(
                new DeleteOperation.Builder().index("users").id("1001").build()).build());
        // 批量删除数据
        co.elastic.clients.elasticsearch.core.BulkRequest request = new co.elastic.clients.elasticsearch.core.BulkRequest.Builder().operations(bulkOperationList).build();
        BulkResponse response = client.bulk(request);
    }

    /**
     * 文档更新
     */
    @Test
    public void testDocUpdate() throws IOException {
        // 修改数据
        UpdateRequest request = new UpdateRequest.Builder().index("users").id("1001").doc(new User("1001", "男", 1)).build();

        UpdateResponse response = client.update(request, User.class);
    }

    /**
     * 插入索引
     */
    @Test
    public void testIndexCreate() throws IOException {
        // 创建索引
        CreateIndexRequest request = new CreateIndexRequest.Builder().index("user2").build();
        CreateIndexResponse createIndexResponse = client.indices().create(request);

        // 响应状态
        boolean acknowledged = createIndexResponse.acknowledged();
        System.out.println("索引操作 ：" + acknowledged);
    }

    /**
     * 查询索引
     */
    @Test
    public void testIndexSearch() throws IOException {
        // 查询索引
        GetIndexRequest request = new GetIndexRequest.Builder().index("user").build();

        GetIndexResponse getIndexResponse = client.indices().get(request);

        // 响应状态
        System.out.println(getIndexResponse);
    }

    /**
     * 删除索引
     */
    @Test
    public void testIndexDelete() throws IOException {
        // 查询索引
        DeleteIndexRequest request = new DeleteIndexRequest.Builder().index("user").build();
        AcknowledgedResponse response = client.indices().delete(request);
        // 响应状态
        System.out.println(response.acknowledged());
    }
}
