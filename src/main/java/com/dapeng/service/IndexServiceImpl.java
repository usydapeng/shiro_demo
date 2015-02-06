package com.dapeng.service;

import com.dapeng.domain.ProductInfo;
import com.dapeng.repository.ProductInfoRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("indexService")
@Transactional
public class IndexServiceImpl implements IndexService {

	@Autowired
	private ProductInfoRepository productInfoRepository;


	@Override
	public void save(String name) {
		ProductInfo productInfo = new ProductInfo();
		productInfo.setName(name);
		productInfoRepository.save(productInfo);
	}

	@Override
	@Cacheable("productInfo")
	public List<SimpleProductInfo> show() {
		List<ProductInfo> productList = productInfoRepository.findAll();

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
