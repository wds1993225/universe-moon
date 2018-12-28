package config;

import kafka.KafKaConfig;
import redis.RedisConfig;
import redis.clients.jedis.Jedis;

public class ComponentStart {

    public static void main(String[] args) {
        Jedis jedis = RedisConfig.getRedisConfig().getJedis();
        jedis.set("haha","abcn");
        String a = jedis.get("haha");
        System.out.println("a");
    }

    public void kafkaConfigTest(){

    }
}
