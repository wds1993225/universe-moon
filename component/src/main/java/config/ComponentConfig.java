package config;

/**
 * Created by wds on 2017/12/27.
 *
 * @author moon
 * <p>
 * 组件的统一配置文件
 * 包含地址，端口
 */
public class ComponentConfig {

    /**
     * redis
     */
    public static String redisHost = "192.168.3.67";
    public static int redisPort = 6379;

    /**
     * zooKeeper
     */
    public static String zooKeeperHost = "localhost";
    public static String zooKeeperPort = "2181";

    /**
     * kafka
     */
    public static String kafkaHost = "192.168.56.14";
    public static String kafkaPort = "9092";


    /**
     * mongo
     */
    public static String mongoDBHost = "192.168.3.69";
    public static String mongoDBPort = "27017";
    public static String mongoDBName = "admin";

}
