package zookeeper;

import config.ComponentConfig;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by wds on 2017/12/26.
 *
 * @author moon
 *         <p>
 *         zooKeeper客户端初始化及建立连接
 */
@Slf4j
public class ZooKeeperConfig {


    private static final ZooKeeperConfig zooKeeperConfig = new ZooKeeperConfig();
    private static ZkClient zkClient;


    private ZooKeeperConfig() {
        zkClient = new ZkClient(ComponentConfig.zooKeeperHost + ":" + ComponentConfig.zooKeeperPort,
                2000, 2000, new SerializableSerializer());
        log.debug("zKClient init finished , host is : {} , port is : {} ",
                ComponentConfig.zooKeeperHost, ComponentConfig.zooKeeperPort);
    }

    public static ZooKeeperConfig getZooKeeperConfig() {
        return zooKeeperConfig;
    }

    /**
     * 获取一个zkClient客户端
     */
    public static ZkClient getZkClient() {
        return zkClient;
    }
}
