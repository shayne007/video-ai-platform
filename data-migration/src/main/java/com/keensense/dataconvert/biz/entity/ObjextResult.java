package com.keensense.dataconvert.biz.entity;

import java.util.Date;

/**
 * @ClassName：ObjextResult
 * @Description： <p> ObjextResult  </p>
 * @Author： - Jason
 * @CreatTime：2019/7/26 - 15:03
 * @Modify By：
 * @ModifyTime： 2019/7/26
 * @Modify marker：
 * @version V1.0
*/
public class ObjextResult {

    /**
     * recog id 转换
     */
    private String idStr;


    /**
     * id
     */
    private Integer id;

    /**
     * serialnumber
     */
    private String serialnumber;

    /**
     * cameraid
     */
    private Long cameraid;

    /**
     * vlprResultId
     */
    private Long vlprResultId;

    /**
     * imgurl
     */
    private String imgurl;

    /**
     * bigimgurl
     */
    private String bigimgurl;

    /**
     * objtype
     */
    private Short objtype;

    /**
     * maincolorTag1
     */
    private Integer maincolorTag1;

    /**
     * maincolorTag2
     */
    private Integer maincolorTag2;

    /**
     * maincolorTag3
     */
    private Integer maincolorTag3;

    /**
     * upcolorTag1
     */
    private Integer upcolorTag1;

    /**
     * upcolorTag2
     */
    private Integer upcolorTag2;

    /**
     * upcolorTag3
     */
    private Integer upcolorTag3;

    /**
     * lowcolorTag1
     */
    private Integer lowcolorTag1;

    /**
     * lowcolorTag2
     */
    private Integer lowcolorTag2;

    /**
     * lowcolorTag3
     */
    private Integer lowcolorTag3;

    /**
     * sex
     */
    private Byte sex;

    /**
     * age
     */
    private Byte age;

    /**
     * wheels
     */
    private Byte wheels;

    /**
     * size
     */
    private Byte size;

    /**
     * bag
     */
    private Byte bag;

    /**
     * handbag
     */
    private Byte handbag;

    /**
     * glasses
     */
    private Byte glasses;

    /**
     * umbrella
     */
    private Byte umbrella;

    /**
     * umbrellaColorTag
     */
    private Integer umbrellaColorTag;

    /**
     * angle
     */
    private Short angle;

    /**
     * tubeid
     */
    private Integer tubeid;

    /**
     * objid
     */
    private Integer objid;

    /**
     * startframeidx
     */
    private Integer startframeidx;

    /**
     * endframeidx
     */
    private Integer endframeidx;

    /**
     * startframepts
     */
    private Integer startframepts;

    /**
     * endframepts
     */
    private Integer endframepts;

    /**
     * frameidx
     */
    private Integer frameidx;

    /**
     * width
     */
    private Short width;

    /**
     * height
     */
    private Short height;

    /**
     * xywh
     */
    private String xywh;

    /**
     * distance
     */
    private Float distance;

    /**
     * faceImgurl
     */
    private String faceImgurl;

    /**
     * peccancy
     */
    private Short peccancy;

    /**
     * bikeColor
     */
    private Integer bikeColor;

    /**
     * bikeGenre
     */
    private Integer bikeGenre;

    /**
     * seatingCount
     */
    private Integer seatingCount;

    /**
     * helmet
     */
    private Byte helmet;

    /**
     * helmetColorTag1
     */
    private Integer helmetColorTag1;

    /**
     * helmetColorTag2
     */
    private Integer helmetColorTag2;

    /**
     * lamShape
     */
    private Short lamShape;

    /**
     * bikeHasPlate
     */
    private Byte bikeHasPlate;

    /**
     * createtime
     */
    private Date createtime;

    /**
     * inserttime
     */
    private Date inserttime;

    /**
     * recogId
     */
    private String recogId;

    /**
     * slaveIp
     */
    private String slaveIp;

    /**
     * coatStyle
     */
    private String coatStyle;

    /**
     * trousersStyle
     */
    private String trousersStyle;

    /**
     * retrycount
     */
    private Integer retrycount;

    /**
     * respirator
     */
    private Integer respirator;

    /**
     * cap
     */
    private Integer cap;

    /**
     * hairStyle
     */
    private Integer hairStyle;

    /**
     * coatTexture
     */
    private Integer coatTexture;

    /**
     * trousersTexture
     */
    private Integer trousersTexture;

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSerialnumber() {
        return serialnumber;
    }

    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber == null ? null : serialnumber.trim();
    }

    public Long getCameraid() {
        return cameraid;
    }

    public void setCameraid(Long cameraid) {
        this.cameraid = cameraid;
    }

    public Long getVlprResultId() {
        return vlprResultId;
    }

    public void setVlprResultId(Long vlprResultId) {
        this.vlprResultId = vlprResultId;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl == null ? null : imgurl.trim();
    }

    public String getBigimgurl() {
        return bigimgurl;
    }

    public void setBigimgurl(String bigimgurl) {
        this.bigimgurl = bigimgurl == null ? null : bigimgurl.trim();
    }

    public Short getObjtype() {
        return objtype;
    }

    public void setObjtype(Short objtype) {
        this.objtype = objtype;
    }

    public Integer getMaincolorTag1() {
        return maincolorTag1;
    }

    public void setMaincolorTag1(Integer maincolorTag1) {
        this.maincolorTag1 = maincolorTag1;
    }

    public Integer getMaincolorTag2() {
        return maincolorTag2;
    }

    public void setMaincolorTag2(Integer maincolorTag2) {
        this.maincolorTag2 = maincolorTag2;
    }

    public Integer getMaincolorTag3() {
        return maincolorTag3;
    }

    public void setMaincolorTag3(Integer maincolorTag3) {
        this.maincolorTag3 = maincolorTag3;
    }

    public Integer getUpcolorTag1() {
        return upcolorTag1;
    }

    public void setUpcolorTag1(Integer upcolorTag1) {
        this.upcolorTag1 = upcolorTag1;
    }

    public Integer getUpcolorTag2() {
        return upcolorTag2;
    }

    public void setUpcolorTag2(Integer upcolorTag2) {
        this.upcolorTag2 = upcolorTag2;
    }

    public Integer getUpcolorTag3() {
        return upcolorTag3;
    }

    public void setUpcolorTag3(Integer upcolorTag3) {
        this.upcolorTag3 = upcolorTag3;
    }

    public Integer getLowcolorTag1() {
        return lowcolorTag1;
    }

    public void setLowcolorTag1(Integer lowcolorTag1) {
        this.lowcolorTag1 = lowcolorTag1;
    }

    public Integer getLowcolorTag2() {
        return lowcolorTag2;
    }

    public void setLowcolorTag2(Integer lowcolorTag2) {
        this.lowcolorTag2 = lowcolorTag2;
    }

    public Integer getLowcolorTag3() {
        return lowcolorTag3;
    }

    public void setLowcolorTag3(Integer lowcolorTag3) {
        this.lowcolorTag3 = lowcolorTag3;
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public Byte getAge() {
        return age;
    }

    public void setAge(Byte age) {
        this.age = age;
    }

    public Byte getWheels() {
        return wheels;
    }

    public void setWheels(Byte wheels) {
        this.wheels = wheels;
    }

    public Byte getSize() {
        return size;
    }

    public void setSize(Byte size) {
        this.size = size;
    }

    public Byte getBag() {
        return bag;
    }

    public void setBag(Byte bag) {
        this.bag = bag;
    }

    public Byte getHandbag() {
        return handbag;
    }

    public void setHandbag(Byte handbag) {
        this.handbag = handbag;
    }

    public Byte getGlasses() {
        return glasses;
    }

    public void setGlasses(Byte glasses) {
        this.glasses = glasses;
    }

    public Byte getUmbrella() {
        return umbrella;
    }

    public void setUmbrella(Byte umbrella) {
        this.umbrella = umbrella;
    }

    public Integer getUmbrellaColorTag() {
        return umbrellaColorTag;
    }

    public void setUmbrellaColorTag(Integer umbrellaColorTag) {
        this.umbrellaColorTag = umbrellaColorTag;
    }

    public Short getAngle() {
        return angle;
    }

    public void setAngle(Short angle) {
        this.angle = angle;
    }

    public Integer getTubeid() {
        return tubeid;
    }

    public void setTubeid(Integer tubeid) {
        this.tubeid = tubeid;
    }

    public Integer getObjid() {
        return objid;
    }

    public void setObjid(Integer objid) {
        this.objid = objid;
    }

    public Integer getStartframeidx() {
        return startframeidx;
    }

    public void setStartframeidx(Integer startframeidx) {
        this.startframeidx = startframeidx;
    }

    public Integer getEndframeidx() {
        return endframeidx;
    }

    public void setEndframeidx(Integer endframeidx) {
        this.endframeidx = endframeidx;
    }

    public Integer getStartframepts() {
        return startframepts;
    }

    public void setStartframepts(Integer startframepts) {
        this.startframepts = startframepts;
    }

    public Integer getEndframepts() {
        return endframepts;
    }

    public void setEndframepts(Integer endframepts) {
        this.endframepts = endframepts;
    }

    public Integer getFrameidx() {
        return frameidx;
    }

    public void setFrameidx(Integer frameidx) {
        this.frameidx = frameidx;
    }

    public Short getWidth() {
        return width;
    }

    public void setWidth(Short width) {
        this.width = width;
    }

    public Short getHeight() {
        return height;
    }

    public void setHeight(Short height) {
        this.height = height;
    }

    public String getXywh() {
        return xywh;
    }

    public void setXywh(String xywh) {
        this.xywh = xywh == null ? null : xywh.trim();
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public String getFaceImgurl() {
        return faceImgurl;
    }

    public void setFaceImgurl(String faceImgurl) {
        this.faceImgurl = faceImgurl == null ? null : faceImgurl.trim();
    }

    public Short getPeccancy() {
        return peccancy;
    }

    public void setPeccancy(Short peccancy) {
        this.peccancy = peccancy;
    }

    public Integer getBikeColor() {
        return bikeColor;
    }

    public void setBikeColor(Integer bikeColor) {
        this.bikeColor = bikeColor;
    }

    public Integer getBikeGenre() {
        return bikeGenre;
    }

    public void setBikeGenre(Integer bikeGenre) {
        this.bikeGenre = bikeGenre;
    }

    public Integer getSeatingCount() {
        return seatingCount;
    }

    public void setSeatingCount(Integer seatingCount) {
        this.seatingCount = seatingCount;
    }

    public Byte getHelmet() {
        return helmet;
    }

    public void setHelmet(Byte helmet) {
        this.helmet = helmet;
    }

    public Integer getHelmetColorTag1() {
        return helmetColorTag1;
    }

    public void setHelmetColorTag1(Integer helmetColorTag1) {
        this.helmetColorTag1 = helmetColorTag1;
    }

    public Integer getHelmetColorTag2() {
        return helmetColorTag2;
    }

    public void setHelmetColorTag2(Integer helmetColorTag2) {
        this.helmetColorTag2 = helmetColorTag2;
    }

    public Short getLamShape() {
        return lamShape;
    }

    public void setLamShape(Short lamShape) {
        this.lamShape = lamShape;
    }

    public Byte getBikeHasPlate() {
        return bikeHasPlate;
    }

    public void setBikeHasPlate(Byte bikeHasPlate) {
        this.bikeHasPlate = bikeHasPlate;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getInserttime() {
        return inserttime;
    }

    public void setInserttime(Date inserttime) {
        this.inserttime = inserttime;
    }

    public String getRecogId() {
        return recogId;
    }

    public void setRecogId(String recogId) {
        this.recogId = recogId == null ? null : recogId.trim();
    }

    public String getSlaveIp() {
        return slaveIp;
    }

    public void setSlaveIp(String slaveIp) {
        this.slaveIp = slaveIp == null ? null : slaveIp.trim();
    }

    public String getCoatStyle() {
        return coatStyle;
    }

    public void setCoatStyle(String coatStyle) {
        this.coatStyle = coatStyle == null ? null : coatStyle.trim();
    }

    public String getTrousersStyle() {
        return trousersStyle;
    }

    public void setTrousersStyle(String trousersStyle) {
        this.trousersStyle = trousersStyle == null ? null : trousersStyle.trim();
    }

    public Integer getRetrycount() {
        return retrycount;
    }

    public void setRetrycount(Integer retrycount) {
        this.retrycount = retrycount;
    }

    public Integer getRespirator() {
        return respirator;
    }

    public void setRespirator(Integer respirator) {
        this.respirator = respirator;
    }

    public Integer getCap() {
        return cap;
    }

    public void setCap(Integer cap) {
        this.cap = cap;
    }

    public Integer getHairStyle() {
        return hairStyle;
    }

    public void setHairStyle(Integer hairStyle) {
        this.hairStyle = hairStyle;
    }

    public Integer getCoatTexture() {
        return coatTexture;
    }

    public void setCoatTexture(Integer coatTexture) {
        this.coatTexture = coatTexture;
    }

    public Integer getTrousersTexture() {
        return trousersTexture;
    }

    public void setTrousersTexture(Integer trousersTexture) {
        this.trousersTexture = trousersTexture;
    }
}