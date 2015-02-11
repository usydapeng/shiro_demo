package com.dapeng.web.controller;

import com.dapeng.service.IndexService;
import com.dapeng.service.SimpleProductInfo;
import com.dapeng.service.UserService;
import com.dapeng.service.exception.UserAccountException;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

	private static Logger logger = LoggerFactory.getLogger(IndexController.class);

	@Autowired
	private IndexService indexService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = {"/", "/index", "/helloworld", "/home"})
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

	@RequestMapping(value = "/p_save_update", method = RequestMethod.GET)
	public String saveOrUpdate(@RequestParam(value = "id", required = false) Long id, Model model){
		if(id == null){
			model.addAttribute("product", new SimpleProductInfo());
			model.addAttribute("updateFlag", false);
		} else {
			SimpleProductInfo productInfo = indexService.getProductById(id);
			model.addAttribute("product", productInfo);
			model.addAttribute("updateFlag", true);
		}
		return "add";
	}

	@RequestMapping(value = "/p_save_update", method = RequestMethod.POST)
	public String saveOrUpdate(@ModelAttribute ProductInfoFormBean product){
		indexService.saveOrUpdateToProduct(product);
		return "redirect:/list";
	}

	@RequestMapping(value = "/test")
	@ResponseBody
	public Map<String, Object> test() {
		Map<String, Object> map = Maps.newHashMap();

		map.put("success", true);
		map.put("message", "this is a error message");

		return map;
	}

	@RequestMapping(value = "/init")
	public String init(){
		userService.init();
		return "index";
	}

	@RequestMapping("/user")
	@ResponseBody
	public Map<String, Object> getUserInfo(@RequestParam(value = "n", defaultValue = "admin") String username){
		Map<String, Object> map = Maps.newHashMap();
		try {
			map.put("message", userService.getAuthorizationByUsername(username));
			map.put("success", true);
		} catch(UserAccountException e){
			logger.info(e.getMessage(), e);
			map.put("success", false);
			map.put("message", e.getMessage());
		}

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

	@RequestMapping(value = "/404")
	public String errorPage(){
		return "404";
	}
}
