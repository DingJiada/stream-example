package com.shouzhi.controller.api.personalcenter;

import com.shouzhi.controller.BaseController;
import com.shouzhi.service.interf.db.ISchCourseTableBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 个人中心-我的课程-全部课程
 * @author WX
 * @date 2021-03-19 13:54:40
 */
@RestController
@RequestMapping("/api/v1/pc/allCourses")
public class PcAllCoursesController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISchCourseTableBaseService schCourseTableBaseService;

    // TODO SOMETHING

}
