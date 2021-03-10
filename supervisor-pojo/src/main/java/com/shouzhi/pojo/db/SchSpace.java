package com.shouzhi.pojo.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * wr_sch_space
 * @author 
 */
public class SchSpace implements Serializable {
    /**
     * 主键(新增操作忽略，更新操作必填)
     */
    private String id;

    /**
     * 父结点id，选中左侧节点时可以获取到
     */
    private String parentId;

    /**
     * 父结点id列表串，由后台系统代码自动生成
     * @ignore
     */
    @JsonIgnore
    private String parentIds;

    /**
     * 空间编码，一般为空间名称的缩写，汉字拼音首字母大写，字母数字则不变，不允许带中文，同一父级下不允许重复。
     * 如：
     * 教学楼A编码为JXLA，A101编码为A101，则JXLA下不允许有两个A101、
     * 本部校区编码为BBXQ，教学楼A编码为JXLA，则BBXQ下不允许有两个JXLA
     */
    private String spaceCode;

    /**
     * 父节点空间编码
     * @ignore
     */
    @JsonIgnore
    private String parentSpaceCode;

    /**
     * 父节点空间编码列表串，由后台系统代码自动生成
     * @ignore
     */
    @JsonIgnore
    private String parentSpaceCodes;

    /**
     * 空间名称(教学楼名称或班级门牌号)
     */
    private String spaceName;

    /**
     * 父节点空间名称
     * @ignore
     */
    @JsonIgnore
    private String parentSpaceName;

    /**
     * 父节点空间名称列表串，由后台系统代码自动生成
     * @ignore
     */
    private String parentSpaceNames;

    /**
     * 空间名称别名
     */
    private String spaceAlias;

    /**
     * 空间类型，1：学校，2：校区，3：楼宇，4：楼层，5：教室
     */
    private String spaceType;

    /**
     * 排序号
     */
    private Integer sortNum;

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
     * 删除人id，用于连表不用于显示
     * @ignore
     */
    @JsonIgnore
    private String deleteId;

    /**
     * 删除人
     * @ignore
     */
    @JsonIgnore
    private String deleteBy;

    /**
     * 删除时间
     * @ignore
     */
    @JsonIgnore
    private Date deleteTime;

    private static final long serialVersionUID = 1L;

    /**
     * 非数据库字段，仅用于条件查询，不用作接收不用作返回
     * @ignore
     */
    @JsonIgnore
    private String parentIdsLike;

    /**
     * 非数据库字段，仅用于条件查询，不用作接收不用作返回
     * @ignore
     */
    @JsonIgnore
    private String includeParentRegion;


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

    public String getSpaceCode() {
        return spaceCode;
    }

    public void setSpaceCode(String spaceCode) {
        this.spaceCode = spaceCode;
    }

    public String getParentSpaceCode() {
        return parentSpaceCode;
    }

    public void setParentSpaceCode(String parentSpaceCode) {
        this.parentSpaceCode = parentSpaceCode;
    }

    public String getParentSpaceCodes() {
        return parentSpaceCodes;
    }

    public void setParentSpaceCodes(String parentSpaceCodes) {
        this.parentSpaceCodes = parentSpaceCodes;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public String getParentSpaceName() {
        return parentSpaceName;
    }

    public void setParentSpaceName(String parentSpaceName) {
        this.parentSpaceName = parentSpaceName;
    }

    public String getParentSpaceNames() {
        return parentSpaceNames;
    }

    public void setParentSpaceNames(String parentSpaceNames) {
        this.parentSpaceNames = parentSpaceNames;
    }

    public String getSpaceAlias() {
        return spaceAlias;
    }

    public void setSpaceAlias(String spaceAlias) {
        this.spaceAlias = spaceAlias;
    }

    public String getSpaceType() {
        return spaceType;
    }

    public void setSpaceType(String spaceType) {
        this.spaceType = spaceType;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
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

    public String getDeleteId() {
        return deleteId;
    }

    public void setDeleteId(String deleteId) {
        this.deleteId = deleteId;
    }

    public String getDeleteBy() {
        return deleteBy;
    }

    public void setDeleteBy(String deleteBy) {
        this.deleteBy = deleteBy;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public String getParentIdsLike() {
        return parentIdsLike;
    }

    public void setParentIdsLike(String parentIdsLike) {
        this.parentIdsLike = parentIdsLike;
    }

    public String getIncludeParentRegion() {
        return includeParentRegion;
    }

    public void setIncludeParentRegion(String includeParentRegion) {
        this.includeParentRegion = includeParentRegion;
    }

    @Override
    public String toString() {
        return "SchSpace{" +
                "id='" + id + '\'' +
                ", parentId='" + parentId + '\'' +
                ", parentIds='" + parentIds + '\'' +
                ", spaceCode='" + spaceCode + '\'' +
                ", parentSpaceCode='" + parentSpaceCode + '\'' +
                ", parentSpaceCodes='" + parentSpaceCodes + '\'' +
                ", spaceName='" + spaceName + '\'' +
                ", parentSpaceName='" + parentSpaceName + '\'' +
                ", parentSpaceNames='" + parentSpaceNames + '\'' +
                ", spaceAlias='" + spaceAlias + '\'' +
                ", spaceType='" + spaceType + '\'' +
                ", sortNum=" + sortNum +
                ", createId='" + createId + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createWay='" + createWay + '\'' +
                ", createTime=" + createTime +
                ", remark='" + remark + '\'' +
                ", isDelete='" + isDelete + '\'' +
                ", deleteId='" + deleteId + '\'' +
                ", deleteBy='" + deleteBy + '\'' +
                ", deleteTime=" + deleteTime +
                '}';
    }
}