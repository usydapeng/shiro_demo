package com.dapeng.repository;

import com.dapeng.domain.UserAccount;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

@CacheConfig(cacheNames = "user")
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

	@Override
	@CachePut(key = "#entity.username")
	<S extends UserAccount> S save(S entity);

	@Cacheable
	UserAccount findOneByUsername(String username);
}
