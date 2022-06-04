package com.shouzhi.controller.api;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.basic.common.ErrorCodeEnum;
import com.shouzhi.controller.BaseController;
import com.shouzhi.pojo.db.SecurityQuestions;
import com.shouzhi.pojo.db.SysUser;
import com.shouzhi.pojo.vo.PageInfoVo;
import com.shouzhi.service.interf.db.ISecurityQuestionsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 密保问题接口
 * @author WX
 * @date 2020-07-16 09:10:21
 */
@RestController
@RequestMapping("/api/v1/securityQuestions")
public class SecurityQuestionsController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISecurityQuestionsService securityQuestionsService;


    /**
     * 查询密保问题列表
     * @apiNote 查询密保问题列表
     * @description 查询接口编写注意,不可使用对应的Model去接收,参数少就挨个写上,参数若真多只能再新建个VoModel
     *              因为新增和修改用的Model接收,里边必须及非必须参数会对接口使用人造成误解,新增或修改里必须参数在查询接口内不见得是必须
     * @param pageNum 页码(如第1页)
     * @param pageSize 每页数量(如每页10条)
     * @param questionDesc 问题描述
     * @author WX
     * @date 2020-07-16 09:12:26
     */
    @PostMapping("/findList/{pageNum}/{pageSize}")
    public CommonResult<PageInfoVo<SecurityQuestions>> findList(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
                                                                @RequestParam(value="questionDesc",required=false) String questionDesc,
                                                                HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<PageInfoVo<SecurityQuestions>> result = new CommonResult<>();
        SecurityQuestions securityQuestions = new SecurityQuestions();
        securityQuestions.setQuestionDesc(questionDesc);
        PageHelper.startPage(pageNum,pageSize);
        List<SecurityQuestions> questions = securityQuestionsService.queryListByPage(securityQuestions);
        PageInfo<SecurityQuestions> pageInfo = new PageInfo<>(questions);
        return result.setStatus(1).setMsg("查询成功").setResultBody(this.filterPage(pageInfo));
    }


    /**
     * 新增密保问题
     * @apiNote 新增密保问题
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param securityQuestions
     * @author WX
     * @date 2020-07-16 09:47:10
     */
    @PostMapping("/save/{permId}")
    public CommonResult<String> save(@PathVariable("permId") String permId, @RequestBody SecurityQuestions securityQuestions, HttpServletRequest req) {
        logger.info("url={},securityQuestions={}", req.getServletPath(),JSON.toJSONString(securityQuestions));
        CommonResult<String> result = new CommonResult<>();
        try {
            securityQuestionsService.save(securityQuestions, permId, req);
            result.setStatus(1).setMsg("新增成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("DB_SQL_INSERT_ERROR"), true, true, logger);
        }
        return result;
    }


    /**
     * 修改密保问题信息
     * @apiNote 修改密保问题
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param securityQuestions
     * @author WX
     * @date 2020-07-16 09:52:13
     */
    @PutMapping("/update/{permId}")
    public CommonResult<String> updateById(@PathVariable("permId") String permId,
                                                  @RequestBody SecurityQuestions securityQuestions, HttpServletRequest req) {
        logger.info("url={},securityQuestions={}", req.getServletPath(),JSON.toJSONString(securityQuestions));
        CommonResult<String> result = new CommonResult<>();
        try {
            securityQuestionsService.update(securityQuestions, permId, req);
            result.setStatus(1).setMsg("更新成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("DB_SQL_UPDATE_ERROR"), true, true, logger);
        }
        return result;
    }

    /**
     * 删除密保问题
     * @apiNote 删除密保问题
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param rowId 被操作记录行ID
     * @author WX
     * @date 2020-07-16 10:05:24
     */
    @DeleteMapping("/delete/{permId}/{rowId}")
    public CommonResult<String> delById(@PathVariable("permId") String permId, @PathVariable("rowId") String rowId,
                                            HttpServletRequest req){
        logger.info("url={}", req.getServletPath());
        CommonResult<String> result = new CommonResult<>();
        try {
            securityQuestionsService.delete(rowId, permId, req);
            result.setStatus(1).setMsg("删除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, new IllegalArgumentException("DB_SQL_DELETE_ERROR"), true, true, logger);
        }
        return result;

    }

    /**
     * 查询单条密保问题记录
     * @apiNote 查询单条密保问题记录，根据ID
     * @param rowId 被操作记录行ID
     * @author WX
     * @date 2020-07-16 10:07:05
     */
    @GetMapping("/find/{rowId}")
    public CommonResult<SecurityQuestions> findById(@PathVariable("rowId") String rowId, HttpServletRequest req){
        logger.info("url={}", req.getServletPath());
        SecurityQuestions sq = securityQuestionsService.selectByPrimaryKey(rowId);
        return CommonResult.<SecurityQuestions>getInstance().setStatus(1).setMsg("查询成功").setResultBody(sq);
    }


}
