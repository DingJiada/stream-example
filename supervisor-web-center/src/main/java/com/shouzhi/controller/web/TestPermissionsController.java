package com.shouzhi.controller.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试ShiroPermissionsController
 * @author WX
 * @date 2019-08-22 21:32:46
 */
/*@Controller
@RequestMapping("/user")*/
public class TestPermissionsController {

    /*@Autowired
    IRegisterUserService registerUserService;

    @RequestMapping("/showUsers")
    @ResponseBody
    public Object showUsers(){
        Map<String,Object> map = new HashMap<>();
        List<RegisterUser> registerUsers = registerUserService.queryListByPage(map);
        return registerUsers;
    }

    @RequiresPermissions("user:show")
    @ResponseBody
    @RequestMapping("/show")
    public String showUser() {
        return "这是学生信息";
    }*/

}
