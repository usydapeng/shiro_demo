package com.dapeng.web.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

	private static Logger logger = LoggerFactory.getLogger(LoginController.class);

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String index(){
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam(value = "username") String username,
						@RequestParam(value = "password") String password,
						Model model){
		UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
		usernamePasswordToken.setRememberMe(true);

		try {
			SecurityUtils.getSubject().login(usernamePasswordToken);
		} catch(AuthenticationException e){
			logger.info(e.getMessage(), e);
			model.addAttribute("username", username);
			return "/login";
		} catch(Exception e){
			logger.info(e.getMessage(), e);
			model.addAttribute("username", username);
			return "/login";
		}

		return "redirect:/home";
	}
}
