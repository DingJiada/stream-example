package com.shouzhi.controller.api;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文档阅读注意
 * @author WX
 * @date 2020-06-08 14:43:41
 */
@RestController
public class AaaDocReadmeController {

    /**
     * 文档阅读注意
     * @apiNote <br/>
     * 首先，本文档给出的所有接口均由程序产出
     *
     * 1.如有发现某个接口部分内容描述不清楚，难以理解。切勿凭空想象，为了双方时间及进度考虑，请立即与开发人员沟通，我们会必须且无条件全力配合
     *
     * 2.如有发现某个接口请求期间发生错误，请先仔细查看响应的错误描述，经自身程序查找无误后再与开发人员沟通，我们会分紧急程度尽快完善并发布
     * &nbsp;例：{"status":0,"errorCode":"1106002","msg":"http消息不可读或丢失，请检查 request body 是否正确携带","resultBody":null}
     * &nbsp;以上错误的原因明显是请求的http消息体有问题，比如选填参数都未填时直接扔个Null却未携带{}或携带格式错误{"key":"val",}等等
     * &nbsp;例：{"status":0,"errorCode":"1106003","msg":"媒体类型错误，请检查Content-Type","resultBody":null}
     * &nbsp;以上错误的原因明显是Content-Type指定错误，比如需要一个"application/json;charset=UTF-8"却扔了一个"application/x-www-form-urlencoded"或"text/plain"等等
     *
     * 3.请勿'过度依赖'每个接口给出的Request-example及Response-example，example中给出的JSON串仅为请求样例，样例数据均由程序反推导后随机生成
     * 有些字段并不适用我们的业务需求，如loginState，程序填充的为"loginState":"vl4905",而我们需要的是"loginState":"1"//或"0"
     * 样例仅表示它的具体含义，并不代表你可以向它一样原模原样去填充并发送请求。
     *
     * 4.部分'重要字段'我们会在Request-parameters及Response-fields中的Type、Description内标注清楚，还请仔细耐心观看以免造成歧义
     * 如：loginState | string | 登录状态，1成功，0失败
     *
     * 5.所有接口以最新发布为准，重大改动会提前通知
     *
     * N.等待补充......
     *
     * 最后，感谢你的耐心阅读、使用、理解与配合。
     */
    @PatchMapping("")
    public void docReadme(){}
}
