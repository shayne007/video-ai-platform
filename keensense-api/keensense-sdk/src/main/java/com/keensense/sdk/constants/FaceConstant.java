package com.keensense.sdk.constants;

import com.keensense.common.config.SpringContext;
import com.keensense.sdk.algorithm.IBodySdkInvoke;
import com.keensense.sdk.algorithm.IFaceSdkInvoke;
import com.keensense.sdk.algorithm.impl.KsFaceSdkInvokeImpl;
import com.keensense.sdk.algorithm.impl.QstBodySdkInvokeImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/***
 * @description:
 * @author jingege
 * @return:
 */
@Slf4j
public class FaceConstant
{

    public static final String FEATURE_LIB_TYPE = "1024";

    public static final String BACK_LIB_TYPE = "1023";

    public static final String SIDE_LIB_TYPE = "1022";

    public static final String VIRTUAL_LIB_TYPE = "virtual_library";

    public static final Integer FACE_MIN_PIXEL = 20;

    public static final Integer OBJ_TYPE = 3;

    public static final Integer DEFAULT_PAGESIZE = 100;

    public static final Integer MAX_PAGESIZE = 5000;

    public static final Float LIMIT_SIMILAR = 0.7F;

    public static final Integer LIMIT_NUMBER = 100;
    public static final Integer BACK_LIMIT_NUMBER = 100;

    public static final String FEATURE_URL = "http://127.0.0.1:20280/rest/feature/extractFromPicture";

    public static final String IMAGE_SEARCH_URL = "http://127.0.0.1:39081/";

    public static final String FEATURE_EXTRACT_TYPE = "JNI";

    public static final String SEARCH_START_AT = "2015-01-01 00:00:00";

    public static final String SUCCESS_FLAG = "1";

    public static final String FAILED_FLAG = "2";

    public static final String RESP_CODE = "200";

    public static final Integer SDK_QST = 1;

    public static final Integer SDK_YC = 2;

    private static IFaceSdkInvoke iFaceSdkInvoke;
    public static final String KSAPP_VERSION = "v4/";
    private static String SERVICE_URL = "http://127.0.0.1:8080/" + KSAPP_VERSION;
    public static String FEATURE_VERSION = "";
    public static String FEATURE_VERSION_SEARCH = "";

    public static boolean setFaceSdkInvoke(String classpath)
    {
//        Object tHandler = ReflectUtil.newInstance(classpath);
//        if (tHandler instanceof IFaceSdkInvoke)
//        {
//            iFaceSdkInvoke = (IFaceSdkInvoke) tHandler;
//            return true;
//        }
        return false;
    }

    public static IFaceSdkInvoke getFaceSdkInvoke()
    {
        if (null == iFaceSdkInvoke)
        {
            reloadFeatureVersion(SERVICE_URL);
            iFaceSdkInvoke = SpringContext.getBean(KsFaceSdkInvokeImpl.class);
            Map<String,Object> var = new HashMap<>();
            var.put("faceServiceUrl", SERVICE_URL);
            var.put("staticFeatureVersion", FEATURE_VERSION_SEARCH);
            var.put("monitorFeatureVersion", FEATURE_VERSION);
            var.put("monitorFeatureVersion", FEATURE_VERSION);
            iFaceSdkInvoke.initParams(var);
            
        }
        return iFaceSdkInvoke;
    }

    public static void reloadFeatureVersion(String url)
    {
        String resp = "";
//        try
//        {
//            resp = HttpGetUtil.request(url + "params", "");
//        }
//        catch (HttpConnectionException e)
//        {
//            log.error("reloadFeatureVersion error",e);
//        }
//        if (StringUtil.isNull(resp)) {
//            return;
//        }
//        Var respVar = Var.fromJson(resp);
//        Var paramArr = respVar.getArray("paramGroups[0].params");
//        StringBuffer staticFeatureVersion = new StringBuffer();
//        StringBuffer monitorFeatureVersion = new StringBuffer();
//        paramArr.foreach(new IVarForeachHandler() {
//
//            private static final long serialVersionUID = 1L;
//
//            @Override
//            public void execute(String index, Var tmpVar)
//            {
//                if ("defaultStaticFeatureVersion"
//                        .equals(tmpVar.getString("key")))
//                {
//                    staticFeatureVersion.append(tmpVar.getString("value"));
//                }
//                if ("defaultMonitorFeatureVersion"
//                        .equals(tmpVar.getString("key")))
//                {
//                    monitorFeatureVersion.append(tmpVar.getString("value"));
//                }
//            }
//        });
//        if (0 < staticFeatureVersion.length())
//        {
//            FEATURE_VERSION_SEARCH = staticFeatureVersion.toString();
//        }
//        if (0 < monitorFeatureVersion.length())
//        {
//            FEATURE_VERSION = monitorFeatureVersion.toString();
//        }
    }
}
