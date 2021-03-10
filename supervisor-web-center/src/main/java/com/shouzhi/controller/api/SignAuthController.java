package com.shouzhi.controller.api;

import com.alibaba.fastjson.JSON;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.basic.utils.SignUtil;
import com.shouzhi.pojo.vo.SignAuthVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Sign签名鉴权接口
 * @author WX
 * @date 2020-08-13 09:40:47
 */
@RestController
@RequestMapping("/public/api/v1/signAuth")
public class SignAuthController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 推/拉流-单签
     * @apiNote
     *          推流或拉流-单个签名，流媒体服务器现以采用鉴权方式推拉流。鉴权未通过的不予进行任何操作。
     *          接口接收两个参数，appName、streamName用于生成签名。如下方地址加黑字体则是对应的appName、streamName
     *          拉流：http://192.168.1.136:8080/<b>live</b>/<b>wangwang</b>.flv，appName则为live，streamName则为wangwang
     *          推流：rtmp://192.168.1.136:1935/<b>live</b>/<b>wangwang</b>，appName则为live，streamName则为wangwang
     *
     *          生成后返回exp、sign两个参数，前端或客户端需使用QueryString方式自行将其拼接在将要推流或拉流的地址后，
     *          如拉流：http://192.168.1.136:8080/live/wangwang.flv?<b>exp=1592791105866</b>&<b>sign=MjI0OWNiMzk1ZmZhYzRkZWRmZDM3NWY1MzkyMGNjMDk=</b>
     *          如推流：rtmp://192.168.1.136:1935/live/wangwang?<b>exp=1493741108860</b>&<b>sign=GQU0EMNiMWDUZmZhYzRkZLDUPDM3NWY1MzkyMGNjKDNT</b>&otherKey=otherVal...
     *          (注：本接口现在采用public方式访问并不代表以后仍采用public方式访问，将来很有可能采用携带token方式访问)
     * @param appName 应用名称
     * @param streamName 流名称
     * @author WX
     * @date 2020-08-13 09:43:25
     */
    @PostMapping("/stream/single")
    public CommonResult<SignAuthVo> streamSingle(@RequestParam("appName") String appName, @RequestParam("streamName") String streamName,
                                                 HttpServletRequest req){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        CommonResult<SignAuthVo> result = new CommonResult<>();
        String exp = String.valueOf(System.currentTimeMillis());
        String base64UrlSign = SignUtil.base64UrlSign(appName, streamName, exp);
        // ?exp=1592535978338&sign=OTllMTI4MDdhYTIxMjVjNWQ3ODQ3YzUwNjQwMjIzYjk=
        return result.setStatus(1).setMsg("签发成功").setResultBody(new SignAuthVo(exp, base64UrlSign));
    }

    // 为其他类型签名占位
    // @PostMapping("/stream/single")


}
