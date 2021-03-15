package com.shouzhi.service.constants;

/**
 * 数据库枚举
 * @author WX
 * @date 2020-11-18 16:46:37
 */
public enum DBEnum {

    // 唯一索引描述

    // wr_basic_auth
    sys_user_id_uniqueIndex("sys_user_id_uniqueIndex", "系统用户已存在"),
    user_name_uniqueIndex("user_name_uniqueIndex", "用户名已存在"),
    person_num_uniqueIndex("person_num_uniqueIndex", "用户编号已存在"),
    user_mobile_uniqueIndex("user_mobile_uniqueIndex", "用户手机已存在"),
    user_email_uniqueIndex("user_email_uniqueIndex", "用户邮箱已存在"),

    // wr_sch_course_table_live
    sch_course_table_base_id_weeks_uniqueIndex("sch_course_table_base_id_weeks_uniqueIndex", "基础课表唯一标识+对应周数的直播计划已存在"),

    // wr_sch_device
    ip_addr_uniqueIndex("ip_addr_uniqueIndex", "IP地址已存在"),
    rtmp_get_url_uniqueIndex("rtmp_get_url_uniqueIndex", "rtmp stream 地址已存在"),
    flv_get_url_uniqueIndex("flv_get_url_uniqueIndex", "flv stream 地址已存在"),
    hls_get_url_uniqueIndex("hls_get_url_uniqueIndex", "hls stream 地址已存在"),
    sch_space_id_device_location_uniqueIndex("sch_space_id_device_location_uniqueIndex", "空间区域+设备位置已存在"),
    rtsp_url_main_uniqueIndex("rtsp_url_main_uniqueIndex", "rtsp main stream 地址已存在"),
    rtsp_url_sub_uniqueIndex("rtsp_url_sub_uniqueIndex", "rtsp sub stream 地址已存在"),

    // wr_sch_semester
    sem_code_uniqueIndex("sem_code_uniqueIndex", "学期编码已存在"),
    school_year_start_end_sem_num_uniqueIndex("school_year_start_end_sem_num_uniqueIndex", "开始学年+结束学年+学期编号已存在"),

    // wr_sch_space
    space_code_parent_space_codes_uniqueIndex("space_code_parent_space_codes_uniqueIndex", "空间编码+父空间编码列表已存在"),
    space_name_parent_space_names_uniqueIndex("space_name_parent_space_names_uniqueIndex", "空间名称+父空间名称列表已存在"),

    // wr_server_host
    protocol_addr_port_type_uniqueIndex("protocol_addr_port_type_uniqueIndex", "主机协议+地址+端口+类型已存在"),

    // wr_sys_department
    dep_code_uniqueIndex("dep_code_uniqueIndex", "部门编码已存在"),

    // wr_sys_permission
    percode_uniqueIndex("percode_uniqueIndex", "权限编码已存在"),

    // wr_sys_role
    role_code_uniqueIndex("role_code_uniqueIndex", "角色编码已存在"),
    ;


    private String code;
    private String desc;

    DBEnum(String code, String desc) {
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
        return "DBEnum{" +
                "code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
