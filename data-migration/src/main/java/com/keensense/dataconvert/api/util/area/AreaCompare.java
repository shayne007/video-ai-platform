package com.keensense.dataconvert.api.util.area;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.api.util.area
 * @Description： <p> AreaCompare  </p>
 * @Author： - Jason
 * @CreatTime：2019/7/25 - 20:14
 * @Modify By：
 * @ModifyTime： 2019/7/25
 * @Modify marker：
 */
public class AreaCompare {

    /**
     * 所处索引
     */
    private int index;


    /**
     * 用于分组排序
     */
    private String imageId;


    /**
     * 宽
     */
    private int w;

    /**
     * 高
     */
    private int h;

    /**
     * (x,)
     */
    private int x;

    /**
     * (,y)
     */
    private int y;


    /**
     * 构造函数
     * @param index
     * @param w
     * @param h
     * @param x
     * @param y
     */
    public AreaCompare(int index, int w, int h, int x, int y) {
        this.index = index;
        this.w = w;
        this.h = h;
        this.x = x;
        this.y = y;
    }


    /**
     * 用于分组排序 取出imageId 对应的组的 面积最大的特征数据
     * @param index
     * @param imageId
     * @param w
     * @param h
     */
    public AreaCompare(int index, String imageId, int w, int h) {
        this.index = index;
        this.imageId = imageId;
        this.w = w;
        this.h = h;
    }

    /**
     * 构造函数
     * @param index
     * @param w
     * @param h
     */
    public AreaCompare(int index, int w, int h) {
        this.index = index;
        this.w = w;
        this.h = h;
    }


    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getW() {
        return w;
    }

    public void setW(Integer w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * 计算面积
     * @return
     */
    public  int getCountArea(){
        return w*h;
    }
}
