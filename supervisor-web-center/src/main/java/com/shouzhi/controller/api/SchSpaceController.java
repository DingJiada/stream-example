package com.shouzhi.controller.api;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.controller.BaseController;
import com.shouzhi.pojo.db.SchSpace;
import com.shouzhi.pojo.vo.PageInfoVo;
import com.shouzhi.pojo.vo.TreeNodeVo;
import com.shouzhi.service.interf.db.ISchSpaceService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 学校空间信息接口
 * @author WX
 * @date 2020-11-05 17:43:17
 */
@RestController
@RequestMapping("/api/v1/schSpace")
public class SchSpaceController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISchSpaceService schSpaceService;

    /**
     * 查询学校空间信息树
     * @apiNote 查询学校空间信息树
     * @author WX
     * @date 2020-11-06 17:47:34
     */
    @PostMapping("/findTree")
    public CommonResult<List<TreeNodeVo>> findTree(HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(),JSON.toJSONString(req.getParameterMap()));
        CommonResult<List<TreeNodeVo>> result = new CommonResult<>();
        List<TreeNodeVo> tree = schSpaceService.findTree(req);
        return result.setStatus(1).setMsg("查询成功").setResultBody(tree);
    }

    /**
     * 后台管理-查询学校空间信息列表
     * @apiNote 查询学校空间信息列表
     * @description 查询接口编写注意,不可使用对应的Model去接收,参数少就挨个写上,参数若真多只能再新建个VoModel
     *              因为新增和修改用的Model接收,里边必须及非必须参数会对接口使用人造成误解,新增或修改里必须参数在查询接口内不见得是必须
     * @param pageNum 页码(如第1页)
     * @param pageSize 每页数量(如每页10条)
     * @param spaceType 空间类型，1：学校，2：校区，3：楼宇，4：楼层，5：教室
     * @param parentId 父结点id，不可与includeSubRegion、includeParentRegion同时使用
     * @param includeSubRegion 包含子区域，值为‘父结点id’，<b>不可</b>与parentId同时使用
     * @param includeParentRegion 包含父区域，值为‘父结点id’，<b>不可</b>与parentId同时使用
     * @author WX
     * @date 2020-11-05 17:49:17
     */
    @PostMapping("/findList/{pageNum}/{pageSize}")
    public CommonResult<PageInfoVo<SchSpace>> findList(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
                                                       @RequestParam(value="spaceType",required=false) String spaceType,
                                                       @RequestParam(value="parentId",required=false) String parentId,
                                                       @RequestParam(value="includeSubRegion",required=false) String includeSubRegion,
                                                       @RequestParam(value="includeParentRegion",required=false) String includeParentRegion,
                                                       HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<PageInfoVo<SchSpace>> result = new CommonResult<>();
        SchSpace schSpace = new SchSpace();
        schSpace.setSpaceType(spaceType);
        schSpace.setParentId(parentId);
        if(StringUtils.isNotBlank(includeSubRegion)){
            schSpace.setParentIdsLike(String.join(includeSubRegion,"/","/"));
        }
        if(StringUtils.isNotBlank(includeParentRegion)){
            schSpace.setIncludeParentRegion(includeParentRegion);
        }
        PageHelper.startPage(pageNum,pageSize);
        List<SchSpace> schSpaces = schSpaceService.queryListByPage(schSpace);
        PageInfo<SchSpace> pageInfo = new PageInfo<>(schSpaces);
        return result.setStatus(1).setMsg("查询成功").setResultBody(this.filterPage(pageInfo));
    }

    /**
     * 后台管理-新增学校空间信息
     * @apiNote 新增学校空间信息
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param schSpace
     * @author WX
     * @date 2020-11-06 10:56:30
     */
    @PostMapping("/save/{permId}")
    public CommonResult<String> saveSchSpace(@PathVariable("permId") String permId, @RequestBody SchSpace schSpace, HttpServletRequest req) {
        logger.info("url={},schSpace={}", req.getServletPath(),JSON.toJSONString(schSpace));
        CommonResult<String> result = new CommonResult<>();
        try {
            schSpaceService.save(schSpace, permId, req);
            result.setStatus(1).setMsg("新增成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


    /**
     * 后台管理-修改学校空间信息
     * @apiNote 修改学校空间信息
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param schSpace
     * @author WX
     * @date 2020-11-06 11:38:51
     */
    @PutMapping("/update/{permId}")
    public CommonResult<String> updateSchSpaceById(@PathVariable("permId") String permId,
                                                      @RequestBody SchSpace schSpace, HttpServletRequest req) {
        logger.info("url={},schSpace={}", req.getServletPath(),JSON.toJSONString(schSpace));
        CommonResult<String> result = new CommonResult<>();
        try {
            schSpaceService.update(schSpace, permId, req);
            result.setStatus(1).setMsg("更新成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }

    /**
     * 后台管理-删除学校空间信息
     * @apiNote 删除学校空间信息，空间信息的删除会<b>级联删除</b>与该空间关联的<b>子空间信息集</b>及与该空间关联的<b>设备信息</b>，
     *          请提示用户谨慎操作
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param rowId 被操作记录行ID
     * @author WX
     * @date 2020-11-06 11:40:19
     */
    @DeleteMapping("/delete/{permId}/{rowId}")
    public CommonResult<String> delBySchSpaceId(@PathVariable("permId") String permId, @PathVariable("rowId") String rowId,
                                                  HttpServletRequest req){
        logger.info("url={}", req.getServletPath());
        CommonResult<String> result = new CommonResult<>();
        try {
            schSpaceService.delete(rowId, permId, req);
            result.setStatus(1).setMsg("删除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


    /**
     * 后台管理-批量删除学校空间信息
     * @apiNote 批量删除学校空间信息，空间信息的批量删除会<b>级联批量删除</b>与该空间关联的<b>子空间信息集</b>及与该空间关联的<b>设备信息</b>，
     *          请提示用户谨慎操作
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param schSpaceIds 学校空间信息ID，对应学校空间信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @author WX
     * @date 2020-11-06 11:42:36
     */
    @DeleteMapping(value = "/batchDelete/{permId}", consumes = "application/x-www-form-urlencoded")
    public CommonResult<String> batchDelBySchSpaceIds(@PathVariable("permId") String permId, @RequestParam("schSpaceIds") String schSpaceIds,
                                                           HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            schSpaceService.batchDelete(schSpaceIds, permId, req);
            result.setStatus(1).setMsg("删除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


    /**
     * 后台管理-导入部分学校空间信息
     * @apiNote 后台管理-导入部分学校空间信息，是否移除旧数据用于移除当前选择parentId父结点id下的已存在数据
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param excelFile input标签的name值
     * @param parentId 父结点id
     * @param delOldData 是否移除旧数据，0：否，1：是
     * @author WX
     * @date 2021-03-02 09:33:19
     */
    @PostMapping("/imp/part/{permId}")
    public CommonResult impPartSchSpace(@PathVariable("permId") String permId, HttpServletRequest req,
                                   @RequestParam("excelFile") MultipartFile excelFile,
                                    @RequestParam("parentId") String parentId, @RequestParam("delOldData") String delOldData) {
        logger.info("url={},ParameterMap={}，file.isEmpty={}，file.getSize={}，file.getContentType={}，file.getOriginalFilename={}，", req.getServletPath(), JSON.toJSONString(req.getParameterMap()),excelFile.isEmpty(),excelFile.getSize(),excelFile.getContentType(),excelFile.getOriginalFilename());
        CommonResult<String> result = new CommonResult<>();
        try {
            Integer count = schSpaceService.impPartSchSpace(permId, excelFile, parentId, delOldData, req);
            result.setStatus(1).setMsg("导入成功"+count+"条");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }

    // TODO 后台管理-导入全部学校空间信息

    /**
     * 后台管理-根据空间类型查询学校空间信息
     * @apiNote 根据空间类型查询学校空间信息列表，无分页，查询全部信息
     * @param spaceType 空间类型，1：学校，2：校区，3：楼宇，4：楼层，5：教室
     * @author WX
     * @date 2021-03-03 14:50:27
     */
    @PostMapping("/listByType/{spaceType}")
    public CommonResult<List<SchSpace>> listByType(@PathVariable("spaceType") String spaceType, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<List<SchSpace>> result = new CommonResult<>();
        SchSpace schSpace = new SchSpace();
        schSpace.setSpaceType(spaceType);
        List<SchSpace> schSpaces = schSpaceService.queryListByPage(schSpace);
        return result.setStatus(1).setMsg("查询成功").setResultBody(schSpaces);
    }

}
