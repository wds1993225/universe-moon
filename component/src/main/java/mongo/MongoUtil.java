package mongo;


import com.mongodb.MongoWriteException;
import lombok.extern.slf4j.Slf4j;

/**
 * mongodb操作的一些封装
 */
@Slf4j
public class MongoUtil {


    /**
     * 这里未做对象类型校验
     * <p>
     * 插入一个对象
     */
    public static <T> void insert(String collectionName, Class<T> clazz, Object o) {
        try {
            MongoConfig.getCollection(collectionName, clazz).insertOne((T) o);
            log.debug("insert success , collection name is {} , object is {}", collectionName, o);
        } catch (MongoWriteException e) {
            e.printStackTrace();
            // error code 为11000时 :(duplicate key error collection)重复主键，插入失败
            if (e.getCode() != 11000) {
                log.error("insert error ", e);
                //这里不抛出去，让程序继续执行
                throw e;
            } else {
                log.debug("key duplicate , object is {}", o);
            }
        }

    }
}
