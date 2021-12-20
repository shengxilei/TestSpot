package com.jianzhi.model;

import java.io.Serializable;

public class Enterprise implements Serializable {
    private String id;

    private String name;

    private String pxzh;

    private String pxmm;

    private Integer isdelete;

    private String role;

    private String areaName;
    
    

    public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPxzh() {
        return pxzh;
    }

    public void setPxzh(String pxzh) {
        this.pxzh = pxzh == null ? null : pxzh.trim();
    }

    public String getPxmm() {
        return pxmm;
    }

    public void setPxmm(String pxmm) {
        this.pxmm = pxmm == null ? null : pxmm.trim();
    }

    public Integer getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(Integer isdelete) {
        this.isdelete = isdelete;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role == null ? null : role.trim();
    }
}