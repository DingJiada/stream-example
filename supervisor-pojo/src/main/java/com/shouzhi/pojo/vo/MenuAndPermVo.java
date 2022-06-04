package com.shouzhi.pojo.vo;

import java.util.List;

/**
 * 用于前端菜单和权限展示
 * @author WX
 * @date 2021-01-19 15:02:21
 */
public class MenuAndPermVo {

    /**
     * 拥有后台权限，0：未拥有、1：拥有
     */
    private String hasBgPerms = "0";

    /**
     * 后台菜单集合
     */
    private List<SysPermissionVo> bgMenus;

    /**
     * 后台权限Code集合
     */
    private List<String> bgPermCodes;

    /**
     * 拥有前台权限，0：未拥有、1：拥有
     */
    private String hasFgPerms = "0";

    /**
     * 前台菜单集合
     */
    private List<SysPermissionVo> fgMenus;

    /**
     * 前台权限Code集合
     */
    private List<String> fgPermCodes;


    public MenuAndPermVo() {
    }

    public MenuAndPermVo(String hasBgPerms, List<SysPermissionVo> bgMenus, List<String> bgPermCodes, String hasFgPerms, List<SysPermissionVo> fgMenus, List<String> fgPermCodes) {
        this.hasBgPerms = hasBgPerms;
        this.bgMenus = bgMenus;
        this.bgPermCodes = bgPermCodes;
        this.hasFgPerms = hasFgPerms;
        this.fgMenus = fgMenus;
        this.fgPermCodes = fgPermCodes;
    }

    public String getHasBgPerms() {
        return hasBgPerms;
    }

    public void setHasBgPerms(String hasBgPerms) {
        this.hasBgPerms = hasBgPerms;
    }

    public List<SysPermissionVo> getBgMenus() {
        return bgMenus;
    }

    public void setBgMenus(List<SysPermissionVo> bgMenus) {
        this.bgMenus = bgMenus;
    }

    public List<String> getBgPermCodes() {
        return bgPermCodes;
    }

    public void setBgPermCodes(List<String> bgPermCodes) {
        this.bgPermCodes = bgPermCodes;
    }

    public String getHasFgPerms() {
        return hasFgPerms;
    }

    public void setHasFgPerms(String hasFgPerms) {
        this.hasFgPerms = hasFgPerms;
    }

    public List<SysPermissionVo> getFgMenus() {
        return fgMenus;
    }

    public void setFgMenus(List<SysPermissionVo> fgMenus) {
        this.fgMenus = fgMenus;
    }

    public List<String> getFgPermCodes() {
        return fgPermCodes;
    }

    public void setFgPermCodes(List<String> fgPermCodes) {
        this.fgPermCodes = fgPermCodes;
    }
}
