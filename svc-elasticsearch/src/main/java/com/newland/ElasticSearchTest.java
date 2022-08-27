package com.newland;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.SearchTemplateResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.SourceConfig;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import co.elastic.clients.json.JsonData;
import com.newland.bean.User;
import com.newland.utils.ElasticsearchClientUtils;
import org.elasticsearch.client.RequestOptions;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class ElasticSearchTest {
    Logger logger=Logger.getLogger(ElasticSearchTest.class.getName());
    private ElasticsearchClient client= ElasticsearchClientUtils.getClient();


    // 1. 查询索引中全部的数据
    @Test
    public void testSearchRequest() throws IOException {
        String searchText = "bike";

        SearchResponse<User> response = client.search(s -> s
                        .index("users")
                        .query(q -> q
                                .match(t -> t
                                        .field("name")
                                        .query(searchText)
                                )
                        ),
                User.class
        );

        TotalHits total = response.hits().total();
        boolean isExactResult = total.relation() == TotalHitsRelation.Eq;

        if (isExactResult) {
            logger.info("There are " + total.value() + " results");
        } else {
            logger.info("There are more than " + total.value() + " results");
        }

        List<Hit<User>> hits = response.hits().hits();
        for (Hit<User> hit: hits) {
            User product = hit.source();
            logger.info("Found product " + product.getName() + ", score " + hit.score());
        }
    }

    // 条件查询
    @Test
    public void testCombinationQuery() throws IOException {
        String searchText = "bike";
        double age = 27;

        // 通过用户名查询
        Query byName = MatchQuery.of(m -> m
                .field("name")
                .query(searchText)
        )._toQuery();

        // 通过最大价格
        Query byMaxPrice = RangeQuery.of(r -> r
                .field("age")
                .gte(JsonData.of(age))
        )._toQuery();

        // 组合查询
        SearchResponse<User> response = client.search(s -> s
                        .index("users")
                        .query(q -> q
                                .bool(b -> b
                                        .must(byName)
                                        .must(byMaxPrice)
                                )
                        ),
                User.class
        );

        List<Hit<User>> hits = response.hits().hits();
        for (Hit<User> hit: hits) {
            User product = hit.source();
            logger.info("Found product " + product.getName() + ", score " + hit.score());
        }
    }

    /**
     * 搜索模板是可以使用不同变量运行的存储搜索。搜索模板允许您在不修改应用程序代码的情况下更改搜索。
     * 在运行模板搜索之前，首先必须创建模板。这是一个返回搜索请求主体的存储脚本，通常被定义为Mustache模板。这个存储的脚本可以在应用程序之外创建，也可以使用Java API客户端创建:
     */
    public void testTemplate() throws IOException {
        // 创建一个script
        client.putScript(r -> r
                .id("query-script")
                .script(s -> s
                        .lang("mustache")
                        .source("{\"query\":{\"match\":{\"{{field}}\":\"{{value}}\"}}}")
                ));
        // 要使用搜索模板，使用searchTemplate方法来引用脚本，并为其参数提供值:
        SearchTemplateResponse<User> response = client.searchTemplate(r -> r
                        .index("users")
                        .id("query-script")
                        .params("field", JsonData.of("name"))
                        .params("value", JsonData.of("leellun")),
                User.class
        );

        List<Hit<User>> hits = response.hits().hits();
        for (Hit<User> hit: hits) {
            User user = hit.source();
            logger.info("Found product " + user.getName() + ", score " + hit.score());
        }
    }
    // 3. 分页查询
    @Test
    public void testPageQuery() throws IOException {
    }

    // 4. 查询排序
    @Test
    public void testSortQuery() throws IOException {
    }

    // 5. 过滤字段
    @Test
    public void testFetchQuery() throws IOException {
    }

    // 6. 组合查询
    @Test
    public void testMultiQuery() throws IOException {
    }

    // 7. 范围查询
    @Test
    public void testQuery7() throws IOException {
    }

    // 8. 模糊查询
    @Test
    public void testQuery8() throws IOException {
    }

    // 9. 高亮查询
    @Test
    public void testQuery9() throws IOException {
    }

    // 10. 聚合查询
    @Test
    public void testQuery10() throws IOException {
    }

    // 11. 分组查询
    @Test
    public void testQuery11() throws IOException {
    }
}
