package com.shouzhi.pojo.db;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

/**
 * wr_log_oper
 * @author 
 */
public class LogOper implements Serializable {
    /**
     * 主键(新增操作忽略，更新操作必填)
     */
    private String id;

    /**
     * 被操作表的名字
     */
    private String tabName;

    /**
     * 操作类型，1：增，2：删，3：改，4：导入(批量新增)，5：批量删除，6：批量更新
     */
    private String operType;

    /**
     * 权限ID或菜单ID(仅限于最后级别的菜单)
     */
    private String permId;

    /**
     * 是否级联操作，0：不是，1：是
     */
    private String isCascade;

    /**
     * 级联标识符(ID)，用于标识哪些条记录为一次级联操作
     */
    private String cascadeId;

    /**
     * 创建人id，用于连表不用于显示
     */
    private String createId;

    /**
     * 创建人账号，不建议填名字，账号更改的可能性很小且又比ID稍微形象化
     */
    private String createBy;

    /**
     * 创建方式（0：程序代码创建，1：导入创建）
     * @ignore
     */
    @JsonIgnore
    private String createWay;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否删除（0：未删除，1：删除）
     * @ignore
     */
    @JsonIgnore
    private String isDelete;

    /**
     * 删除时间
     * @ignore
     */
    @JsonIgnore
    private Date deleteTime;

    private static final long serialVersionUID = 1L;

    //---------以下为View字段,非DB字段,不再单独创建Vo去维护---------
    /**
     *  权限名称或菜单名称(仅限于最后级别的菜单)
     *  @ignore
     */
    private String permName;

    public LogOper() {
    }

    public LogOper(String id, String tabName, String operType, String permId, String isCascade, String cascadeId, String createId, String createBy) {
        this.id = id;
        this.tabName = tabName;
        this.operType = operType;
        this.permId = permId;
        this.isCascade = isCascade;
        this.cascadeId = cascadeId;
        this.createId = createId;
        this.createBy = createBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public String getOperType() {
        return operType;
    }

    public void setOperType(String operType) {
        this.operType = operType;
    }

    public String getPermId() {
        return permId;
    }

    public void setPermId(String permId) {
        this.permId = permId;
    }

    public String getIsCascade() {
        return isCascade;
    }

    public void setIsCascade(String isCascade) {
        this.isCascade = isCascade;
    }

    public String getCascadeId() {
        return cascadeId;
    }

    public void setCascadeId(String cascadeId) {
        this.cascadeId = cascadeId;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateWay() {
        return createWay;
    }

    public void setCreateWay(String createWay) {
        this.createWay = createWay;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public String getPermName() {
        return permName;
    }

    public void setPermName(String permName) {
        this.permName = permName;
    }


}