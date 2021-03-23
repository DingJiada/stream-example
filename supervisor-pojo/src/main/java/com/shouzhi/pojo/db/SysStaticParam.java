package com.shouzhi.pojo.db;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * wr_sys_static_param
 * @author 
 */
public class SysStaticParam implements Serializable {
    /**
     * 主键(新增操作忽略，更新操作必填)
     */
    private String id;

    /**
     * 父结点id，选中左侧节点时可以获取到
     * @ignore
     */
    private String parentId;

    /**
     * 参数名称
     * @ignore
     */
    private String paramName;

    /**
     * 参数值
     */
    private String paramVal;

    /**
     * 参数描述，用来解释这个参数代表什么意思，展示给用户看的
     * @ignore
     */
    private String paramDesc;

    /**
     * 展示类型（0：未知，1：span文本，2：text输入框，3：img图像，4：img图像列表，5：switch开关）
     * @ignore
     */
    private String showType;

    /**
     * 备注，开发人员自己看的，不需要展示
     * @ignore
     */
    @JsonIgnore
    private String remark;

    private static final long serialVersionUID = 1L;

    public SysStaticParam() {
    }

    public SysStaticParam(String id, String paramVal) {
        this.id = id;
        this.paramVal = paramVal;
    }

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

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamVal() {
        return paramVal;
    }

    public void setParamVal(String paramVal) {
        this.paramVal = paramVal;
    }

    public String getParamDesc() {
        return paramDesc;
    }

    public void setParamDesc(String paramDesc) {
        this.paramDesc = paramDesc;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "SysStaticParam{" +
                "id='" + id + '\'' +
                ", parentId='" + parentId + '\'' +
                ", paramName='" + paramName + '\'' +
                ", paramVal='" + paramVal + '\'' +
                ", paramDesc='" + paramDesc + '\'' +
                ", showType='" + showType + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}