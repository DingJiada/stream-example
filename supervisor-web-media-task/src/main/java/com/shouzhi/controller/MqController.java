package com.shouzhi.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shouzhi.basic.utils.CredentialsUtil;
import com.shouzhi.config.RabbitmqConfig;
import com.shouzhi.service.common.RedisTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Controller
@RequestMapping("/mq")
public class MqController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RabbitTemplate rabbitTemplate;


    /**
     * 测试
     * @return
     */
    @RequestMapping("/getStr")
    @ResponseBody
    public Object getStr(){
        logger.info("进入/getStr");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", "on_dvr");
        jsonObject.put("app", "live");
        jsonObject.put("client_id", 1478);
        jsonObject.put("cwd", "/home/wsm/srs.oschina/trunk");
        jsonObject.put("file", "./objs/nginx/html/recorded/live/XE001_XS20130003/2020/08/20/15.53.40.999.flv");
        jsonObject.put("param", "?serviceType=yjk&hallCode=XE001&studentCode=XS20130003&publishType=sw&time=1597645911654&sign=ZDdjM2I0NzYxZmMxZTVkNTFkMTY2ZDY3MWIzYmI0YzQ=");
        jsonObject.put("stream", "XE001_XS20130003");
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXC_MEDIA_PROCESSOR, RabbitmqConfig.ROUTINGKEY_MEDIA_SERVER_DVR, JSON.toJSONString(jsonObject));
        return "123";
    }


}
