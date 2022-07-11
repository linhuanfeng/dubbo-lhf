import com.lhf.dubbo.common.bean.RpcRequest;
import com.lhf.dubbo.common.bean.URL;


public class NettyClientTest {
    public static void main(String[] args) {
        URL url = new URL();
        url.setHost("127.0.0.1");
        url.setPort(8069);
        try {
            RpcRequest request = new RpcRequest();
            request.setMethodName("eat");
            request.setInterfaceName("com.lhf.dubbo.config.spring.api.EatService");
            request.setArguments(new Object[]{"uuuuuuuuu"});
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
