package com.shouzhi.controller.api;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.basic.common.ErrorCodeEnum;
import com.shouzhi.controller.BaseController;
import com.shouzhi.pojo.db.SysRole;
import com.shouzhi.pojo.db.SysUser;
import com.shouzhi.pojo.vo.PageInfoVo;
import com.shouzhi.service.interf.db.ISysRoleService;
import com.shouzhi.service.interf.db.ISysUserRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 系统角色接口
 * @author WX
 * @date 2020-06-12 10:26:36
 */
@RestController
@RequestMapping("/api/v1/sysRole")
public class SysRoleController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    /**
     * 后台管理-查询系统角色列表
     * @apiNote 查询系统角色列表
     * @description 查询接口编写注意,不可使用对应的Model去接收,参数少就挨个写上,参数若真多只能再新建个VoModel
     *              因为新增和修改用的Model接收,里边必须及非必须参数会对接口使用人造成误解,新增或修改里必须参数在查询接口内不见得是必须
     * @param pageNum 页码(如第1页)
     * @param pageSize 每页数量(如每页10条)
     * @param roleName 角色名称
     * @param roleType 角色类型
     * @author WX
     * @date 2020-06-12 10:26:36
     */
    @PostMapping("/findList/{pageNum}/{pageSize}")
    public CommonResult<PageInfoVo<SysRole>> findList(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
                                                      @RequestParam(value="roleName",required=false) String roleName,
                                                      @RequestParam(value="roleType",required=false) String roleType,
                                                      HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<PageInfoVo<SysRole>> result = new CommonResult<>();
        SysRole sysRole = new SysRole();
        sysRole.setRoleName(roleName);
        sysRole.setRoleType(roleType);
        PageHelper.startPage(pageNum,pageSize);
        List<SysRole> sysRoles = sysRoleService.queryListByPage(sysRole);
        PageInfo<SysRole> pageInfo = new PageInfo<>(sysRoles);
        return result.setStatus(1).setMsg("查询成功").setResultBody(this.filterPage(pageInfo));
    }

    /**
     * 后台管理-新增系统角色
     * @apiNote 新增系统角色
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param sysRole
     * @author WX
     * @date 2020-06-12 10:26:36
     */
    @PostMapping("/save/{permId}")
    public CommonResult<String> saveRole(@PathVariable("permId") String permId, @RequestBody SysRole sysRole, HttpServletRequest req) {
        logger.info("url={},sysRole={}", req.getServletPath(),JSON.toJSONString(sysRole));
        CommonResult<String> result = new CommonResult<>();
        try {
            sysRole.setRoleType("2");
            sysRoleService.save(sysRole, permId, req);
            result.setStatus(1).setMsg("新增成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("DB_SQL_INSERT_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 后台管理-修改系统角色
     * @apiNote 修改系统角色
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param sysRole
     * @author WX
     * @date 2020-06-12 10:26:36
     */
    @PutMapping("/update/{permId}")
    public CommonResult<String> updateRoleById(@PathVariable("permId") String permId,
                                                     @RequestBody SysRole sysRole, HttpServletRequest req) {
        logger.info("url={},sysRole={}", req.getServletPath(),JSON.toJSONString(sysRole));
        CommonResult<String> result = new CommonResult<>();
        try {
            sysRoleService.update(sysRole, permId, req);
            result.setStatus(1).setMsg("更新成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("DB_SQL_UPDATE_ERROR"), true, true, logger);
        }
        return result;
    }

    /**
     * 后台管理-删除系统角色
     * @apiNote 删除系统角色，系统角色的删除会级联删除与该角色关联的其他关联关系，比如：该角色下的用户关联、该角色下的权限关联
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param rowId 被操作记录行ID
     * @author WX
     * @date 2020-06-12 10:26:36
     */
    @DeleteMapping("/delete/{permId}/{rowId}")
    public CommonResult<String> delByRoleId(@PathVariable("permId") String permId, @PathVariable("rowId") String rowId,
                                                  HttpServletRequest req){
        logger.info("url={}", req.getServletPath());
        CommonResult<String> result = new CommonResult<>();
        try {
            sysRoleService.delete(rowId, permId, req);
            result.setStatus(1).setMsg("删除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("DB_SQL_DELETE_ERROR"), true, true, logger);
        }
        return result;

    }

    /**
     * 后台管理-查询单条系统角色记录
     * @apiNote 查询单条系统角色记录，根据ID
     * @param rowId 被操作记录行ID
     * @author WX
     * @date 2020-06-12 10:26:36
     */
    @GetMapping("/find/{rowId}")
    public CommonResult<SysRole> findByRoleId(@PathVariable("rowId") String rowId, HttpServletRequest req){
        logger.info("url={}", req.getServletPath());
        SysRole sysRole = sysRoleService.selectByPrimaryKey(rowId);
        return CommonResult.<SysRole>getInstance().setStatus(1).setMsg("查询成功").setResultBody(sysRole);
    }


    /**
     * 后台管理-查询角色成员
     * @apiNote 根据角色Id查询角色成员(系统用户列表)
     * @description 查询接口编写注意,不可使用对应的Model去接收,参数少就挨个写上,参数若真多只能再新建个VoModel
     *              因为新增和修改用的Model接收,里边必须及非必须参数会对接口使用人造成误解,新增或修改里必须参数在查询接口内不见得是必须
     * @param pageNum 页码(如第1页)
     * @param pageSize 每页数量(如每页10条)
     * @param sysRoleId 角色Id
     * @author WX
     * @date 2020-08-24 10:59:43
     */
    @PostMapping("/findRoleMembers/{pageNum}/{pageSize}")
    public CommonResult<PageInfoVo<SysUser>> findSysUsersByRoleId(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
                                                      @RequestParam("sysRoleId") String sysRoleId, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<PageInfoVo<SysUser>> result = new CommonResult<>();
        PageHelper.startPage(pageNum,pageSize);
        List<SysUser> sysUsers = sysRoleService.findSysUsersByRoleId(sysRoleId);
        PageInfo<SysUser> pageInfo = new PageInfo<>(sysUsers);
        return result.setStatus(1).setMsg("查询成功").setResultBody(this.filterPage(pageInfo));
    }


    /**
     * 后台管理-移除角色成员
     * @apiNote 后台管理-移除角色成员
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param sysRoleId 角色ID
     * @param sysUserId 角色成员ID
     * @author WX
     * @date 2020-08-24 14:09:35
     */
    @DeleteMapping(value = "/delRoleMember/{permId}", consumes = "application/x-www-form-urlencoded")
    public CommonResult<String> delRoleMember(@PathVariable("permId") String permId,
                                              @RequestParam("sysRoleId") String sysRoleId, @RequestParam("sysUserId") String sysUserId,
                                              HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            sysRoleService.delRoleMember(sysRoleId, sysUserId, permId, req);
            result.setStatus(1).setMsg("移除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("DB_SQL_DELETE_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 后台管理-批量移除角色成员
     * @apiNote 后台管理-批量移除角色成员
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param sysRoleId 角色ID
     * @param sysUserIds 角色成员ID，对应角色成员列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @author WX
     * @date 2020-08-24 15:04:36
     */
    @DeleteMapping(value = "/batchDelRoleMember/{permId}", consumes = "application/x-www-form-urlencoded")
    public CommonResult<String> batchDelRoleMember(@PathVariable("permId") String permId,
                                                   @RequestParam("sysRoleId") String sysRoleId, @RequestParam("sysUserIds") String sysUserIds,
                                                   HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            sysRoleService.batchDelRoleMember(sysRoleId, sysUserIds, permId, req);
            result.setStatus(1).setMsg("移除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("DB_SQL_DELETE_ERROR"), true, true, logger);
        }
        return result;
    }





    //--------------------------------------------------sysUserRole部分--------------------------------------------

    /**
     * 后台管理-用户角色-修改用户角色
     * @apiNote 后台管理-用户角色-修改用户角色
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param sysUserId 用户ID
     * @param sysRoleIds 系统角色ID，对应角色列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @author WX
     * @date 2020-07-29 19:27:16
     */
    @PostMapping("/updateSysUserRole/{permId}")
    public CommonResult<String> updateSysUserRole(@PathVariable("permId") String permId, @RequestParam("sysUserId") String sysUserId,
                                                  @RequestParam(value="sysRoleIds", required=false) String sysRoleIds, HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(),JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            sysUserRoleService.updateSysUserRole(sysUserId, sysRoleIds, permId, req);
            result.setStatus(1).setMsg("修改成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }

    /**
     * 后台管理-用户角色-批量修改用户角色
     * @apiNote 后台管理-用户角色-批量修改用户角色
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param sysUserIds 用户ID，对应用户信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param sysRoleIds 系统角色ID，对应角色列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @author WX
     * @date 2020-07-30 09:42:43
     */
    @PostMapping("/batchUpdateSysUserRole/{permId}")
    public CommonResult<String> batchUpdateSysUserRole(@PathVariable("permId") String permId, @RequestParam("sysUserIds") String sysUserIds,
                                                       @RequestParam(value="sysRoleIds", required=false) String sysRoleIds, HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(),JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            sysUserRoleService.batchUpdateSysUserRole(sysUserIds, sysRoleIds, permId, req);
            result.setStatus(1).setMsg("修改成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


}
