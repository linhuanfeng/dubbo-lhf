# dubbo-lhf
仿dubbo轻量级RPC框架，基于Netty,Zookeeper,SpringBoot
### 使用教程
使用方式dubbo相同
#### 1、首先创建一个SpringBoot项目，引入依赖
```xml
<dependency>
    <groupId>com.lhf.dubbo</groupId>
    <artifactId>dubbo-config-spring</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 2、启动类标注解`@EnableDubbo`,服务接口标注`@RpcService(interfaceClass = HelloService.class)`
### 3、配置zk注册中心地址和dubbo服务暴露的接口即可
```yml
dubbo:
    registry:
        connect-string: 192.168.42.100:2181,192.168.42.101:2181,192.168.42.102:2181
    protocol:
        port: 9090
```
创作不易，靓仔靓女给个star吧！！
完工！
具体可参考dubbo-test测试模块
