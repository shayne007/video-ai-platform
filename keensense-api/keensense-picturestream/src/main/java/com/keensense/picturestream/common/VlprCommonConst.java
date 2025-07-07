package com.keensense.picturestream.common;

import com.loocme.security.encrypt.Base64;
import com.loocme.sys.util.ByteUtil;

public class VlprCommonConst {

    private VlprCommonConst(){}

    /**从GSTL获取特征添加固定头*/
    private static final byte[] GSTL = {'G','L','S','T'};
    private static final byte[] timestamp = {0,0,0,0,0,0,0,0};
    private static final byte[] version =  ByteUtil.int2ByteLow(143,4);

    public static String getGlstFeature(String feature){
        byte[] featureByte = Base64.decode(feature.getBytes());
        byte[] result = new byte[featureByte.length+version.length+GSTL.length+timestamp.length];
        System.arraycopy(GSTL,0,result,0,GSTL.length);
        System.arraycopy(version,0,result,GSTL.length,version.length);
        System.arraycopy(timestamp,0,result,GSTL.length+version.length,timestamp.length);
        System.arraycopy(featureByte,0,result,GSTL.length+version.length+timestamp.length,featureByte.length);
        return new String(Base64.encode(result));
    }
}
