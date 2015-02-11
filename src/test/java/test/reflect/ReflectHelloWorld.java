package test.reflect;

import com.dapeng.domain.ProductInfo;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectHelloWorld {

	@Test
	public void demo1() throws Exception {
		Class clazz = Class.forName("java.lang.Double");
		Constructor constructor = clazz.getConstructor(String.class);
		System.out.println(constructor.newInstance("123456.89"));
	}

	@Test
	public void demo2() throws Exception {
		Class clazz = Class.forName("com.dapeng.domain.ProductInfo");
		Field nameField = clazz.getDeclaredField("name");
		nameField.setAccessible(true);
		ProductInfo productInfo = new ProductInfo();
		productInfo.setName("优课急送");
		System.out.println(nameField.get(productInfo));

		nameField.set(productInfo, "多贝公开课");
		System.out.println(productInfo.getName());
		nameField.setAccessible(false);
	}

	@Test
	public void demo3() throws Exception {
		Class clazz = Class.forName("com.dapeng.domain.ProductInfo");

		Object instance = clazz.newInstance();
		System.out.println(instance.getClass());

		Method setNameMethod = clazz.getDeclaredMethod("setName", String.class);
		Method getNameMethod = clazz.getDeclaredMethod("getName");

		System.out.println("111:" + setNameMethod.invoke(instance, "多贝公开课之优课急送"));
		System.out.println("222:" + getNameMethod.invoke(instance));

	}

	@Test
	public void demo4() throws Exception {
		Class clazz = Class.forName("com.dapeng.domain.ProductInfo");

		Annotation[] annotations = clazz.getAnnotations();
		for(Annotation annotation : annotations){
//			System.out.println(annotation.getClass());
			System.out.println(annotation.annotationType());
		}

		System.out.println("-----------------------------------------------");

		Annotation[] ans = clazz.getDeclaredAnnotations();
		for(Annotation an : ans){
			System.out.println(an.annotationType());
		}

		System.out.println("-------------------------------------------------");

		Class superClazz = clazz.getSuperclass();
		annotations = superClazz.getAnnotations();
		for(Annotation annotation : annotations){
			System.out.println(annotation.annotationType());
		}

		System.out.println("-------------------------------------------------");

	}
}
