package com.dapeng.repository;

import com.dapeng.domain.ProductInfo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductInfoRepository extends JpaRepository<ProductInfo, Long> {

	@Override
	@CacheEvict(value = "product")
	<S extends ProductInfo> S save(S entity);

	@Override
	@Cacheable(value = "product")
	List<ProductInfo> findAll();
}
