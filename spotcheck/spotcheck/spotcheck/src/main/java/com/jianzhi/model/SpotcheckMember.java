package com.jianzhi.model;

import java.io.Serializable;

public class SpotcheckMember implements Serializable {
    private Integer id;

    private Integer xh;

    private String dwm;

    private String xm;

    private Integer xb;

    private Integer age;

    private String jobtitle;

    private String sfzhm;

    private String kmm;
    
    private String bz;

    private Integer checkstatus;

    private Integer jg;

    private Integer spid;

    private String pxbmid;
    
    private String mhdwm;
    
    private Integer isZz;
    
    private Integer isFj;//是否为附加抽中人员

    private  String khphoto;//图片地址

    private Integer p_bj;

    private Integer ll_jg;

    private Integer xhjd;

    private Integer num;

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getXhjd() {
        return xhjd;
    }

    public void setXhjd(Integer xhjd) {
        this.xhjd = xhjd;
    }

    public Integer getLl_jg() {
        return ll_jg;
    }

    public void setLl_jg(Integer ll_jg) {
        this.ll_jg = ll_jg;
    }

    public Integer getP_bj() {
        return p_bj;
    }

    public void setP_bj(Integer p_bj) {
        this.p_bj = p_bj;
    }



    public String getPxbmid() {
        return pxbmid;
    }

    public void setPxbmid(String pxbmid) {
        this.pxbmid = pxbmid;
    }

    public String getKhphoto() {
        return khphoto;
    }

    public void setKhphoto(String khphoto) {
        this.khphoto = khphoto;
    }

    public Integer getIsFj() {
		return isFj;
	}

	public void setIsFj(Integer isFj) {
		this.isFj = isFj;
	}

	public Integer getIsZz() {
		return isZz;
	}

	public void setIsZz(Integer isZz) {
		this.isZz = isZz;
	}

	public String getMhdwm() {
		return mhdwm;
	}

	public void setMhdwm(String mhdwm) {
		this.mhdwm = mhdwm;
	}

	private static final long serialVersionUID = 1L;

    
    public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
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
}