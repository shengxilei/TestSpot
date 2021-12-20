package com.jianzhi.model;

import java.io.Serializable;
import java.util.Date;

public class Pxbm implements Serializable {
    private String id;

    private String mid;

    private String yyyy;

    private String djc;

    private String uId;

    private String xm;

    private String xb;

    private String sfzhm;

    private Integer age2;

    private String sgzBh;

    private String pxzzh;

    private String xlCode;

    private String xl;

    private String zcCode;

    private String zc;

    private String zy;

    private String areaCode;

    private String areaName;

    private String dwid;

    private String dwm;

    private String kmCode;

    private String kmm;

    private String isFp;

    private String isZs;

    private Date addt;

    private String notifyId;

    private String classId;

    private String zt;

    private String isRealPx;

    private String bz;

    private String ksYyyy;

    private String kscjId;

    private Integer fs;

    private Integer fs2;

    private String kq;

    private String jg;

    private String opZt;

    private String realOp;

    private String realOpDwid;

    private Date realOpT;

    private String impTy;

    private String isCjtj;

    private String isUse;

    private Date changeT;

    private String bz2;//备注

    private Integer downN;

    private String allowDealToPxz;
    
    private Integer isCNS;//承诺书

    private Integer isSB;//社保
    
    private Integer spid;
    
    private String jqdwm;//精确查询单位名
    
    private Integer isfp;//是否已被分配
    
    private Integer isApply;
    
    private String apply;//申请原因
    
    private String reply;//批准回复

    private static final long serialVersionUID = 1L;

    private  String khphoto;//图片地址

    public String getKhphoto() {
        return khphoto;
    }

    public Integer p_bj;

    private Integer is_jd;

    public Integer getIs_jd() {
        return is_jd;
    }

    public void setIs_jd(Integer is_jd) {
        this.is_jd = is_jd;
    }

    public Integer getP_bj() {
        return p_bj;
    }

    public void setP_bj(Integer p_bj) {
        this.p_bj = p_bj;
    }

    public void setKhphoto(String khphoto) {
        this.khphoto = khphoto;
    }

    public String getApply() {
		return apply;
	}

	public void setApply(String apply) {
		this.apply = apply;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public Integer getIsApply() {
		return isApply;
	}

	public void setIsApply(Integer isApply) {
		this.isApply = isApply;
	}

	public Integer getIsfp() {
		return isfp;
	}

	public void setIsfp(Integer isfp) {
		this.isfp = isfp;
	}

	public Integer getSpid() {
		return spid;
	}

	public void setSpid(Integer spid) {
		this.spid = spid;
	}

	public String getJqdwm() {
		return jqdwm;
	}

	public void setJqdwm(String jqdwm) {
		this.jqdwm = jqdwm;
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

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid == null ? null : mid.trim();
    }

    public String getYyyy() {
        return yyyy;
    }

    public void setYyyy(String yyyy) {
        this.yyyy = yyyy == null ? null : yyyy.trim();
    }

    public String getDjc() {
        return djc;
    }

    public void setDjc(String djc) {
        this.djc = djc == null ? null : djc.trim();
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId == null ? null : uId.trim();
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm == null ? null : xm.trim();
    }

    public String getXb() {
        return xb;
    }

    public void setXb(String xb) {
        this.xb = xb == null ? null : xb.trim();
    }

    public String getSfzhm() {
        return sfzhm;
    }

    public void setSfzhm(String sfzhm) {
        this.sfzhm = sfzhm == null ? null : sfzhm.trim();
    }

    public Integer getAge2() {
        return age2;
    }

    public void setAge2(Integer age2) {
        this.age2 = age2;
    }

    public String getSgzBh() {
        return sgzBh;
    }

    public void setSgzBh(String sgzBh) {
        this.sgzBh = sgzBh == null ? null : sgzBh.trim();
    }

    public String getPxzzh() {
        return pxzzh;
    }

    public void setPxzzh(String pxzzh) {
        this.pxzzh = pxzzh == null ? null : pxzzh.trim();
    }

    public String getXlCode() {
        return xlCode;
    }

    public void setXlCode(String xlCode) {
        this.xlCode = xlCode == null ? null : xlCode.trim();
    }

    public String getXl() {
        return xl;
    }

    public void setXl(String xl) {
        this.xl = xl == null ? null : xl.trim();
    }

    public String getZcCode() {
        return zcCode;
    }

    public void setZcCode(String zcCode) {
        this.zcCode = zcCode == null ? null : zcCode.trim();
    }

    public String getZc() {
        return zc;
    }

    public void setZc(String zc) {
        this.zc = zc == null ? null : zc.trim();
    }

    public String getZy() {
        return zy;
    }

    public void setZy(String zy) {
        this.zy = zy == null ? null : zy.trim();
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode == null ? null : areaCode.trim();
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName == null ? null : areaName.trim();
    }

    public String getDwid() {
        return dwid;
    }

    public void setDwid(String dwid) {
        this.dwid = dwid == null ? null : dwid.trim();
    }

    public String getDwm() {
        return dwm;
    }

    public void setDwm(String dwm) {
        this.dwm = dwm == null ? null : dwm.trim();
    }

    public String getKmCode() {
        return kmCode;
    }

    public void setKmCode(String kmCode) {
        this.kmCode = kmCode == null ? null : kmCode.trim();
    }

    public String getKmm() {
        return kmm;
    }

    public void setKmm(String kmm) {
        this.kmm = kmm == null ? null : kmm.trim();
    }

    public String getIsFp() {
        return isFp;
    }

    public void setIsFp(String isFp) {
        this.isFp = isFp == null ? null : isFp.trim();
    }

    public String getIsZs() {
        return isZs;
    }

    public void setIsZs(String isZs) {
        this.isZs = isZs == null ? null : isZs.trim();
    }

    public Date getAddt() {
        return addt;
    }

    public void setAddt(Date addt) {
        this.addt = addt;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId == null ? null : notifyId.trim();
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId == null ? null : classId.trim();
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt == null ? null : zt.trim();
    }

    public String getIsRealPx() {
        return isRealPx;
    }

    public void setIsRealPx(String isRealPx) {
        this.isRealPx = isRealPx == null ? null : isRealPx.trim();
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz == null ? null : bz.trim();
    }

    public String getKsYyyy() {
        return ksYyyy;
    }

    public void setKsYyyy(String ksYyyy) {
        this.ksYyyy = ksYyyy == null ? null : ksYyyy.trim();
    }

    public String getKscjId() {
        return kscjId;
    }

    public void setKscjId(String kscjId) {
        this.kscjId = kscjId == null ? null : kscjId.trim();
    }

    public Integer getFs() {
        return fs;
    }

    public void setFs(Integer fs) {
        this.fs = fs;
    }

    public Integer getFs2() {
        return fs2;
    }

    public void setFs2(Integer fs2) {
        this.fs2 = fs2;
    }

    public String getKq() {
        return kq;
    }

    public void setKq(String kq) {
        this.kq = kq == null ? null : kq.trim();
    }

    public String getJg() {
        return jg;
    }

    public void setJg(String jg) {
        this.jg = jg == null ? null : jg.trim();
    }

    public String getOpZt() {
        return opZt;
    }

    public void setOpZt(String opZt) {
        this.opZt = opZt == null ? null : opZt.trim();
    }

    public String getRealOp() {
        return realOp;
    }

    public void setRealOp(String realOp) {
        this.realOp = realOp == null ? null : realOp.trim();
    }

    public String getRealOpDwid() {
        return realOpDwid;
    }

    public void setRealOpDwid(String realOpDwid) {
        this.realOpDwid = realOpDwid == null ? null : realOpDwid.trim();
    }

    public Date getRealOpT() {
        return realOpT;
    }

    public void setRealOpT(Date realOpT) {
        this.realOpT = realOpT;
    }

    public String getImpTy() {
        return impTy;
    }

    public void setImpTy(String impTy) {
        this.impTy = impTy == null ? null : impTy.trim();
    }

    public String getIsCjtj() {
        return isCjtj;
    }

    public void setIsCjtj(String isCjtj) {
        this.isCjtj = isCjtj == null ? null : isCjtj.trim();
    }

    public String getIsUse() {
        return isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse == null ? null : isUse.trim();
    }

    public Date getChangeT() {
        return changeT;
    }

    public void setChangeT(Date changeT) {
        this.changeT = changeT;
    }

    public String getBz2() {
        return bz2;
    }

    public void setBz2(String bz2) {
        this.bz2 = bz2 == null ? null : bz2.trim();
    }

    public Integer getDownN() {
        return downN;
    }

    public void setDownN(Integer downN) {
        this.downN = downN;
    }

    public String getAllowDealToPxz() {
        return allowDealToPxz;
    }

    public void setAllowDealToPxz(String allowDealToPxz) {
        this.allowDealToPxz = allowDealToPxz == null ? null : allowDealToPxz.trim();
    }
}