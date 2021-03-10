package com.shouzhi.service.constants;

/**
 * 文件路径常量
 * @author WX
 * @date 2021-02-06 10:20:17
 */
public class FilePathConst {
    /**
     * 私有化构造方法
     */
    private FilePathConst(){}

    /**
     * 类路径(相对路径,需要用项目所在服务器的根路径去拼接)
     */
    public static final String REL_CLASS_PATH = "WEB-INF/classes/";

    /**
     * excel导入模板文件路径(相对路径,需要用项目所在服务器的根路径去拼接)
     */
    public static final String REL_EXCEL_IMP_PATH = REL_CLASS_PATH + "static/jxls-template/imp/";

}
