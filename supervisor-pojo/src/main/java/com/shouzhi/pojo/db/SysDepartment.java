package com.shouzhi.pojo.db;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

/**
 * wr_sys_department
 * @author 
 */
public class SysDepartment implements Serializable {
    /**
     * 主键(新增操作忽略，更新操作必填)
     */
    private String id;

    /**
     * 父结点id
     */
    private String parentId;

    /**
     * 父结点id列表串，由后台系统代码自动生成
     * @ignore
     */
    private String parentIds;

    /**
     * 部门名称
     */
    private String depName;

    /**
     * 部门编码
     */
    private String depCode;

    /**
     * 部门描述
     */
    private String depDesc;

    /**
     * 部门类型：1_1：校区、1_2：院、1_3：系(专业)；职能部门应为2_开头，目前暂时没有用到
     */
    private String depType;

    /**
     * 排序号
     */
    private Integer sortNum;

    /**
     * 归属类型，1：校区/院/系或专业(学生)，2：职能部门(老师)
     */
    private String ascriptionType;

    /**
     * 创建人id，用于连表不用于显示
     * @ignore
     */
    @JsonIgnore
    private String createId;

    /**
     * 创建人账号，不建议填名字，账号更改的可能性很小且又比ID稍微形象化
     * @ignore
     */
    @JsonIgnore
    private String createBy;

    /**
     * 创建方式（0：程序代码创建，1：导入创建）
     * @ignore
     */
    @JsonIgnore
    private String createWay;

    /**
     * 创建时间
     * @ignore
     */
    @JsonIgnore
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

    /**
     * 父结点名称
     */
    private String parentName;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getDepCode() {
        return depCode;
    }

    public void setDepCode(String depCode) {
        this.depCode = depCode;
    }

    public String getDepDesc() {
        return depDesc;
    }

    public void setDepDesc(String depDesc) {
        this.depDesc = depDesc;
    }

    public String getDepType() {
        return depType;
    }

    public void setDepType(String depType) {
        this.depType = depType;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public String getAscriptionType() {
        return ascriptionType;
    }

    public void setAscriptionType(String ascriptionType) {
        this.ascriptionType = ascriptionType;
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

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    @Override
    public String toString() {
        return "SysDepartment{" +
                "id='" + id + '\'' +
                ", parentId='" + parentId + '\'' +
                ", parentIds='" + parentIds + '\'' +
                ", depName='" + depName + '\'' +
                ", depCode='" + depCode + '\'' +
                ", depDesc='" + depDesc + '\'' +
                ", depType='" + depType + '\'' +
                ", sortNum=" + sortNum +
                ", ascriptionType='" + ascriptionType + '\'' +
                ", createId='" + createId + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createWay='" + createWay + '\'' +
                ", createTime=" + createTime +
                ", remark='" + remark + '\'' +
                ", isDelete='" + isDelete + '\'' +
                ", deleteTime=" + deleteTime +
                ", parentName='" + parentName + '\'' +
                '}';
    }
}