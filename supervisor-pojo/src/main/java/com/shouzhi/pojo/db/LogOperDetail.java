package com.shouzhi.pojo.db;

import java.io.Serializable;

/**
 * wr_log_oper_detail
 * @author 
 */
public class LogOperDetail implements Serializable {
    /**
     * 主键(新增操作忽略，更新操作必填)
     */
    private String id;

    /**
     * 日志操作记录表ID
     */
    private String logOperId;

    /**
     * 被操作表的行ID
     */
    private String rowId;

    /**
     * 老数据(或被操作之前的数据)
     */
    private String oldVal;

    /**
     * 新数据(或被操作之后的数据)
     */
    private String newVal;


    private static final long serialVersionUID = 1L;

    public LogOperDetail() {
    }

    public LogOperDetail(String id, String logOperId, String rowId, String oldVal, String newVal) {
        this.id = id;
        this.logOperId = logOperId;
        this.rowId = rowId;
        this.oldVal = oldVal;
        this.newVal = newVal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogOperId() {
        return logOperId;
    }

    public void setLogOperId(String logOperId) {
        this.logOperId = logOperId;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }


    public String getOldVal() {
        return oldVal;
    }

    public void setOldVal(String oldVal) {
        this.oldVal = oldVal;
    }

    public String getNewVal() {
        return newVal;
    }

    public void setNewVal(String newVal) {
        this.newVal = newVal;
    }

    @Override
    public String toString() {
        return "LogOperDetail{" +
                "id=" + id +
                ", logOperId=" + logOperId +
                ", rowId=" + rowId +
                ", oldVal='" + oldVal + '\'' +
                ", newVal='" + newVal + '\'' +
                '}';
    }
}