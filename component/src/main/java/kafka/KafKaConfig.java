package kafka;

import config.ComponentConfig;
import lombok.extern.slf4j.Slf4j;
import redis.RedisConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Properties;

/**
 * Created by moon on 2018/3/30 .
 * <p>
 * kafka配置
 */
@Slf4j
public class KafKaConfig {

    private static final KafKaConfig kafKaConfig = new KafKaConfig();

    private static JedisPool jedisPool;

    private KafKaConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(10);
        jedisPool = new JedisPool(config, ComponentConfig.redisHost, ComponentConfig.redisPort);
        log.debug("JedisPoolConfig and JedisPool init finished , host is : {} , port is : {}",
                ComponentConfig.redisHost, ComponentConfig.redisPort);
    }

    public static KafKaConfig getKafKaConfig() {
        return kafKaConfig;
    }


    /**
     * 获得一个Jedis对象
     */
    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    private static Properties initProperties() {
        return null;
    }

}
