package corgi.response;

import com.alibaba.fastjson.JSON;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by huanghuanlai on 16/6/20.
 */
public final class ResponseContext {

    private ResponseContext(){}

    public static HttpServletResponse get(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    public static void writeData(Object data){
        HttpServletResponse response = get();
        response.setCharacterEncoding("utf-8");
        try {
            if(data instanceof String || data instanceof StringBuffer || data instanceof StringBuilder){
                response.getWriter().print(data.toString());
            }else{
                response.getWriter().print(JSON.toJSON(data));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
