# universe-moon
>愿我如星君如月，夜夜流光相皎洁

----

### Architecture

![](https://github.com/wds1993225/universe-moon/blob/master/MoonNo.4.png)


### CORE
>包含基本的工具，包含爬虫框架

### COMPONENT
>包含对redis，zookeeper的操作和配置

### WEB
>提供web服务


依赖关系应该为

component ---> core ---> web

----

### core 包含爬虫项目
> 每一个模块都是一个服务，所有的服务都注册到Zookeeper进行统一管理和扩展。


#### 组件

##### Zookeeper
> 管理所有的服务，支持负责均衡和热插拔。

##### Administor
> 负责管理整个系统的运行，通过控制ZooKeeper来控制系统服务的启动和停止，及提供系统运行的参数。

##### Monitor
> 系统的监控，通过连接ZooKeeper来监控系统运行情况，以及通过邮箱的形式进行警报。监控数据应该上传到ElasticSearch中形成运行报告。

##### Logger
> 系统运行日志，包括ZooKeeper生产的日志和各个服务生产的日志，上传到ElasticSearch中进行统计。

##### SeedRepo
> 保存种子任务，种子的构建需要以一个更加解耦，灵活的方式构建，以便配个各个组件。

##### URLManager
> 负责URL的重要性排序，分发，调度，任务分配。
一般来说，一个爬取任务中包含几千到一万个URL，这些URL最好是来自不同的host，这样，不会给一个host在很短一段时间内造成高峰值。

##### Message Queue
> 用于传递生产出的种子任务，连接Producer和Work。需要根据种子的构建方式进行特定构建，支持不同种子通道，支持任务消费失败后的重新消费，支持任务消费成功后的上报。

##### Result Queue
> 传递下载或解析后的数据。可以支持多种类型的数据，支持多种类型的消费者。

##### DB
> 用于保存文件，原始文件推荐使用HDFS，解析后的结构化数据推荐MongoDB。

##### Work

###### 

----

##### 启动方式:

    MoonTask moonParam = new MoonTask("http://www.baidu.com");
    SpiderCore spiderCore = new SpiderCore(moonParam, new HttpDownloader(), null, null);
    spiderCore.start();


##### 任务队列:

    默认的blockQueue
    扩展的RedisQueue

    队列中存放请求和处理的基本参数 {@see:MoonTask}

##### 去重方式：

    通过redis去重，去重使用的key : MD5(url+参数)

##### 针对反爬
    
    默认使用随机UserAgent，可以配置中关闭
    
### TODO
    
    上传失败策略
    完善日志工具
    修改代理池
    现在所有的工具类都为线程不安全（DateUtil,HttpUtil..）