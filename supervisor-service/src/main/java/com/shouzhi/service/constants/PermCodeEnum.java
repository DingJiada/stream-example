package com.shouzhi.service.constants;

/**
 * 权限Code
 * @author WX
 * @date 2021-01-20 10:35:22
 */
public enum PermCodeEnum {
    //  Idea Ctrl+Shift+Alt 多选

    // 后台
    HOME("home", "首页"),
    USER_MANAGER("userManager", "用户管理"),
    BASIC_AUTH("basicAuth", "系统账号"),
    BASIC_AUTH_QUERY("basicAuth:query", "系统账号-查询账号"),
    BASIC_AUTH_BATCH_LOCKED("basicAuth:batchLocked", "系统账号-批量停用"),
    BASIC_AUTH_BATCH_UNLOCKED("basicAuth:batchUnlocked", "系统账号-批量启用"),
    BASIC_AUTH_BATCH_CANCEL("basicAuth:batchCancel", "系统账号-批量注销"),
    BASIC_AUTH_RESET_PWD("basicAuth:resetPwd", "系统账号-重置密码"),
    BASIC_AUTH_LOCKED("basicAuth:locked", "系统账号-停用账号"),
    BASIC_AUTH_UNLOCKED("basicAuth:unlocked", "系统账号-启用账号"),
    BASIC_AUTH_CANCEL("basicAuth:cancel", "系统账号-注销账号"),
    BASIC_AUTH_EXPORT("basicAuth:export", "系统账号-导出账号"),
    SYS_USER("sysUser", "系统用户"),
    SYS_USER_QUERY("sysUser:query", "系统用户-查询用户"),
    SYS_USER_CREATE("sysUser:create", "系统用户-添加用户"),
    SYS_USER_UPDATE("sysUser:update", "系统用户-修改用户"),
    SYS_USER_DELETE("sysUser:delete", "系统用户-删除用户"),
    SYS_USER_BATCH_DELETE("sysUser:batchDelete", "系统用户-批量删除"),
    SYS_USER_TO_REGISTER("sysUser:toRegister", "系统用户-一键注册"),
    SYS_USER_TO_BATCH_REGISTER("sysUser:toBatchRegister", "系统用户-批量注册"),
    SYS_ROLE_UPDATE_SYS_USER_ROLE("sysRole:updateSysUserRole", "系统用户-修改角色"),
    SYS_ROLE_BATCH_UPDATE_SYS_USER_ROLE("sysRole:batchUpdateSysUserRole", "系统用户-批量修改角色"),
    SYS_USER_EXPORT("sysUser:export", "系统用户-导出用户"),
    SYS_USER_IMPORT("sysUser:import", "系统用户-导入用户"),
    SYS_ROLE("sysRole", "系统角色"),
    SYS_ROLE_QUERY("sysRole:query", "系统角色-查询角色"),
    SYS_ROLE_CREATE("sysRole:create", "系统角色-添加角色"),
    SYS_ROLE_UPDATE("sysRole:update", "系统角色-修改角色"),
    SYS_ROLE_DELETE("sysRole:delete", "系统角色-删除角色"),
    SYS_ROLE_FIND_ROLE_MEMBERS("sysRole:findRoleMembers", "系统角色-查询角色成员"),
    SYS_ROLE_DEL_ROLE_MEMBER("sysRole:delRoleMember", "系统角色-移除角色成员"),
    SYS_ROLE_BATCH_DEL_ROLE_MEMBER("sysRole:batchDelRoleMember", "系统角色-批量移除角色成员"),
    SYS_ROLE_PERMISSION("sysRolePermission", "角色权限"),
    SYS_ROLE_PERMISSION_QUERY("sysRolePermission:query", "角色权限-查询角色权限"),
    SYS_ROLE_PERMISSION_BATCH_SAVE("sysRolePermission:batchSave", "角色权限-批量保存角色权限"),
    CLOUD_EXAM("cloudExam", "云考试管理"),
    EXAMINATION_HALL("examinationHall", "考场(试)管理"),
    EXAMINATION_HALL_QUERY("examinationHall:query", "考场(试)管理-查询考场(试)"),
    EXAMINATION_HALL_CREATE("examinationHall:create", "考场(试)管理-添加考场(试)"),
    EXAMINATION_HALL_UPDATE("examinationHall:update", "考场(试)管理-修改考场(试)"),
    EXAMINATION_HALL_DELETE("examinationHall:delete", "考场(试)管理-删除考场(试)"),
    EXAMINATION_HALL_BATCH_DELETE("examinationHall:batchDelete", "考场(试)管理-批量删除"),
    EXAMINATION_HALL_EXPORT("examinationHall:export", "考场(试)管理-导出考场(试)"),
    EXAMINEE_QUERY("examinee:query", "考场(试)管理-查看考生"),
    EXAMINEE_CREATE("examinee:create", "考场(试)管理-查看考生-添加考生"),
    EXAMINEE_UPDATE("examinee:update", "考场(试)管理-查看考生-修改考生"),
    EXAMINEE_DELETE("examinee:delete", "考场(试)管理-查看考生-删除考生"),
    EXAMINEE_BATCH_DELETE("examinee:batchDelete", "考场(试)管理-查看考生-批量删除"),
    EXAMINEE_EXPORT("examinee:export", "考场(试)管理-查看考生-导出考生"),
    EXAM_ALBUM("examAlbum", "考场视频管理"),
    EXAM_ALBUM_QUERY("examAlbum:query", "考场视频管理-查询专辑"),
    EXAM_ALBUM_DELETE("examAlbum:delete", "考场视频管理-删除专辑"),
    EXAM_ALBUM_BATCHDELETE("examAlbum:batchDelete", "考场视频管理-批量删除"),
    EXAM_VIDEO_QUERY("examVideo:query", "考场视频管理-考场视频-查询考场视频"),
    EXAM_VIDEO_DELETE("examVideo:delete", "考场视频管理-考场视频-删除考场视频"),
    EXAM_VIDEO_BATCH_DELETE("examVideo:batchDelete", "考场视频管理-考场视频-批量删除"),
    SYSTEM_SETTING("systemSetting", "系统设置"),
    SYS_PERMISSION("sysPermission", "菜单设置"),
    SYS_PERMISSION_FIND_TREE("sysPermission:findTree", "菜单设置-查询菜单树"),
    SYS_PERMISSION_QUERY("sysPermission:query", "菜单设置-查询菜单"),
    SYS_PERMISSION_CREATE("sysPermission:create", "菜单设置-添加菜单"),
    SYS_PERMISSION_UPDATE("sysPermission:update", "菜单设置-修改菜单"),
    SYS_PERMISSION_DELETE("sysPermission:delete", "菜单设置-删除菜单"),
    SYS_PERMISSION_BATCH_DELETE("sysPermission:batchDelete", "菜单设置-批量删除"),
    SYS_STATIC_PARAM("sysStaticParam", "参数设置"),
    SYS_STATIC_PARAM_QUERY("sysStaticParam:query", "参数设置-查询参数"),
    SYS_STATIC_PARAM_BATCH_UPDATE("sysStaticParam:batchUpdate", "参数设置-批量保存"),
    SYS_STATIC_PARAM_UPLOAD_IMG("sysStaticParam:uploadImg", "参数设置-上传图片"),
    SYS_STATIC_PARAM_DELETE_IMG("sysStaticParam:deleteImg", "参数设置-删除图片"),
    SERVER_HOST("serverHost", "服务器设置"),
    SERVER_HOST_QUERY("serverHost:query", "查询服务器"),
    SERVER_HOST_UPDATE("serverHost:update", "修改服务器"),
    BASIC_SETTING("basicSetting", "基础设置"),
    SCH_SPACE("schSpace", "空间管理"),
    SCH_SPACE_FIND_TREE("schSpace:findTree", "空间管理-查询空间树"),
    SCH_SPACE_QUERY("schSpace:query", "空间管理-查询空间"),
    SCH_SPACE_CREATE("schSpace:create", "空间管理-新增空间"),
    SCH_SPACE_UPDATE("schSpace:update", "空间管理-修改空间"),
    SCH_SPACE_DELETE("schSpace:delete", "空间管理-删除空间"),
    SCH_SPACE_BATCH_DELETE("schSpace:batchDelete", "空间管理-批量删除"),
    SCH_SPACE_IMPORT("schSpace:import", "空间管理-导入"),
    SCH_DEVICE("schDevice", "设备信息"),
    SCH_DEVICE_QUERY("schDevice:query", "设备信息-查询设备"),
    SCH_DEVICE_CREATE("schDevice:create", "设备信息-添加设备"),
    SCH_DEVICE_UPDATE("schDevice:update", "设备信息-修改设备"),
    SCH_DEVICE_DELETE("schDevice:delete", "设备信息-删除设备"),
    SCH_DEVICE_BATCH_DELETE("schDevice:batchDelete", "设备信息-批量删除"),
    SCH_DEVICE_CHECK_ONLINE("schDevice:checkOnline", "设备信息-检测设备"),
    SCH_DEVICE_BATCH_CHECK_ONLINE("schDevice:batchCheckOnline", "设备信息-批量检测设备"),
    SCH_DEVICE_IMPORT("schDevice:import", "设备信息-导入"),
    SCH_COURSE_TABLE_BASE("schCourseTableBase", "基础课表"),
    SCH_COURSE_TABLE_BASE_QUERY("schCourseTableBase:query", "基础课表-查询课表"),
    SCH_COURSE_TABLE_BASE_TABLE_GRID("schCourseTableBase:tableGrid", "基础课表-查询课表(网格版)"),
    SCH_COURSE_TABLE_BASE_UPDATE("schCourseTableBase:update", "基础课表-更新课表"),
    SCH_COURSE_CATEGORY("schCourseCategory", "课程类别"),
    SCH_COURSE_CATEGORY_QUERY("schCourseCategory:query", "课程类别-查询课程类别"),
    SCH_COURSE_CATEGORY_CREATE("schCourseCategory:create", "课程类别-添加课程类别"),
    SCH_COURSE_CATEGORY_UPDATE("schCourseCategory:update", "课程类别-修改课程类别"),
    SCH_COURSE_CATEGORY_DELETE("schCourseCategory:delete", "课程类别-删除课程类别"),
    SCH_COURSE_CATEGORY_BATCH_DELETE("schCourseCategory:batchDelete", "课程类别-批量删除"),
    SCH_COURSE_CATEGORY_IMPORT("schCourseCategory:import", "课程类别-导入"),
    SYS_DEPARTMENT("sysDepartment", "组织单位"),
    SYS_DEPARTMENT_FIND_TREE("sysDepartment:findTree", "组织单位-部门树"),
    SYS_DEPARTMENT_QUERY("sysDepartment:query", "组织单位-查询组织"),
    SYS_DEPARTMENT_CREAT_E("sysDepartment:create", "组织单位-添加组织"),
    SYS_DEPARTMENT_UPDATE("sysDepartment:update", "组织单位-修改组织"),
    SYS_DEPARTMENT_DELETE("sysDepartment:delete", "组织单位-删除组织"),
    SYS_DEPARTMENT_BATCH_DELETE("sysDepartment:batchDelete", "组织单位-批量删除"),
    SYS_DEPARTMENT_IMPORT("sysDepartment:import", "组织单位-导入"),
    SCH_GRADE_CLASS("schGradeClass", "班级管理"),
    SCH_GRADE_CLASS_QUERY("schGradeClass:query", "班级管理-查询班级"),
    SCH_GRADE_CLASS_CREATE("schGradeClass:create", "班级管理-添加班级"),
    SCH_GRADE_CLASS_UPDATE("schGradeClass:update", "班级管理-修改班级"),
    SCH_GRADE_CLASS_DELETE("schGradeClass:delete", "班级管理-删除班级"),
    SCH_GRADE_CLASS_BATCH_DELETE("schGradeClass:batchDelete", "班级管理-批量删除"),
    SCH_GRADE_CLASS_IMPORT("schGradeClass:import", "班级管理-导入"),
    SCH_SEMESTER("schSemester", "学期管理"),
    SCH_SEMESTER_QUERY("schSemester:query", "学期管理-查询学期"),
    SCH_SEMESTER_CREATE("schSemester:create", "学期管理-添加学期"),
    SCH_SEMESTER_UPDATE("schSemester:update", "学期管理-修改学期"),
    SCH_SEMESTER_DELETE("schSemester:delete", "学期管理-删除学期"),
    SCH_SEMESTER_BATCH_DELETE("schSemester:batchDelete", "学期管理-批量删除"),
    SCH_SEMESTER_IMPORT("schSemester:import", "学期管理-导入"),
    LOG_MANAGER("logManager", "日志管理"),
    LOG_LOGIN("logLogin", "登录日志"),
    LOG_LOGIN_QUERY("logLogin:query", "登录日志-查询登录日志"),
    LOG_OPER("logOper", "操作日志"),

    // 前台
    FG_COURSE_LIVE("fgCourseLive", "课程直播"),
    FG_CLASS_PLAY_BACK("fgClassPlayBack", "课堂回放"),
    FG_PATROL_COURSE("fgPatrolCourse", "在线巡课"),
    FG_PATROL_COURSE_QUERY("fgPatrolCourse:query", "在线巡课-查询课表"),
    FG_PATROL_COURSE_DETAIL("fgPatrolCourseDetail", "在线巡课-课表详情(内联菜单)"),
    FG_PATROL_COURSE_DETAIL_QUERY("fgPatrolCourseDetail:query", "在线巡课-查询课表详情"),
    FG_PATROL_COURSE_COURSE_TREE("fgPatrolCourse:courseTree", "在线巡课-查询课表树"),
    FG_PATROL_COURSE_DETAIL_PTZ("fgPatrolCourseDetail:ptz", "在线巡课-云台控制"),
    FG_EXAM_HALL("fgExamHall", "云监考"),
    ;


    private String code;
    private String desc;

    PermCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return "PermCodeEnum{" +
                "code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
