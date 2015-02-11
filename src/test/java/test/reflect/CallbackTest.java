package test.reflect;

import com.dapeng.domain.ProductInfo;
import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CallbackTest {

	@Test
	public void demo1(){
		final String helloworld = "zhangsan";
		System.out.println(print(3L, new ICook() {
			@Override
			public String process(String name) {
				return helloworld + " :callback process: " + name;
			}
		}));
	}

	@Test
	public void demo2(){
		System.out.println(print(3L, (name) ->  "helloworld : " + name));
	}

	@Test
	public void demo3(){
		ProductInfo productInfo = new ProductInfo();
		productInfo.setId(12L);
		productInfo.setName("zhangsan");
//		printhel(productInfo, (ProductInfo p) -> "zhangsan".equals(p.getName()) && p.getId() > 12L && p.getId() < 18L);
	}

	@Test
	public void demo4(){
		List<String> list = Lists.newArrayList();
		list.add("a");
		list.add("c");
		list.add("b");
		System.out.println(list);
		Collections.sort(list, (o1, o2) -> o1.compareTo(o2));
		System.out.println(list);
	}

	@Test
	public void demo5(){
		List<String> list = Lists.newArrayList();
		list.add("a");
		list.add("c");
		list.add("b");
		System.out.println(list.stream().map((String name) -> name.toUpperCase()).collect(Collectors.toList()));
	}

	@Test
	public void demo6(){
		List<String> names = new ArrayList<>();
		names.add("TaoBao");
		names.add("ZhiFuBao");
		List<String> lowercaseNames = names.stream().map((String name) -> name.toLowerCase()).collect(Collectors.toList());
		System.out.println(lowercaseNames);
	}

	@Test
	public void demo7(){
		List<String> list = Lists.newArrayList();
		list.add("helloworld");
		list.add("zhangsan");
		list.add("lisis");
		System.out.println(list.stream().map((String name) -> name + "__________").collect(Collectors.toList()));
	}



	@Test
	public void demo8(){
		List<String> names = new ArrayList<>();
		names.add("TaoBao");
		names.add("ZhiFuBao");
		List<String> lowercaseNames = FluentIterable.from(names).transform(new Function<String, String>() {
			@Override
			public String apply(String name) {
				return name.toLowerCase();
			}
		}).toList();
		System.out.println(lowercaseNames);
	}

	@Test
	public void demo9(){
		List<String> list = Lists.newArrayList();
		list.add("a");
		list.add("d");
		list.add("b");
		list.add("c");
		System.out.println(list.stream().map(name -> name.toUpperCase()).collect(Collectors.toList()));
		System.out.println(list.stream().map(String::toUpperCase).collect(Collectors.toList()));
		list.stream().map(name -> Strings.padEnd(name, 10, '@')).forEach(System.out::println);
	}

	@Test
	public void demo10(){
		String[] array = new String[]{"a", "c", "d", "b"};
		for(int i : Lists.newArrayList(1, 2, 3, 4)){
			System.out.println(i);
			Stream.of(array).map(name -> Strings.padEnd(name, i, '@')).forEach(System.out::println);
		}
	}

	@Test
	public void demo11(){
		String[] array = new String[]{"a", "b"};
		for(int i = 1; i < 5; i ++){
			final int a = i;
			Stream.of(array).map(name -> Strings.padEnd(name, a, 'A')).forEach(System.out::println);
		}
	}

	@Test
	public void demo12(){
		System.out.println(Lists.newArrayList(1, 2, 3, 4).getClass().getGenericSuperclass());
	}

	@Test
	public void demo13(){
		Lists.newArrayList("zhangsan", "lissi").stream().map(name -> name.charAt(0)).forEach(System.out::println);
	}

	@Test
	public void demo14(){
		System.out.println(Stream.generate(Math::random).limit(100).collect(Collectors.toList()));
	}

	@Test
	public void demo15(){
		List<Double> list = Lists.newArrayList();
		while (true){
			list.add(Math.random());
		}
	}

	public String print(Long id, ICook cook){
		return cook.process(id.toString());
	}
}
