# universe-moon
>愿我如星君如月，夜夜流光相皎洁

----


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