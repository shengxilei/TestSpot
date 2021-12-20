package com.jianzhi.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Qualifications implements Serializable {
    private String id;

    private String dwm;
    
    private String mhdwm;

    private String zsBh;

    private Date fzdateStart;

    private Date fzdateEnd;
    
    private String zzcontent;
    
    private String dwid;
    
    private String areaName;//用于查询地区
    
    
    private static final long serialVersionUID = 1L;
    
    

    public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getDwid() {
		return dwid;
	}

	public void setDwid(String dwid) {
		this.dwid = dwid;
	}

	public String getMhdwm() {
		return mhdwm;
	}

	public void setMhdwm(String mhdwm) {
		this.mhdwm = mhdwm;
	}

	

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getDwm() {
        return dwm;
    }

    public void setDwm(String dwm) {
        this.dwm = dwm == null ? null : dwm.trim();
    }

    public String getZsBh() {
        return zsBh;
    }

    public void setZsBh(String zsBh) {
        this.zsBh = zsBh == null ? null : zsBh.trim();
    }

    public String getFzdateStart() {
    	String strDateFormat = "yyyy/MM/dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        if(fzdateStart==null) {
        	return null;
        }
		return sdf.format(fzdateStart);
    }

    public void setFzdateStart(Date fzdateStart) {
        this.fzdateStart = fzdateStart;
    }

    public String getFzdateEnd() {
    	String strDateFormat = "yyyy/MM/dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        if(fzdateEnd==null) {
        	return null;
        }
		return sdf.format(fzdateEnd);
    }

    public void setFzdateEnd(Date fzdateEnd) {
        this.fzdateEnd = fzdateEnd;
    }

    public String getZzcontent() {
        return zzcontent;
    }

    public void setZzcontent(String zzcontent) {
        this.zzcontent = zzcontent == null ? null : zzcontent.trim();
    }
}