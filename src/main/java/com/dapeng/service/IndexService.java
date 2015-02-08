package com.dapeng.service;

import com.dapeng.domain.ProductInfo;

import java.util.List;

public interface IndexService {

	ProductInfo save(String name);

	List<SimpleProductInfo> show();

}
