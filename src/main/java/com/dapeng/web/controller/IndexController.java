package com.dapeng.web.controller;

import com.dapeng.service.IndexService;
import com.dapeng.service.SimpleProductInfo;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class IndexController {

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
	public Map<String, Object> test(){
		Map<String, Object> map = Maps.newHashMap();

		map.put("success", true);
		map.put("message", "this is a error message");

		return map;
	}
}
