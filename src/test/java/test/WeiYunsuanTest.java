package test;

public class WeiYunsuanTest {

	public static boolean isInGroup(Integer userRole, Integer group){
		if(userRole != null){
			return 0 != (userRole & group);
		}
		return false;
	}

	public static void main(String[] args) {
		System.out.println(1 & 8);// 8
		System.out.println(1 & 9);// 1 8
		System.out.println(1 & 10);//xxx
		System.out.println(1 & 15);// 1 2 4 8
		System.out.println(1 & 16);//xxx
		System.out.println(2 & 15);
		System.out.println(4 & 15);
	}
}
