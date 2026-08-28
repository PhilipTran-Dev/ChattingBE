package com.ChattingApp.ChattingAppBackend.Config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class MongoConfig {
    private static final String MONGO_URI = "mongodb+srv://phuclnoucamp5002942_db_user:9WAgFhonGR1JeMkf@tingting.sspibcu.mongodb.net/chat_db_fix?retryWrites=true&w=majority&appName=TingTing";
    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(MONGO_URI);
    }
    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory(MongoClient mongoClient) {
        return new SimpleMongoClientDatabaseFactory(mongoClient, "TingTing");
    }
    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDatabaseFactory) {
        return new MongoTemplate(mongoDatabaseFactory);
    }
}