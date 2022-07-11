import com.lhf.dubbo.common.bean.URL;
import com.lhf.dubbo.rpc.RpcInvocation;

public class NettyClientTest {
    public static void main(String[] args) {
        URL url = new URL();
        url.setHost("114.132.42.100");
        url.setPort(8068);
        try {
            RpcInvocation invocation = new RpcInvocation();
            invocation.setMethodName("sayHello");
            invocation.setInterfaceName("com.lhf.dubbo.config.spring.Service");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
