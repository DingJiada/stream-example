package com.shouzhi.controller.api;

import com.alibaba.fastjson.JSON;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.controller.BaseController;
import com.shouzhi.service.interf.db.IShortcutMenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 快捷菜单接口
 * @author WX
 * @date 2021-01-21 17:54:29
 */
@RestController
@RequestMapping("/api/v1/shortcutMenu")
public class ShortcutMenuController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IShortcutMenuService shortcutMenuService;

    /**
     * 批量保存用户快捷菜单
     * @apiNote 批量保存用户快捷菜单
     * @param permId 权限ID或菜单ID(仅限于最后级别的菜单)
     * @param sysUserId 系统用户ID（非账户ID，不要搞混）
     * @param permissionIds 资源权限ID列表集合，该参数名对于前端来说不存在，容易造成误解，前端需要传的只是个数组对象，如：["1","2"]
     * @author WX
     * @date 2021-01-22 09:40:09
     */
    @PostMapping("/batchSave/{permId}/{sysUserId}")
    public CommonResult<String> batchSave(@PathVariable("permId") String permId, @PathVariable("sysUserId") String sysUserId,
                                          @RequestBody List<String> permissionIds, HttpServletRequest req) {
        logger.info("url={},permissionIds={}", req.getServletPath(), JSON.toJSONString(permissionIds));
        CommonResult<String> result = new CommonResult<>();
        try {
            shortcutMenuService.batchSave(sysUserId, permId, permissionIds, req);
            result.setStatus(1).setMsg("保存成功");
        } catch (Exception e) {
            this.fillIllegalArgResult(result, e, true, true, logger);
        }
        return result;
    }


}
