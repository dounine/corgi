package corgi.exception;

import com.alibaba.fastjson.JSON;
import corgi.commons.ResponseText;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by huanghuanlai on 16/4/23.
 */
public class ExceptionHandler extends AbstractHandlerExceptionResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);
    private static final int COMMON_ERROR_CODE = 3;

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if(ex instanceof SerException){
            ResponseText responseText = new ResponseText();
            responseText.setErrno(COMMON_ERROR_CODE);
            responseText.setMsg(ex.getMessage());
            response.setCharacterEncoding("utf-8");
            Object resp = null;
            String callback = request.getParameter("callback");
            if(StringUtils.isNotBlank(callback)){
                resp = new StringBuffer(callback).append("(").append(JSON.toJSON(responseText)).append(")");
            }else{
                resp = JSON.toJSON(responseText);
            }
            try {
                response.getWriter().print(resp);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ModelAndView();
        }
        return null;
    }
}
