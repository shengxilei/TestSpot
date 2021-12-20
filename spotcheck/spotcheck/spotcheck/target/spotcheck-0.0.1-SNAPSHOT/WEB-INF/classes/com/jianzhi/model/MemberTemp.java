package com.jianzhi.model;

import java.io.Serializable;

public class MemberTemp implements Serializable {
    private Integer id;

    private Integer xh;

    private String dwm;

    private String xm;

    private Integer xb;

    private Integer age;

    private String jobtitle;

    private String sfzhm;

    private String kmm;

    private Integer checkstatus;

    private Integer jg;

    private Integer spid;

    private String bz;

    private String err;
    
    private String pxbmid;
    
    private Integer isCNS;
    
    private Integer isSB;

    private static final long serialVersionUID = 1L;

    
    
    public String getPxbmid() {
		return pxbmid;
	}

	public void setPxbmid(String pxbmid) {
		this.pxbmid = pxbmid;
	}

	public Integer getIsCNS() {
		return isCNS;
	}

	public void setIsCNS(Integer isCNS) {
		this.isCNS = isCNS;
	}

	public Integer getIsSB() {
		return isSB;
	}

	public void setIsSB(Integer isSB) {
		this.isSB = isSB;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getXh() {
        return xh;
    }

    public void setXh(Integer xh) {
        this.xh = xh;
    }

    public String getDwm() {
        return dwm;
    }

    public void setDwm(String dwm) {
        this.dwm = dwm == null ? null : dwm.trim();
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm == null ? null : xm.trim();
    }

    public Integer getXb() {
        return xb;
    }

    public void setXb(Integer xb) {
        this.xb = xb;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getJobtitle() {
        return jobtitle;
    }

    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle == null ? null : jobtitle.trim();
    }

    public String getSfzhm() {
        return sfzhm;
    }

    public void setSfzhm(String sfzhm) {
        this.sfzhm = sfzhm == null ? null : sfzhm.trim();
    }

    public String getKmm() {
        return kmm;
    }

    public void setKmm(String kmm) {
        this.kmm = kmm == null ? null : kmm.trim();
    }

    public Integer getCheckstatus() {
        return checkstatus;
    }

    public void setCheckstatus(Integer checkstatus) {
        this.checkstatus = checkstatus;
    }

    public Integer getJg() {
        return jg;
    }

    public void setJg(Integer jg) {
        this.jg = jg;
    }

    public Integer getSpid() {
        return spid;
    }

    public void setSpid(Integer spid) {
        this.spid = spid;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz == null ? null : bz.trim();
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err == null ? null : err.trim();
    }
}