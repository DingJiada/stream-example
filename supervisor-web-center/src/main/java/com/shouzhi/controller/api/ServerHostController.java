package com.shouzhi.controller.api;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.controller.BaseController;
import com.shouzhi.pojo.db.ServerHost;
import com.shouzhi.pojo.vo.PageInfoVo;
import com.shouzhi.service.interf.db.IServerHostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务器主机接口
 * @author WX
 * @date 2021-02-03 16:39:28
 */
@RestController
@RequestMapping("/api/v1/serverHost")
public class ServerHostController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IServerHostService serverHostService;

    /**
     * 查询服务器主机列表
     * @apiNote 查询服务器主机列表
     * @description 查询接口编写注意,不可使用对应的Model去接收,参数少就挨个写上,参数若真多只能再新建个VoModel
     *              因为新增和修改用的Model接收,里边必须及非必须参数会对接口使用人造成误解,新增或修改里必须参数在查询接口内不见得是必须
     * @param pageNum 页码(如第1页)
     * @param pageSize 每页数量(如每页10条)
     * @param hostProtocol 主机协议
     * @param hostAddr 主机地址
     * @param hostPort 主机端口
     * @param hostType 主机类型，1：流媒体服务器推流、2：流媒体服务器http拉流、3：资源服务器点播、4：流媒体服务器ws拉流
     * @author WX
     * @date 2021-02-03 16:42:09
     */
    @PostMapping("/findList/{pageNum}/{pageSize}")
    public CommonResult<PageInfoVo<ServerHost>> findList(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
                                                         @RequestParam(value="hostProtocol",required=false) String hostProtocol,
                                                         @RequestParam(value="hostAddr",required=false) String hostAddr,
                                                         @RequestParam(value="hostPort",required=false) String hostPort,
                                                         @RequestParam(value="hostType",required=false) String hostType,
                                                          HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(),JSON.toJSONString(req.getParameterMap()));
        CommonResult<PageInfoVo<ServerHost>> result = new CommonResult<>();
        Map<String, Object> map = new HashMap<>();
        map.put("hostProtocolLike", hostProtocol);
        map.put("hostAddrLike", hostAddr);
        map.put("hostPortLike", hostPort);
        map.put("hostType", hostType);
        PageHelper.startPage(pageNum,pageSize);
        List<ServerHost> serverHosts = serverHostService.queryListByPage(map);
        PageInfo<ServerHost> pageInfo = new PageInfo<>(serverHosts);
        return result.setStatus(1).setMsg("查询成功").setResultBody(this.filterPage(pageInfo));
    }


    /**
     * 修改服务器主机
     * @apiNote 修改服务器主机
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param serverHost
     * @author WX
     * @date 2021-02-03 17:31:18
     */
    @PutMapping("/update/{permId}")
    public CommonResult<String> update(@PathVariable("permId") String permId, @RequestBody ServerHost serverHost,
                                       HttpServletRequest req) {
        logger.info("url={},serverHost={}", req.getServletPath(), JSON.toJSONString(serverHost));
        CommonResult<String> result = new CommonResult<>();
        try {
            serverHostService.update(serverHost, permId, req);
            result.setStatus(1).setMsg("更新成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }

}
