package com.shouzhi.pojo.db;

import java.io.Serializable;
import java.util.Date;

/**
 * wr_sch_course_table_base_grade_class
 * @author 
 */
public class SchCourseTableBaseGradeClass implements Serializable {
    /**
     * 主键(新增操作忽略，更新操作必填)
     */
    private String id;

    /**
     * 学校基础课程表id
     */
    private String schCourseTableBaseId;

    /**
     * 学校年级班级表id
     */
    private String schGradeClassId;

    /**
     * 创建人id，用于连表不用于显示
     */
    private String createId;

    /**
     * 创建人账号，不建议填名字，账号更改的可能性很小且又比ID稍微形象化
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 备注
     */
    private String remark;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchCourseTableBaseId() {
        return schCourseTableBaseId;
    }

    public void setSchCourseTableBaseId(String schCourseTableBaseId) {
        this.schCourseTableBaseId = schCourseTableBaseId;
    }

    public String getSchGradeClassId() {
        return schGradeClassId;
    }

    public void setSchGradeClassId(String schGradeClassId) {
        this.schGradeClassId = schGradeClassId;
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
        SchCourseTableBaseGradeClass other = (SchCourseTableBaseGradeClass) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getSchCourseTableBaseId() == null ? other.getSchCourseTableBaseId() == null : this.getSchCourseTableBaseId().equals(other.getSchCourseTableBaseId()))
            && (this.getSchGradeClassId() == null ? other.getSchGradeClassId() == null : this.getSchGradeClassId().equals(other.getSchGradeClassId()))
            && (this.getCreateId() == null ? other.getCreateId() == null : this.getCreateId().equals(other.getCreateId()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSchCourseTableBaseId() == null) ? 0 : getSchCourseTableBaseId().hashCode());
        result = prime * result + ((getSchGradeClassId() == null) ? 0 : getSchGradeClassId().hashCode());
        result = prime * result + ((getCreateId() == null) ? 0 : getCreateId().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", schCourseTableBaseId=").append(schCourseTableBaseId);
        sb.append(", schGradeClassId=").append(schGradeClassId);
        sb.append(", createId=").append(createId);
        sb.append(", createBy=").append(createBy);
        sb.append(", createTime=").append(createTime);
        sb.append(", remark=").append(remark);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}