package com.keensense.search.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.loocme.sys.annotation.database.Column;
import com.loocme.sys.annotation.database.Id;
import com.loocme.sys.annotation.database.Table;
import lombok.Data;
import lombok.ToString;

/**
 * Created by zhanx xiaohui on 2019-05-06.
 */
@Table(TableName = "apes_result")
@Data
@ToString(callSuper = true, includeFieldNames = true)
public class APEResult {
    @Id
    @JSONField(name = "ApeID")
    @Column(ColumnName = "apeid")
    private String apeId;

    @JSONField(name = "Name")
    @Column(ColumnName = "name")
    private String name;

    @JSONField(name = "Model")
    @Column(ColumnName = "model")
    private String model;

    @JSONField(name = "IPAddr")
    @Column(ColumnName = "ipaddr")
    private String ipAddr;

    @JSONField(name = "IPV6Addr")
    @Column(ColumnName = "ipv6addr")
    private String ipv6Addr;

    @JSONField(name = "Port")
    @Column(ColumnName = "port")
    private Integer port;

    @JSONField(name = "Longitude")
    @Column(ColumnName = "longitude")
    private Double longitude;

    @JSONField(name = "Latitude")
    @Column(ColumnName = "latitude")
    private Double latitude;

    @JSONField(name = "PlaceCode")
    @Column(ColumnName = "placecode")
    private String placeCode;

    @JSONField(name = "Place")
    @Column(ColumnName = "place")
    private String place;

    @JSONField(name = "OrgCode")
    @Column(ColumnName = "orgcode")
    private String orgCode;

    @JSONField(name = "CapDirection")
    @Column(ColumnName = "capdirection")
    private Integer capDirection;

    @JSONField(name = "MonitorDirection")
    @Column(ColumnName = "monitordirection")
    private String monitorDirection;

    @JSONField(name = "MonitorAreaDesc")
    @Column(ColumnName = "monitorareadesc")
    private String monitorAreaDesc;

    @JSONField(name = "IsOnline")
    @Column(ColumnName = "isonline")
    private String isOnline;

    @JSONField(name = "OwnerApsID")
    @Column(ColumnName = "ownerapsid")
    private String ownerApsID;

    @JSONField(name = "UserId")
    @Column(ColumnName = "userid")
    private String userId;

    @JSONField(name = "Password")
    @Column(ColumnName = "password")
    private String password;
}
