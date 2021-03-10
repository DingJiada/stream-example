package com.shouzhi.controller.web;

import com.github.pagehelper.PageInfo;
import com.shouzhi.basic.common.CommonResult;
import com.shouzhi.pojo.custom.DocTestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

// 请将此文件移至com.shouzhi.controller.api下并生成后查看
/**
 * 摄像机API|123
 * //从开始到下一个doc标签之前都会显示,包括本行
 * @apiNote 类级别的@apiNote不会被显示
 * @author WX
 * @date 2019-12-18 23:00:24
 */
// 这里的注释不会显示
@RestController
@RequestMapping("/api/v1/docTestApi")
public class DocTestApi {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 我不会被显示
    /**
     * 从这里开始到下一个doc标签之前都会显示
     * //我也会被显示
     * @description 这里可以写补充性注释或其他注释，不会被生成至文档
     * @apiNote 这是文档详细描述1，这是详细描述2，这是详细描述3，这是详细描述4，这是详细描述5，
     * 这是详细描述6，这是详细描述7，这是详细描述8，这是详细描述9，这是详细描述10。
     * @param docTestModel
     * @author WX
     * @return
     */
    // 这里的注释也不会显示
    @PostMapping("/addStream")
    public CommonResult<String> addStream(@RequestBody DocTestModel docTestModel){
        return null;
    }

    /**
     * 查询流地址
     * @description 这里可以写补充性注释或其他注释，不会被生成至文档
     * @apiNote 查询流地址，返回流列表
     * @param docTestModel
     * @author WX
     * @return
     */
    @GetMapping("/queryStream")
    public PageInfo queryStream(DocTestModel docTestModel){
        return null;
    }


    /**
     * 删除流地址
     * @description 这里可以写补充性注释或其他注释，不会被生成至文档
     * @apiNote 删除流地址，传入ID
     * @param id 流的ID
     * @author WX
     * @return
     */
    @DeleteMapping("/deleteStream")
    public CommonResult<String> deleteStream(Integer id){
        return null;
    }


    /**
     * 更新流地址
     * @description 这里可以写补充性注释或其他注释，不会被生成至文档
     * @apiNote 更新流地址，传入ID
     * @param docTestModel
     * @author WX
     * @return
     */
    @PutMapping("/updateStream")
    public CommonResult<String> updateStream(@RequestBody DocTestModel docTestModel){
        return null;
    }


}
