package com.keensense.common.config;

import com.alibaba.fastjson.JSON;
import com.keensense.common.annotation.VIID;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author ycl
 * @date 2019/5/10
 */
public class HandlerMethodArgumentResolver1400 implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        int parameterIndex = methodParameter.getParameterIndex();
        if (parameterIndex != 0) {
            return false;
        }
        Annotation[] annotations = methodParameter.getDeclaringClass().getAnnotations();
        Annotation[] parameterAnnotations = methodParameter.getMethod().getDeclaredAnnotations();
        for (Annotation anno : parameterAnnotations) {
            if (anno.annotationType() == GetMapping.class ||
                    anno.annotationType() == DeleteMapping.class) {
                return false;
            }
        }

        for (Annotation anno : annotations) {
            if (anno.annotationType() == VIID.class) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        ServletWebRequest servletWebRequest = (ServletWebRequest) nativeWebRequest;
        HttpServletRequest request = servletWebRequest.getRequest();
        // 获取输入流
        InputStream inputStream = request.getInputStream();
        //转string
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        String s = result.toString("UTF-8");
        Method method = methodParameter.getMethod();
        if (method == null) {
            return null;
        }

        Class<?> clz = method.getParameterTypes()[0];
        if (clz == null) {
            return null;
        } else if (clz == List.class) {
            //外层数组前后区间
            int i1 = s.indexOf('[');
            int i2 = s.lastIndexOf(']');
            Type[] types = method.getGenericParameterTypes();
            Type[] typeArguments = ((ParameterizedType) types[0]).getActualTypeArguments();
            Type type = typeArguments[0];
            String subs = s.substring(i1, i2 + 1);
            return JSON.parseArray(subs, (Class) type);
        } else {
            s = s.substring(s.indexOf('{', s.indexOf('{') + 1), s.lastIndexOf('}'));
            return JSON.parseObject(s, clz);
        }

    }

}


