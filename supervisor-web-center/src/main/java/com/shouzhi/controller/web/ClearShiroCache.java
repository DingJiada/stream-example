package com.shouzhi.controller.web;


import com.shouzhi.config.CustomRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 手动调用controller清除shiro的缓存
 */
@RestController
public class ClearShiroCache {

	//注入realm
	@Autowired
	private CustomRealm customRealm;

	@RequestMapping("/clearShiroCache")
	public String clearShiroCache(){

		// 清除缓存，将来正常开发要在service调用customRealm.clearCached()
		// 在用户添加权限 或者 删除权限之后 都需要调用customRealm.clearCached();来清除所有的缓存。
		customRealm.clearCached();

		return "success";
	}

}
