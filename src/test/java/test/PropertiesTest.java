package test;

import org.junit.Test;

import java.util.Enumeration;
import java.util.Properties;

public class PropertiesTest {

	@Test
	public void test1() {
		Properties statusCodes = new Properties();
		statusCodes.put("404", 4044);
		statusCodes.put("500", 404000);

		for (Enumeration<?> enumeration = statusCodes.propertyNames(); enumeration.hasMoreElements();) {
			String viewName = (String) enumeration.nextElement();
			String value = statusCodes.getProperty(viewName);
			System.out.println(viewName + "   " + value);
		}

		printProperties(statusCodes);
	}

	@Test
	public void test2(){
		Properties exceptions = new Properties();
		exceptions.put("404", 404);
		exceptions.put("500", 404);

		for(Enumeration<?> enumeration = exceptions.propertyNames(); enumeration.hasMoreElements();){
			String key = (String) enumeration.nextElement();
			Object value = exceptions.get(key);

			System.out.println(key  + "  " + value);
		}
	}

	@Test
	public void test4(){
		Properties exceptions = new Properties();
		exceptions.put("404", 404);
		exceptions.put("500", 404);
		for(Enumeration<?> enumeration = exceptions.propertyNames(); enumeration.hasMoreElements();){
			String key = (String) enumeration.nextElement();
			Object value = exceptions.get(key);

			System.out.println(key  + "  " + value);
		}
	}

	private void printProperties(Properties properties){
		for(Enumeration<?> enumeration = properties.propertyNames(); enumeration.hasMoreElements();){
			String key = (String) enumeration.nextElement();
			Object value = properties.getProperty(key);

			System.out.println(key  + "  " + value);
		}

		for(Enumeration<?> enumeration = properties.propertyNames(); enumeration.hasMoreElements();) {
			String viewName = (String) enumeration.nextElement();
			String value = properties.getProperty(viewName);
			System.out.println(viewName + "   " + value);
		}

	}
}
