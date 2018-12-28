package kafka;

import kafka.server.QuotaType;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;


/**
 * 操作kafka的工具类
 */
@Slf4j
public class KafkaUtil {

    /**
     * 获取一个kafka producer
     */
    public static KafkaProducer<String, String> getProducer() {
        Properties properties = KafKaConfig.getKafKaConfig().getProducerProperties();
        log.debug("get a producer success");
        return new KafkaProducer<String, String>(properties);
    }

    /**
     * 获取一个kafka consumer
     */
    public static KafkaConsumer<String, String> getConsumer() {
        Properties properties = KafKaConfig.getKafKaConfig().getConsumerProperties();
        log.debug("get a consumer success");
        return new KafkaConsumer<String, String>(properties);
    }


    /**
     * 发送消息
     *
     * @param topic    kafka的topic
     * @param msg      要发送的消息
     * @param producer 生产者
     */
    public static void sendMsg(String topic, String msg, KafkaProducer<String, String> producer) {
        producer.send(new ProducerRecord<String, String>(topic, msg));
        log.debug("sendMsg , topic is [{}] , msg is [{}]", topic, msg);
    }

    /**
     * 消费消息
     *
     * @param topic    订阅主题
     * @param timeout  超时时间，consumer等待的超时时间，直到kafka中没有消息
     * @param consumer 消费者
     */
    public static void consume(String topic, long timeout, KafkaConsumer<String, String> consumer) {
        //Consumer订阅了Topic为topic的消息
        consumer.subscribe(Arrays.asList(topic));
        //调用poll方法来轮循Kafka集群的消息,Consumer等待timeout,直到Kafka集群中没有消息为止
        ConsumerRecords<String, String> records = consumer.poll(timeout);
        for (ConsumerRecord record : records) {
            log.debug("offset is [{}] , value is [{}]", record.offset(), record.value());
        }
    }


}
