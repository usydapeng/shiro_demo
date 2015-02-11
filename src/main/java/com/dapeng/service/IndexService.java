package com.dapeng.service;

import com.dapeng.domain.ProductInfo;
import com.dapeng.web.controller.ProductInfoFormBean;

import java.util.List;

public interface IndexService {

	ProductInfo save(String name);

	List<SimpleProductInfo> show();

	SimpleProductInfo getProductById(Long productId);

	void saveOrUpdateToProduct(ProductInfoFormBean product);
}
