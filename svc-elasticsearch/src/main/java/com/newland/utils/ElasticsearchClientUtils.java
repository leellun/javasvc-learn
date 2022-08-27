package com.newland.utils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import javax.net.ssl.SSLContext;

/**
 * Author: leell
 * Date: 2022/8/27 09:50:52
 */
public class ElasticsearchClientUtils {
    private static ElasticsearchClient client;
    static ElasticsearchTransport transport;
    static {
        try {
            RestClientBuilder builder = RestClient.builder(
                    new HttpHost("192.168.66.11", 9200, "https"));
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (TrustStrategy) (chain, authType) -> true).build();
            SSLIOSessionStrategy sessionStrategy = new SSLIOSessionStrategy(sslContext, NoopHostnameVerifier.INSTANCE);
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials("leellun", "leellun"));

            builder.setHttpClientConfigCallback(httpClientBuilder -> {
                httpClientBuilder.disableAuthCaching();
                httpClientBuilder.setSSLStrategy(sessionStrategy);
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                return httpClientBuilder;
            });

            RestClient restClient = builder.build();
            transport = new RestClientTransport(
                    restClient, new JacksonJsonpMapper());
            client = new ElasticsearchClient(transport);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ElasticsearchTransport getTransport() {
        return transport;
    }

    public static ElasticsearchClient getClient() {
        return client;
    }
}
