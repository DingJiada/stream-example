package com.shouzhi.controller.api;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.controller.BaseController;
import com.shouzhi.pojo.db.SysPermission;
import com.shouzhi.pojo.vo.MenuAndPermVo;
import com.shouzhi.pojo.vo.PageInfoVo;
import com.shouzhi.pojo.vo.SysPermissionVo;
import com.shouzhi.pojo.vo.TreeNodeVo;
import com.shouzhi.service.interf.db.ISysPermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统菜单(资源权限)接口
 * @author WX
 * @date 2020-06-10 09:57:05
 */
@RestController
@RequestMapping("/api/v1/sysPermission")
public class SysPermissionController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISysPermissionService sysPermissionService;

    /**
     * 查询系统菜单(资源权限)树
     * @apiNote 查询系统菜单(资源权限)树，菜单树分两种：前端平台菜单、后端平台菜单。用处：点击父节点查询子节点
     * @param ascriptionType 归属类型，1：后端平台的菜单，2：前端平台的菜单
     * @author WX
     * @date 2020-10-30 10:03:16
     */
    @PostMapping("/findTree")
    public CommonResult<List<TreeNodeVo>> findTree(@RequestParam("ascriptionType") String ascriptionType, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(),JSON.toJSONString(req.getParameterMap()));
        CommonResult<List<TreeNodeVo>> result = new CommonResult<>();
        List<TreeNodeVo> tree = sysPermissionService.findTree(ascriptionType, req);
        return result.setStatus(1).setMsg("查询成功").setResultBody(tree);
    }

    /**
     * 查询系统菜单(资源权限)列表
     * @apiNote 查询系统菜单(资源权限)列表
     * @description 查询接口编写注意,不可使用对应的Model去接收,参数少就挨个写上,参数若真多只能再新建个VoModel
     *              因为新增和修改用的Model接收,里边必须及非必须参数会对接口使用人造成误解,新增或修改里必须参数在查询接口内不见得是必须
     * @param pageNum 页码(如第1页)
     * @param pageSize 每页数量(如每页10条)
     * @param type 资源类型，1：一级菜单，2：二级菜单，button：按钮，btn_menu：按钮菜单(内联菜单)
     * @param ascriptionType 归属类型，1：后端平台的菜单，2：前端平台的菜单
     * @param parentId 父结点id，不可与includeParentRegion同时使用
     * @param includeParentRegion 包含父区域，值为‘父结点id’，<b>不可</b>与parentId同时使用
     * @author WX
     * @date 2020-06-10 10:19:28
     */
    @PostMapping("/findList/{pageNum}/{pageSize}")
    public CommonResult<PageInfoVo<SysPermission>> findList(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
                                                            @RequestParam(value="type",required=false) String type,
                                                            @RequestParam(value="ascriptionType",required=false) String ascriptionType,
                                                            @RequestParam(value="parentId",required=false) String parentId,
                                                            @RequestParam(value="includeParentRegion",required=false) String includeParentRegion, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(),JSON.toJSONString(req.getParameterMap()));
        CommonResult<PageInfoVo<SysPermission>> result = new CommonResult<>();
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("ascriptionTypeLike", ascriptionType);
        map.put("parentId", parentId);
        map.put("includeParentRegion", includeParentRegion);
        PageHelper.startPage(pageNum,pageSize);
        List<SysPermission> sysPermissions = sysPermissionService.queryListByPage(map);
        PageInfo<SysPermission> pageInfo = new PageInfo<>(sysPermissions);
        return result.setStatus(1).setMsg("查询成功").setResultBody(this.filterPage(pageInfo));
    }

    /**
     * 新增系统菜单(资源权限)
     * @apiNote 新增系统菜单(资源权限)，本业务含有图片上传，请注意Content-Type不再是application/json; charset=utf-8
     *          而是multipart/form-data
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param sysPermission
     * @param imgFile file input标签的name值
     * @author WX
     * @date 2020-06-10 10:22:42
     */
    @PostMapping("/save/{permId}")
    public CommonResult<String> savePermission(@PathVariable("permId") String permId, SysPermission sysPermission,
                                               @RequestParam(value="imgFile", required=false) MultipartFile imgFile, HttpServletRequest req) {
        logger.info("url={},sysPermission={}", req.getServletPath(),JSON.toJSONString(sysPermission));
        return sysPermissionService.save(sysPermission,imgFile,permId, req);
    }


    /**
     * 修改系统菜单(资源权限)
     * @apiNote 修改系统菜单(资源权限)，本业务含有图片上传，请注意Content-Type不再是application/json; charset=utf-8
     *          而是multipart/form-data
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param sysPermission
     * @param imgFile file input标签的name值
     * @author WX
     * @date 2020-06-11 14:07:25
     */
    @PutMapping("/update/{permId}")
    public CommonResult<String> updatePermission(@PathVariable("permId") String permId, SysPermission sysPermission,
                                                 @RequestParam(value="imgFile", required=false) MultipartFile imgFile, HttpServletRequest req) {
        logger.info("url={},sysPermission={}", req.getServletPath(),JSON.toJSONString(sysPermission));
        CommonResult<String> result = new CommonResult<>();
        try {
            sysPermissionService.update(sysPermission, imgFile, permId, req);
            result.setStatus(1).setMsg("更新成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }

    /**
     * 删除系统菜单(资源权限)
     * @apiNote 删除系统菜单(资源权限)，系统菜单的删除会<b>级联删除</b>与该菜单关联的<b>子菜单集</b>
     *          及与该菜单关联的<b>角色下的权限</b>和与该菜单关联的<b>子菜单集</b>关联的<b>角色下的权限</b>，
     *          请提示用户谨慎操作
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param rowId 被操作记录行ID
     * @author WX
     * @date 2020-06-11 16:52:28
     */
    @DeleteMapping("/delete/{permId}/{rowId}")
    public CommonResult<String> delByPermissionId(@PathVariable("permId") String permId, @PathVariable("rowId") String rowId,
                                                  HttpServletRequest req){
        logger.info("url={}", req.getServletPath());
        CommonResult<String> result = new CommonResult<>();
        try {
            sysPermissionService.delete(rowId, permId, req);
            result.setStatus(1).setMsg("删除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("DB_SQL_DELETE_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 批量删除系统菜单(资源权限)
     * @apiNote 批量删除系统菜单(资源权限)，系统菜单的批量删除会<b>级联批量删除</b>与该菜单关联的<b>子菜单集</b>
     *          及与该菜单关联的<b>角色下的权限</b>和与该菜单关联的<b>子菜单集</b>关联的<b>角色下的权限</b>，
     *          请提示用户谨慎操作
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param sysPermissionIds 系统资源权限ID，对应系统资源权限信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @author WX
     * @date 2020-11-03 10:06:05
     */
    @DeleteMapping(value = "/batchDelete/{permId}", consumes = "application/x-www-form-urlencoded")
    public CommonResult<String> batchDelBySysPermissionIds(@PathVariable("permId") String permId, @RequestParam("sysPermissionIds") String sysPermissionIds,
                                                  HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            sysPermissionService.batchDelete(sysPermissionIds, permId, req);
            result.setStatus(1).setMsg("删除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("DB_SQL_DELETE_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 查询单条系统菜单(资源权限)记录
     * @apiNote 查询单条系统资源权限记录，根据ID
     * @param rowId 被操作记录行ID
     * @author WX
     * @date 2020-06-11 18:21:04
     */
    @GetMapping("/find/{rowId}")
    public CommonResult<SysPermission> findByPermissionId(@PathVariable("rowId") String rowId, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        SysPermission SysPermission = sysPermissionService.selectByPrimaryKey(rowId);
        return CommonResult.<SysPermission>getInstance().setStatus(1).setMsg("查询成功").setResultBody(SysPermission);
    }


    /**
     * 获取用户菜单(资源权限)列表
     * @apiNote 获取用户动态菜单(资源权限)列表，登录成功后调用，会返回前端及后端的全部菜单和权限集合。
     *          后端的全部菜单和权限集合：一般用于后台管理页面左侧手风琴动态菜单和每个页面按钮权限控制
     *          前端的全部菜单和权限集合：一般用于前台页眉顶部导航动态菜单和每个页面按钮权限控制
     * @author WX
     * @date 2020-06-15 16:39:51
     */
    @GetMapping("/menuPerms")
    public CommonResult<MenuAndPermVo> menuPerms(HttpServletRequest req) throws Exception {
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        MenuAndPermVo menuAndPerms = sysPermissionService.getMenuAndPerms(req);
        return CommonResult.<MenuAndPermVo>getInstance().setStatus(1).setMsg("查询成功").setResultBody(menuAndPerms);
    }

}
