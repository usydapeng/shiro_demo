package com.dapeng.service;

import com.dapeng.domain.Product;
import com.dapeng.repository.ProductRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("indexService")
@Transactional
public class IndexServiceImpl implements IndexService {

	@Autowired
	private ProductRepository productRepository;


	@Override
	public void save(String name) {
		Product product = new Product();
		product.setName(name);
		productRepository.save(product);
	}

	@Override
	public List<SimpleProductInfo> show() {
		List<Product> productList = productRepository.findAll();

		List<SimpleProductInfo> simpleProductInfoList = Lists.newArrayList();
		for(Product product : productList){
			SimpleProductInfo simpleProductInfo = new SimpleProductInfo();
			simpleProductInfo.setId(product.getId());
			simpleProductInfo.setName(product.getName());
			simpleProductInfoList.add(simpleProductInfo);
		}

		return simpleProductInfoList;
	}
}
