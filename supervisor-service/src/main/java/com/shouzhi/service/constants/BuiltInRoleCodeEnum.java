package com.shouzhi.service.constants;

/**
 * 内置角色编码
 * @author WX
 * @date 2020-07-22 13:58:07
 */
public enum BuiltInRoleCodeEnum {

    BUILT_IN_TEACHER("built_in_teacher", "内置角色_老师"),
    ;


    private String code;
    private String desc;

    BuiltInRoleCodeEnum(String code, String desc) {
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
        return "BuiltInRoleCodeEnum{" +
                "code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
