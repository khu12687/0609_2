package log;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/*
 * Class 클래스를 이용하면, 클래스의 코드의 로드 시점을 개발자가 주도할 수도 있다..
 * 일반적으로 현재 클래스에서 사용되는 여러 클래스는 실행시점에 자바에 의해 static영역에 클래스
 * 코드가 올라가는데, Class 클래스를 이용하면, 실행 시점에 개발자가 동적으로 클래스의
 * 코드를 static영역에 올릴 수 있다..
 * 즉 개발자가 주도하여 동적으로 올려보자!!
 * 비유) 자바스크립트에서 DOM을 메모리에 생성하는 주체?
 * 	html 문서가 로드될때 1:1 대응하면서 브라우저가 올림..
 *	개발자가 DOM을 올리는 법과 유사 document.createElement("div");
 * */
public class DynamicLoad extends JFrame{
	JTextField t_input;
	JButton bt;
	
	public DynamicLoad() {
		t_input = new JTextField(33);
		bt = new JButton("동적생성");
		
		setLayout(new FlowLayout());
		add(t_input);
		add(bt);
		setSize(500,300);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		bt.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				createObject();
			}
		});
	}
	public void createObject() {
		//텍스트 필드에 입력한 스트링정보를 이용하여 실제 클래스화 시켜
		//인스턴스 생성하고 화면에 붙여보자~~
		String classString = t_input.getText();
		System.out.println("당신이 생성하고 픈 클래스명은"+classString);
		try {
			Class myClass = Class.forName(classString);
			
			try {
				//myClass의 인스턴스를 생성!!
				Object obj = myClass.newInstance();
				this.add((Component)obj);
				this.revalidate(); //update
				
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(this, "안존재 !! API를 좀 공부해라!!");
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		new DynamicLoad();
	}
}
