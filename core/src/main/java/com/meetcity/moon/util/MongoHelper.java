package com.meetcity.moon.util;

import com.meetcity.moon.epk.Com;
import com.meetcity.moon.epk.Company;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


/**
 * MongoDB操作工具
 * <p>
 * <p>
 * how to use it?
 * <p>
 * eg:
 * <p>
 * MongoHelper mongoHelper = new MongoHelper(Com.MONGODB_IP, Com.MONGODB_PORT, Com.DATABASE_NAME);
 * MongoCollection<Company> collection = mongoHelper.getCollection(Com.COLLECTION_COMPANY, Company.class);
 * Company company = new Company();
 * company.id ="121";
 * collection.insertOne(company);
 */
public class MongoHelper {

    private static final Logger logger = LoggerFactory
            .getLogger(MongoHelper.class);

    private String ip = "";
    private int port = 0;
    private String DBName = "";


    /**
     * 初始化MongoDB客户端
     *
     * @param ip     服务器地址  eg: "192.168.3.69"
     * @param port   端口   eg: "27017"
     * @param DBName 数据库名   eg "admin:
     */
    public MongoHelper(String ip, int port, String DBName) {
        this.ip = ip;
        this.port = port;
        this.DBName = DBName;
    }


    /**
     * 没有复用MongoClient，仅支持单次
     */
    private MongoClient getMongoClient() {
        MongoClient mongoClient = null;
        try {
            // 连接到 mongo 服务
            CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                    fromProviders(PojoCodecProvider.builder().automatic(true).build()));
            mongoClient = new MongoClient(ip + ":" + String.valueOf(port), MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());
            logger.debug("Connect to mongo successfully");
            logger.debug("ip is {} , port is {} , database is {}", ip, port, DBName);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return mongoClient;
    }

    /**
     * 获取一个MongoDB的Collection
     *
     * @param collectionName 集合名称
     * @param documentClass  pojo类型
     */
    public <T> MongoCollection<T> getCollection(String collectionName, Class<T> documentClass) {
        MongoClient client = this.getMongoClient();
        MongoDatabase database = client.getDatabase(DBName);
        return database.getCollection(collectionName, documentClass);
    }


    @Deprecated
    private MongoDatabase getMongoDataBase(MongoClient mongoClient) {
        MongoDatabase mongoDataBase = null;
        try {
            if (mongoClient != null) {
                // 连接到数据库
                mongoDataBase = mongoClient.getDatabase(DBName);
                logger.debug("Connect to DataBase successfully");
            } else {
                throw new RuntimeException("MongoClient不能够为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mongoDataBase;
    }


    public void closeMongoClient(MongoDatabase mongoDataBase,
                                 MongoClient mongoClient) {
        if (mongoDataBase != null) {
            mongoDataBase = null;
        }
        if (mongoClient != null) {
            mongoClient.close();
        }
        logger.debug("CloseMongoClient successfully");
    }


}
