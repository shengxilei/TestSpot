package com.jianzhi.model;

import java.io.Serializable;

public class SpotPrize implements Serializable {
    private Integer id;

    private Integer spmemid;

    private Integer status;

    private Integer spid;
    
    private String dwm;
    

    public String getDwm() {
		return dwm;
	}

	public void setDwm(String dwm) {
		this.dwm = dwm;
	}

	private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSpmemid() {
        return spmemid;
    }

    public void setSpmemid(Integer spmemid) {
        this.spmemid = spmemid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSpid() {
        return spid;
    }

    public void setSpid(Integer spid) {
        this.spid = spid;
    }
}