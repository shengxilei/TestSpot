package com.jianzhi.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SpotcheckMemberChange implements Serializable {
    private Integer id;

    private Integer xh;
    
    private Integer memid;

    private String dwm;

    private String xm;

    private Integer xb;

    private Integer age;

    private String jobtitle;

    private String sfzhm;

    private String kmm;

    private Integer beforestatus;

    private Integer spid;

    private Date creatdate;

    private Integer afterstatus;

    private String bz;
    
    private String creattime; //用于格式化时间
    
    private String editor;
    

    public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	private static final long serialVersionUID = 1L;
    
    
    
    
    public String getCreattime() {
    	String strDateFormat = "yyyy/MM/dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        if(creatdate==null) {
        	return null;
        }
		return sdf.format(creatdate);
	}

	

	public Integer getMemid() {
		return memid;
	}

	public void setMemid(Integer memid) {
		this.memid = memid;
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

    public Integer getBeforestatus() {
        return beforestatus;
    }

    public void setBeforestatus(Integer beforestatus) {
        this.beforestatus = beforestatus;
    }

    public Integer getSpid() {
        return spid;
    }

    public void setSpid(Integer spid) {
        this.spid = spid;
    }

    public Date getCreatdate() {
        return creatdate;
    }

    public void setCreatdate(Date creatdate) {
        this.creatdate = creatdate;
    }

    public Integer getAfterstatus() {
        return afterstatus;
    }

    public void setAfterstatus(Integer afterstatus) {
        this.afterstatus = afterstatus;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz == null ? null : bz.trim();
    }
}