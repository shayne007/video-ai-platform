package com.keensense.sdk.jni;

public class U2sRecogNativeSingle
{
    private static U2sRecogNative recog = null;

    public static byte[] GetFeature(int objType, byte[] picture)
    {
        if (null == recog)
        {
            return null;
        }
        return recog.GetFeature(objType, picture);
    }

    public static String ObjectDetectionOnImage(int objType, byte[] picture)
    {
        if (null == recog)
        {
            return null;
        }
        return recog.ObjectDetectionOnImage(objType, picture);
    }
}
