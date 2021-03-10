package com.shouzhi.pojo.db;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * wr_sys_role
 * @author 
 */
public class SysRole implements Serializable {
    /**
     * 主键(新增操作忽略，更新操作必填)
     */
    private String id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码，自定义角色无编写限制，内置角色需以"built_in_"开头，如：built_in_teacher
     */
    private String roleCode;

    /**
     * 角色具体描述
     */
    private String roleDesc;

    /**
     * 角色类型，1：内置角色，系统上线前就需要提前知道的角色 2：自定义角色
     * @ignore
     */
    private String roleType;

    /**
     * 是否锁定，1：锁定，0未锁定
     */
    private String isLocked;

    /**
     * 创建人id，用于连表不用于显示
     * @ignore
     */
    private String createId;

    /**
     * 创建人账号，不建议填名字，账号更改的可能性很小且又比ID稍微形象化
     * @ignore
     */
    private String createBy;

    /**
     * 创建时间
     * @ignore
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


    /**
     * 权限集合
     * @ignore
     */
    @JsonIgnore
    private List<SysPermission> sysPermissions;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(String isLocked) {
        this.isLocked = isLocked;
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

    public List<SysPermission> getSysPermissions() {
        return sysPermissions;
    }

    public void setSysPermissions(List<SysPermission> sysPermissions) {
        this.sysPermissions = sysPermissions;
    }
}