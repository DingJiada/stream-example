package com.shouzhi.controller.api;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.controller.BaseController;
import com.shouzhi.pojo.db.SchDevice;
import com.shouzhi.pojo.vo.PageInfoVo;
import com.shouzhi.service.interf.db.ISchDeviceService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * 学校设备信息接口
 * @author WX
 * @date 2020-11-11 14:28:39
 */
@RestController
@RequestMapping("/api/v1/schDevice")
public class SchDeviceController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISchDeviceService schDeviceService;

    /**
     * 后台管理-查询学校设备信息列表
     * @apiNote 查询学校设备信息列表
     * @description 查询接口编写注意,不可使用对应的Model去接收,参数少就挨个写上,参数若真多只能再新建个VoModel
     *              因为新增和修改用的Model接收,里边必须及非必须参数会对接口使用人造成误解,新增或修改里必须参数在查询接口内不见得是必须
     * @param pageNum 页码(如第1页)
     * @param pageSize 每页数量(如每页10条)
     * @param deviceType 设备类型，1：摄像机，2：讲台计算机
     * @param deviceName 设备名称
     * @param ipAddr 设备ip地址
     * @param serialNumber 设备序列号
     * @param schSpaceId 学校空间信息id，不可与includeSubRegion同时使用
     * @param includeSubRegion 包含子区域，值为‘学校空间信息id’，<b>不可</b>与schSpaceId同时使用
     * @author WX
     * @date 2020-11-11 14:31:23
     */
    @PostMapping("/findList/{pageNum}/{pageSize}")
    public CommonResult<PageInfoVo<SchDevice>> findList(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
                                                        @RequestParam(value="schSpaceId",required=false) String schSpaceId,
                                                        @RequestParam(value="deviceType",required=false) String deviceType,
                                                        @RequestParam(value="deviceName",required=false) String deviceName,
                                                        @RequestParam(value="ipAddr",required=false) String ipAddr,
                                                        @RequestParam(value="serialNumber",required=false) String serialNumber,
                                                        @RequestParam(value="includeSubRegion",required=false) String includeSubRegion,
                                                        HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<PageInfoVo<SchDevice>> result = new CommonResult<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put("schSpaceId", schSpaceId);
        map.put("deviceType", deviceType);
        map.put("deviceName", deviceName);
        map.put("ipAddr", ipAddr);
        map.put("serialNumber", serialNumber);
        if(StringUtils.isNotBlank(includeSubRegion)){
            map.put("includeSubRegion", includeSubRegion);
            map.put("schSpaceIdsLike", String.join(includeSubRegion,"/","/"));
        }
        // 默认展示时过滤合成流，防止在设备展示页面被误删除
        map.put("deviceTypeNotEq", "overlay");
        PageHelper.startPage(pageNum,pageSize);
        List<SchDevice> schDevices = schDeviceService.queryListByPage(map);
        PageInfo<SchDevice> pageInfo = new PageInfo<>(schDevices);
        return result.setStatus(1).setMsg("查询成功").setResultBody(this.filterPage(pageInfo));
    }

    /**
     * 后台管理-新增学校设备信息
     * @apiNote 新增学校设备信息
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param schDevice
     * @author WX
     * @date 2020-11-11 14:51:06
     */
    @PostMapping("/save/{permId}")
    public CommonResult<String> saveSchDevice(@PathVariable("permId") String permId, @RequestBody SchDevice schDevice, HttpServletRequest req) {
        logger.info("url={},schDevice={}", req.getServletPath(),JSON.toJSONString(schDevice));
        CommonResult<String> result = new CommonResult<>();
        try {
            schDeviceService.save(schDevice, permId, req);
            result.setStatus(1).setMsg("新增成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


    /**
     * 后台管理-修改学校设备信息
     * @apiNote 修改学校设备信息
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param schDevice
     * @author WX
     * @date 2020-11-11 15:20:37
     */
    @PutMapping("/update/{permId}")
    public CommonResult<String> updateSchDeviceById(@PathVariable("permId") String permId,
                                                    @RequestBody SchDevice schDevice, HttpServletRequest req) {
        logger.info("url={},schDevice={}", req.getServletPath(),JSON.toJSONString(schDevice));
        CommonResult<String> result = new CommonResult<>();
        try {
            schDeviceService.update(schDevice, true, permId, req);
            result.setStatus(1).setMsg("更新成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


    /**
     * 后台管理-设置设备启用状态
     * @apiNote 设置设备启用状态
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param rowId 被操作记录行ID(路径参数)
     * @param isActive 是否激活设备，0：未激活，1：激活(路径参数)
     * @author WX
     * @date 2021-02-26 14:55:19
     */
    @PutMapping("/setActive/{permId}/{rowId}/{isActive}")
    public CommonResult<String> setActive(@PathVariable("permId") String permId, @PathVariable("rowId") String rowId,
                                          @PathVariable("isActive") String isActive, HttpServletRequest req) {
        logger.info("url={}", req.getServletPath());
        CommonResult<String> result = new CommonResult<>();
        try {
            SchDevice schDevice = new SchDevice();
            schDevice.setId(rowId);
            schDevice.setIsActive(isActive);
            schDeviceService.update(schDevice, false, permId, req);
            result.setStatus(1).setMsg("更新成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


    /**
     * 后台管理-设置云台启用状态
     * @apiNote 设置云台启用状态
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param rowId 被操作记录行ID(路径参数)
     * @param isEnableControl 是否启用云台控制，默认未启用，0：未启用，1：启用(路径参数)
     * @author WX
     * @date 2021-02-26 14:55:19
     */
    @PutMapping("/setEnableControl/{permId}/{rowId}/{isEnableControl}")
    public CommonResult<String> setEnableControl(@PathVariable("permId") String permId, @PathVariable("rowId") String rowId,
                                          @PathVariable("isEnableControl") String isEnableControl, HttpServletRequest req) {
        logger.info("url={}", req.getServletPath());
        CommonResult<String> result = new CommonResult<>();
        try {
            SchDevice schDevice = new SchDevice();
            schDevice.setId(rowId);
            schDevice.setIsEnableControl(isEnableControl);
            schDeviceService.update(schDevice, false, permId, req);
            result.setStatus(1).setMsg("更新成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


    /**
     * 后台管理-删除学校设备信息
     * @apiNote 删除学校设备信息
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param rowId 被操作记录行ID
     * @author WX
     * @date 2020-11-11 15:37:06
     */
    @DeleteMapping("/delete/{permId}/{rowId}")
    public CommonResult<String> delBySchDeviceId(@PathVariable("permId") String permId, @PathVariable("rowId") String rowId,
                                                 HttpServletRequest req){
        logger.info("url={}", req.getServletPath());
        CommonResult<String> result = new CommonResult<>();
        try {
            schDeviceService.delete(rowId, permId, req);
            result.setStatus(1).setMsg("删除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


    /**
     * 后台管理-批量删除学校设备信息
     * @apiNote 批量删除学校设备信息
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param schDeviceIds 学校设备信息ID，对应学校设备信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @author WX
     * @date 2020-11-11 16:12:30
     */
    @DeleteMapping(value = "/batchDelete/{permId}", consumes = "application/x-www-form-urlencoded")
    public CommonResult<String> batchDelBySchSpaceIds(@PathVariable("permId") String permId, @RequestParam("schDeviceIds") String schDeviceIds,
                                                         HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<String> result = new CommonResult<>();
        try {
            schDeviceService.batchDelete(schDeviceIds, permId, req);
            result.setStatus(1).setMsg("删除成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


    /**
     * 后台管理-导入学校设备信息
     * @apiNote 后台管理-导入学校设备信息，设备导入时默认移除对应空间区域下的旧设备数据，请做好数据备份
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param excelFile input标签的name值
     * @author WX
     * @date 2021-03-05 09:05:19
     */
    @PostMapping("/imp/{permId}")
    public CommonResult impSchDevice(@PathVariable("permId") String permId, HttpServletRequest req,
                                        @RequestParam("excelFile") MultipartFile excelFile) {
        logger.info("url={},ParameterMap={}，file.isEmpty={}，file.getSize={}，file.getContentType={}，file.getOriginalFilename={}，", req.getServletPath(), JSON.toJSONString(req.getParameterMap()),excelFile.isEmpty(),excelFile.getSize(),excelFile.getContentType(),excelFile.getOriginalFilename());
        CommonResult<String> result = new CommonResult<>();
        try {
            Integer count = schDeviceService.impSchDevice(permId, excelFile, req);
            result.setStatus(1).setMsg("导入成功"+count+"条");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


    /**
     * 后台管理-检测设备是否在线
     * @apiNote 检测设备是否在线(连接状态)，注意：前端请求该接口<b>必须设置请求的超时时间</b>，超时时间为<b>2S</b>。
     *          服务器在接到请求后会根据ID查询对应的设备，并检测设备是否在线，检测期间因网络波动等不确定因素，会造成超时。
     *          实现会尽最大努力尝试访问主机，但防火墙和服务器配置可能会阻止请求，从而导致无法访问的状态，而某些特定端口可能是可访问的。
     * @description service设置的超时时间为5S，让前端设置为6S的原因是：一旦遇到不在线的设备，服务器5S刚处理完毕，而前端若也为5S
     *              此时很有可能已经放弃此次请求了，拿不到真正的状态
     * @param schDeviceId 学校设备信息id
     * @author WX
     * @date 2020-11-12 10:28:16
     */
    @PostMapping("/checkOnline")
    public CommonResult<SchDevice> checkOnline(@RequestParam("schDeviceId") String schDeviceId, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<SchDevice> result = new CommonResult<>();
        try {
            SchDevice checkOnline = schDeviceService.checkOnline(schDeviceId);
            result.setStatus(1).setMsg("检测完成").setResultBody(checkOnline);
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


    /**
     * 后台管理-批量检测设备是否在线
     * @apiNote 批量检测设备是否在线(连接状态)，注意：前端请求该接口<b>必须设置请求的超时时间</b>，超时时间为<b>schDeviceId个数*1S</b>。
     *          如：schDeviceIds="2,18,49"，则超时时间应为<b>3*1S</b>，即3S
     *          服务器在接到请求后会根据ID查询对应的设备，并检测设备是否在线，检测期间因网络波动等不确定因素，会造成超时。
     *          实现会尽最大努力尝试访问主机，但防火墙和服务器配置可能会阻止请求，从而导致无法访问的状态，而某些特定端口可能是可访问的。
     * @description service设置的超时时间为5S，让前端设置为6S的原因是：一旦遇到不在线的设备，服务器5S刚处理完毕，而前端若也为5S
     *              此时很有可能已经放弃此次请求了，拿不到真正的状态
     * @param schDeviceIds 学校设备信息ID，对应学校设备信息列表内的每一行的ID，多个ID间使用英文逗号分隔
     * @author WX
     * @date 2020-12-14 16:40:27
     */
    @PostMapping("/batchCheckOnline")
    public CommonResult<List<SchDevice>> batchCheckOnline(@RequestParam("schDeviceIds") String schDeviceIds, HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<List<SchDevice>> result = new CommonResult<>();
        try {
            List<SchDevice> schDevices = schDeviceService.batchCheckOnline(schDeviceIds);
            result.setStatus(1).setMsg("检测完成").setResultBody(schDevices);
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }

}
