package com.keensense.search.domain;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by zhanx xiaohui on 2019-05-06.
 */
@Table(name = "apes_result")
@Data
@ToString(callSuper = true, includeFieldNames = true)
public class APEResult {
    @Id
    @JSONField(name = "ApeID")
    @Column(name = "apeid")
    private String apeId;

    @JSONField(name = "Name")
    @Column(name = "name")
    private String name;

    @JSONField(name = "Model")
    @Column(name = "model")
    private String model;

    @JSONField(name = "IPAddr")
    @Column(name = "ipaddr")
    private String ipAddr;

    @JSONField(name = "IPV6Addr")
    @Column(name = "ipv6addr")
    private String ipv6Addr;

    @JSONField(name = "Port")
    @Column(name = "port")
    private Integer port;

    @JSONField(name = "Longitude")
    @Column(name = "longitude")
    private Double longitude;

    @JSONField(name = "Latitude")
    @Column(name = "latitude")
    private Double latitude;

    @JSONField(name = "PlaceCode")
    @Column(name = "placecode")
    private String placeCode;

    @JSONField(name = "Place")
    @Column(name = "place")
    private String place;

    @JSONField(name = "OrgCode")
    @Column(name = "orgcode")
    private String orgCode;

    @JSONField(name = "CapDirection")
    @Column(name = "capdirection")
    private Integer capDirection;

    @JSONField(name = "MonitorDirection")
    @Column(name = "monitordirection")
    private String monitorDirection;

    @JSONField(name = "MonitorAreaDesc")
    @Column(name = "monitorareadesc")
    private String monitorAreaDesc;

    @JSONField(name = "IsOnline")
    @Column(name = "isonline")
    private String isOnline;

    @JSONField(name = "OwnerApsID")
    @Column(name = "ownerapsid")
    private String ownerApsID;

    @JSONField(name = "UserId")
    @Column(name = "userid")
    private String userId;

    @JSONField(name = "Password")
    @Column(name = "password")
    private String password;
}
