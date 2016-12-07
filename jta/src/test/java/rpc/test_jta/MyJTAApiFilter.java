package rpc.test_jta;

import com.dounine.corgi.jta.impl.JTAApiFilterImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

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
