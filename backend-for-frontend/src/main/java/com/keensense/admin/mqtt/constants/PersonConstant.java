package com.keensense.admin.mqtt.constants;

import java.util.HashMap;
import java.util.Map;

public class PersonConstant {

    /**
     * 附属物：是否戴口罩
     */
    public final static Map<Integer,String> RESPIRATOR = new HashMap<>();
    static {
        RESPIRATOR.put(-1,"未知");
        RESPIRATOR.put(0,"没戴口罩");
        RESPIRATOR.put(1,"有戴口罩");
    }

    /**
     * 上衣纹理
     */
    public final static Map<Integer,String> COATTEXTURE = new HashMap<>();
    static {
        COATTEXTURE.put(-1,"未知");
        COATTEXTURE.put(1,"净色");
        COATTEXTURE.put(2,"间条");
        COATTEXTURE.put(3,"格子");
        COATTEXTURE.put(4,"图案");
        COATTEXTURE.put(5,"拼接");
    }

    /**
     * 附属物：是否戴帽子
     */
    public final static Map<Integer,String> CAP = new HashMap<>();
    static {
        CAP.put(-1,"未知");
        CAP.put(1,"没戴帽子");
        CAP.put(2,"有戴帽子");
    }

    /**
     * 发型
     */
    public final static Map<Integer,String> HAIRSTYLE = new HashMap<>();
    static {
        HAIRSTYLE.put(-1,"未知");
        HAIRSTYLE.put(1,"长发");
        HAIRSTYLE.put(2,"短发");
    }

    /**
     * 下衣纹理
     */
    public final static Map<Integer,String> TROUSERSTEXTURE = new HashMap<>();
    static {
        TROUSERSTEXTURE.put(-1,"未知");
        TROUSERSTEXTURE.put(1,"净色");
        TROUSERSTEXTURE.put(2,"间条");
        TROUSERSTEXTURE.put(3,"图案");
    }

    /**
     * 附属物：是否有拉杆箱
     */
    public final static Map<Integer,String> LUGGAGE = new HashMap<>();
    static {
        LUGGAGE.put(-1,"未知");
        LUGGAGE.put(0,"没有拉杆箱");
        LUGGAGE.put(1,"有拉杆箱");
    }

    /**
     * 附属物：是否有手推车
     */
    public final static Map<Integer,String> TROLLEY = new HashMap<>();
    static {
        TROLLEY.put(-1,"未知");
        TROLLEY.put(0,"没有手推车");
        TROLLEY.put(1,"有手推车");
    }

    /**
     * 性别
     */
    public final static Map<Integer,String> SEX = new HashMap<>();
    static {
        SEX.put(-1,"未知");
        SEX.put(1,"男性");
        SEX.put(2,"女性");
    }

    /**
     * 附属物：是否背包
     */
    public final static Map<Integer,String> BAG = new HashMap<>();
    static {
        BAG.put(-1,"未知");
        BAG.put(0,"没背包");
        BAG.put(1,"有背包");
    }

    /**
     * 附属物：是否有手提包
     */
    public final static Map<Integer,String> CARRYBAG = new HashMap<>();
    static {
        CARRYBAG.put(-1,"未知");
        CARRYBAG.put(0,"没手提包");
        CARRYBAG.put(1,"有手提包");
    }

    /**
     * 附属物：是否戴眼镜
     */
    public final static Map<Integer,String> GLASSES = new HashMap<>();
    static {
        GLASSES.put(-1,"未知");
        GLASSES.put(0,"没戴眼镜");
        GLASSES.put(1,"有戴眼镜");
    }

    /**
     * 附属物：是否打伞
     */
    public final static Map<Integer,String> UMBRELLA = new HashMap<>();
    static {
        UMBRELLA.put(-1,"未知");
        UMBRELLA.put(0,"没打伞");
        UMBRELLA.put(1,"有打伞");
    }

}
