# dubbo-lhf
仿dubbo轻量级RPC框架，基于Netty,Zookeeper,SpringBoot

### 分层设计

### 使用教程

使用方式和dubbo相同
#### 1、在项目中引入依赖
```xml
<dependency>
    <groupId>com.lhf.dubbo</groupId>
    <artifactId>dubbo-config-spring</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

#### 2、标注启动注解

SpringBoot项目的启动类添加`@EnableDubbo`注解;

RPC服务接口标注`@RpcService(interfaceClass = HelloService.class)`

#### 3、参数配置

配置zk注册中心地址和dubbo服务暴露的接口即可

```yml
dubbo:
    registry:
        connect-string: 192.168.42.100:2181,192.168.42.101:2181,192.168.42.102:2181
    protocol:
        port: 9090
```
创作不易，靓仔靓女给个star吧！！
具体可参考dubbo-test测试模块
欢迎关注公众号`小锋coding`留言交流！
