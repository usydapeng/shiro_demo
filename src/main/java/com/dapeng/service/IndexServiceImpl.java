package com.dapeng.service;

import com.dapeng.domain.ProductInfo;
import com.dapeng.repository.ProductInfoRepository;
import com.dapeng.web.controller.ProductInfoFormBean;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("indexService")
@Transactional
public class IndexServiceImpl implements IndexService {

	@Autowired
	private ProductInfoRepository productInfoRepository;


	@Override
	@CachePut(value = "product", key = "#name")
	public ProductInfo save(String name) {
		ProductInfo productInfo = new ProductInfo();
		productInfo.setName(name);
		return productInfoRepository.save(productInfo);
	}

	@Override
	public List<SimpleProductInfo> show() {
		List<ProductInfo> productList = productInfoRepository.findAll();

		List<SimpleProductInfo> simpleProductInfoList = Lists.newArrayList();
		for(ProductInfo productInfo : productList){
			simpleProductInfoList.add(retrieveSimpleProductInfo(productInfo));
		}

		return simpleProductInfoList;
	}

	private SimpleProductInfo retrieveSimpleProductInfo(ProductInfo productInfo) {
		SimpleProductInfo simpleProductInfo = new SimpleProductInfo();
		simpleProductInfo.setId(productInfo.getId());
		simpleProductInfo.setName(productInfo.getName());
		return simpleProductInfo;
	}

	@Override
	@Transactional(readOnly = true)
	public SimpleProductInfo getProductById(Long productId) {
		return retrieveSimpleProductInfo(productInfoRepository.getOne(productId));
	}

	@Override
	public void saveOrUpdateToProduct(ProductInfoFormBean product) {
		ProductInfo productInfo = new ProductInfo();
		productInfo.setId(product.getId());
		productInfo.setName(product.getName());
		productInfoRepository.save(productInfo);
	}

}
