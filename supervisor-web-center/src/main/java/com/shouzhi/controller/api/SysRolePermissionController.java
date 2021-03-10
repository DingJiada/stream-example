package com.shouzhi.controller.api;

import com.alibaba.fastjson.JSON;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.basic.common.ErrorCodeEnum;
import com.shouzhi.controller.BaseController;
import com.shouzhi.pojo.db.SysPermission;
import com.shouzhi.pojo.po.SysPermissionPo;
import com.shouzhi.pojo.vo.SysPermissionSettingVo;
import com.shouzhi.pojo.vo.TreeNodeVo;
import com.shouzhi.service.interf.db.ISysRolePermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 系统角色-资源权限接口
 * @author WX
 * @date 2020-06-12 14:02:29
 */
@RestController
@RequestMapping("/api/v1/sysRolePermission")
public class SysRolePermissionController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISysRolePermissionService sysRolePermissionService;

    /**
     * 查询角色权限设置列表
     * @apiNote 查询角色权限设置列表，根据系统角色ID，返回权限树。
     *          根节点的attributes属性内存有：defaultCheckedKeys用于设置默认选中、defaultExpandedKeys用于设置默认展开
     * @param sysRoleId 系统角色ID
     * @author WX
     * @date 2020-08-25 16:02:29
     */
    @GetMapping("/permissionSettings/{sysRoleId}")
    public CommonResult<List<TreeNodeVo>> permissionSettingList(@PathVariable("sysRoleId") String sysRoleId, HttpServletRequest req){
        logger.info("url={}", req.getServletPath());
        List<TreeNodeVo> settingVos = sysRolePermissionService.selectPermissionSettingList(sysRoleId);
        return CommonResult.<List<TreeNodeVo>>getInstance().setStatus(1).setMsg("查询成功").setResultBody(settingVos);
    }

    /**
     * 批量保存系统角色-资源权限
     * @apiNote 批量保存系统角色-资源权限
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param sysRoleId 系统角色ID(路径参数)
     * @param permissionIds 资源权限ID列表集合，该参数名对于前端来说不存在，容易造成误解，前端需要传的只是个数组对象，如：["1","2"]
     * @author WX
     * @date 2020-06-12 14:02:29
     */
    @PostMapping("/batchSave/{permId}/{sysRoleId}")
    public CommonResult<String> batchSave(@PathVariable("permId") String permId, @PathVariable("sysRoleId") String sysRoleId,
                                         @RequestBody List<String> permissionIds, HttpServletRequest req) {
        logger.info("url={},permissionIds={}", req.getServletPath(), JSON.toJSONString(permissionIds));
        CommonResult<String> result = new CommonResult<>();
        try {
            sysRolePermissionService.batchSave(sysRoleId, permId, permissionIds, req);
            result.setStatus(1).setMsg("保存成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


}
