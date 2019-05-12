package mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import config.ComponentConfig;
import lombok.extern.slf4j.Slf4j;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


/**
 * mongodb工具类
 * <p>
 * MongoClient 是线程安全的
 * <p>
 * <p>
 * How to user it ?
 * eg:
 * <p>
 * MongoCollection<Company> collection = MongoConfig.getCollection(Com.COLLECTION_COMPANY, Company.class);
 * Company company = new Company();
 * company.id = "121121";
 * collection.insertOne(company);
 */
@Slf4j
public class MongoConfig {


    private static final MongoConfig mongoConfig = new MongoConfig();

    private static MongoClient mongoClient;

    private MongoConfig() {
        try {
            // 连接到 mongo 服务
            CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                    fromProviders(PojoCodecProvider.builder().automatic(true).build()));
            mongoClient = new MongoClient(ComponentConfig.mongoDBHost + ":" + ComponentConfig.mongoDBPort, MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());
            log.debug("Connect to mongo successfully");
            log.debug("ip is {} , port is {} , database is {}", ComponentConfig.mongoDBHost, ComponentConfig.mongoDBPort, ComponentConfig.mongoDBName);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * 获取配置对象
     */
    public static MongoConfig getMongoConfig() {
        return mongoConfig;
    }

    /**
     * 获取一个MongoDatabase对象
     */
    public static MongoDatabase getDatabase(String DatabaseName) {
        return mongoClient.getDatabase(DatabaseName);
    }


    /**
     * 获取一个Collection对象
     *
     * @param collectionName 集合名称
     * @param clazz          操作对象类型
     */
    public static <T> MongoCollection<T> getCollection(String collectionName, Class<T> clazz) {
        return mongoClient.getDatabase(ComponentConfig.mongoDBName).getCollection(collectionName, clazz);
    }
}
