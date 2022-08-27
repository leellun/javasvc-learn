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

package com.newland.usage;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.json.JsonData;
import com.newland.utils.ElasticsearchClientUtils;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.logging.Logger;

/**
 * 索引操作
 */
public class IndexingTest  {
    private ElasticsearchClient esClient= ElasticsearchClientUtils.getClient();

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    IndexResponse result = IndexResponse.of(r -> r
        .index("product")
        .id("bk-1")
        .version(1)
        .primaryTerm(1)
        .seqNo(1)
        .result(Result.Created)
        .shards(s -> s.total(1).successful(1).failed(0))
    );

    /**
     * 插入索引
     * @throws Exception
     */
    @Test
    public void singleDocumentDSL() throws Exception {

        //tag::single-doc-dsl
        Product product = new Product("bk-1", "City bike", 123.0);

        IndexResponse response = esClient.index(i -> i
            .index("products")
            .id(product.getSku())
            .document(product)
        );

        logger.info("Indexed with version " + response.version());
    }

    /**
     * 通过IndexRequest 插入索引
     * @throws Exception
     */
    @Test
    public void singleDocumentDSLwithOf() throws Exception {
        //tag::single-doc-dsl-of
        Product product = new Product("bk-1", "City bike", 123.0);

        IndexRequest<Product> request = IndexRequest.of(i -> i
            .index("products")
            .id(product.getSku())
            .document(product)
        );

        IndexResponse response = esClient.index(request);

        logger.info("Indexed with version " + response.version());
        //end::single-doc-dsl-of
    }

    @Test
    public void singleDocumentBuilder() throws Exception {
        //tag::single-doc-builder
        Product product = new Product("bk-1", "City bike", 123.0);

        IndexRequest.Builder<Product> indexReqBuilder = new IndexRequest.Builder<>();
        indexReqBuilder.index("product");
        indexReqBuilder.id(product.getSku());
        indexReqBuilder.document(product);

        IndexResponse response = esClient.index(indexReqBuilder.build());

        logger.info("Indexed with version " + response.version());
        //end::single-doc-builder
    }

    /**
     * 异步插入索引ElasticsearchAsyncClient
     * @throws Exception
     */
    @Test
    public void singleDocumentDSLAsync() throws Exception {
        //tag::single-doc-dsl-async
        ElasticsearchAsyncClient esAsyncClient = new ElasticsearchAsyncClient(ElasticsearchClientUtils.getTransport());

        Product product = new Product("bk-1", "City bike", 123.0);

        esAsyncClient.index(i -> i
            .index("products")
            .id(product.getSku())
            .document(product)
        ).whenComplete((response, exception) -> {
            if (exception != null) {
                logger.info("Failed to index"+ exception);
            } else {
                logger.info("Indexed with version " + response.version());
            }
        });
        //end::single-doc-dsl-async
        Thread.sleep(10*1000);
    }

    /**
     * 通过json创建索引
     * @throws Exception
     */
    @Test
    public void singleDocumentJson() throws Exception {
        //tag::single-doc-json
        Reader input = new StringReader(
            "{'@timestamp': '2022-04-08T13:55:32Z', 'level': 'warn', 'message': 'Some log message'}"
            .replace('\'', '"'));

        IndexRequest<JsonData> request = IndexRequest.of(i -> i
            .index("logs")
            .withJson(input)
        );

        IndexResponse response = esClient.index(request);

        logger.info("Indexed with version " + response.version());
        //end::single-doc-json
    }
}
