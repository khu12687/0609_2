package log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.JFrame;

/*
 * Class class 는 현재클래스의 대한 정보를 얻을 수 있다 
 * */
//Class 클래스를 이용하면 현재 클래스에 대한 정보를 추출할 수 있다
//ex) 일반 파일 package 경로로 접근하는 법 this.getClassLoader().getResourceAsStream
public class LogApp extends JFrame{
	String className = this.getClass().getName();
	private String name="apple";
	public LogApp() {
		//java.lang 안에는 현재 객체의 인스턴스의 원본인 클래스 정보를 추출할 수 있는 Class 클래스를 지원해준다
		Class logAppClass = this.getClass();		
		Constructor[] cons = logAppClass.getConstructors();
		System.out.println("이 클래스의 생성자 수는"+cons.length);
		
		Method[] method = logAppClass.getMethods();
		for(int i=0; i<method.length;i++) {
			
			System.out.println("이클래스의 매서드는"+method[i].getName());
		}
		//맴버변수 정보 조회
		Field[] field = logAppClass.getFields();
		for(int i=0; i<field.length; i++) {
			System.out.println("이 클래스의 맴버변수 : "+field[i]);
		}
		String className = this.getClass().getName();
		System.out.println(className+"의 로그");
	}
	public void showMsg() {
		
	}
	public void test() {	
		
	}
	public static void main(String[] args) {
		new LogApp();
	}
}

