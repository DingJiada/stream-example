package com.shouzhi.pojo.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用于前端menu展示
 * @author WX
 */
public class SysPermissionVo implements Serializable {
    /**
     * 主键
     */
    private String id;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源类型，1：一级菜单，2：二级菜单，button：按钮
     */
    private String type;

    /**
     * 访问url地址
     */
    private String url;

    /**
     * 权限代码字符串（可用来判断是否拥有该权限,避免拿汉字来判断）
     */
    private String percode;

    /**
     * 父结点id
     */
    private String parentId;

    /**
     * 父结点id列表串
     */
    private String parentIds;

    /**
     * 排序号
     */
    private Integer sortNum;

    /**
     * 图标样式
     */
    private String iconStyle;

    /**
     * 图标地址
     */
    private String iconUrl;

    /**
     * 子节点
     */
    private List<SysPermissionVo> children;


    private static final long serialVersionUID = 1L;

    public SysPermissionVo() {
    }

    public SysPermissionVo(String id, String name, String type, String url, String percode, String parentId, String parentIds, Integer sortNum, String iconStyle, String iconUrl, List<SysPermissionVo> children) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.url = url;
        this.percode = percode;
        this.parentId = parentId;
        this.parentIds = parentIds;
        this.sortNum = sortNum;
        this.iconStyle = iconStyle;
        this.iconUrl = iconUrl;
        this.children = children;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPercode() {
        return percode;
    }

    public void setPercode(String percode) {
        this.percode = percode;
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

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public String getIconStyle() {
        return iconStyle;
    }

    public void setIconStyle(String iconStyle) {
        this.iconStyle = iconStyle;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public List<SysPermissionVo> getChildren() {
        return children;
    }

    public void setChildren(List<SysPermissionVo> children) {
        this.children = children;
    }
}