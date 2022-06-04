package com.shouzhi.controller.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.basic.common.ErrorCodeEnum;
import com.shouzhi.controller.BaseController;
import com.shouzhi.pojo.db.ExamVideo;
import com.shouzhi.pojo.vo.PageInfoVo;
import com.shouzhi.service.interf.db.IExamVideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 云监考-后台-监考视频管理-视频接口
 * @author WX
 * @date 2020-08-06 17:42:05
 */
@RestController
@RequestMapping("/api/v1/examVideo")
public class ExamVideoController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IExamVideoService examVideoService;

    /**
     * 后台管理-查询考试视频列表
     * @apiNote 后台管理-查询考试(考生)视频列表，需将上层[专辑列表]中的<b>考试(考场)id</b>带过来，当作此接口的必要查询参数
     * @description 查询接口编写注意,不可使用对应的Model去接收,参数少就挨个写上,参数若真多只能再新建个VoModel
     *              因为新增和修改用的Model接收,里边必须及非必须参数会对接口使用人造成误解,新增或修改里必须参数在查询接口内不见得是必须
     * @param pageNum 页码(如第1页)
     * @param pageSize 每页数量(如每页10条)
     * @param examinationHallId 考试id，必填参数
     * @author WX
     * @date 2020-08-06 17:52:18
     */
    @PostMapping("/findList/{pageNum}/{pageSize}")
    public CommonResult<PageInfoVo<ExamVideo>> findList(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
                                                        @RequestParam(value="examinationHallId",required=true) String examinationHallId,
                                                        HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<PageInfoVo<ExamVideo>> result = new CommonResult<>();
        ExamVideo examVideo = new ExamVideo();
        examVideo.setExaminationHallId(examinationHallId);
        examVideo.setVideoStatus("4");
        PageHelper.startPage(pageNum,pageSize);
        List<ExamVideo> examVideos = examVideoService.queryListByPage(examVideo);
        // TODO nginx 静态资源鉴权
        /*examVideos.forEach(ConsumerWrapper.throwingConsumerWrapper(e -> {
            URL url = new URL(e.getVideoPath());
            String[] split = url.getPath().substring(1, url.getPath().lastIndexOf(".")).split("/");
            String exp = String.valueOf(System.currentTimeMillis());
            String base64UrlSign = SignUtil.base64UrlSign(split[0], split[1], exp);
            // ?exp=1592535978338&sign=OTllMTI4MDdhYTIxMjVjNWQ3ODQ3YzUwNjQwMjIzYjk=
            e.setLiveAddressPull(e.getLiveAddressPull()+"?exp="+exp+"&sign="+base64UrlSign);
        }));*/
        PageInfo<ExamVideo> pageInfo = new PageInfo<>(examVideos);
        return result.setStatus(1).setMsg("查询成功").setResultBody(this.filterPage(pageInfo));
    }

    // /**
    //  * 后台管理-新增考试视频
    //  * @apiNote 后台管理-新增考试(考生)视频，需将上层[考试管理]中的<b>考试(考场)id</b>和<b>考场编号</b>带过来，当作此接口的必要新增参数
    //  * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
    //  * @param examVideo
    //  * @author WX
    //  * @date 2020-08-06 18:00:43
    //  */
    /*@PostMapping("/save/{permId}")
    public CommonResult<String> saveExamVideo(@PathVariable("permId") String permId,
                                             @RequestBody ExamVideo examVideo, HttpServletRequest req) {
        logger.info("url={},examVideo={}", req.getServletPath(),JSON.toJSONString(examVideo));
        CommonResult<String> result = new CommonResult<>();
        try {
            examVideoService.saveExamVideo(examVideo, permId, req);
            result.setStatus(1).setMsg("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            result.setErrorResult(ErrorCodeEnum.DB_SQL_INSERT_ERROR);
        }
        return result;
    }*/


    // /**
    //  * 后台管理-修改考试视频
    //  * @apiNote 后台管理-修改考试(考生)视频
    //  * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
    //  * @param examVideo
    //  * @author WX
    //  * @date 2020-08-06 18:13:19
    //  */
    /*@PutMapping("/update/{permId}")
    public CommonResult<String> updateExamVideo(@PathVariable("permId") String permId,
                                               @RequestBody ExamVideo examVideo, HttpServletRequest req) {
        logger.info("url={},examVideo={}", req.getServletPath(),JSON.toJSONString(examVideo));
        CommonResult<String> result = new CommonResult<>();
        try {
            examVideoService.updateExamVideo(examVideo, permId, req);
            result.setStatus(1).setMsg("更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            result.setErrorResult(ErrorCodeEnum.DB_SQL_UPDATE_ERROR);
        }
        return result;
    }*/


    /**
     * 后台管理-更新考试视频观看数量
     * @apiNote 后台管理-更新考试(考生)视频观看数量，需前端监听播放器paly事件，进入页面后<b>第一次点击播放时</b>调用接口，
     *          暂停，再重新播放不算，不允许调用。需做好策略，进入页面内<b>只能触发一次</b>且只能点播放时触发！不可进入页面就触发。
     *          重复进入某考生视频详情播放页面，点击播放视频可重复触发，即触发策略仅是<b>页面级别</b>的。
     *          注：该接口统计的是视频观看数量而不是视频所在详情页面访问数量。
     *          由于没有特意编写模型，文档工具逆向推导不出来，该接口Response-example如下：
     *          {"status":1,"errorCode":null,"msg":"更新成功","resultBody":{"watchCount":26,"rowId":"5583104331416aba3f0"}}
     * @param rowId 当前视频Id
     * @author WX
     * @date 2020-08-21 15:42:36
     */
    @PostMapping("/watchCount/{rowId}")
    public CommonResult<JSONObject> updateWatchCount(@PathVariable("rowId") String rowId, HttpServletRequest req) {
        logger.info("url={},ParameterMap={}", req.getServletPath(),JSON.toJSONString(req.getParameterMap()));
        CommonResult<JSONObject> result = new CommonResult<>();
        try {
            Long watchCount = examVideoService.updateWatchCount(rowId, req);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("watchCount", watchCount);
            jsonObject.put("rowId", rowId);
            result.setStatus(1).setMsg("更新成功").setResultBody(jsonObject);
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("DB_SQL_UPDATE_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 后台管理-删除考试视频
     * @apiNote 后台管理-删除考试(考生)视频
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param rowId 被操作记录行ID(路径参数)
     * @author WX
     * @date 2020-08-06 18:17:49
     */
    @DeleteMapping("/delete/{permId}/{rowId}")
    public CommonResult<String> delById(@PathVariable("permId") String permId, @PathVariable("rowId") String rowId,
                                        HttpServletRequest req){
        logger.info("url={}", req.getServletPath());
        CommonResult<String> result = new CommonResult<>();
        try {
            examVideoService.delete(rowId, permId, req);
            result.setStatus(1).setMsg("删除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


    /**
     * 后台管理-批量删除考试视频
     * @apiNote 后台管理-批量删除考试(考生)视频
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param examVideoIds 考试(考生)视频ID，对应考试(考生)视频列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @author WX
     * @date 2020-08-06 18:23:10
     */
    @DeleteMapping(value = "/batchDelete/{permId}", consumes = "application/x-www-form-urlencoded")
    public CommonResult<String> batchDelByIds(@PathVariable("permId") String permId,
                                              @RequestParam("examVideoIds") String examVideoIds, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            examVideoService.batchDelete(examVideoIds, permId, req);
            result.setStatus(1).setMsg("删除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }

}
