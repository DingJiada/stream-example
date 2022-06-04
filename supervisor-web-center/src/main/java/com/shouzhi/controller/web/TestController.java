package com.shouzhi.controller.web;

import com.shouzhi.service.common.RedisTemplateService;
import com.shouzhi.basic.utils.CredentialsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 测试页面，用于测试各项功能是否可以正常使用，是否集成成功
 */
@Controller
@RequestMapping("/public/test")
public class TestController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /*@Autowired
    IRegisterUserService registerUserService;*/

    @Autowired
    RedisTemplateService redisTemplateService;
    @Autowired  //@Qualifier("customProperties")
            Properties customProperties;



    @RequestMapping("/getStr")
    @ResponseBody
    public Object getStr(){
        logger.info("进入/getStr");
        Map<String, Object> map = new HashMap<>();
        map.put("key1","val1");
        map.put("key2","中文val2");
        return map;
    }

    /*@RequestMapping("/getRegisterUser")
    @ResponseBody
    public CommonResult<RegisterUser> getRegisterUser(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        RegisterUser registerUser = registerUserService.selectByPrimaryKey(1L);
        if(registerUser!=null){
            //resp.sendRedirect("/test/input");
            req.setAttribute("key","123456req");
            req.getRequestDispatcher("/test/input").forward(req,resp);
            return null;

        }
        System.out.println("88888888");
        return new CommonResult<RegisterUser>().setStatus(1).setResultBody(registerUser);
    }*/

    // @RequestMapping("/{page}")
    // public String showPage(@PathVariable("page") String page, HttpServletRequest request){
    //     System.out.println(request.getAttribute("key"));
    //     request.setAttribute("key", request.getAttribute("key"));
    //     return page;
    // }


    @RequestMapping("/setRedis")
    @ResponseBody
    public Object setRedis(){
        redisTemplateService.setStr("wxkey123","wxvalue456");
        System.out.println("设置完成！");
        return "设置完成！";
    }


    @RequestMapping("/getRedis")
    @ResponseBody
    public Object getRedis(){
        String wxkey123 = redisTemplateService.getStr("wxkey123");
        System.out.println(wxkey123);
        return wxkey123;
    }


    @RequestMapping("/redisDB")
    @ResponseBody
    public Object redisDB(){
        String wxkey123 = redisTemplateService.getStr("wxkey123");
        System.out.println(wxkey123);

        redisTemplateService.selectDB(1).setStr("wxkey123-db1","wxvalue456-db1");
        String wxkey1231 = redisTemplateService.getStr("wxkey123-db1");
        System.out.println(wxkey1231);

        redisTemplateService.setStr("123-db1","456-db1");
        String str = redisTemplateService.getStr("123-db1");
        System.out.println(str);

        redisTemplateService.selectDB(0).setStr("wxkey123-db0","wxvalue456-db0");
        String wxkey123db0 = redisTemplateService.getStr("wxkey123-db0");
        System.out.println(wxkey123db0);
        return wxkey123;
    }


    /*@RequestMapping("/testDB")
    @ResponseBody
    public Object testDB(){
        logger.info("进入/testDB");
        Map<String, Object> map = new HashMap<>();
        List<RegisterUser> registerUsers = registerUserService.queryListByPage(map);
        return registerUsers;
    }*/

    @RequestMapping("/getProperties")
    @ResponseBody
    public Object customProperties(){
        System.out.println(customProperties.getProperty("cstm.file.back.path"));
        return customProperties;
    }


    @RequestMapping("/testUtils")
    @ResponseBody
    public Object testUtils(){
        String md5Pwd = CredentialsUtil.MD5Pwd("dadw", "salt", 1024);
        return md5Pwd;
    }

    @RequestMapping("/validateCode")
    public String send(Model model){
        model.addAttribute("validateCode","123456");
        return "jmail/emailValidateCode";
    }


}
