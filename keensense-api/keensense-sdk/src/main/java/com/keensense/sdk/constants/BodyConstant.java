package com.keensense.sdk.constants;

import com.keensense.common.config.SpringContext;
import com.keensense.sdk.algorithm.IBodySdkInvoke;
import com.keensense.sdk.algorithm.impl.QstBodySdkInvokeImpl;
import com.loocme.sys.datastruct.Var;
import com.loocme.sys.util.ReflectUtil;

import org.springframework.beans.factory.annotation.Autowired;
/**
 * @description:
 * @author jingege
 * @return:
 */

public class BodyConstant
{

    private static IBodySdkInvoke iBodySdkInvoke;
    public static final int BODY_TYPE = 1;
    private static final String SERVICE_URL = "http://127.0.0.1:39081/";

    public static boolean setBodySdkInvoke(String classpath)
    {
        Object tHandler = ReflectUtil.newInstance(classpath);
        if (tHandler instanceof IBodySdkInvoke)
        {
            iBodySdkInvoke = (IBodySdkInvoke) tHandler;
            return true;
        }
        return false;
    }

    public static IBodySdkInvoke getBodySdkInvoke()
    {
        if (null == iBodySdkInvoke){
            iBodySdkInvoke = SpringContext.getBean(QstBodySdkInvokeImpl.class);
            Var var = Var.newObject();
            var.set("bodyServiceUrl", SERVICE_URL);
            iBodySdkInvoke.initParams(var);
        }
        return iBodySdkInvoke;
    }
}
