package test.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.junit.Test;

import java.net.URL;

public class HelloWorld {

	@Test
	public void demo(){
		CacheManager.create();
		String[] cacheManageNames = CacheManager.getInstance().getCacheNames();
		for(String name : cacheManageNames){
			System.out.println(name);
		}
		System.out.println("====================");
	}

	@Test
	public void demo2(){
		URL url = getClass().getResource("/anotherconfigurationname.xml");
		System.out.println(url);
	}

	@Test
	public void demo3(){
		CacheManager cacheManager = CacheManager.create();
		cacheManager.addCache("hello");
		Cache cache = cacheManager.getCache("hello");
		System.out.println(cache);

		cache.put(new Element("key1", "value1"));

		Cache cache1 = cacheManager.getCache("hello");
		System.out.println(cache1.get("key1"));
		System.out.println(cache1.get("key1"));
	}

}
