package redis;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * @author moon
 *         <p>
 *         <p>
 *         redis 的操作封装
 */
@Slf4j
public class RedisUtil {


    /**
     * 操作SET集
     * <p>
     * 判断 一个元素 是否在 一个SET 中
     *
     * @param setName SET集合的名字
     * @param value   要判断的值
     */
    public static boolean isExistInSET(String setName, String value) {
        Jedis jedis = null;
        boolean isExist = false;
        try {
            jedis = RedisConfig.getRedisConfig().getJedis();
            isExist = jedis.sismember(setName, value);
            log.debug("SETName is : {} , value is : {} , value status is : {}", setName, value, isExist);
        } catch (Throwable t) {
            t.printStackTrace();
            log.error("setOperate error , setName is : {}  , value is : {} , error is :", setName, value, t);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return isExist;
    }

    /**
     * 操作SET集
     *
     * @param setName SET集合的名字
     * @param value   要加入SET集合的值
     * @return 插入成功返回 1，已有数据 0 ，插入失败
     */
    public static Long setOperate(String setName, String value) {
        Jedis jedis = null;
        Long insertSuccess = null;
        try {
            jedis = RedisConfig.getRedisConfig().getJedis();
            insertSuccess = jedis.sadd(setName, value);
            log.debug("SETName is : {} , set value is : {} , set status is : {} ", setName, value, insertSuccess);
        } catch (Throwable t) {
            t.printStackTrace();
            log.error("setOperate error , setName is : {}  , value is : {} , error is :  ", setName, value, t);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return insertSuccess;
    }


    /**
     * 从列表的头部弹出一个值
     * <p>
     * 阻塞队列
     */
    public static String blPop(String listName) {
        Jedis jedis = null;
        String value = null;
        try {
            jedis = RedisConfig.getRedisConfig().getJedis();
            List<String> list = jedis.blpop(0, listName);
            if (list != null) {
                value = list.get(1);        //list(0) 是列表名 ， list(1)是元素名
                log.debug("blPop get a value  , listName is : {}  , value is : {}  ", listName, value);
            } else {
                log.debug("blPop don t get any , list is null , listName is : {} ", listName);
            }
        } catch (Throwable t) {
            t.printStackTrace();
            log.error("setOperate error , listName is : {} , error is :  ", listName, t);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return value;
    }


    /**
     * 将值加入列表尾部
     */
    public static void RPush(String listName, String value) {
        Jedis jedis = null;
        Long listSize = null;
        try {
            jedis = RedisConfig.getRedisConfig().getJedis();
            listSize = jedis.rpush(listName, value);
            log.debug("a value Rpush into redis , value is : {} , list name is : {} , size is : {} ", value, listName, listSize);
        } catch (Throwable t) {
            t.printStackTrace();
            log.error("setOperate error , listName is : {}  , value is : {} , error is : ", listName, value, t);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 返回指定 LIST 的长度
     */
    public static Long lLen(String listName) {
        Jedis jedis = null;
        Long listSize = null;
        try {
            jedis = RedisConfig.getRedisConfig().getJedis();
            listSize = jedis.llen(listName);
            log.debug("current list name is : {} , size is : {} ", listName, listSize);
        } catch (Throwable t) {
            t.printStackTrace();
            log.error("setOperate error , listName is : {}  , error is : ", listName, t);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return listSize;
    }


    public static void setString(String key, String value) {

        Jedis jedis = null;
        try {
            jedis = RedisConfig.getRedisConfig().getJedis();
            jedis.set(key, value);
        } catch (Throwable t) {
            t.printStackTrace();
            log.error("set error : key is : {} , value is : {} , error is :", key, value, t);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }

    public static String getString(String key) {
        String value = null;
        Jedis jedis = null;
        try {
            jedis = RedisConfig.getRedisConfig().getJedis();
            value = jedis.get(key);
        } catch (Throwable t) {
            t.printStackTrace();
            log.error("get error : key is : {} , error is : ", key, t);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return value;
    }


}
