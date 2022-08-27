/*
 * Licensed to Elasticsearch B.V. under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch B.V. licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.newland.api_conventions;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.CalendarInterval;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.json.JsonData;
import com.newland.utils.ElasticsearchClientUtils;
import org.junit.Test;

import java.io.*;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 加载json测试
 */
public class LoadingJsonTest {
    private ElasticsearchClient client= ElasticsearchClientUtils.getClient();

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * 索引type结构定义
     * @throws IOException
     */
    @Test
    public void loadIndexDefinition() throws IOException {

        //tag::load-index
        InputStream input =new FileInputStream("some-index.json"); //<1>

        CreateIndexRequest req = CreateIndexRequest.of(b -> b
            .index("some-index")
            .withJson(input) //<2>
        );

        boolean created = client.indices().create(req).acknowledged();
        //end::load-index
    }

    @Test
    public void query1() throws IOException {
        //tag::query
        Reader queryJson = new StringReader(
            "{" +
            "  \"query\": {" +
            "    \"range\": {" +
            "      \"@timestamp\": {" +
            "        \"gt\": \"now-1w\"" +
            "      }" +
            "    }" +
            "  }" +
            "}");

        SearchRequest aggRequest = SearchRequest.of(b -> b
            .withJson(queryJson) //<1>
            .aggregations("max-cpu", a1 -> a1 //<2>
                .dateHistogram(h -> h
                    .field("@timestamp")
                    .calendarInterval(CalendarInterval.Hour)
                )
                .aggregations("max", a2 -> a2
                    .max(m -> m.field("host.cpu.usage"))
                )
            )
            .size(0)
        );

        Map<String, Aggregate> aggs = client
            .search(aggRequest, Void.class) //<3>
            .aggregations();
        //end::query
    }

    @Test
    public void query2() throws IOException {
        //tag::query-and-agg
        Reader queryJson = new StringReader(
            "{" +
            "  \"query\": {" +
            "    \"range\": {" +
            "      \"@timestamp\": {" +
            "        \"gt\": \"now-1w\"" +
            "      }" +
            "    }" +
            "  }," +
            "  \"size\": 100" + //<1>
            "}");

        Reader aggregationJson = new StringReader(
            "{" +
            "  \"size\": 0, " + //<2>
            "  \"aggregations\": {" +
            "    \"hours\": {" +
            "      \"date_histogram\": {" +
            "        \"field\": \"@timestamp\"," +
            "        \"interval\": \"hour\"" +
            "      }," +
            "      \"aggregations\": {" +
            "        \"max-cpu\": {" +
            "          \"max\": {" +
            "            \"field\": \"host.cpu.usage\"" +
            "          }" +
            "        }" +
            "      }" +
            "    }" +
            "  }" +
            "}");

        SearchRequest aggRequest = SearchRequest.of(b -> b
            .withJson(queryJson) //<3>
            .withJson(aggregationJson) //<4>
            .ignoreUnavailable(true) //<5>
        );

        Map<String, Aggregate> aggs = client
            .search(aggRequest, Void.class)
            .aggregations();
        //end::query-and-agg
    }
}
