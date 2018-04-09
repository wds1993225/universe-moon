package zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

/**
 * Created by wds on 2017/12/27.
 *
 * @author moon
 */
@Slf4j
public class ZooKeeperUtil {


    /**
     * 创建新节点
     *
     * @param path 路径
     * @param data 节点数据
     */
    public static void createNode(String path, Object data, ZkClient zkClient) {
        try {
            if (!isNodeExist(path, zkClient)) {
                String newPath = zkClient.create(path, data, CreateMode.PERSISTENT);
                log.debug("new node create success , path is : {} ", newPath);
            } else {
                log.debug("node exist , path is : {} ", path);
            }
        } catch (Throwable t) {
            log.error("create node error , path is : {} , error is : ", path, t);
        }

    }

    /**
     * 删除一个节点
     *
     * @param path 节点路径
     */
    public static void deleteNode(String path, ZkClient zkClient) {
        try {
            if (isNodeExist(path, zkClient)) {
                boolean isSuccess = zkClient.delete(path);
                log.debug("a node delete success , path is : {} ", path);
            } else {
                log.debug("node is not exist , path is : {} ", path);
            }
        } catch (Throwable t) {
            log.error("delete node error , path is : {} , error is : ", path, t);
        }

    }

    /**
     * 更新节点数据
     *
     * @param path 节点路径
     */
    public static Object getDataFromNode(String path, ZkClient zkClient) {
        Object data = null;
        try {
            if (isNodeExist(path, zkClient)) {
                data = zkClient.readData(path);
                log.debug("get data success , data is : {} ", String.valueOf(data));
            } else {
                log.debug("node is not exist , path is : {} ", path);
            }
        } catch (Throwable t) {
            log.error("get data error , path is : {} , error is : ", path, t);
        }
        return data;
    }

    /**
     * 更新节点数据
     *
     * @param path 节点路径
     * @param data 节点数据
     */
    public static void upDataFromNode(String path, Object data, ZkClient zkClient) {
        try {
            if (isNodeExist(path, zkClient)) {
                zkClient.writeData(path, data);
            } else {
                log.debug("node is not exist , path is : {} ,data is : {} ", path, String.valueOf(data));
            }
        } catch (Throwable t) {
            log.error("updata error ,path is : {} , data is : {} , error is :", path, data, t);
        }
    }


    /**
     * 判断节点是否存在
     *
     * @param path 节点路径
     */
    public static boolean isNodeExist(String path, ZkClient zkClient) {

        return zkClient.exists(path);
    }


}
