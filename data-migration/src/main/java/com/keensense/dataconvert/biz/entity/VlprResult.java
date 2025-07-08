package com.keensense.dataconvert.biz.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName：VlprResult
 * @Description： <p> VlprResult  </p>
 * @Author： - Jason
 * @CreatTime：2019/7/26 - 14:02
 * @Modify By：
 * @ModifyTime： 2019/7/26
 * @Modify marker：
 * @version V1.0
*/
public class VlprResult implements Serializable {

    /**
     * recog id 转换
     */
    private String idStr;

    /**
     * objType
     */
    private Integer objType  = 2;

    /**
     * id
     */
    private Integer id;

    /**
     * taskid
     */
    private Long taskid;

    /**
     * isobjextresult
     */
    private Byte isobjextresult;

    /**
     * serialnumber
     */
    private String serialnumber;

    /**
     * cameraid
     */
    private Long cameraid;

    /**
     * license
     */
    private String license;

    /**
     * licenseattribution
     */
    private String licenseattribution;

    /**
     * platecolor
     */
    private String platecolor;

    /**
     * platetype
     */
    private Short platetype;

    /**
     * confidence
     */
    private Short confidence;

    /**
     * direction
     */
    private Short direction;

    /**
     * locationinfo
     */
    private String locationinfo;

    /**
     * carcolor
     */
    private String carcolor;

    /**
     * carlogo
     */
    private String carlogo;

    /**
     * imagepath
     */
    private String imagepath;

    /**
     * smallimgurl
     */
    private String smallimgurl;

    /**
     * imageurl
     */
    private String imageurl;

    /**
     * inserttime
     */
    private Date inserttime;

    /**
     * createtime
     */
    private Date createtime;

    /**
     * frameIndex
     */
    private Long frameIndex;

    /**
     * carspeed
     */
    private Double carspeed;

    /**
     * vehiclekind
     */
    private String vehiclekind;

    /**
     * vehiclebrand
     */
    private String vehiclebrand;

    /**
     * vehicleseries
     */
    private String vehicleseries;

    /**
     * vehiclestyle
     */
    private String vehiclestyle;

    /**
     * tag
     */
    private Byte tag;

    /**
     * paper
     */
    private Byte paper;

    /**
     * sun
     */
    private Byte sun;

    /**
     * drop
     */
    private Byte drop;

    /**
     * call
     */
    private Byte call;

    /**
     * crash
     */
    private Byte crash;

    /**
     * danger
     */
    private Byte danger;

    /**
     * mainbelt
     */
    private Byte mainbelt;

    /**
     * secondbelt
     */
    private Byte secondbelt;

    /**
     * vehicleresion
     */
    private String vehicleresion;

    /**
     * vehicleconfidence
     */
    private Short vehicleconfidence;

    /**
     * peccancy
     */
    private Short peccancy;

    /**
     * objid
     */
    private Integer objid;

    /**
     * startframepts
     */
    private Integer startframepts;

    /**
     * startframeidx
     */
    private Integer startframeidx;

    /**
     * recogId
     */
    private String recogId;

    /**
     * slaveIp
     */
    private String slaveIp;

    /**
     * retrycount
     */
    private Integer retrycount;

    /**
     * width
     */
    private long width;

    /**
     * height
     */
    private long height;

    public long getWidth() {
        return width;
    }

    public void setWidth(long width) {
        this.width = width;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public Integer getObjType() {
        return objType;
    }

    public void setObjType(Integer objType) {
        this.objType = objType;
    }

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

    public Long getTaskid() {
        return taskid;
    }

    public void setTaskid(Long taskid) {
        this.taskid = taskid;
    }

    public Byte getIsobjextresult() {
        return isobjextresult;
    }

    public void setIsobjextresult(Byte isobjextresult) {
        this.isobjextresult = isobjextresult;
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

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license == null ? null : license.trim();
    }

    public String getLicenseattribution() {
        return licenseattribution;
    }

    public void setLicenseattribution(String licenseattribution) {
        this.licenseattribution = licenseattribution == null ? null : licenseattribution.trim();
    }

    public String getPlatecolor() {
        return platecolor;
    }

    public void setPlatecolor(String platecolor) {
        this.platecolor = platecolor == null ? null : platecolor.trim();
    }

    public Short getPlatetype() {
        return platetype;
    }

    public void setPlatetype(Short platetype) {
        this.platetype = platetype;
    }

    public Short getConfidence() {
        return confidence;
    }

    public void setConfidence(Short confidence) {
        this.confidence = confidence;
    }

    public Short getDirection() {
        return direction;
    }

    public void setDirection(Short direction) {
        this.direction = direction;
    }

    public String getLocationinfo() {
        return locationinfo;
    }

    public void setLocationinfo(String locationinfo) {
        this.locationinfo = locationinfo == null ? null : locationinfo.trim();
    }

    public String getCarcolor() {
        return carcolor;
    }

    public void setCarcolor(String carcolor) {
        this.carcolor = carcolor == null ? null : carcolor.trim();
    }

    public String getCarlogo() {
        return carlogo;
    }

    public void setCarlogo(String carlogo) {
        this.carlogo = carlogo == null ? null : carlogo.trim();
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath == null ? null : imagepath.trim();
    }

    public String getSmallimgurl() {
        return smallimgurl;
    }

    public void setSmallimgurl(String smallimgurl) {
        this.smallimgurl = smallimgurl == null ? null : smallimgurl.trim();
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl == null ? null : imageurl.trim();
    }

    public Date getInserttime() {
        return inserttime;
    }

    public void setInserttime(Date inserttime) {
        this.inserttime = inserttime;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Long getFrameIndex() {
        return frameIndex;
    }

    public void setFrameIndex(Long frameIndex) {
        this.frameIndex = frameIndex;
    }

    public Double getCarspeed() {
        return carspeed;
    }

    public void setCarspeed(Double carspeed) {
        this.carspeed = carspeed;
    }

    public String getVehiclekind() {
        return vehiclekind;
    }

    public void setVehiclekind(String vehiclekind) {
        this.vehiclekind = vehiclekind == null ? null : vehiclekind.trim();
    }

    public String getVehiclebrand() {
        return vehiclebrand;
    }

    public void setVehiclebrand(String vehiclebrand) {
        this.vehiclebrand = vehiclebrand == null ? null : vehiclebrand.trim();
    }

    public String getVehicleseries() {
        return vehicleseries;
    }

    public void setVehicleseries(String vehicleseries) {
        this.vehicleseries = vehicleseries == null ? null : vehicleseries.trim();
    }

    public String getVehiclestyle() {
        return vehiclestyle;
    }

    public void setVehiclestyle(String vehiclestyle) {
        this.vehiclestyle = vehiclestyle == null ? null : vehiclestyle.trim();
    }

    public Byte getTag() {
        return tag;
    }

    public void setTag(Byte tag) {
        this.tag = tag;
    }

    public Byte getPaper() {
        return paper;
    }

    public void setPaper(Byte paper) {
        this.paper = paper;
    }

    public Byte getSun() {
        return sun;
    }

    public void setSun(Byte sun) {
        this.sun = sun;
    }

    public Byte getDrop() {
        return drop;
    }

    public void setDrop(Byte drop) {
        this.drop = drop;
    }

    public Byte getCall() {
        return call;
    }

    public void setCall(Byte call) {
        this.call = call;
    }

    public Byte getCrash() {
        return crash;
    }

    public void setCrash(Byte crash) {
        this.crash = crash;
    }

    public Byte getDanger() {
        return danger;
    }

    public void setDanger(Byte danger) {
        this.danger = danger;
    }

    public Byte getMainbelt() {
        return mainbelt;
    }

    public void setMainbelt(Byte mainbelt) {
        this.mainbelt = mainbelt;
    }

    public Byte getSecondbelt() {
        return secondbelt;
    }

    public void setSecondbelt(Byte secondbelt) {
        this.secondbelt = secondbelt;
    }

    public String getVehicleresion() {
        return vehicleresion;
    }

    public void setVehicleresion(String vehicleresion) {
        this.vehicleresion = vehicleresion == null ? null : vehicleresion.trim();
    }

    public Short getVehicleconfidence() {
        return vehicleconfidence;
    }

    public void setVehicleconfidence(Short vehicleconfidence) {
        this.vehicleconfidence = vehicleconfidence;
    }

    public Short getPeccancy() {
        return peccancy;
    }

    public void setPeccancy(Short peccancy) {
        this.peccancy = peccancy;
    }

    public Integer getObjid() {
        return objid;
    }

    public void setObjid(Integer objid) {
        this.objid = objid;
    }

    public Integer getStartframepts() {
        return startframepts;
    }

    public void setStartframepts(Integer startframepts) {
        this.startframepts = startframepts;
    }

    public Integer getStartframeidx() {
        return startframeidx;
    }

    public void setStartframeidx(Integer startframeidx) {
        this.startframeidx = startframeidx;
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

    public Integer getRetrycount() {
        return retrycount;
    }

    public void setRetrycount(Integer retrycount) {
        this.retrycount = retrycount;
    }
}