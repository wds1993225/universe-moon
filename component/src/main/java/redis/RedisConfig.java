package redis;

import config.ComponentConfig;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Connection;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author moon
 *         <p>
 *         <p>
 *         redis 的基本配置
 */

@Slf4j
public class RedisConfig {

    /**
     * 饿汉单例模式， 类创建的时候就已经创建了一个静态对象
     * 线程安全
     */
    private static final RedisConfig redisConfig = new RedisConfig();

    private static JedisPool jedisPool;

    private RedisConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(10);
        jedisPool = new JedisPool(config, ComponentConfig.redisHost, ComponentConfig.redisPort);
        log.debug("JedisPoolConfig and JedisPool init finished , host is : {} , port is : {}",
                ComponentConfig.redisHost, ComponentConfig.redisPort);
    }

    public static RedisConfig getRedisConfig() {
        return redisConfig;
    }


    /**
     * 获得一个Jedis对象
     */
    public Jedis getJedis() {
        return jedisPool.getResource();
    }
}
