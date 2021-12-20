package com.jianzhi.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Spotcheck implements Serializable {
    private Integer id;

    private String name;

    private String areaName;

    private String yyyy;

    private String num;

    private Integer bfb;

    private Date creattime;

    private Date start;

    private Date end;
    
    private String kmm;

    private Integer status;

    private Integer isdelete;
    
    private String startdate;
    
    private String enddate;
    
    private Integer spotstatus;
    
    private String now;
    
    private Integer isInput;
    
    private Integer isDqenter;
    
    private Integer isXhenter;
    
    private Integer isRead;//设置是否已读

    private Integer xhjd;

    private Date xhenterTime;

    private Date jdTime;

    private Integer enterTime;

    public Integer getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(Integer enterTime) {
        this.enterTime = enterTime;
    }

    public Date getXhenterTime() {
        return xhenterTime;
    }

    public void setXhenterTime(Date xhenterTime) {
        this.xhenterTime = xhenterTime;
    }

    public Date getJdTime() {
        return jdTime;
    }

    public void setJdTime(Date jdTime) {
        this.jdTime = jdTime;
    }

    public Integer getXhjd() {
        return xhjd;
    }

    public void setXhjd(Integer xhjd) {
        this.xhjd = xhjd;
    }

    public Integer getIsRead() {
		return isRead;
	}



	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}



	public Integer getIsDqenter() {
		return isDqenter;
	}



	public void setIsDqenter(Integer isDqenter) {
		this.isDqenter = isDqenter;
	}



	public Integer getIsXhenter() {
		return isXhenter;
	}



	public void setIsXhenter(Integer isXhenter) {
		this.isXhenter = isXhenter;
	}



	public Integer getIsInput() {
		return isInput;
	}



	public void setIsInput(Integer isInput) {
		this.isInput = isInput;
	}



	public String getNow() {
    	String strDateFormat = "yyyy/MM/dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
		return sdf.format(new Date());
	}

	

	public String getStartdate() {
    	String strDateFormat = "yyyy/MM/dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        if(start==null) {
        	return null;
        }
		return sdf.format(getStart());
	}

	public void setStartdate(String startdate) {
		
		this.startdate = startdate;
	}

	public String getEnddate() {
		
		String strDateFormat = "yyyy/MM/dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        if(end==null) {
        	return null;
        }
		return sdf.format(getEnd());
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName == null ? null : areaName.trim();
    }

    public String getYyyy() {
        return yyyy;
    }

    public void setYyyy(String yyyy) {
        this.yyyy = yyyy == null ? null : yyyy.trim();
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num == null ? null : num.trim();
    }

    public Integer getBfb() {
        return bfb;
    }

    public void setBfb(Integer bfb) {
        this.bfb = bfb;
    }

    public Date getCreattime() {
        return creattime;
    }

    public void setCreattime(Date creattime) {
        this.creattime = creattime;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(Integer isdelete) {
        this.isdelete = isdelete;
    }

	public String getKmm() {
		return kmm;
	}

	public void setKmm(String kmm) {
		this.kmm = kmm;
	}

	public Integer getSpotstatus() {
		Date now=new Date();
		if(start!=null&&end!=null) {
			if(start.getTime()>now.getTime()) {
				return 0;
			}else if(end.getTime()<now.getTime()){
				return 2;
			}else if(start.getTime()<now.getTime()&&end.getTime()>now.getTime()) {
				return 1;
			}
		}
		return 0;
	}


    
}