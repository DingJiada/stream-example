package com.shouzhi.pojo.db;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

/**
 * wr_sch_semester
 * @author 
 */
public class SchSemester implements Serializable {
    /**
     * 主键(新增操作忽略，更新操作必填)
     */
    private String id;

    /**
     * 学期编码，默认规则为"开始学年-结束学年-学期编号”，如：2012-2013-1
     */
    private String semCode;

    /**
     * 开始学年，如：2012
     */
    private Integer schoolYearStart;

    /**
     * 结束学年，如：2013
     */
    private Integer schoolYearEnd;

    /**
     * 学期编号，1：第一学期，2：第二学期
     */
    private Integer semNum;

    /**
     * 学期开始日期，如：2012-02-28
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date semDateStart;

    /**
     * 学期结束日期，如：2012-07-01
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date semDateEnd;

    /**
     * 学期总周数，如：22
     */
    private Integer totalWeeks;

    /**
     * 是否当前学期，注意同一学年只允许存在一个当前学期；0：否，1：是
     */
    private String isCurrentSem;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSemCode() {
        return semCode;
    }

    public void setSemCode(String semCode) {
        this.semCode = semCode;
    }

    public Integer getSchoolYearStart() {
        return schoolYearStart;
    }

    public void setSchoolYearStart(Integer schoolYearStart) {
        this.schoolYearStart = schoolYearStart;
    }

    public Integer getSchoolYearEnd() {
        return schoolYearEnd;
    }

    public void setSchoolYearEnd(Integer schoolYearEnd) {
        this.schoolYearEnd = schoolYearEnd;
    }

    public Integer getSemNum() {
        return semNum;
    }

    public void setSemNum(Integer semNum) {
        this.semNum = semNum;
    }

    public Date getSemDateStart() {
        return semDateStart;
    }

    public void setSemDateStart(Date semDateStart) {
        this.semDateStart = semDateStart;
    }

    public Date getSemDateEnd() {
        return semDateEnd;
    }

    public void setSemDateEnd(Date semDateEnd) {
        this.semDateEnd = semDateEnd;
    }

    public Integer getTotalWeeks() {
        return totalWeeks;
    }

    public void setTotalWeeks(Integer totalWeeks) {
        this.totalWeeks = totalWeeks;
    }

    public String getIsCurrentSem() {
        return isCurrentSem;
    }

    public void setIsCurrentSem(String isCurrentSem) {
        this.isCurrentSem = isCurrentSem;
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

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        SchSemester other = (SchSemester) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getSemCode() == null ? other.getSemCode() == null : this.getSemCode().equals(other.getSemCode()))
            && (this.getSchoolYearStart() == null ? other.getSchoolYearStart() == null : this.getSchoolYearStart().equals(other.getSchoolYearStart()))
            && (this.getSchoolYearEnd() == null ? other.getSchoolYearEnd() == null : this.getSchoolYearEnd().equals(other.getSchoolYearEnd()))
            && (this.getSemNum() == null ? other.getSemNum() == null : this.getSemNum().equals(other.getSemNum()))
            && (this.getSemDateStart() == null ? other.getSemDateStart() == null : this.getSemDateStart().equals(other.getSemDateStart()))
            && (this.getSemDateEnd() == null ? other.getSemDateEnd() == null : this.getSemDateEnd().equals(other.getSemDateEnd()))
            && (this.getTotalWeeks() == null ? other.getTotalWeeks() == null : this.getTotalWeeks().equals(other.getTotalWeeks()))
            && (this.getIsCurrentSem() == null ? other.getIsCurrentSem() == null : this.getIsCurrentSem().equals(other.getIsCurrentSem()))
            && (this.getCreateId() == null ? other.getCreateId() == null : this.getCreateId().equals(other.getCreateId()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getCreateWay() == null ? other.getCreateWay() == null : this.getCreateWay().equals(other.getCreateWay()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()))
            && (this.getDeleteTime() == null ? other.getDeleteTime() == null : this.getDeleteTime().equals(other.getDeleteTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSemCode() == null) ? 0 : getSemCode().hashCode());
        result = prime * result + ((getSchoolYearStart() == null) ? 0 : getSchoolYearStart().hashCode());
        result = prime * result + ((getSchoolYearEnd() == null) ? 0 : getSchoolYearEnd().hashCode());
        result = prime * result + ((getSemNum() == null) ? 0 : getSemNum().hashCode());
        result = prime * result + ((getSemDateStart() == null) ? 0 : getSemDateStart().hashCode());
        result = prime * result + ((getSemDateEnd() == null) ? 0 : getSemDateEnd().hashCode());
        result = prime * result + ((getTotalWeeks() == null) ? 0 : getTotalWeeks().hashCode());
        result = prime * result + ((getIsCurrentSem() == null) ? 0 : getIsCurrentSem().hashCode());
        result = prime * result + ((getCreateId() == null) ? 0 : getCreateId().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getCreateWay() == null) ? 0 : getCreateWay().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        result = prime * result + ((getDeleteTime() == null) ? 0 : getDeleteTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", semCode=").append(semCode);
        sb.append(", schoolYearStart=").append(schoolYearStart);
        sb.append(", schoolYearEnd=").append(schoolYearEnd);
        sb.append(", semNum=").append(semNum);
        sb.append(", semDateStart=").append(semDateStart);
        sb.append(", semDateEnd=").append(semDateEnd);
        sb.append(", totalWeeks=").append(totalWeeks);
        sb.append(", isCurrentSem=").append(isCurrentSem);
        sb.append(", createId=").append(createId);
        sb.append(", createBy=").append(createBy);
        sb.append(", createWay=").append(createWay);
        sb.append(", createTime=").append(createTime);
        sb.append(", remark=").append(remark);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", deleteTime=").append(deleteTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}