package com.shouzhi.controller.api;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.basic.common.ErrorCodeEnum;
import com.shouzhi.controller.BaseController;
import com.shouzhi.pojo.db.ExaminationHall;
import com.shouzhi.pojo.vo.PageInfoVo;
import com.shouzhi.service.interf.db.IExaminationHallService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 云监考-后台-监考视频管理-专辑接口
 * @author WX
 * @date 2020-08-07 09:51:06
 */
@RestController
@RequestMapping("/api/v1/examAlbum")
public class ExamAlbumController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IExaminationHallService examinationHallService;

    /**
     * 后台管理-查询专辑列表
     * @apiNote 后台管理-查询专辑列表，<b>考试时间</b>需要前端自己利用format将startTime由'yyyy-MM-dd  hh:mm:ss'格式化成'yyyy-MM-dd'
     * @description 查询接口编写注意,不可使用对应的Model去接收,参数少就挨个写上,参数若真多只能再新建个VoModel
     *              因为新增和修改用的Model接收,里边必须及非必须参数会对接口使用人造成误解,新增或修改里必须参数在查询接口内不见得是必须
     * @param pageNum 页码(如第1页)
     * @param pageSize 每页数量(如每页10条)
     * @author WX
     * @date 2020-08-07 09:57:19
     */
    @PostMapping("/findList/{pageNum}/{pageSize}")
    public CommonResult<PageInfoVo<ExaminationHall>> findList(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
                                                              HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<PageInfoVo<ExaminationHall>> result = new CommonResult<>();
        ExaminationHall examinationHall = new ExaminationHall();
        examinationHall.setShowAlbum("1");
        PageHelper.startPage(pageNum,pageSize);
        List<ExaminationHall> examinationHalls = examinationHallService.queryListByPage(examinationHall);
        PageInfo<ExaminationHall> pageInfo = new PageInfo<>(examinationHalls);
        return result.setStatus(1).setMsg("查询成功").setResultBody(this.filterPage(pageInfo));
    }


    /**
     * 后台管理-删除专辑
     * @apiNote 后台管理-删除专辑，考试专辑的删除会级联删除与该专辑关联的所有视频
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param rowId 被操作记录行ID(路径参数)
     * @author WX
     * @date 2020-08-07 10:05:16
     */
    @DeleteMapping("/delete/{permId}/{rowId}")
    public CommonResult<String> delById(@PathVariable("permId") String permId, @PathVariable("rowId") String rowId,
                                        HttpServletRequest req){
        logger.info("url={}", req.getServletPath());
        CommonResult<String> result = new CommonResult<>();
        try {
            examinationHallService.deleteAlbum(rowId, permId, req);
            result.setStatus(1).setMsg("删除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("DB_SQL_DELETE_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 后台管理-批量删除专辑
     * @apiNote 后台管理-批量删除专辑，考试专辑的批量删除会级联批量删除与该专辑关联的所有视频
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param examAlbumIds 专辑信息ID，对应专辑信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @author WX
     * @date 2020-08-07 10:07:46
     */
    @DeleteMapping(value = "/batchDelete/{permId}", consumes = "application/x-www-form-urlencoded")
    public CommonResult<String> batchDelByIds(@PathVariable("permId") String permId,
                                              @RequestParam("examAlbumIds") String examAlbumIds, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            String examinationHallIds = examAlbumIds;
            examinationHallService.batchDeleteAlbum(examinationHallIds, permId, req);
            result.setStatus(1).setMsg("删除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("DB_SQL_DELETE_ERROR"), true, true, logger);
        }
        return result;
    }

}
