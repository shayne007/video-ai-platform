package com.keensense.job.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.name;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * <p>
 * 视频点位表
 * </p>
 *
 * @author cuiss
 * @since 2018-11-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("camera")
public class Camera extends Model<Camera> {

    private static final long serialVersionUID = 1L;

    public static final Long CAMERA_TYPE_VAS = 1L;

    /**
     * 点位id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Setter
    @Getter
    private Long id;
    /**
     * 点位名称
     */
    @Setter
    @Getter
    private String name;
    /**
     * 点位类型
     */
    @Setter
    @Getter
    private Long cameratype;
    /**
     * 机型
     */
    @Setter
    @Getter
    private Long type;
    /**
     * 行政区域
     */
    @Setter
    @Getter
    private String region;
    /**
     * 经度
     */
    @Setter
    @Getter
    private String longitude;
    /**
     * 纬度
     */
    @Setter
    @Getter
    private String latitude;
    /**
     * 方向
     */
    @Setter
    @Getter
    private String direction;
    /**
     * 地点
     */
    @Setter
    @Getter
    private String location;
    /**
     * 状态
     */
    @Setter
    @Getter
    private Long status;
    /**
     * 描述
     */
    @Setter
    @Getter
    private String dsc;
    /**
     * 品牌id
     */
    @Setter
    @Getter
    private Long brandid;
    /**
     * 品牌名称
     */
    @Setter
    @Getter
    private String brandname;
    /**
     * 型号
     */
    @Setter
    @Getter
    private String model;
    /**
     * ip地址
     */
    @Setter
    @Getter
    private String ip;
    /**
     * 端口1
     */
    @Setter
    @Getter
    private Long port1;
    /**
     * 端口2
     */
    @Setter
    @Getter
    private Long port2;
    /**
     * 登陆账号
     */
    @Setter
    @Getter
    private String account;
    /**
     * 密码
     */
    @Setter
    @Getter
    private String password;
    /**
     * 通道
     */
    @Setter
    @Getter
    private Long channel;
    /**
     * 设备编号
     */
    @Setter
    @Getter
    private String extcameraid;
    /**
     * 管理责任单位
     */
    @Setter
    @Getter
    private String admindept;
    /**
     * 责任人
     */
    @Setter
    @Getter
    private String admin;
    /**
     * 联系电话
     */
    @Setter
    @Getter
    private String telephone;
    /**
     * 联系地址
     */
    @Setter
    @Getter
    private String address;
    /**
     * url
     */
    @Setter
    @Getter
    private String url;
    /**
     * 允许区域
     */
    @Setter
    @Getter
    private String follwarea;
    /**
     * 缩略图
     */
    @TableField("thumb_nail")
    @Setter
    @Getter
    private String thumbNail;
    /**
     * 监控点组id
     */
    @Setter
    @Getter
    private Long cameragroupid;
    /**
     * 创建时间
     */
    @TableField("create_time")
    @Setter
    @Getter
    private Date createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    /**
     * name,type,region,longitude,latitude,status,extcameraid
     * @param obi
     * @return
     */
    @Override
    public boolean equals(Object obi) {
        if (this == obi) {return true;}
        if (obi == null || getClass() != obi.getClass()){ return false;}
        Camera camera = (Camera) obi;
        return Objects.equals(name, camera.name) &&
                Objects.equals(type, camera.type) &&
                Objects.equals(region, camera.region) &&
                Objects.equals(longitude, camera.longitude) &&
                Objects.equals(latitude, camera.latitude) &&
                Objects.equals(status, camera.status) &&
                Objects.equals(extcameraid, camera.extcameraid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, region, longitude, latitude, status, extcameraid);
    }

    @Override
    public String toString() {
        return "Camera{" +
        ", id=" + id +
        ", name=" + name +
        ", cameratype=" + cameratype +
        ", type=" + type +
        ", region=" + region +
        ", longitude=" + longitude +
        ", latitude=" + latitude +
        ", direction=" + direction +
        ", location=" + location +
        ", status=" + status +
        ", dsc=" + dsc +
        ", brandid=" + brandid +
        ", brandname=" + brandname +
        ", model=" + model +
        ", ip=" + ip +
        ", port1=" + port1 +
        ", port2=" + port2 +
        ", account=" + account +
        ", password=" + password +
        ", channel=" + channel +
        ", extcameraid=" + extcameraid +
        ", admindept=" + admindept +
        ", admin=" + admin +
        ", telephone=" + telephone +
        ", address=" + address +
        ", url=" + url +
        ", follwarea=" + follwarea +
        ", thumbNail=" + thumbNail +
        ", cameragroupid=" + cameragroupid +
        ", createTime=" + createTime +
        "}";
    }
}
