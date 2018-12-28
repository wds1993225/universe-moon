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
 * <p>
 * modify on 2018/12/27
 * <p>
 * Moon begin to finish this code , just for fun.
 */
@Slf4j
public class KafKaConfig {

    private static final KafKaConfig kafKaConfig = new KafKaConfig();

    private static Properties producerProperties;

    private static Properties consumerProperties;

    private KafKaConfig() {

        //Producer的配置
        producerProperties = new Properties();
        //bootstrap.servers是Kafka集群的IP地址
        // properties.put("bootstrap.servers","192.168.1.110:9092,192.168.1.110:9092");  //多机器
        producerProperties.put("bootstrap.servers", ComponentConfig.kafkaHost + ":" + ComponentConfig.kafkaPort);
        producerProperties.put("acks", "all");
        producerProperties.put("retries", 0);
        producerProperties.put("batch.size", 16384);
        producerProperties.put("linger.ms", 1);
        producerProperties.put("buffer.memory", 33554432);
        //序列化类型   Kafka消息是以键值对的形式发送到Kafka集群的,其中Key是可选的，Value可以是任意类型
        //Message被发送到Kafka集群之前，Producer需要把不同类型的消息序列化为二进制类型　
        producerProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        log.debug("KafKaConfig properties init finished , host is : {} , port is : {}",
                ComponentConfig.kafkaHost, ComponentConfig.kafkaPort);


        //Consumer的配置
        consumerProperties = new Properties();
        consumerProperties.put("bootstrap.servers", ComponentConfig.kafkaHost + ":" + ComponentConfig.kafkaPort);
        //Consumer分组ID
        consumerProperties.put("group.id", "group-1");
        consumerProperties.put("enable.auto.commit", "true");
        consumerProperties.put("auto.commit.interval.ms", "1000");
        consumerProperties.put("auto.offset.reset", "earliest");
        consumerProperties.put("session.timeout.ms", "30000");
        consumerProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

    }

    public static KafKaConfig getKafKaConfig() {
        return kafKaConfig;
    }


    /**
     * 获得一个Kafka properties对象
     */
    public Properties getProducerProperties() {
        return producerProperties;
    }

    public Properties getConsumerProperties() {
        return consumerProperties;
    }

    private static Properties initProperties() {
        return null;
    }

}
