package com.shouzhi.controller.api;

import com.alibaba.fastjson.JSON;
import com.shouzhi.basic.utils.FileDownloadUtil;
import com.shouzhi.controller.BaseController;
import com.shouzhi.service.constants.FilePathConst;
import com.shouzhi.service.customexception.FileDownloadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 文件下载接口
 * @author WX
 * @date 2021-02-06 10:35:39
 */
@Controller
@RequestMapping("api/v1/fileDownload")
public class FileDownloadController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 下载excel模板
     * @apiNote 下载excel模板。
     *          sys_user：系统用户导入模板.xls、sch_space_part：学校空间部分导入模板.xls、sch_space_all：学校空间全部导入模板.xls、
     *          sch_device：学校设备导入模板.xls、
     * @param fName 文件名称，具体模板对应名称见上方接口描述
     * @param req req
     * @author WX
     * @date 2021-02-06 10:36:18
     */
    @PostMapping("/excelTemplate")
    public void excelTemplate(@RequestParam("fName") String fName, HttpServletRequest req, HttpServletResponse resp){
        logger.info("url={},ParameterMap={}", req.getServletPath(), JSON.toJSONString(req.getParameterMap()));
        switch (fName){
            case "sys_user":
                FileDownloadUtil.projectFile("系统用户导入模板.xls", FilePathConst.REL_EXCEL_IMP_PATH+"sys_user.xls", req, resp);
                break;
            case "sch_space_part":
                FileDownloadUtil.projectFile("学校空间部分导入模板.xls", FilePathConst.REL_EXCEL_IMP_PATH+"sch_space_part.xls", req, resp);
                break;
            case "sch_space_all":
                FileDownloadUtil.projectFile("学校空间全部导入模板.xls", FilePathConst.REL_EXCEL_IMP_PATH+"sch_space_all.xls", req, resp);
                break;
            case "sch_device":
                FileDownloadUtil.projectFile("学校设备导入模板.xls", FilePathConst.REL_EXCEL_IMP_PATH+"sch_device.xls", req, resp);
                break;
            case "sch_course_category":
                FileDownloadUtil.projectFile("学校课程类别导入模板.xls", FilePathConst.REL_EXCEL_IMP_PATH+"sch_course_category.xls", req, resp);
                break;
            default:
                throw new FileDownloadException("下载excel模板失败，未找到该文件");
        }
    }

}
