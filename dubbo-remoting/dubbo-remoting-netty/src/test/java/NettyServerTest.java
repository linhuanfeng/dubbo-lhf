import com.lhf.dubbo.common.bean.URL;

public class NettyServerTest {
    public static void main(String[] args) {
        URL url = new URL();
        url.setHost("127.0.0.1");
        url.setPort(8068);
        try {
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
