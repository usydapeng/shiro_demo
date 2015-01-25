package test.slug;

import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.Set;

public class GenerateSlugTest {

	@Test
	public void demo(){
		Set<Long> set = Sets.newHashSet();

		while (set.size() < 1000){
			set.add(RandomStrUtils.generateNumberString(6));
		}


		StringBuffer sqlBuffer = new StringBuffer("insert into slug_info(slug) values ");
		for(Long slug : set){
			sqlBuffer.append("(" + slug + "),");
		}
		System.out.println(sqlBuffer);
	}
}
