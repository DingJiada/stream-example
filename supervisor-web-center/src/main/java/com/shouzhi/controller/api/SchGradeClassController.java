package com.shouzhi.controller.api;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.controller.BaseController;
import com.shouzhi.pojo.db.SchGradeClass;
import com.shouzhi.pojo.vo.PageInfoVo;
import com.shouzhi.service.interf.db.ISchGradeClassService;
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
 * 学校年级班级接口
 * @author WX
 * @date 2020-12-01 16:43:00
 */
@RestController
@RequestMapping("/api/v1/schGradeClass")
public class SchGradeClassController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISchGradeClassService schGradeClassService;

    /**
     * 查询学校年级班级列表
     * @apiNote 查询学校年级班级列表
     * @description 查询接口编写注意,不可使用对应的Model去接收,参数少就挨个写上,参数若真多只能再新建个VoModel
     *              因为新增和修改用的Model接收,里边必须及非必须参数会对接口使用人造成误解,新增或修改里必须参数在查询接口内不见得是必须
     * @param pageNum 页码(如第1页)
     * @param pageSize 每页数量(如每页10条)
     * @param gradeYear 入学年份(年级年份)，如：2010
     * @param gradeName 年级名称，1：一年级、2：二年级、3：三年级、4：四年级、5：五年级、6：六年级、7：初一、8：初二、9：初三、10：高一、11：高二、12：高三、13：大一、14：大二、15：大三、16：大四、17：大五、18：大六
     * @param className 班级名称，如：1班、二班、计算机1201班等
     * @author WX
     * @date 2020-12-01 17:08:16
     */
    @PostMapping("/findList/{pageNum}/{pageSize}")
    public CommonResult<PageInfoVo<SchGradeClass>> findList(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
                                                            @RequestParam(value="gradeYear",required=false) Integer gradeYear,
                                                            @RequestParam(value="gradeName",required=false) Integer gradeName,
                                                            @RequestParam(value="className",required=false) String className,
                                                            HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(),JSON.toJSONString(req.getParameterMap()));
        CommonResult<PageInfoVo<SchGradeClass>> result = new CommonResult<>();
        Map<String, Object> map = new HashMap<>();
        map.put("gradeYear", gradeYear);
        map.put("gradeName", gradeName);
        map.put("classNameLike", className);
        PageHelper.startPage(pageNum,pageSize);
        List<SchGradeClass> schGradeClasses = schGradeClassService.queryListByPage(map);
        PageInfo<SchGradeClass> pageInfo = new PageInfo<>(schGradeClasses);
        return result.setStatus(1).setMsg("查询成功").setResultBody(this.filterPage(pageInfo));
    }

    /**
     * 新增学校年级班级
     * @apiNote 新增学校年级班级
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param schGradeClass
     * @author WX
     * @date 2020-12-01 17:09:56
     */
    @PostMapping("/save/{permId}")
    public CommonResult<String> save(@PathVariable("permId") String permId, @RequestBody SchGradeClass schGradeClass,
                                     HttpServletRequest req) {
        logger.info("url={},schGradeClass={}", req.getServletPath(),JSON.toJSONString(schGradeClass));
        CommonResult<String> result = new CommonResult<>();
        try {
            schGradeClassService.save(schGradeClass, permId, req);
            result.setStatus(1).setMsg("新增成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


    /**
     * 修改学校年级班级
     * @apiNote 修改学校年级班级
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param schGradeClass
     * @author WX
     * @date 2020-12-01 17:11:28
     */
    @PutMapping("/update/{permId}")
    public CommonResult<String> update(@PathVariable("permId") String permId, @RequestBody SchGradeClass schGradeClass,
                                       HttpServletRequest req) {
        logger.info("url={},schGradeClass={}", req.getServletPath(),JSON.toJSONString(schGradeClass));
        CommonResult<String> result = new CommonResult<>();
        try {
            schGradeClassService.update(schGradeClass, permId, req);
            result.setStatus(1).setMsg("更新成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }

    /**
     * 删除学校年级班级
     * @apiNote 删除学校年级班级
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param rowId 被操作记录行ID
     * @author WX
     * @date 2020-12-01 17:13:09
     */
    @DeleteMapping("/delete/{permId}/{rowId}")
    public CommonResult<String> delId(@PathVariable("permId") String permId, @PathVariable("rowId") String rowId,
                                      HttpServletRequest req){
        logger.info("url={}", req.getServletPath());
        CommonResult<String> result = new CommonResult<>();
        try {
            schGradeClassService.delete(rowId, permId, req);
            result.setStatus(1).setMsg("删除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


    /**
     * 批量删除学校年级班级
     * @apiNote 批量删除学校年级班级
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param schGradeClassIds 学校年级班级ID，对应学校年级班级信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @author WX
     * @date 2020-12-01 17:16:50
     */
    @DeleteMapping(value = "/batchDelete/{permId}", consumes = "application/x-www-form-urlencoded")
    public CommonResult<String> batchDelByIds(@PathVariable("permId") String permId, @RequestParam("schGradeClassIds") String schGradeClassIds,
                                              HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            schGradeClassService.batchDelete(schGradeClassIds, permId, req);
            result.setStatus(1).setMsg("删除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }

    /**
     * 后台管理-基础设置-班级数据的导入
     * @apiNote 后台管理-基础设置-班级数据的导入
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param excelFile input标签的name值
     * @author Dingjd
     * @date 2021-03-15 11:22:19
     */
    @PostMapping("/imp/{permId}")
    public CommonResult impGradeClass(@PathVariable("permId") String permId, HttpServletRequest req,
                                      @RequestParam("excelFile") MultipartFile excelFile) {
        logger.info("url={},ParameterMap={}，file.isEmpty={}，file.getSize={}，file.getContentType={}，file.getOriginalFilename={}，", req.getServletPath(), JSON.toJSONString(req.getParameterMap()),excelFile.isEmpty(),excelFile.getSize(),excelFile.getContentType(),excelFile.getOriginalFilename());
        CommonResult<String> result = new CommonResult<>();
        try {

            Integer count = schGradeClassService.impGradeClassService(permId, excelFile, req);

            result.setStatus(1).setMsg("导入成功" + count + "条");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }
}
