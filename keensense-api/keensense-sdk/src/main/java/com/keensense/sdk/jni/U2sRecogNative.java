package com.keensense.sdk.jni;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

@Slf4j
public class U2sRecogNative
{

    static
    {
        if (SystemUtil.isLinux)
        {
            System.loadLibrary("VideoAnalysisSDK");
            System.loadLibrary("ImageRecog_jni");
        }
    }
    
    public native int Init(String sdkParam);
    
    public native int InitByGpuCore(int gpuCoreNum);

    public native byte[] GetFeature(int objType, byte[] picture);
    
    public native float CompareFeature(byte[] feature1, byte[] feature2);
    
    /**
     * @param type 1-jpg;2-png
     * @param picture 二进制流
     * @return
     */
    public native String ObjectDetectionOnImage(int type, byte[] picture);

    public native int Release(String sdkParam);
    
    public static void main(String[] args) throws FileNotFoundException, IOException
    {
        U2sRecogNative recog = new U2sRecogNative();
        recog.Init("{}");
        
        // 读取图片
        // /home/jnitest/java/pic1.jpg
        byte[] pic1By = fileToBytes("/home/jnitest/java/pic1.jpg");
//        byte[] pic1Feature = recog.GetFeature(1, pic1By);
//        // IOUtils.write(pic1Feature, new FileOutputStream(new File("/home/jnitest/java/pic1.bin")));
//        System.out.println("----get feature1 ok." + pic1Feature.length);
//        
//        byte[] pic2By = fileToBytes("/home/jnitest/java/pic2.jpg");
//        byte[] pic2Feature = recog.GetFeature(1, pic2By);
//        System.out.println("----get feature2 ok." + pic2Feature.length);
//        
//        
//        float distance0 = recog.CompareFeature(pic1Feature, pic1Feature);
//        System.out.println("----compare pic1 and pic1 ok." + distance0);
//        
//        float distance1 = recog.CompareFeature(pic1Feature, pic2Feature);
//        System.out.println("----compare pic1 and pic2 ok." + distance1);
        
        // 对图片的目标进行结构化
        String str = recog.ObjectDetectionOnImage(1, pic1By);
        System.out.println("-----" + str);
        
        recog.Release("{}");
    }
    
    private static byte[] fileToBytes(String filepath)
    {
        InputStream is = null;
        byte[] retBy = null;
        try
        {
            is = new FileInputStream(new File(filepath));
            retBy = IOUtils.toByteArray(is);
            IOUtils.closeQuietly(is);
        }
        catch (Exception e)
        {
           log.error("fileToBytes error",e);
           retBy = null;
        }
        return retBy;
    }
}
