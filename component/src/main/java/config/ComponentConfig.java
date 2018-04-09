package config;

/**
 * Created by wds on 2017/12/27.
 *
 * @author moon
 *         <p>
 *         组件的统一配置文件
 *         包含地址，端口
 */
public class ComponentConfig {

    /**
     * redis
     */
    public static String redisHost = "localhost";
    public static int redisPort = 6379;

    /**
     * zooKeeper
     */
    public static String zooKeeperHost = "localhost";
    public static String zooKeeperPort = "2181";


    public static String kafkaHost = "192.168.56.14";
    public static String kafkaPort = "";


}
