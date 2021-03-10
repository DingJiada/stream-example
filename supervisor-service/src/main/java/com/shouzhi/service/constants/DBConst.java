package com.shouzhi.service.constants;

/**
 * 数据库相关常量
 * @author WX
 * @date 2020-06-11 10:34:57
 */
public final class DBConst {
    /**
     * 私有化构造方法
     */
    private DBConst(){}

    /**
     * 表名字
     */
    public static final String TABLE_NAME_WR_BASIC_AUTH                     = "wr_basic_auth";
    // public static final String TABLE_NAME_WR_BASIC_AUTH_DEL                 = "wr_basic_auth_del";
    public static final String TABLE_NAME_WR_EXAM_VIDEO                     = "wr_exam_video";
    public static final String TABLE_NAME_WR_EXAMINATION_HALL               = "wr_examination_hall";
    public static final String TABLE_NAME_WR_EXAMINATION_HALL_INVIGILATORS  = "wr_examination_hall_Invigilators";
    public static final String TABLE_NAME_WR_EXAMINEE                       = "wr_examinee";
    public static final String TABLE_NAME_WR_LOG_LOGIN                      = "wr_log_login";
    public static final String TABLE_NAME_WR_LOG_OPER                       = "wr_log_oper";
    public static final String TABLE_NAME_WR_LOG_OPER_DETAIL                = "wr_log_oper_detail";
    public static final String TABLE_NAME_WR_SCH_COURSE_CATEGORY            = "wr_sch_course_category";
    public static final String TABLE_NAME_WR_SCH_COURSE_TABLE_BASE          = "wr_sch_course_table_base";
    public static final String TABLE_NAME_WR_SCH_COURSE_TABLE_LIVE          = "wr_sch_course_table_live";
    public static final String TABLE_NAME_WR_SCH_DEVICE                     = "wr_sch_device";
    public static final String TABLE_NAME_WR_SCH_GRADE_CLASS                = "wr_sch_grade_class";
    public static final String TABLE_NAME_WR_SCH_SEMESTER                   = "wr_sch_semester";
    public static final String TABLE_NAME_WR_SCH_SPACE                      = "wr_sch_space";
    public static final String TABLE_NAME_WR_SECURITY_QUESTION_SET          = "wr_security_question_set";
    public static final String TABLE_NAME_WR_SECURITY_QUESTIONS             = "wr_security_questions";
    public static final String TABLE_NAME_WR_SERVER_HOST                    = "wr_server_host";
    public static final String TABLE_NAME_WR_SHORTCUT_MENU                  = "wr_shortcut_menu";
    public static final String TABLE_NAME_WR_SYS_DEPARTMENT                 = "wr_sys_department";
    public static final String TABLE_NAME_WR_SYS_DEPARTMENT_USER            = "wr_sys_department_user";
    public static final String TABLE_NAME_WR_SYS_PERMISSION                 = "wr_sys_permission";
    public static final String TABLE_NAME_WR_SYS_ROLE                       = "wr_sys_role";
    public static final String TABLE_NAME_WR_SYS_ROLE_PERMISSION            = "wr_sys_role_permission";
    public static final String TABLE_NAME_WR_SYS_USER                       = "wr_sys_user";
    public static final String TABLE_NAME_WR_SYS_USER_ROLE                  = "wr_sys_user_role";

    /**
     * 操作类型 新增、删除、更新、批量新增(导入)、批量删除、批量更新
     */
    public static final String OPER_TYPE_INSERT         = "1";
    public static final String OPER_TYPE_DELETE         = "2";
    public static final String OPER_TYPE_UPDATE         = "3";
    public static final String OPER_TYPE_BATCH_INSERT   = "4";
    public static final String OPER_TYPE_BATCH_DELETE   = "5";
    public static final String OPER_TYPE_BATCH_UPDATE   = "6";

    /**
     * 是否为级(关)联操作 0：不是、1：是
     */
    public static final String NO_CASCADE = "0";
    public static final String IS_CASCADE = "1";

    /**
     * 表字段对应的枚举值，以数据库字段注释为准
     */
    public static final String WR_LOG_LOGIN_LOGIN_STATE_SUCCESS                 = "1";
    public static final String WR_LOG_LOGIN_LOGIN_STATE_FAIL                    = "0";
    public static final String WR_LOG_LOGIN_LOGIN_CODE_SUCCESS                  = "200";
    public static final String WR_LOG_LOGIN_LOGIN_CODE_PWD_ERROR                = "101";
    public static final String WR_LOG_LOGIN_LOGIN_CODE_CAPTCHA_ERROR            = "102";
    public static final String WR_LOG_LOGIN_LOGIN_CODE_FAIL_REPEAT              = "104";

    /**
     * 每张表的id名称，如果每张表不一样，需要继续单独定义，本系统所有表id名称都为"id"，所以是统一的
     */
    public static final String TABLE_UNIFIED_ID = "id";

}
