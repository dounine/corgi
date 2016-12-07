package rpc.test_jta;

import com.dounine.corgi.jta.filter.impl.JTAApiFilterImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by huanghuanlai on 2016/11/30.
 */
@Aspect
@Component
public class MyJTAApiFilter extends JTAApiFilterImpl {

    @Around("execution(* rpc.test_jta.code..*.*(..))")
    public Object aroundMethod(ProceedingJoinPoint pjd) throws Throwable {
        return super.aroundMethod(pjd);
    }

}
