package com.shouzhi.controller.api;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.controller.BaseController;
import com.shouzhi.pojo.db.SysDepartment;
import com.shouzhi.pojo.vo.PageInfoVo;
import com.shouzhi.pojo.vo.TreeNodeVo;
import com.shouzhi.service.interf.db.ISysDepartmentService;
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
 * 系统部门(组织结构)接口
 * @author WX
 * @date 2020-11-30 12:35:24
 */
@RestController
@RequestMapping("/api/v1/sysDepartment")
public class SysDepartmentController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISysDepartmentService sysDepartmentService;

    /**
     * 查询系统部门(组织结构)树
     * @apiNote 查询系统部门(组织结构)树，部门树分两种：校区/院/系(学生)、职能部门(老师)。用处：点击父节点查询子节点
     * @param ascriptionType 归属类型，1：校区/院/系或专业(学生)，2：职能部门(老师)
     * @author WX
     * @date 2020-11-30 14:05:16
     */
    @PostMapping("/findTree")
    public CommonResult<List<TreeNodeVo>> findTree(@RequestParam("ascriptionType") String ascriptionType, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(),JSON.toJSONString(req.getParameterMap()));
        CommonResult<List<TreeNodeVo>> result = new CommonResult<>();
        List<TreeNodeVo> tree = sysDepartmentService.findTree(ascriptionType, req);
        return result.setStatus(1).setMsg("查询成功").setResultBody(tree);
    }

    /**
     * 查询系统部门(组织结构)列表
     * @apiNote 查询系统部门(组织结构)列表
     * @description 查询接口编写注意,不可使用对应的Model去接收,参数少就挨个写上,参数若真多只能再新建个VoModel
     *              因为新增和修改用的Model接收,里边必须及非必须参数会对接口使用人造成误解,新增或修改里必须参数在查询接口内不见得是必须
     * @param pageNum 页码(如第1页)
     * @param pageSize 每页数量(如每页10条)
     * @param depType 部门类型：1_1：校区、1_2：院、1_3：系(专业)；职能部门应为2_开头，目前暂时没有用到
     * @param parentId 父结点id，不可与includeParentRegion同时使用
     * @param depName 部门名称
     * @param ascriptionType 归属类型，1：校区/院/系或专业(学生)，2：职能部门(老师)
     * @param includeParentRegion 包含父区域，值为‘父结点id’，<b>不可</b>与parentId同时使用
     * @author WX
     * @date 2020-11-30 14:08:24
     */
    @PostMapping("/findList/{pageNum}/{pageSize}")
    public CommonResult<PageInfoVo<SysDepartment>> findList(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
                                                            @RequestParam(value="depType",required=false) String depType,
                                                            @RequestParam(value="parentId",required=false) String parentId,
                                                            @RequestParam(value="depName",required=false) String depName,
                                                            @RequestParam(value="ascriptionType",required=false) String ascriptionType,
                                                            @RequestParam(value="includeParentRegion",required=false) String includeParentRegion,
                                                            HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(),JSON.toJSONString(req.getParameterMap()));
        CommonResult<PageInfoVo<SysDepartment>> result = new CommonResult<>();
        Map<String, Object> map = new HashMap<>();
        map.put("depType", depType);
        map.put("parentId", parentId);
        map.put("depNameLike", depName);
        map.put("ascriptionTypeLike", ascriptionType);
        map.put("includeParentRegion", includeParentRegion);
        PageHelper.startPage(pageNum,pageSize);
        List<SysDepartment> sysDepartments = sysDepartmentService.queryListByPage(map);
        PageInfo<SysDepartment> pageInfo = new PageInfo<>(sysDepartments);
        return result.setStatus(1).setMsg("查询成功").setResultBody(this.filterPage(pageInfo));
    }

    /**
     * 新增系统部门(组织结构)
     * @apiNote 新增系统部门(组织结构)
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param sysDepartment
     * @author WX
     * @date 2020-11-30 14:14:08
     */
    @PostMapping("/save/{permId}")
    public CommonResult<String> saveDepartment(@PathVariable("permId") String permId, @RequestBody SysDepartment sysDepartment,
                                               HttpServletRequest req) {
        logger.info("url={},SysDepartment={}", req.getServletPath(),JSON.toJSONString(sysDepartment));
        CommonResult<String> result = new CommonResult<>();
        try {
            sysDepartmentService.save(sysDepartment, permId, req);
            result.setStatus(1).setMsg("新增成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


    /**
     * 修改系统部门(组织结构)
     * @apiNote 修改系统部门(组织结构)
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param sysDepartment
     * @author WX
     * @date 2020-11-30 14:23:25
     */
    @PutMapping("/update/{permId}")
    public CommonResult<String> updateDepartment(@PathVariable("permId") String permId, @RequestBody SysDepartment sysDepartment,
                                                 HttpServletRequest req) {
        logger.info("url={},SysDepartment={}", req.getServletPath(),JSON.toJSONString(sysDepartment));
        CommonResult<String> result = new CommonResult<>();
        try {
            sysDepartmentService.update(sysDepartment, permId, req);
            result.setStatus(1).setMsg("更新成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }

    /**
     * 删除系统部门(组织结构)
     * @apiNote 删除系统部门(组织结构)，系统部门的删除会<b>级联删除</b>与该部门关联的<b>子部门集</b>及与该部门关联的<b>用户下的部门</b>，
     *          请提示用户谨慎操作
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param rowId 被操作记录行ID
     * @author WX
     * @date 2020-11-30 14:33:09
     */
    @DeleteMapping("/delete/{permId}/{rowId}")
    public CommonResult<String> delByDepartmentId(@PathVariable("permId") String permId, @PathVariable("rowId") String rowId,
                                                  HttpServletRequest req){
        logger.info("url={}", req.getServletPath());
        CommonResult<String> result = new CommonResult<>();
        try {
            sysDepartmentService.delete(rowId, permId, req);
            result.setStatus(1).setMsg("删除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


    /**
     * 批量删除系统部门(组织结构)
     * @apiNote 批量删除系统部门(组织结构)，系统部门的批量删除会<b>级联批量删除</b>与该部门关联的<b>子部门集</b>及与该部门关联的<b>用户下的部门</b>，
     *          请提示用户谨慎操作
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param sysDepartmentIds 系统部门ID，对应系统部门信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @author WX
     * @date 2020-11-30 14:46:50
     */
    @DeleteMapping(value = "/batchDelete/{permId}", consumes = "application/x-www-form-urlencoded")
    public CommonResult<String> batchDelByDepartmentIds(@PathVariable("permId") String permId, @RequestParam("sysDepartmentIds") String sysDepartmentIds,
                                                           HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            sysDepartmentService.batchDelete(sysDepartmentIds, permId, req);
            result.setStatus(1).setMsg("删除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }

    /**
     * 后台管理-基础设置-组织单位的导入
     * @apiNote 后台管理-基础设置-组织单位的导入
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param excelFile input标签的name值
     * @param parentId 父节点id
     * @param ascriptionType 归属类型，1：校区/院/系或专业(学生)，2：职能部门(老师)
     * @author Dingjd
     * @date 2021/3/15 17:06
     **/
    @PostMapping("/imp/{permId}")
    public CommonResult impDepartment(@PathVariable("permId") String permId, HttpServletRequest req,
                                      @RequestParam("excelFile") MultipartFile excelFile,
                                      @RequestParam("parentId") String parentId,
                                      @RequestParam("ascriptionType") String ascriptionType) {
        logger.info("url={},ParameterMap={}，file.isEmpty={}，file.getSize={}，file.getContentType={}，file.getOriginalFilename={}，", req.getServletPath(), JSON.toJSONString(req.getParameterMap()),excelFile.isEmpty(),excelFile.getSize(),excelFile.getContentType(),excelFile.getOriginalFilename());
        CommonResult<String> result = new CommonResult<>();
        try {

            Integer count = sysDepartmentService.impDepartmentService(permId, excelFile, parentId, ascriptionType, req);

            result.setStatus(1).setMsg("导入成功"+count+"条");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }

    /**
     * 后台管理-根据部门类型查询部门信息
     * @apiNote 根据部门类型查询部门信息 无分页
     * @param depType 部门类型：1_1：校区、1_2：院、1_3：系(专业)
     * @author Dingjd
     * @date 2021/3/16 16:38
     **/
    @PostMapping("/listByType/{depType}")
    public CommonResult<List<SysDepartment>> listByType(@PathVariable("depType") String depType, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<List<SysDepartment>> result = new CommonResult<>();

        SysDepartment sysDepartment = new SysDepartment();
        sysDepartment.setDepType(depType);

        List<SysDepartment> sysDepartments = sysDepartmentService.queryListByDepType(sysDepartment);

        return result.setStatus(1).setMsg("查询成功").setResultBody(sysDepartments);
    }

}
