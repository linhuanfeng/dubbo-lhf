import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.remoting.Channel;
import com.lhf.dubbo.remoting.ExchangeChannel;
import com.lhf.dubbo.remoting.exchange.ExchangeHandler;
import com.lhf.dubbo.remoting.exchange.ExchangeHandlerAdapter;
import com.lhf.dubbo.remoting.transport.netty.NettyClient;
import com.lhf.dubbo.remoting.transport.netty.NettyServer;
import com.lhf.dubbo.rpc.RpcInvocation;

import java.util.concurrent.CompletableFuture;

public class NettyClientTest {
    public static void main(String[] args) {
        ExchangeHandler requestHandler = new ExchangeHandlerAdapter() {
            @Override
            public Object reply(Channel channel, Object message) {
                return null;
            }

            @Override
            public void connected(Channel channel) {

            }

            @Override
            public void disconnected(Channel channel) {

            }

            @Override
            public void sent(Channel channel, Object message) {
                // 接受到信息
                System.out.println("====================");
                System.out.println(channel+""+message);
            }

            @Override
            public void received(Channel channel, Object message) {

            }

            @Override
            public void caught(Channel channel, Throwable exception) {

            }
        };
        URL url = new URL();
        url.setHost("114.132.42.100");
        url.setPort(8068);
        try {
            NettyClient nettyClient = new NettyClient(url, requestHandler);
            RpcInvocation invocation = new RpcInvocation();
            invocation.setMethodName("sayHello");
            invocation.setInterfaceName("com.lhf.dubbo.config.spring.Service");
            nettyClient.write(invocation);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
