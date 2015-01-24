package com.dapeng.core.http;

import com.alibaba.druid.support.http.WebStatFilter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

public class DruidWebStatFilter extends WebStatFilter {

	@Override
	public void init(FilterConfig config) throws ServletException {
		super.init(config);

	}
}
