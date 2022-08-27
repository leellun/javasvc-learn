package com.newland.test;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.CalendarInterval;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.json.JsonData;
import com.newland.utils.ElasticsearchClientUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 文档查询
 */
public class ESTest_Doc_Query {
    private static ElasticsearchClient esClient = ElasticsearchClientUtils.getClient();

    // 1. 查询索引中全部的数据
    @Test
    public void testQueryAll() throws IOException {
        SearchResponse response = esClient.search(SearchRequest.of(builder -> builder.index("users").query(Query.of(q -> q.matchAll(m -> m)))), User.class);
        HitsMetadata<User> hits = response.hits();

        System.out.println(hits.total());
        System.out.println(response.took());

        for (Hit<User> hit : hits.hits()) {
            System.out.println(hit.source());
        }
    }

    // 2. 条件查询 : termQuery
    @Test
    public void testTermQuery() throws IOException {
        SearchResponse response = esClient.search(SearchRequest.of(
                builder -> builder.index("users").query(TermQuery.of(
                        term -> term.field("age").value(30))._toQuery())), User.class);
        HitsMetadata<User> hits = response.hits();

        System.out.println(hits.total());
        System.out.println(response.took());

        for (Hit<User> hit : hits.hits()) {
            System.out.println(hit.source());
        }
    }

    // 3. 分页查询
    @Test
    public void testPageQuery() throws IOException {
        SearchResponse<User> response = esClient.search(builder ->
                builder.index("users").from(2).size(10), User.class);
        HitsMetadata<User> hits = response.hits();

        System.out.println(hits.total());
        System.out.println(response.took());

        for (Hit<User> hit : hits.hits()) {
            System.out.println(hit.source());
        }
    }

    // 4. 查询排序
    @Test
    public void testOrderQuery() throws IOException {
        SearchResponse<User> response = esClient.search(builder ->
                builder.index("users").sort(s ->
                        s.field(f -> f.field("age").order(SortOrder.Desc))), User.class);
        HitsMetadata<User> hits = response.hits();

        System.out.println(hits.total());
        System.out.println(response.took());

        for (Hit<User> hit : hits.hits()) {
            System.out.println(hit.source());
        }
    }

    // 5. 过滤字段
    @Test
    public void testFilterQuery() throws IOException {
        List<String> excludes = Arrays.asList(new String[]{"age"});
        List<String> includes = new ArrayList<>();
        SearchResponse<User> response = esClient.search(
                builder -> builder.index("users").source(
                        s -> s.filter(
                                filter -> filter.includes(includes).excludes(excludes))), User.class);
        HitsMetadata<User> hits = response.hits();

        System.out.println(hits.total());
        System.out.println(response.took());

        for (Hit<User> hit : hits.hits()) {
            System.out.println(hit.source());
        }
    }

    // 6. 组合查询
    @Test
    public void testCombinationQuery() throws IOException {
        SearchResponse<User> response = esClient.search(
                builder -> builder.index("users").query(
                        q -> q.bool(
                                b -> b.must(
                                        m -> m.term(t -> t.field("age").value(30))
                                ).must(
                                        m -> m.term(t -> t.field("sex").value("男"))
                                ).mustNot(
                                        m -> m.term(t -> t.field("sex").value("女"))
                                ).must(
                                        m -> m.term(t -> t.field("age").value(40))
                                ).must(
                                        m -> m.term(t -> t.field("age").value(50))
                                )
                        )
                ), User.class);
        HitsMetadata<User> hits = response.hits();

        System.out.println(hits.total());
        System.out.println(response.took());

        for (Hit<User> hit : hits.hits()) {
            System.out.println(hit.source());
        }
    }

    // 7. 范围查询
    @Test
    public void testRangeTest() throws IOException {
        SearchResponse<User> response = esClient.search(
                builder -> builder.index("users").query(
                        q -> q.range(
                                r -> r.gte(JsonData.of(30)).lt(JsonData.of(50)))), User.class);
        HitsMetadata<User> hits = response.hits();

        System.out.println(hits.total());
        System.out.println(response.took());

        for (Hit<User> hit : hits.hits()) {
            System.out.println(hit.source());
        }
    }

    // 8. 模糊查询
    @Test
    public void testFuzzyTest() throws IOException {
        SearchResponse<User> response = esClient.search(
                builder -> builder.index("users").query(
                        q -> q.fuzzy(f -> f.field("name").fuzziness("wangwu"))), User.class);

        HitsMetadata<User> hits = response.hits();
        System.out.println(hits.total());
        System.out.println(response.took());

        for (Hit<User> hit : hits.hits()) {
            System.out.println(hit.source());
        }
    }

    // 9. 高亮查询
    @Test
    public void tesHighlightTest() throws IOException {
        SearchResponse<User> response = esClient.search(
                builder -> builder.index("users").query(
                        q -> q.term(
                                term -> term.field("name").value("zhangsan"))).highlight(
                        high -> high.preTags("<font color='red'>").postTags("</font>")), User.class);

        HitsMetadata<User> hits = response.hits();
        System.out.println(hits.total());
        System.out.println(response.took());

        for (Hit<User> hit : hits.hits()) {
            System.out.println(hit.source());
        }
    }

    // 10. 聚合查询
    @Test
    public void testAggregationTest() throws IOException {
        SearchResponse<User> response = esClient.search(
                builder -> builder.index("users")
                        .aggregations("maxAge", a1 -> a1 //<2>
                                .dateHistogram(h -> h
                                        .field("@timestamp")
                                        .calendarInterval(CalendarInterval.Hour)
                                )
                                .aggregations("max", a2 -> a2
                                        .max(m -> m.field("age"))
                                )
                        )
                , User.class);

        HitsMetadata<User> hits = response.hits();
        System.out.println(hits.total());
        System.out.println(response.took());

        for (Hit<User> hit : hits.hits()) {
            System.out.println(hit.source());
        }
    }

    // 11. 分组查询
    @Test
    public void testhistogramTest() throws IOException {
        SearchResponse<User> response = esClient.search(
                builder -> builder.index("users")
                        .aggregations("ageGroup", a1 -> a1
                                .histogram(h -> h.field("age")))
                , User.class);

        HitsMetadata<User> hits = response.hits();
        System.out.println(hits.total());
        System.out.println(response.took());

        for (Hit<User> hit : hits.hits()) {
            System.out.println(hit.source());
        }
    }
}
