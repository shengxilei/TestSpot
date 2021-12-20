package com.jianzhi.model;

import java.io.Serializable;

public class QualificationsDetail implements Serializable {
    private Integer id;

    private String dwm;

    private String zsBh;

    private String zzKmm;

    private String zzCode;

    private String qualid;
    
    private String dwid;

    private static final long serialVersionUID = 1L;
    
    

    public String getDwid() {
		return dwid;
	}

	public void setDwid(String dwid) {
		this.dwid = dwid;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getZzKmm() {
        return zzKmm;
    }

    public void setZzKmm(String zzKmm) {
        this.zzKmm = zzKmm == null ? null : zzKmm.trim();
    }

    public String getZzCode() {
        return zzCode;
    }

    public void setZzCode(String zzCode) {
        this.zzCode = zzCode == null ? null : zzCode.trim();
    }

    public String getQualid() {
        return qualid;
    }

    public void setQualid(String qualid) {
        this.qualid = qualid == null ? null : qualid.trim();
    }
}