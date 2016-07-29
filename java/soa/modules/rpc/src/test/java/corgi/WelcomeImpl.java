package corgi;

/**
 * Created by huanghuanlai on 16/7/28.
 */
public class WelcomeImpl implements IWelcome {
    @Override
    public String hello() {
        return "你好,我是通过RPC调用返回内容!!!";
    }
}
