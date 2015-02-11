package com.dapeng.repository;

import com.dapeng.domain.ProductInfo;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@CacheConfig(cacheNames = "product")
public interface ProductInfoRepository extends JpaRepository<ProductInfo, Long> {

	@Override
	@CacheEvict(allEntries = true)
	<S extends ProductInfo> S save(S entity);

	@Override
	@Cacheable
	List<ProductInfo> findAll();

	@Override
	@Cacheable
	ProductInfo getOne(Long id);
}
