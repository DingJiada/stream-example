package com.shouzhi.controller;

/*@Controller
@RequestMapping("/test")*/
public class TestController {
    /*private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IWrSpacesService wrSpacesService;
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


    @RequestMapping("/getProperties")
    @ResponseBody
    public Object customProperties(){
        System.out.println(customProperties.getProperty("custom.wechat.subscription.appid"));
        return customProperties;
    }

    @RequestMapping("/testUtils")
    @ResponseBody
    public Object testUtils(){
        String md5Pwd = CredentialsUtil.MD5Pwd("dadw", "salt", 1024);
        return md5Pwd;
    }


    @RequestMapping("/testD")
    @ResponseBody
    public Object testD(){
        WrSpaces wrSpaces = new WrSpaces();
        wrSpaces.setSpaceId(1);
        wrSpaces.setCode("code1");
        wrSpaces.setName("name1");
        wrSpaces.setParentId(1);
        wrSpaces.setIsActive(false);
        wrSpaces.setIsDel(false);
        wrSpaces.setCreateTime(new Date());


        wrSpaces = wrSpacesService.selectByPrimaryKey(27);
        // Integer integer = wrSpacesService.insertSelective(wrSpaces);
        // Integer integer = wrSpacesService.updateByPrimaryKeySelective(wrSpaces);
        // System.out.println(integer);
        return wrSpaces;
    }*/

}
