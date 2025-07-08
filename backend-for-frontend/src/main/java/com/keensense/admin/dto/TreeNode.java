package com.keensense.admin.dto;

import java.util.*;

/**
 * 构建bootstrap tree
 * @author fanxiaoming
 */
public class TreeNode {
    private String id;
    private String name;
    private String pId;
    private String unitLevel;
	private boolean isParent;
    private Integer nodeType;
    private String url;
    private String slaveip;
    private List<TreeNode> treeNodeList;

    private String serialnumber;




    public List<TreeNode> getTreeNodeList() {
		return treeNodeList;
	}

	public void setTreeNodeList(List<TreeNode> treeNodeList) {
		this.treeNodeList = treeNodeList;
	}

	public String getSerialnumber() {
		return serialnumber;
	}

	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}



	public String getUnitLevel() {
		return unitLevel;
	}
	public void setUnitLevel(String unitLevel) {
		this.unitLevel = unitLevel;
	}
	public boolean getIsParent() {
		return isParent;
	}
	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public Integer getNodeType() {
		return nodeType;
	}
	public void setNodeType(Integer nodeType) {
		this.nodeType = nodeType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

    public String getSlaveip() {
        return slaveip;
    }

    public void setSlaveip(String slaveip) {
        this.slaveip = slaveip;
    }
}
