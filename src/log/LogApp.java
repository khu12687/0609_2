package log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.JFrame;

/*
 * Class class �� ����Ŭ������ ���� ������ ���� �� �ִ� 
 * */
//Class Ŭ������ �̿��ϸ� ���� Ŭ������ ���� ������ ������ �� �ִ�
//ex) �Ϲ� ���� package ��η� �����ϴ� �� this.getClassLoader().getResourceAsStream
public class LogApp extends JFrame{
	String className = this.getClass().getName();
	private String name="apple";
	public LogApp() {
		//java.lang �ȿ��� ���� ��ü�� �ν��Ͻ��� ������ Ŭ���� ������ ������ �� �ִ� Class Ŭ������ �������ش�
		Class logAppClass = this.getClass();		
		Constructor[] cons = logAppClass.getConstructors();
		System.out.println("�� Ŭ������ ������ ����"+cons.length);
		
		Method[] method = logAppClass.getMethods();
		for(int i=0; i<method.length;i++) {
			
			System.out.println("��Ŭ������ �ż����"+method[i].getName());
		}
		//�ɹ����� ���� ��ȸ
		Field[] field = logAppClass.getFields();
		for(int i=0; i<field.length; i++) {
			System.out.println("�� Ŭ������ �ɹ����� : "+field[i]);
		}
		String className = this.getClass().getName();
		System.out.println(className+"�� �α�");
	}
	public void showMsg() {
		
	}
	public void test() {	
		
	}
	public static void main(String[] args) {
		new LogApp();
	}
}

