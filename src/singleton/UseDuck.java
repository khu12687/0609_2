package singleton;

public class UseDuck {
	public static void main(String[] args) {
		Duck d1 = Duck.getInstance();
		Duck d2 =Duck.getInstance();
		System.out.println(d1);
		System.out.println(d2);
	}
}
