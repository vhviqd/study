package com.xuecheng.manage_cms.config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {
    @Value("${spring.data.mongodb.database}")
    String db;

    /**
     * 创建好这个bean之后，就可以在任何地方进行注入：
     * @param mongoClient
     * @return
     */
    @Bean
    public GridFSBucket getGridFSBucket(MongoClient mongoClient){
        MongoDatabase database = mongoClient.getDatabase(db);
        //GridFSBucket用于打开下载流对象
        GridFSBucket gridFSBucket = GridFSBuckets.create(database);
        return gridFSBucket;
    }

}
