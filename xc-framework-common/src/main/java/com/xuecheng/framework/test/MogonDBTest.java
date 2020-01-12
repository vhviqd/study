package com.xuecheng.framework.test;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

class MogonDBTest {

    public static void main(String[] args) {
        //创建mongodb 客户端
        MongoClient mongoClient = new MongoClient("localhost", 27017);

        //或者采用连接字符串
        //MongoClientURI connectionString = new
//        MongoClientURI("mongodb://root:root@localhost:27017");

        //MongoClient mongoClient = new MongoClient(connectionString);
        //连接数据库
        MongoDatabase database = mongoClient.getDatabase("xc_cms");
        // 连接collection
        MongoCollection<Document> collection = database.getCollection("cms_page");
        //查询第一个文档
        Document myDoc = collection.find().first();

        //得到文件内容 json串
        String json = myDoc.toJson();
        System.out.println(json);
    }

}
