package com.shouzhi.controller.api;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.controller.BaseController;
import com.shouzhi.pojo.db.SchCourseCategory;
import com.shouzhi.pojo.vo.PageInfoVo;
import com.shouzhi.service.interf.db.ISchCourseCategoryService;
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
 * 学校课程类别接口
 * @author WX
 * @date 2020-12-02 14:30:14
 */
@RestController
@RequestMapping("/api/v1/schCourseCategory")
public class SchCourseCategoryController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISchCourseCategoryService schCourseCategoryService;

    /**
     * 查询学校课程类别列表
     * @apiNote 查询学校课程类别列表
     * @description 查询接口编写注意,不可使用对应的Model去接收,参数少就挨个写上,参数若真多只能再新建个VoModel
     *              因为新增和修改用的Model接收,里边必须及非必须参数会对接口使用人造成误解,新增或修改里必须参数在查询接口内不见得是必须
     * @param pageNum 页码(如第1页)
     * @param pageSize 每页数量(如每页10条)
     * @param categoryCode 分类代码
     * @param categoryName 分类名称
     * @author WX
     * @date 2020-12-02 14:33:06
     */
    @PostMapping("/findList/{pageNum}/{pageSize}")
    public CommonResult<PageInfoVo<SchCourseCategory>> findList(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
                                                                @RequestParam(value="categoryCode",required=false) String categoryCode,
                                                                @RequestParam(value="categoryName",required=false) String categoryName,
                                                                HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(),JSON.toJSONString(req.getParameterMap()));
        CommonResult<PageInfoVo<SchCourseCategory>> result = new CommonResult<>();
        Map<String, Object> map = new HashMap<>();
        map.put("categoryCode", categoryCode);
        map.put("categoryNameLike", categoryName);
        PageHelper.startPage(pageNum,pageSize);
        List<SchCourseCategory> schCourseCategories = schCourseCategoryService.queryListByPage(map);
        PageInfo<SchCourseCategory> pageInfo = new PageInfo<>(schCourseCategories);
        return result.setStatus(1).setMsg("查询成功").setResultBody(this.filterPage(pageInfo));
    }

    /**
     * 新增学校课程类别
     * @apiNote 新增学校课程类别
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param schCourseCategory
     * @author WX
     * @date 2020-12-02 16:04:23
     */
    @PostMapping("/save/{permId}")
    public CommonResult<String> save(@PathVariable("permId") String permId, @RequestBody SchCourseCategory schCourseCategory,
                                     HttpServletRequest req) {
        logger.info("url={},schCourseCategory={}", req.getServletPath(),JSON.toJSONString(schCourseCategory));
        CommonResult<String> result = new CommonResult<>();
        try {
            schCourseCategoryService.save(schCourseCategory, permId, req);
            result.setStatus(1).setMsg("新增成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


    /**
     * 修改学校课程类别
     * @apiNote 修改学校课程类别
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param schCourseCategory
     * @author WX
     * @date 2020-12-02 16:08:38
     */
    @PutMapping("/update/{permId}")
    public CommonResult<String> update(@PathVariable("permId") String permId, @RequestBody SchCourseCategory schCourseCategory,
                                       HttpServletRequest req) {
        logger.info("url={},schCourseCategory={}", req.getServletPath(),JSON.toJSONString(schCourseCategory));
        CommonResult<String> result = new CommonResult<>();
        try {
            schCourseCategoryService.update(schCourseCategory, permId, req);
            result.setStatus(1).setMsg("更新成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }

    /**
     * 删除学校课程类别
     * @apiNote 删除学校课程类别
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param rowId 被操作记录行ID
     * @author WX
     * @date 2020-12-02 16:11:20
     */
    @DeleteMapping("/delete/{permId}/{rowId}")
    public CommonResult<String> delId(@PathVariable("permId") String permId, @PathVariable("rowId") String rowId,
                                      HttpServletRequest req){
        logger.info("url={}", req.getServletPath());
        CommonResult<String> result = new CommonResult<>();
        try {
            schCourseCategoryService.delete(rowId, permId, req);
            result.setStatus(1).setMsg("删除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


    /**
     * 批量删除学校课程类别
     * @apiNote 批量删除学校课程类别
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param schCourseCategoryIds 学校课程类别ID，对应学校课程类别信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @author WX
     * @date 2020-12-02 16:16:08
     */
    @DeleteMapping(value = "/batchDelete/{permId}", consumes = "application/x-www-form-urlencoded")
    public CommonResult<String> batchDelByIds(@PathVariable("permId") String permId, @RequestParam("schCourseCategoryIds") String schCourseCategoryIds,
                                              HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            schCourseCategoryService.batchDelete(schCourseCategoryIds, permId, req);
            result.setStatus(1).setMsg("删除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }

    /**
     * 后台管理-基础设置-课程类别的导入
     * @apiNote 后台管理-基础设置-课程类别的导入
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param excelFile input标签的name值
     * @author Dingjd
     * @date 2021-03-12 09:55:19
     */
    @PostMapping("/imp/{permId}")
    public CommonResult impCourseCategory(@PathVariable("permId") String permId, HttpServletRequest req,
                                          @RequestParam("excelFile") MultipartFile excelFile) {
        logger.info("url={},ParameterMap={}，file.isEmpty={}，file.getSize={}，file.getContentType={}，file.getOriginalFilename={}，", req.getServletPath(), JSON.toJSONString(req.getParameterMap()),excelFile.isEmpty(),excelFile.getSize(),excelFile.getContentType(),excelFile.getOriginalFilename());
        CommonResult<String> result = new CommonResult<>();
        try {

            Integer count = schCourseCategoryService.impCourseCategoryService(permId, excelFile, req);

            result.setStatus(1).setMsg("导入成功" + count + "条");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }
}
