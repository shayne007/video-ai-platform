package com.keensense.search.constant;

import com.keensense.search.domain.FaceResult;
import com.keensense.search.domain.NonMotorVehiclesResult;
import com.keensense.search.domain.PersonResult;
import com.keensense.search.domain.VlprResult;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhanx xiaohui on 2019-07-22.
 */
public class MethodConst {
    private MethodConst(){}

    protected static final Map<String, Method> faceMethod = new HashMap<>();
    protected static final Map<String, Method> personMethod = new HashMap<>();
    protected static final Map<String, Method> nonmotorMethod = new HashMap<>();
    protected static final Map<String, Method> motorMethod = new HashMap<>();

    static{
        for(Method method : FaceResult.class.getMethods()){
            faceMethod.put(method.getName().toLowerCase(), method);
        }
        for(Method method : FaceResult.class.getSuperclass().getMethods()){
            faceMethod.put(method.getName().toLowerCase(), method);
        }
        for(Method method : PersonResult.class.getMethods()){
            personMethod.put(method.getName().toLowerCase(), method);
        }
        for(Method method : PersonResult.class.getSuperclass().getMethods()){
            personMethod.put(method.getName().toLowerCase(), method);
        }
        for(Method method : NonMotorVehiclesResult.class.getMethods()){
            nonmotorMethod.put(method.getName().toLowerCase(), method);
        }
        for(Method method : NonMotorVehiclesResult.class.getSuperclass().getMethods()){
            nonmotorMethod.put(method.getName().toLowerCase(), method);
        }
        for(Method method : VlprResult.class.getMethods()){
            motorMethod.put(method.getName().toLowerCase(), method);
        }
        for(Method method : VlprResult.class.getSuperclass().getMethods()){
            motorMethod.put(method.getName().toLowerCase(), method);
        }
    }
}

/**
 * @program: platform
 * @description:
 * @author: zhan xiaohui
 * @create: 2019-07-22 13:21
 **/