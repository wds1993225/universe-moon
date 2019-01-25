package com.moon.util;


/**
 * http util
 * <p>
 * 支持代理
 * <p>
 * 不使用连接池
 * <p>
 * 与{@linkplain HttpLoader}的区别：
 * <p>
 * HttpClient与服务器建立连接后，会维护一个TCP的连接，避免反复建立连接导致资源消耗
 * 所以{@linkplain HttpLoader}中会维护一个连接池，复用HttpClient
 * <p>
 * 但是如果使用代理服务器，每次请求都需要将请求发送到不同服务器，所以每次建立连接后都要断开连接，
 * （要发送到不同服务器），因此不需要维护连接池，每次都需要重新实例化HttpClient。
 * <p>
 * {@see http://hc.apache.org/httpcomponents-client-4.5.x/tutorial/html/connmgmt.html#d5e374}
 */
public class HttpProxyLoader {


}
