package test.slug;

import com.google.common.collect.Sets;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

import java.util.Set;

public class GenerateSlugTest {

	@Test
	public void demo(){
		Set<Long> set = Sets.newHashSet();

		while (set.size() < 1000){
			set.add(RandomStrUtils.generateNumberString(6));
		}

		System.out.println(set);
	}
}
