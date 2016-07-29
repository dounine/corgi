package corgi.rpc.invoke;

import com.dounine.fasthttp.medias.IMedia;

import java.io.OutputStream;

/**
 * 请求与响应接口
 */
public interface Invoke {

    /**
     * 拉取数据
     * @return 响应内容
     */
    String fetch(IMedia media, String url );

    /**
     * 推送数据
     * @param data 数据
     * @param out 输出流
     */
    void push(Object data, OutputStream out);

}
