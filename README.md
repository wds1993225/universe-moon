# universe-moon
>愿我如星君如月，夜夜流光相皎洁

----

## Architecture

![](https://github.com/wds1993225/universe-moon/blob/master/MoonNo.4.png)

## component-dependency
    
    Eden:包含基本的工具，包含爬虫框架
    Component:包含对redis，zookeeper等的操作和配置
    WEB:提供web服务
    Core:单机爬虫
    
    Metis:自己学习的笔记
    
    
    依赖关系应该为: component --> core --> web






----

### Eden 包含爬虫项目
> 每一个模块都是一个服务，所有的服务都注册到Zookeeper进行统一管理和扩展。


### 组成:



* #### SeedRepo
    > 保存种子任务，种子的构建需要以一个更加解耦，灵活的方式构建，以便配合各个组件。

* #### URLManager
    > 负责URL的重要性排序，分发，调度，任务分配。
一般来说，一个爬取任务中包含几千到一万个URL，这些URL最好是来自不同的host，这样，不会给一个host在很短一段时间内造成高峰值。

* #### Task Queue
    > 用于传递生产出的种子任务，连接```Producer```和```Worker```。需要根据种子的构建方式进行特定构建，支持不同种子通道，支持任务消费失败后的重新消费，支持任务消费成功后的上报。

* #### Result Queue
    > 传递下载或解析后的数据。可以支持多种类型的数据，支持多种类型的消费者。

* #### Storage
    > 用于保存文件，原始文件推荐使用```HDFS```，解析后的结构化数据推荐```MongoDB```。
* #### Duplicate removal
    > 用于去重，通常使用```Redis```+```Bloom filter```实现，在添加任务前要知道任务是否被执行过。
* #### Worker
    > 承担整个消费者的主要任务，包括下载，解析，上传三个部分。

    * ##### Downloader
        > 承担整个```Worker```的网络请求工作，会根据不同的种子任务进行相应的例如操作```模拟浏览器```，执行 ```JavaScript```脚本等，配合常见的反爬工具例如```代理Ip```，```验证码识别```等。
        下载器应该有多种不同的实现，包含通用的接口，这里希望使用Golang进行实现。
    * ##### Processor
        > 承担```Worker```的解析工作，包括将网页解析成结构化的文本，或者从原始数据中解析出新的```URL```进行进一步的请求。
        解析器应该支持多种解析规则，包括```XPath```,```正则表达式```，```CSS选择器```。
    * ##### Uploader
        > 承担```Worker```的上传操作，会将解析的结果推入```Result Queue```中。
        上传器应该可以配合种子任务中的各种上传规则进行相应队列的上传操作。
        
* #### Zookeeper
    >管理所有的服务，支持负责均衡和热插拔。

* #### Administrator
    > 负责管理整个系统的运行，通过控制```ZooKeeper```来控制系统服务的启动和停止，及提供系统运行的参数。

* #### Logger
    > 保存系统运行的情况，日志一方面来自```ZooKeeper```，一方面来自各个服务。
    数据最终会上报到```ElasticSearch```中进行日志的收集。
    
* #### Monitor
    > 监控的数据主要来自于```ZooKeeper```，目的就是为了监控各个服务的运行情况，如果系统出现问题就会进行邮件报警。
    监控数据最终会收集到```ElasticSearch```中，配合```Grafana```进行视图展示。



----

#### 启动方式:

    MoonTask moonParam = new MoonTask("http://www.baidu.com");
    SpiderCore spiderCore = new SpiderCore(moonParam, new HttpDownloader(), null, null);
    spiderCore.start();


#### 任务队列:

    默认的blockQueue
    扩展的RedisQueue

    队列中存放请求和处理的基本参数 {@see:MoonTask}

#### 去重方式：

    通过redis去重，去重使用的key : MD5(url+参数)

#### 针对反爬
    
    默认使用随机UserAgent，可以配置中关闭
    
### TODO
    
    上传失败策略
    完善日志工具
    修改代理池
    现在所有的工具类都为线程不安全（DateUtil,HttpUtil..）