package com.keensense.job.async.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @ClassName: SerializationUtils
 * @Description: 序列化工具类
 * @Author: cuiss
 * @CreateDate: 2019/8/10 13:32
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Slf4j
public class SerializationUtils {

    /**
     * 序列化对象
     * @param serializable
     * @param outputStream
     */
    public static void serialize(Serializable serializable, OutputStream outputStream){
        if(outputStream == null){
            log.error(">>>>error:outputstream must not be null.......");
            throw new IllegalArgumentException("outputstream must not be null......");
        }else{
            ObjectOutputStream objectOutputStream = null;
            try{
                objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(serializable);
            }catch (IOException e){
                e.printStackTrace();;
                log.error(">>>>IOException:"+e.getMessage());
            }finally{
                if(objectOutputStream != null){
                    try{
                        objectOutputStream.close();
                    }catch (IOException e){
                        log.error("IOException:关闭流异常");
                    }
                }
            }
        }
    }

    /***
     * 对象序列化成字节数组
     * @param serializable
     * @return
     */
    public static byte[] serialize(Serializable serializable){
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        serialize(serializable,baos);
        return baos.toByteArray();
    }

    /**
     * 反序列化对象
     * @param inputStream
     * @return
     */
    public static Object deserialize(InputStream inputStream){
        ObjectInputStream objectInputStream = null;
        Object object = null;
        if(inputStream == null){
            log.error(">>>>error:inputstream must not be null.......");
            throw new IllegalArgumentException("inputstream must not be null......");
        }else{
            try{
                objectInputStream = new ObjectInputStream(inputStream);
                object = objectInputStream.readObject();
            }catch (Exception e){
                e.printStackTrace();;
                log.error(">>>>IOException:"+e.getMessage());
            }finally{
                if(objectInputStream != null){
                    try{
                        objectInputStream.close();
                    }catch (IOException e){
                        log.error("IOException:关闭流异常");
                    }
                }
            }
        }
        return object;
    }

    /**
     * 反序列化字节数组
     * @param objectData
     * @return
     */
    public static Object deserialize(byte[] objectData){
        Object object = null;
        if(objectData == null){
            log.error(">>>>error:objectData must not be null.......");
            throw new IllegalArgumentException("objectData must not be null......");
        }else{
            ByteArrayInputStream bais = new ByteArrayInputStream(objectData);
            object = deserialize(bais);
        }
        return object;
    }

}
