package com.jianzhi.model;

public class Photo {

    private  Integer ID;

    private Integer spid;

    private String pxbmId;

    private String khphoto;

    public String getKhphoto() {
        return khphoto;
    }

    public void setKhphoto(String khphoto) {
        this.khphoto = khphoto;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Integer getSpid() {
        return spid;
    }

    public void setSpid(Integer spid) {
        this.spid = spid;
    }

    public String getPxbmId() {
        return pxbmId;
    }

    public void setPxbmId(String pxbmId) {
        this.pxbmId = pxbmId;
    }
}
