package com.dounine.corgi.validation;

import com.alibaba.fastjson.JSON;
import com.dounine.corgi.commons.ResponseText;
import com.dounine.corgi.jsonp.Callback;
import com.dounine.corgi.response.ResponseContext;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GlobalValidation {

    private static final int COMMON_ERROR_CODE = 3;

    @Pointcut("execution(* com.dounine.corgi..web..*.*(..))")
    protected void pointCut() {
    }

    @Around("pointCut()")
    protected Object around(ProceedingJoinPoint pjp) throws Throwable {
        BindingResult result = null;
        String callbackFun = null;//jsonp callback method
        MethodSignature methodSignature = (MethodSignature) pjp
                .getSignature();
        Method method = methodSignature.getMethod();
        Annotation[] annotationList = method.getAnnotations();
        Annotation[][] argAnnotations = method.getParameterAnnotations();
        String[] argNames = methodSignature.getParameterNames();
        Object[] args = pjp.getArgs();
        int length = args!=null?args.length:0;
        if (length!=0) {
            for (int i = 1; i < length; i++) {
                Object object = args[i];
                if (null != object) {
                    if (null == result && object instanceof BindingResult) {
                        result = (BindingResult) object;
                    }
                    if (null != argAnnotations[i] && argAnnotations[i].length > 0) {//检测是否带注解
                        if (null != argAnnotations[i][0]
                                && argAnnotations[i][0] instanceof Callback
                                && null != object
                                && StringUtils.isNotBlank(object.toString())) {//回调函数不能为空
                            callbackFun = object.toString();
                        }
                        if (null != result && null != callbackFun) {//两个都找到了,可以退出循环
                            break;
                        }
                    }
                }
            }
        }

        if (writeResult(result, callbackFun)) {
            return null;
        }

        return pjp.proceed(args);
    }


    protected boolean writeResult(BindingResult result, String callbackFun) {
        if (result != null && result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            if (null != fieldErrors && fieldErrors.size() > 0) {
                ResponseText responseText = new ResponseText();
                List<ValidRep> validReps = new ArrayList<>();
                for (FieldError fieldError : fieldErrors) {
                    ValidRep vr = new ValidRep();
                    vr.setCode(fieldError.getCode());
                    vr.setField(fieldError.getField());
                    vr.setMsg(fieldError.getDefaultMessage());
                    validReps.add(vr);
                }
                validReps = validReps.stream().sorted((a, b) -> {//排序,阻止每次验证提示信息不一致
                    return b.getField().compareTo(a.getField());
                }).collect(Collectors.toList());
                if (null != callbackFun) {
                    StringBuilder sb = new StringBuilder(callbackFun);
                    sb.append("(");
                    sb.append(JSON.toJSON(validReps));//TODO 对象内容不多,但是使用json转换时间过长,建议专门针对此对象列表写一个转换器
                    sb.append(")");
                    ResponseContext.writeData(sb);
                } else {
                    responseText.setData(validReps);
                    responseText.setCode(COMMON_ERROR_CODE);
                    ResponseContext.writeData(responseText);
                }
                return true;
            }
        }
        return false;
    }
}
