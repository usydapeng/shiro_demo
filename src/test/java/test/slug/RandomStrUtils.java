package test.slug;

import java.util.Random;

public class RandomStrUtils {

	public static final String numberStr = "123456789";

	public static Long generateNumberString(int length){
		StringBuffer numberBuffer = new StringBuffer();
		Random random = new Random();
		for(int i = 0; i < length; i ++){
			numberBuffer.append(numberStr.charAt(random.nextInt(9)));
		}
		return Long.parseLong(numberBuffer.toString());
	}

	public static void main(String[] args) {
		System.out.println(generateNumberString(6));
		System.out.println(generateNumberString(6));
	}
}
