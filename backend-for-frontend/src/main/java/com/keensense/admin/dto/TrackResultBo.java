package com.keensense.admin.dto;

import com.keensense.admin.base.BaseBo;

public class TrackResultBo extends BaseBo {

    private static final long serialVersionUID = -7349161984916460204L;

    private String id;

    private String serialnumber;

    private String imgurl;

    private String bigimgUrl;

    private String cameraid;

    private Short objtype;

    private String batchNo;

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSerialnumber() {
        return serialnumber;
    }

    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getBigimgUrl() {
        return bigimgUrl;
    }

    public void setBigimgUrl(String bigimgUrl) {
        this.bigimgUrl = bigimgUrl;
    }

    public String getCameraid() {
        return cameraid;
    }

    public void setCameraid(String cameraid) {
        this.cameraid = cameraid;
    }

    public Short getObjtype() {
        return objtype;
    }

    public void setObjtype(Short objtype) {
        this.objtype = objtype;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
