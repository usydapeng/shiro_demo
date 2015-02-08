package test.cache;

import com.dapeng.config.AppConfig;
import com.dapeng.domain.ProductInfo;
import com.dapeng.service.IndexService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@TransactionConfiguration
public class SpringCacheTest {

	@Autowired
	private IndexService indexService;

	@Autowired
	private SimpleCacheManager cacheManager;

	@Test
	public void demo1(){
		System.out.println(indexService);
		System.out.println(cacheManager);
		System.out.println("----------------------------------");

		indexService.save("理想");
		Cache cache = cacheManager.getCache("product");
		Cache.ValueWrapper wrapper = cache.get("理想");
		System.out.println(wrapper.get());

		indexService.show();
		cache = cacheManager.getCache("product");
	}

}
