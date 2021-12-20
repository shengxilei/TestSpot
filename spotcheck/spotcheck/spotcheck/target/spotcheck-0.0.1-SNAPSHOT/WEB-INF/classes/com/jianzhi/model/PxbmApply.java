package com.jianzhi.model;

import java.io.Serializable;
import java.util.Date;

public class PxbmApply implements Serializable {
    private Integer id;

    private String pxid;

    private Integer isCns;

    private Integer isSb;

    private String apply;

    private String reply;

    private Integer status;

    private Date applytime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPxid() {
        return pxid;
    }

    public void setPxid(String pxid) {
        this.pxid = pxid == null ? null : pxid.trim();
    }

    public Integer getIsCns() {
        return isCns;
    }

    public void setIsCns(Integer isCns) {
        this.isCns = isCns;
    }

    public Integer getIsSb() {
        return isSb;
    }

    public void setIsSb(Integer isSb) {
        this.isSb = isSb;
    }

    public String getApply() {
        return apply;
    }

    public void setApply(String apply) {
        this.apply = apply == null ? null : apply.trim();
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply == null ? null : reply.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getApplytime() {
        return applytime;
    }

    public void setApplytime(Date applytime) {
        this.applytime = applytime;
    }
}