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

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetResponse;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.newland.utils.ElasticsearchClientUtils;
import org.junit.Test;

import java.util.logging.Logger;

public class ReadingTest {
    private ElasticsearchClient esClient= ElasticsearchClientUtils.getClient();

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Test
    public void getById() throws Exception {
        //tag::get-by-id
        GetResponse<Product> response = esClient.get(g -> g
            .index("products") // <1>
            .id("bk-1"),
            Product.class      // <2>
        );

        if (response.found()) {
            Product product = response.source();
            logger.info("Product name " + product.getName());
        } else {
            logger.info ("Product not found");
        }
        //end::get-by-id

    }

    @Test
    public void getByIdJson() throws Exception {
        //tag::get-by-id-json
        GetResponse<ObjectNode> response = esClient.get(g -> g
            .index("products")
            .id("bk-1"),
            ObjectNode.class     // <1>
        );

        if (response.found()) {
            ObjectNode json = response.source();
            String name = json.get("name").asText();
            logger.info("Product name " + name);
        } else {
            logger.info("Product not found");
        }
        //end::get-by-id-json
    }
}
