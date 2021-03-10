package com.shouzhi.pojo.custom;

import java.io.Serializable;
import java.util.Date;

/**
 * ApiDocTest测试类
 */
public class DocTestModel implements Serializable {

    /**
     * ID字段|主键
     * @required
     * @since v1.0
     * @apiNote 字段级别不可用
     * @see DocTestModel
     */
    private Long id;

    /**
     * 教师ID
     * //此处也会被显示
     * @description 这里可以写补充性注释或其他注释，不会被生成至文档
     * @required
     */
    private Long teaId;

    /**
     * 从smart-doc 1.8.0开始，mock tag用于在对象基本类型字段设置自定义文档展示值。设置值后smart-doc不再帮你生成随机值。方便可以通过smart-doc直接输出交付文档。
     * @required
     * @mock 13
     */
    private String regWxOpenid;

    // 这种注释不会被读取到
    private String regWxNickname;

    private Integer regWxSex;

    private String regWxLanguage;

    private String regWxCity;

    private String regWxProvince;

    private String regWxCountry;

    private String regWxHeadimgurl;

    /**
     * @ignore
     */
    private String createBy;

    /**
     * @ignore
     */
    private Date createTime;

    /**
     * @ignore
     */
    private String remark;

    /**
     * @ignore
     */
    private String isDelete;

    /**
     * 没有Get、Set方法
     */
    private String noGetSet;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTeaId() {
        return teaId;
    }

    public void setTeaId(Long teaId) {
        this.teaId = teaId;
    }

    public String getRegWxOpenid() {
        return regWxOpenid;
    }

    public void setRegWxOpenid(String regWxOpenid) {
        this.regWxOpenid = regWxOpenid == null ? null : regWxOpenid.trim();
    }

    public String getRegWxNickname() {
        return regWxNickname;
    }

    public void setRegWxNickname(String regWxNickname) {
        this.regWxNickname = regWxNickname == null ? null : regWxNickname.trim();
    }

    public Integer getRegWxSex() {
        return regWxSex;
    }

    public void setRegWxSex(Integer regWxSex) {
        this.regWxSex = regWxSex;
    }

    public String getRegWxLanguage() {
        return regWxLanguage;
    }

    public void setRegWxLanguage(String regWxLanguage) {
        this.regWxLanguage = regWxLanguage == null ? null : regWxLanguage.trim();
    }

    public String getRegWxCity() {
        return regWxCity;
    }

    public void setRegWxCity(String regWxCity) {
        this.regWxCity = regWxCity == null ? null : regWxCity.trim();
    }

    public String getRegWxProvince() {
        return regWxProvince;
    }

    public void setRegWxProvince(String regWxProvince) {
        this.regWxProvince = regWxProvince == null ? null : regWxProvince.trim();
    }

    public String getRegWxCountry() {
        return regWxCountry;
    }

    public void setRegWxCountry(String regWxCountry) {
        this.regWxCountry = regWxCountry == null ? null : regWxCountry.trim();
    }

    public String getRegWxHeadimgurl() {
        return regWxHeadimgurl;
    }

    public void setRegWxHeadimgurl(String regWxHeadimgurl) {
        this.regWxHeadimgurl = regWxHeadimgurl == null ? null : regWxHeadimgurl.trim();
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
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
        this.remark = remark == null ? null : remark.trim();
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete == null ? null : isDelete.trim();
    }


}