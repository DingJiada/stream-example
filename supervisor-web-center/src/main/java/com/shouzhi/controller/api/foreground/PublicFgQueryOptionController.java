package com.shouzhi.controller.api.foreground;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.controller.BaseController;
import com.shouzhi.pojo.db.SchCourseCategory;
import com.shouzhi.pojo.db.SchSpace;
import com.shouzhi.pojo.db.SysDepartment;
import com.shouzhi.service.interf.db.ISchCourseCategoryService;
import com.shouzhi.service.interf.db.ISchSpaceService;
import com.shouzhi.service.interf.db.ISysDepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 匿名-前台-查询选项(筛选条件)
 * @author WX
 * @date 2021-01-08 13:54:52
 */
@RestController
@RequestMapping("/public/api/v1/fg/queryOption")
public class PublicFgQueryOptionController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISchSpaceService schSpaceService;

    @Autowired
    private ISysDepartmentService sysDepartmentService;

    @Autowired
    private ISchCourseCategoryService schCourseCategoryService;


    /**
     * 匿名-获取查询选项列表
     * @apiNote 匿名-获取查询选项列表，用于前台各列表页内顶端的查询选项列表，具体返回示例以真实接口为准
     * @author WX
     * @date 2021-01-08 14:06:17
     */
    @PostMapping("/findList")
    public CommonResult<JSONObject> findList(HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<JSONObject> result = new CommonResult<>();
        // 空间类型，仅返回 2：校区 的节点，其余节点使用‘懒加载’方式通过‘下方接口’获取
        SchSpace ss = new SchSpace();
        ss.setSpaceType("2");
        List<SchSpace> schSpaces = schSpaceService.queryListByPage(ss);
        // 组织结构(部门)类型，仅返回 1_2：院 的节点
        HashMap<String, Object> map = new HashMap<>();
        map.put("depType", "1_2");
        List<SysDepartment> sysDepartments = sysDepartmentService.queryListByPage(map);
        List<SchCourseCategory> schCourseCategories = schCourseCategoryService.queryListByPage(new HashMap<>());

        JSONObject r = new JSONObject();
        // 构建空间位置
        List<JSONObject> collect = schSpaces.stream().map(schSpace -> {
            JSONObject j = new JSONObject();
            j.put("schSpaceId", schSpace.getId());
            j.put("text", schSpace.getSpaceName());
            // 此type后台无用处，给前端使用，用于判别用户点击哪一层(1:校区,2:楼宇,3:楼层,4:教室,5:部门,6:课程类别)，否则会出现多层同列选中的bug，规则：值不相等且不能为随机数，如若变更需通知前端
            j.put("type", 1);
            return j;
        }).collect(Collectors.toList());
        r.put("schSpaces", collect);

        // 构建部门组织（开课单位）
        List<JSONObject> collect1 = sysDepartments.stream().map(sysDepartment -> {
            JSONObject j = new JSONObject();
            j.put("sysDepId", sysDepartment.getId());
            j.put("text", sysDepartment.getDepName());
            // 此type后台无用处，给前端使用，用于判别用户点击哪一层，否则会出现多层同列选中的bug，规则：值不相等且不能为随机数，如若变更需通知前端
            j.put("type", 5);
            return j;
        }).collect(Collectors.toList());
        r.put("sysDepartments", collect1);

        // 构建课程类别
        List<JSONObject> collect2 = schCourseCategories.stream().map(schCourseCategory -> {
            JSONObject j = new JSONObject();
            j.put("schCourseCategoryId", schCourseCategory.getId());
            j.put("text", schCourseCategory.getCategoryName());
            // 此type后台无用处，给前端使用，用于判别用户点击哪一层，否则会出现多层同列选中的bug，规则：值不相等且不能为随机数，如若变更需通知前端
            j.put("type", 6);
            return j;
        }).collect(Collectors.toList());
        r.put("schCourseCategories", collect2);

        return result.setStatus(1).setMsg("查询成功").setResultBody(r);
    }

    /**
     * 匿名-获取学校空间节点查询选项列表
     * @apiNote 匿名-获取学校空间节点查询选项列表，用于前台各列表页内顶端的查询选项列表，具体返回示例以真实接口为准
     * @param schSpaceParentId 学校空间节点父ID
     * @author WX
     * @date 2021-01-08 14:06:17
     */
    @PostMapping("/schSpaceNodes")
    public CommonResult<List<JSONObject>> schSpaceNodes(@RequestParam("schSpaceParentId") String schSpaceParentId, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<List<JSONObject>> result = new CommonResult<>();
        SchSpace ss = new SchSpace();
        ss.setParentId(schSpaceParentId);
        List<SchSpace> schSpaces = schSpaceService.queryListByPage(ss);
        // 构建空间位置
        List<JSONObject> collect = schSpaces.stream()
                .map(schSpace -> {
                    JSONObject j = new JSONObject();
                    j.put("schSpaceId", schSpace.getId());
                    j.put("text", schSpace.getSpaceName());
                    // 此type后台无用处，给前端使用，用于判别用户点击哪一层，否则会出现多层同列选中的bug，规则：值不相等且不能为随机数，如若变更需通知前端
                    if("3".equals(schSpace.getSpaceType())){
                        j.put("type", 2);
                    } else if("4".equals(schSpace.getSpaceType())){
                        j.put("type", 3);
                    } else if("5".equals(schSpace.getSpaceType())){
                        j.put("type", 4);
                    }
                    return j;
                }).collect(Collectors.toList());
        return result.setStatus(1).setMsg("查询成功").setResultBody(collect);
    }

}
