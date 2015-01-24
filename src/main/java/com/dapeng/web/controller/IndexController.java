package com.dapeng.web.controller;

import com.dapeng.service.IndexService;
import com.dapeng.service.SimpleProductInfo;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class IndexController {

	private static Logger logger = LoggerFactory.getLogger(IndexController.class);

	@Autowired
	private IndexService indexService;

	@RequestMapping(value = {"/", "/index", "/helloworld"})
	public String index(){

		return "index";
	}

	@RequestMapping(value = "/add")
	public String add(@RequestParam("name") String name){
		indexService.save(name);
		return "redirect:/list";
	}

	@RequestMapping(value = "/list")
	public String show(Model model){
		List<SimpleProductInfo> list = indexService.show();
		model.addAttribute("list", list);
		return "list";
	}

	@RequestMapping(value = "/test")
	@ResponseBody
	public Map<String, Object> test() {
		Map<String, Object> map = Maps.newHashMap();

		map.put("success", true);
		map.put("message", "this is a error message");

		return map;
	}

	@RequestMapping(value = "/demo")
	public String demo(@RequestParam(value = "name", defaultValue = "zhangsan") String name,
					   HttpServletRequest request){

		System.out.println(request.getMethod());
		logger.info(request.getMethod());

		System.out.println(name);
		logger.info(name);

//		System.out.println(request.getMethod());
//		logger.info(request.getMethod().toString());

		int a = 10;
		int b = 0;

		a = a / b;

		System.out.println(a);

		return "index";
	}
}
