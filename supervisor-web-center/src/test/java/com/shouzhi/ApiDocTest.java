package com.shouzhi;

import com.alibaba.fastjson.JSON;
import com.power.common.util.DateTimeUtil;
import com.power.doc.builder.*;
import com.power.doc.constants.DocGlobalConstants;
import com.power.doc.model.*;
import com.shouzhi.basic.common.ErrorCodeEnum;
import com.shouzhi.basic.common.VerifyOperTypeEnum;
import com.shouzhi.service.constants.BuiltInRoleCodeEnum;
import com.shouzhi.service.constants.PermCodeEnum;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ApiDoc测试
 * 简单描述它是什么？
 * 是一个生成文档的工具，不像Swagger那样会造成代码侵入和污染，依赖代码注释生成API文档
 * 直接导入依赖，创建单元测试类，编写配置代码，执行生成，完事
 * 当然，这还要取决于你的团队编码注释是否写的完整
 *
 * @author yu 2018/06/11.
 */
public class ApiDocTest {

    /**
     * 包括设置请求头，缺失注释的字段批量在文档生成期使用定义好的注释
     */
    @Deprecated
    @Test
    public void testBuilderControllersApi() {
        String thisPath = this.getClass().getClassLoader().getResource("").getPath();
        String pojoPath = thisPath + "../../../supervisor-pojo/";
        //TODO 改为你自己的
        String commonBasicPath = "E:/java/JetBrains/Intellij_IDEA/IdeaProjects/shouzhi-common-basic";

        ApiConfig config = new ApiConfig();
        config.setServerUrl("http://192.168.1.185:8180/supervisor-center");
        //true会严格要求注释，推荐设置true
        config.setStrict(true);
        //当把AllInOne设置为true时，Smart-doc将会把所有接口生成到一个Markdown、HHTML或者AsciiDoc中
        config.setAllInOne(true);
        //since 1.7.9 新增是否显示接口作者 默认true
        config.setShowAuthor(false);
        //生成html时加密文档名不暴露controller的名称
        config.setMd5EncryptedHtmlName(true);

        //自动将驼峰入参字段在文档中转为下划线格式,//@since 1.8.7 版本开始
        // config.setRequestFieldToUnderline(true);
        //自动将驼峰入参字段在文档中转为下划线格式,//@since 1.8.7 版本开始
        // config.setResponseFieldToUnderline(true);

        //since 1.7.5
        //如果该选项的值为false,则smart-doc生成allInOne.md文件的名称会自动添加版本号
        //config.setCoverOld(true);


        //since 1.7.5
        //设置项目名(非必须)，如果不设置会导致在使用一些自动添加标题序号的工具显示的序号不正常
        config.setProjectName("督导录播系统");


        //指定文档输出路径
        //@since 1.7 版本开始，选择生成静态html doc文档可使用该路径：DocGlobalConstants.HTML_DOC_OUT_PATH;
        config.setOutPath(DocGlobalConstants.HTML_DOC_OUT_PATH);
        // config.setOutPath("E:\\chen\\test-smart");

        // @since 1.2,如果不配置该选项，则默认匹配全部的controller,
        // 如果需要配置有多个controller可以使用逗号隔开
        config.setPackageFilters("com.shouzhi.controller.api");

        //不指定SourcePaths默认加载代码为项目src/main/java下的,如果项目的某一些实体来自外部代码可以一起加载
        config.setSourceCodePaths(
                //自1.7.0版本开始，在此处可以不设置本地代码路径，单独添加外部代码路径即可
                SourceCodePath.path().setDesc("本项目代码").setPath("src/main/java"),
                SourceCodePath.path().setDesc("supervisor-pojo").setPath(pojoPath),
                SourceCodePath.path().setDesc("shouzhi-common-basic").setPath(commonBasicPath)//,
        );


        //对于外部jar的类，编译后注释会被擦除，无法获取注释，但是如果量比较多请使用setSourcePaths来加载外部代码
        //如果有这种场景，则自己添加字段和注释，api-doc后期遇到同名字段则直接给相应字段加注释
        /*config.setCustomResponseFields(
                CustomRespField.field().setName("success").setDesc("成功返回true,失败返回false"),
                CustomRespField.field().setName("message").setDesc("接口响应信息"),
                CustomRespField.field().setName("data").setDesc("接口响应数据"),
                CustomRespField.field().setName("code").setValue("00000").setDesc("响应代码")
        );*/

        //设置公用请求头，如果没有请求头，可以不用设置
        /*config.setRequestHeaders(
                ApiReqHeader.header().setName("token").setRequired(true).setType("string").setDesc("token"),
                ApiReqHeader.header().setName("partnerId").setType("string").setRequired(true).setDesc("合作方账号")
        );*/


        //非必须只有当setAllInOne设置为true时文档变更记录才生效
        /*config.setRevisionLogs(
                RevisionLog.getLog().setRevisionTime("2018/12/15").setAuthor("chen").setRemarks("测试").setStatus("创建").setVersion("V1.0"),
                RevisionLog.getLog().setRevisionTime("2018/12/16").setAuthor("chen2").setRemarks("测试2").setStatus("修改").setVersion("V2.0")
        );*/

        //添加数据字典 since 1.7.5
        config.setDataDictionaries(
                ApiDataDictionary.dict().setTitle("验证账户-操作类型operType").setEnumClass(VerifyOperTypeEnum.class)
                        .setCodeField("code") //字典码值字段名
                        .setDescField("desc") //字段码
                ,ApiDataDictionary.dict().setTitle("内置角色编码").setEnumClass(BuiltInRoleCodeEnum.class)
                        .setCodeField("code").setDescField("desc")
                ,ApiDataDictionary.dict().setTitle("权限Code").setEnumClass(PermCodeEnum.class)
                        .setCodeField("code").setDescField("desc")
                /*,ApiDataDictionary.dict().setTitle("性别字典").setEnumClass(GenderEnum.class)
                        .setCodeField("code").setDescField("desc")*/
        );

        //1.7.9 添加错误码处理，用于替代遍历代码
        config.setErrorCodeDictionaries(
                ApiErrorCodeDictionary.dict().setEnumClass(ErrorCodeEnum.class)
                        .setCodeField("code") //错误码值字段名
                        .setDescField("msg")//错误码描述
        );

        /*//设置项目错误码列表，设置自动生成错误列表,
        List<ApiErrorCode> errorCodeList = new ArrayList<>();
        for (ErrorCodeEnum codeEnum : ErrorCodeEnum.values()) {
            ApiErrorCode errorCode = new ApiErrorCode();
            errorCode.setValue(codeEnum.getCode()).setDesc(codeEnum.getDesc());
            errorCodeList.add(errorCode);
        }
        //如果没需要可以不设置
        config.setErrorCodes(errorCodeList);*/

        /*//TODO 设置后未生效
        //1.8.5，使用自定义对象替换指定对象做文档渲染，必须使用全类名
        //泛型书写例子com.power.doc.model.LoginDto<com.power.doc.User>
        config.setApiObjectReplacements(
                ApiObjectReplacement.builder().setClassName("com.github.pagehelper.PageInfo")
                        .setReplacementClassName("com.shouzhi.pojo.vo.PageInfoVo<com.shouzhi.pojo.db.LogLogin>")
        );*/

        System.out.println(JSON.toJSONString(config));
        // ApiConfig config1 = JSON.parseObject(json,ApiConfig.class);
        // System.out.println(JSON.toJSONString(config1));

        long start = System.currentTimeMillis();
        //获取接口数据后自行处理
        HtmlApiDocBuilder.buildApiDoc(config);
        // AdocDocBuilder.builderControllersApi(config);
        // ApiDocBuilder.builderControllersApi(config);
        // PostmanJsonBuilder.buildPostmanApi(config);
        // ApiDataBuilder.getApiData(config);
        // ApiAllData apiAllData = ApiDataBuilder.getApiData(config);
        // List<ApiDoc> docList = ApiDocBuilder.listOfApiData(config);
        long end = System.currentTimeMillis();
        DateTimeUtil.printRunTime(end, start);
    }
}
