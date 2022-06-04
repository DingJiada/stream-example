package com.shouzhi.pojo.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * wr_sys_user
 * @author 
 */
public class SysUser implements Serializable {

    /**
     * 主键(新增操作忽略，更新操作必填)
     */
    private String id;

    /**
     * 姓名(人名)
     */
    private String personName;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别，1:男，2:女，0：未知
     */
    private String sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 用户编号（例：学生有学号，教师有工号）
     */
    private String personNum;

    /**
     * 职称/职位
     */
    private String position;

    /**
     * 个人简介
     */
    private String profile;

    /**
     * 头像url
     */
    private String headImgUrl;

    /**
     * 是否注册，1：注册，0未注册，-1已注销
     */
    private String isRegistered;

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
     * 创建方式（0：程序代码创建，1：导入创建）
     * @ignore
     */
    private String createWay;

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
     * 角色集合
     * @ignore
     */
    @JsonIgnore
    private List<SysRole> sysRoles;

    /**
     * 系统角色名称串
     * @ignore
     */
    private String sysRoleNames;

    /**
     * 系统部门名称串
     * @ignore
     */
    private String sysDepartmentNames;

    /**
     * 系统角色Ids串，多个使用英文逗号分隔
     * @ignore
     */
    private String sysRoleIds;

    /**
     * 系统角色ID(查询条件)
     * @ignore
     */
    @JsonIgnore
    private String sysRoleId;

    /**
     * 查询条件
     * @ignore
     */
    @JsonIgnore
    private String personNumOrName;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPersonNum() {
        return personNum;
    }

    public void setPersonNum(String personNum) {
        this.personNum = personNum;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getIsRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(String isRegistered) {
        this.isRegistered = isRegistered;
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

    public List<SysRole> getSysRoles() {
        return sysRoles;
    }

    public void setSysRoles(List<SysRole> sysRoles) {
        this.sysRoles = sysRoles;
    }

    public String getSysRoleNames() {
        return sysRoleNames;
    }

    public void setSysRoleNames(String sysRoleNames) {
        this.sysRoleNames = sysRoleNames;
    }

    public String getSysDepartmentNames() {
        return sysDepartmentNames;
    }

    public void setSysDepartmentNames(String sysDepartmentNames) {
        this.sysDepartmentNames = sysDepartmentNames;
    }

    public String getSysRoleIds() {
        return sysRoleIds;
    }

    public void setSysRoleIds(String sysRoleIds) {
        this.sysRoleIds = sysRoleIds;
    }

    public String getSysRoleId() {
        return sysRoleId;
    }

    public void setSysRoleId(String sysRoleId) {
        this.sysRoleId = sysRoleId;
    }

    public String getPersonNumOrName() {
        return personNumOrName;
    }

    public void setPersonNumOrName(String personNumOrName) {
        this.personNumOrName = personNumOrName;
    }
}