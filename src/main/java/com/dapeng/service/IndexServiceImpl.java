package com.dapeng.service;

import com.dapeng.dao.ProductInfoDAO;
import com.dapeng.domain.ProductInfo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("indexService")
@Transactional
public class IndexServiceImpl implements IndexService {

	@Autowired
	private ProductInfoDAO productInfoDAO;


	@Override
	public void save(String name) {
		ProductInfo productInfo = new ProductInfo();
		productInfo.setName(name);
		productInfoDAO.save(productInfo);
	}

	@Override
	public List<SimpleProductInfo> show() {
		List<ProductInfo> productList = productInfoDAO.findAll();

		List<SimpleProductInfo> simpleProductInfoList = Lists.newArrayList();
		for(ProductInfo productInfo : productList){
			SimpleProductInfo simpleProductInfo = new SimpleProductInfo();
			simpleProductInfo.setId(productInfo.getId());
			simpleProductInfo.setName(productInfo.getName());
			simpleProductInfoList.add(simpleProductInfo);
		}

		return simpleProductInfoList;
	}
}
