package com.keensense.common.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.keensense.common.annotation.VIID;
import com.keensense.common.util.Messenger;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author ycl
 * @date 2019/5/10
 */
public class HandlerMethodReturnValueHandler1400 implements HandlerMethodReturnValueHandler {

    private final HandlerMethodReturnValueHandler delegate;

    public HandlerMethodReturnValueHandler1400(HandlerMethodReturnValueHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean supportsReturnType(MethodParameter methodParameter) {
        Annotation[] annotations = methodParameter.getDeclaringClass().getAnnotations();
        for (Annotation anno : annotations) {
            if (anno.annotationType() == VIID.class) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void handleReturnValue(Object o, MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception {
        Method method = methodParameter.getMethod();
        if (method == null) {
            return;
        }
        JSONObject jo = new JSONObject();
        Type type = method.getGenericReturnType();
        if (type instanceof ParameterizedType) {
            // 强制转型为带参数的泛型类型，
            Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
            String[] pathNames = typeArguments[0].getTypeName().split("\\.");
            String name = pathNames[pathNames.length - 1];
            JSONObject objList = new JSONObject();
            objList.put(name + "Object", o);
            //list-size
            JSONArray jsonArr = (JSONArray) JSON.toJSON(o);
            if (jsonArr != null && !jsonArr.isEmpty()) {
                objList.put("Count", Integer.valueOf(Messenger.acceptMsg()));
            }
            jo.put(name + "ListObject", objList);
        } else {
            String[] pathNames = type.getTypeName().split("\\.");
            String name = pathNames[pathNames.length-1];
            jo.put(name + "Object", o);
        }
        delegate.handleReturnValue(jo, methodParameter, modelAndViewContainer, nativeWebRequest);
    }
}
