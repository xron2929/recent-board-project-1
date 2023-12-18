package com.example.demo.mongo;


import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Arrays;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {
    public MongoConfig(@Value("${spring.data.mongodb.database}") String mongoDatabase,
                       @Value("${spring.data.mongodb.test.connectionString}") String connectionString) {

        this.mongoDatabase = mongoDatabase;
        this.connectionString = connectionString;
    }



    private String mongoDatabase;
    private String connectionString;

    @Override
    protected String getDatabaseName() {
        return mongoDatabase;
    }


    @Override
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(this.connectionString);
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1).build();
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(serverApi)
                .build();
        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), mongoDatabase);
    }

}

