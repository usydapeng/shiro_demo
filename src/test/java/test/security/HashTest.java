package test.security;

import org.junit.Test;

import java.security.MessageDigest;

public class HashTest {

	@Test
	public void demo1() throws Exception {
		String password = "helloworld";
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		digest.reset();
		digest.update(new byte[]{76,78,98,12,34,56});
		byte[] input = digest.digest(password.getBytes("UTF-8"));

		for(byte by : input){
			System.out.print(by);
		}
		System.out.println();
	}

}
