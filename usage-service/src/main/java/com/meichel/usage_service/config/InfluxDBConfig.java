package com.meichel.usage_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.influxdb.v3.client.InfluxDBClient;

@Configuration
public class InfluxDBConfig {

    @Value("${influxdb.host}")
    private String host;

    @Value("${influxdb.token}")
    private char[] token;

    @Value("${influxdb.database}")
    private String database;

    @Bean
    public InfluxDBClient influxDBClient() {
        InfluxDBClient client = InfluxDBClient.getInstance(host, token, database);
        return client;
    }

}
