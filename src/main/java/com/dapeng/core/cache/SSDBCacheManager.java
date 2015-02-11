package com.dapeng.core.cache;

import com.google.common.collect.Lists;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;

import java.util.Collection;

public class SSDBCacheManager extends AbstractCacheManager {

	@Override
	protected Collection<? extends Cache> loadCaches() {
		return Lists.newArrayList();
	}
}
