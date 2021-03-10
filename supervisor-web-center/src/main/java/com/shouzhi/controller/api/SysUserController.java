package com.shouzhi.controller.api;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.basic.common.ErrorCodeEnum;
import com.shouzhi.basic.constants.FileTypeConstants;
import com.shouzhi.basic.utils.ExcelRead;
import com.shouzhi.basic.utils.FileDownloadUtil;
import com.shouzhi.controller.BaseController;
import com.shouzhi.pojo.db.SysUser;
import com.shouzhi.pojo.vo.PageInfoVo;
import com.shouzhi.service.constants.FilePathConst;
import com.shouzhi.service.interf.db.ISysUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 系统用户接口
 * @author WX
 * @date 2020-06-15 10:02:35
 */
@RestController
@RequestMapping("/api/v1/sysUser")
public class SysUserController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISysUserService sysUserService;

    /**
     * 后台管理-查询系统用户列表
     * @apiNote 后台管理-查询系统用户列表
     * @description 查询接口编写注意,不可使用对应的Model去接收,参数少就挨个写上,参数若真多只能再新建个VoModel
     *              因为新增和修改用的Model接收,里边必须及非必须参数会对接口使用人造成误解,新增或修改里必须参数在查询接口内不见得是必须
     * @param pageNum 页码(如第1页)
     * @param pageSize 每页数量(如每页10条)
     * @param personNumOrName 真实姓名 或 用户编号（例：学生有学号，教师有工号）
     * @param sex 性别
     * @param isRegistered 是否注册(注册状态)
     * @param sysRoleId 系统角色ID，对应角色列表内的每一行的ID
     * @author WX
     * @date 2020-06-15 10:06:28
     */
    @PostMapping("/findList/{pageNum}/{pageSize}")
    public CommonResult<PageInfoVo<SysUser>> findList(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
                                                      @RequestParam(value="personNumOrName",required=false) String personNumOrName,
                                                      @RequestParam(value="sex",required=false) String sex,
                                                      @RequestParam(value="isRegistered",required=false) String isRegistered,
                                                      @RequestParam(value="sysRoleId",required=false) String sysRoleId, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<PageInfoVo<SysUser>> result = new CommonResult<>();
        SysUser sysUser = new SysUser();
        sysUser.setPersonNumOrName(personNumOrName);
        sysUser.setSex(sex);
        sysUser.setIsRegistered(isRegistered);
        sysUser.setSysRoleId(sysRoleId);
        PageHelper.startPage(pageNum,pageSize);
        List<SysUser> sysUsers = sysUserService.queryListByPage(sysUser);
        PageInfo<SysUser> pageInfo = new PageInfo<>(sysUsers);
        return result.setStatus(1).setMsg("查询成功").setResultBody(this.filterPage(pageInfo));
    }

    /**
     * 后台管理-新增系统用户
     * @apiNote 后台管理-新增系统用户
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param personNum 用户编号（例：学生有学号，教师有工号）
     * @param personName 真实姓名
     * @param sysRoleIds 系统角色ID，对应角色列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param selectedRegister 是否自动注册账户，是为1，否为0
     * @author WX
     * @date 2020-06-15 10:07:35
     */
    @PostMapping("/save/{permId}")
    public CommonResult<String> saveSysUser(@PathVariable("permId") String permId, @RequestParam("personNum") String personNum,
                                            @RequestParam("personName") String personName, @RequestParam("sysRoleIds") String sysRoleIds,
                                            @RequestParam("selectedRegister") String selectedRegister, HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(),JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            sysUserService.saveSysUserAndRole(personNum, personName, sysRoleIds, selectedRegister, permId, req);
            result.setStatus(1).setMsg("新增成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("DB_SQL_INSERT_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 后台管理-修改系统用户信息
     * @apiNote 后台管理-修改系统用户
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param sysUser
     * @author WX
     * @date 2020-06-15 10:08:45
     */
    @PutMapping("/update/{permId}")
    public CommonResult<String> updateSysUserById(@PathVariable("permId") String permId,
                                               @RequestBody SysUser sysUser, HttpServletRequest req) {
        logger.info("url={},sysUser={}", req.getServletPath(),JSON.toJSONString(sysUser));
        CommonResult<String> result = new CommonResult<>();
        try {
            sysUserService.update(sysUser, permId, req);
            result.setStatus(1).setMsg("更新成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("DB_SQL_UPDATE_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 后台管理-删除系统用户
     * @apiNote 后台管理-删除系统用户
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param rowId 被操作记录行ID(路径参数)
     * @author WX
     * @date 2020-06-15 10:10:08
     */
    @DeleteMapping("/delete/{permId}/{rowId}")
    public CommonResult<String> delByUserId(@PathVariable("permId") String permId, @PathVariable("rowId") String rowId,
                                            HttpServletRequest req){
        logger.info("url={}", req.getServletPath());
        CommonResult<String> result = new CommonResult<>();
        try {
            sysUserService.delete(rowId, permId, req);
            result.setStatus(1).setMsg("删除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;

    }


    /**
     * 后台管理-批量删除系统用户
     * @apiNote 后台管理-批量删除系统用户
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param sysUserIds 用户ID，对应用户信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @author WX
     * @date 2020-07-30 20:08:16
     */
    @DeleteMapping(value = "/batchDelete/{permId}", consumes = "application/x-www-form-urlencoded")
    public CommonResult<String> batchDelByUserIds(@PathVariable("permId") String permId, @RequestParam("sysUserIds") String sysUserIds,
                                            HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            sysUserService.batchDelete(sysUserIds, permId, req);
            result.setStatus(1).setMsg("删除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("DB_SQL_DELETE_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 后台管理-导入系统用户
     * @apiNote 后台管理-导入系统用户
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param excelFile input标签的name值
     * @param sysRoleIds 系统角色ID，对应角色列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @param selectedRegister 是否自动注册账户，是为1，否为0
     * @author WX
     * @date 2021-02-04 16:42:23
     */
    @PostMapping("/imp/{permId}")
    public CommonResult impSysUser(@PathVariable("permId") String permId, HttpServletRequest req,
                                   @RequestParam("excelFile") MultipartFile excelFile, @RequestParam("sysRoleIds") String sysRoleIds,
                                   @RequestParam("selectedRegister") String selectedRegister) {
        logger.info("url={},ParameterMap={}，file.isEmpty={}，file.getSize={}，file.getContentType={}，file.getOriginalFilename={}，", req.getServletPath(), JSON.toJSONString(req.getParameterMap()),excelFile.isEmpty(),excelFile.getSize(),excelFile.getContentType(),excelFile.getOriginalFilename());
        CommonResult<String> result = new CommonResult<>();
        try {
            Integer count = sysUserService.impSysUser(permId, excelFile, sysRoleIds, selectedRegister, req);
            result.setStatus(1).setMsg("导入成功"+count+"条");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


    /**
     * 后台管理-查询单条系统用户
     * @apiNote 后台管理-查询单条系统用户记录，根据ID
     * @param rowId 被操作记录行ID(路径参数)
     * @author WX
     * @date 2020-06-15 10:11:20
     */
    @GetMapping("/find/{rowId}")
    public CommonResult<SysUser> findByUserId(@PathVariable("rowId") String rowId, HttpServletRequest req){
        logger.info("url={}", req.getServletPath());
        SysUser sysUser = sysUserService.selectByPrimaryKey(rowId);
        return CommonResult.<SysUser>getInstance().setStatus(1).setMsg("查询成功").setResultBody(sysUser);
    }

    /**
     * 后台管理-查询多条系统用户
     * @apiNote 后台管理-查询多条系统用户记录，根据多个ID，无分页。
     * @param sysUserIds 用户ID，对应用户信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @author WX
     * @date 2020-08-06 10:22:16
     */
    @PostMapping("/manyByIds")
    public CommonResult<List<SysUser>> findManyByIds(@RequestParam("sysUserIds") String sysUserIds,
                                                    HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<List<SysUser>> result = new CommonResult<>();
        Map<String,Object> map = new HashMap<>();
        map.put("list",Arrays.asList(sysUserIds.split(",")));
        map.put("idIn","1");
        List<SysUser> sysUsers = sysUserService.BatchSelect(map);
        return result.setStatus(1).setMsg("查询成功").setResultBody(sysUsers);
    }


    /**
     * 后台管理-一键注册
     * @apiNote 后台管理-一键注册，注册后的账号和密码均为当前选中行的用户编号（例：学生有学号，教师有工号）
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param rowId 被操作记录行ID(路径参数)
     * @author WX
     * @date 2020-07-29 14:41:26
     */
    @PostMapping("/toRegister/{permId}/{rowId}")
    public CommonResult<String> toRegister(@PathVariable("permId") String permId, @PathVariable("rowId") String rowId, HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(),JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            sysUserService.toRegister(rowId, permId, req);
            result.setStatus(1).setMsg("注册成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


    /**
     * 后台管理-批量注册
     * @apiNote 后台管理-批量注册，注册后的账号和密码均为当前选中行的每行所对应的用户编号（例：学生有学号，教师有工号）
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param sysUserIds 用户ID，对应用户信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @author WX
     * @date 2020-07-30 18:01:46
     */
    @PostMapping("/toBatchRegister/{permId}")
    public CommonResult<String> toBatchRegister(@PathVariable("permId") String permId, @RequestParam("sysUserIds") String sysUserIds, HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(),JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            sysUserService.toBatchRegister(sysUserIds, permId, req);
            result.setStatus(1).setMsg("注册成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


    /**
     * 后台管理-根据角色查询系统用户列表
     * @apiNote 后台管理-根据角色相关字段查询系统用户列表，比如角色ID、角色类型、角色code等
     * @description 查询接口编写注意,不可使用对应的Model去接收,参数少就挨个写上,参数若真多只能再新建个VoModel
     *              因为新增和修改用的Model接收,里边必须及非必须参数会对接口使用人造成误解,新增或修改里必须参数在查询接口内不见得是必须
     * @param pageNum 页码(如第1页)
     * @param pageSize 每页数量(如每页10条)
     * @param sysRoleId 系统角色ID，对应角色列表内的每一行的ID
     * @param sysRoleType 系统角色类型，对应角色列表内的每一行的类型
     * @param sysRoleCode 系统角色编码，对应角色列表内的每一行的编码
     * @author WX
     * @date 2020-08-05 16:03:45
     */
    @PostMapping("/usersByRole/{pageNum}/{pageSize}")
    public CommonResult<PageInfoVo<SysUser>> usersByRole(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
                                                         @RequestParam(value="sysRoleId",required=false) String sysRoleId,
                                                         @RequestParam(value="sysRoleType",required=false) String sysRoleType,
                                                         @RequestParam(value="sysRoleCode",required=false) String sysRoleCode, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<PageInfoVo<SysUser>> result = new CommonResult<>();
        Map<String, Object> map = new HashMap<>();
        map.put("sysRoleId", sysRoleId);
        map.put("sysRoleType", sysRoleType);
        map.put("sysRoleCode", sysRoleCode);
        PageHelper.startPage(pageNum,pageSize);
        List<SysUser> sysUsers = sysUserService.selectByRoleParam(map);
        PageInfo<SysUser> pageInfo = new PageInfo<>(sysUsers);
        return result.setStatus(1).setMsg("查询成功").setResultBody(this.filterPage(pageInfo));
    }


    /**
     * 后台管理-查询内置角色系统用户列表
     * @apiNote 后台管理-查询某个<b>内置角色</b>下的<b>系统用户列表</b>，无分页。内置角色编码 可查看文档底部的<b>数据字典</b>。
     *          比如：查询内置角色老师下的用户，sysRoleCode='built_in_teacher'
     * @description 查询接口编写注意,不可使用对应的Model去接收,参数少就挨个写上,参数若真多只能再新建个VoModel
     *              因为新增和修改用的Model接收,里边必须及非必须参数会对接口使用人造成误解,新增或修改里必须参数在查询接口内不见得是必须
     * @param sysRoleCode 系统角色编码，对应角色列表内的每一行的编码
     * @param personNumOrName 真实姓名 或 用户编号（例：学生有学号，教师有工号）
     * @author WX
     * @date 2020-08-05 16:30:19
     */
    @PostMapping("/usersBuiltIn")
    public CommonResult<List<SysUser>> usersBuiltIn(@RequestParam("sysRoleCode") String sysRoleCode,
                                                    @RequestParam(value="personNumOrName",required=false) String personNumOrName,
                                                   HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<List<SysUser>> result = new CommonResult<>();
        Map<String, Object> map = new HashMap<>();
        map.put("personNumOrName", personNumOrName);
        map.put("sysRoleType", "1");
        map.put("sysRoleCode", sysRoleCode);
        List<SysUser> sysUsers = sysUserService.selectByRoleParam(map);
        return result.setStatus(1).setMsg("查询成功").setResultBody(sysUsers);
    }
    // 如果哪天需要分页查了，路径编写参考 /usersBuiltIn/{pageNum}/{pageSize}








    // TODO 应该有‘个人中心’controller，有时间把它挪出去
    /**
     * 个人中心-上传/更新头像
     * @apiNote 修改系统用户头像，本接口带有文件上传，<b>请注意↑↑→Content-Type</b>
     * @param rowId 被操作记录行ID(路径参数)（sysUserId）
     * @param imgFile file input标签的name值
     * @author WX
     * @date 2020-07-17 16:47:54
     */
    @PostMapping("/upload/headImg/{rowId}")
    public CommonResult<String> uploadHeadImgById(@PathVariable("rowId") String rowId, @RequestParam(value="imgFile") MultipartFile imgFile,
                                                  HttpServletRequest req) {
        logger.info("url={}", req.getServletPath());
        CommonResult<String> result = new CommonResult<>();
        try {
            sysUserService.uploadHeadImgById(imgFile, rowId, "-", req);
            result.setStatus(1).setMsg("上传成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


    /**
     * 个人中心-查询基本资料
     * @apiNote 查询个人中心系统用户基本资料，根据用户ID(sysUserId)，非账号ID
     * @param rowId 被操作记录行ID(路径参数)
     * @author WX
     * @date 2020-06-15 10:11:20
     */
    @GetMapping("/basicInfo/{rowId}")
    public CommonResult<SysUser> findbasicInfoById(@PathVariable("rowId") String rowId, HttpServletRequest req){
        logger.info("url={}", req.getServletPath());
        SysUser sysUser = sysUserService.findbasicInfoById(rowId,req);
        return CommonResult.<SysUser>getInstance().setStatus(1).setMsg("查询成功").setResultBody(sysUser);
    }


    /**
     * 个人中心-修改基本资料
     * @apiNote 修改个人中心系统用户基本资料，根据用户ID(sysUserId)，非账号ID
     * @description 个人中心为每个人都能访问的，菜单是死的，无动态菜单这一说，自然也无菜单ID(permId)，所以单独写的接口
     * @param sysUser
     * @author WX
     * @date 2020-07-23 10:33:20
     */
    @PutMapping("/basicInfo")
    public CommonResult<String> updateBasicInfoById(@RequestBody SysUser sysUser, HttpServletRequest req) {
        logger.info("url={},sysUser={}", req.getServletPath(),JSON.toJSONString(sysUser));
        CommonResult<String> result = new CommonResult<>();
        try {
            sysUserService.update(sysUser, "", req);
            result.setStatus(1).setMsg("更新成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("DB_SQL_UPDATE_ERROR"), true, true, logger);
        }
        return result;
    }

}
