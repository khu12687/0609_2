package excel.exchange;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import db.join.ConnectionManager;

public class ExchangeData extends JFrame implements ActionListener{
	JTable table;
	JScrollPane scroll;
	JTextArea xmlArea;
	JTextArea jsonArea;
	JButton bt_test;
	JButton bt_db;
	JButton bt_xml;
	JButton bt_json;
	String path="C:/web_appDB/oracle_workspace/project0601/src/data";
	EmpModel model;
	JFileChooser chooser;
	
	public ExchangeData() {
		table = new JTable(model=new EmpModel());
		scroll = new JScrollPane(table);
		xmlArea = new JTextArea("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		jsonArea = new JTextArea();
		bt_test = new JButton("엑셀파일만들기");
		bt_db = new JButton("DB를 엑셀로");
		bt_xml= new JButton("XML을 엑셀로 저장");
		bt_json= new JButton("JSON을 엑셀로 저장");
		chooser = new JFileChooser(path);
		
		//스타일 적용
		table.setPreferredSize(new Dimension(400,500));
		xmlArea.setPreferredSize(new Dimension(300,500));
		jsonArea.setPreferredSize(new Dimension(300,500));
		jsonArea.setFont(new Font("돋움",Font.BOLD,20));
		//조립
		setLayout(new FlowLayout());
		add(scroll);
		add(xmlArea);
		add(jsonArea);
		add(bt_test);
		add(bt_db);
		add(bt_xml);
		add(bt_json);
		
		loadDB();
		
		setSize(1200,600);
		setVisible(true);
		setLocationRelativeTo(null);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		//버튼과 리스너 연결
		bt_test.addActionListener(this);
		bt_db.addActionListener(this);
		bt_xml.addActionListener(this);
		bt_json.addActionListener(this);
		
		
	}

	public void actionPerformed(ActionEvent e) {
		Object obj =e.getSource();
		if(obj==bt_test) {
			createExcel();
		}else if(obj==bt_db) {
			convertDBToExcel();
		}else if(obj==bt_xml) {
			convertXMLtoExcel();
		}else if(obj==bt_json) {
			convertJsonToExcel();
		}
	}
	//엑셀파일을 자바프로그래밍적으로 생성해보자!!(내용도 구성하고..)
	//POI 라이브러리가 요구된다
	public void createExcel() {
		//비어있는 워크북 생성!!
		HSSFWorkbook book = new HSSFWorkbook();
		
		//엑셀의 내용구성
		HSSFSheet sheet = book.createSheet(); //쉬트생성
		HSSFRow row = sheet.createRow(0); //row 생성
		row.createCell(0).setCellValue("고유번호"); //car_id
		row.createCell(1).setCellValue("차이름"); //name
		row.createCell(2).setCellValue("브랜드"); //brand
		row.createCell(3).setCellValue("가격"); //price
		
		//데이터 구성하기
		HSSFRow data1 = sheet.createRow(1);
		data1.createCell(0).setCellValue("1");
		data1.createCell(1).setCellValue("Audi");
		data1.createCell(2).setCellValue("아우디");
		data1.createCell(3).setCellValue("8500");
		
		//파일과 디레토리를 구분하는 / 슬래시, 역슬래시를 알아서 생성
		String sp = File.pathSeparator;
		FileOutputStream fos = null;
		try {
			//엑셀파일을 동적으로 생성한다!!
			book.write(fos = new FileOutputStream(path+"/test.xls"));
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(fos!=null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		JOptionPane.showMessageDialog(this, "파일생성했어요");
		
	}
	
	//EMP 테ㅣ블 로드!!
	public void loadDB() {
		ConnectionManager connectionManger = null;
		connectionManger = ConnectionManager.getInstance();
		Connection con = connectionManger.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql="select * from emp";
		
		try {
			pstmt = con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			ArrayList list = new ArrayList();
			while(rs.next()) {
				Emp emp = new Emp();
				emp.setEmpno(rs.getInt("empno"));
				emp.setEname(rs.getString("ename"));
				emp.setJob(rs.getString("job"));
				emp.setMgr(rs.getInt("mgr"));
				emp.setHiredate(rs.getString("hiredate"));
				emp.setSal(rs.getInt("sal"));
				emp.setComm(rs.getInt("comm"));
				emp.setDeptno(rs.getInt("deptno"));
				
				list.add(emp);
			}
			
			//모델의 list에 대입!!
			model.list=list;
			table.updateUI();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			connectionManger.closeDB(pstmt,rs);
		}
	}
	
	//데이터베이스를 엑셀로 저장
	public void convertDBToExcel() {
		int ans = chooser.showSaveDialog(this);
		FileOutputStream fos = null; //빈파일 만들기 위한 파일출력스트림
		
		if(ans==JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile(); //유저가 저장할 파일명!!
			
			//위의 파일명으로 엑셀생성하자!!
			
			//비어있는 엑셀 생성!!
			HSSFWorkbook book = new HSSFWorkbook();
			
			//파일로 저장하기 전에, 내용을 채워넣자!!
			HSSFSheet sheet = book.createSheet(); //비어있는 쉬트 생성!!
			
			//ArrayList에 들어있는 데이터 수만큼 엑셀의 row도 생성
			for(int i=0; i<model.list.size(); i++) {
				HSSFRow row = sheet.createRow(i);
				Emp emp = model.list.get(i);
				row.createCell(0).setCellValue(emp.getEmpno()); //empno
				row.createCell(1).setCellValue(emp.getEname()); //ename
				row.createCell(2).setCellValue(emp.getJob()); //job
				row.createCell(3).setCellValue(emp.getMgr()); //mgr
				row.createCell(4).setCellValue(emp.getHiredate()); //hiredate
				row.createCell(5).setCellValue(emp.getSal()); //sal
				row.createCell(6).setCellValue(emp.getComm()); //comm
				row.createCell(7).setCellValue(emp.getDeptno()); //deptno
			}
			
			//채워진 엑셀을 실제 물리적 파일로 생성!!
			try {
				book.write(fos=new FileOutputStream(file));
				JOptionPane.showMessageDialog(this, "파일생성완료");
			} catch (FileNotFoundException e) {
					e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				if(fos!=null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		}
	}
	//XML을 엑셀로 변환하여 저장!!
	public void convertXMLtoExcel() {
		//javaSE에는 자체 지원하는 파서가 있다!!
		SAXParserFactory factory = SAXParserFactory.newInstance();
		MyXMLHandler handler = new MyXMLHandler();
		FileOutputStream fos = null; //빈 파일 생성용 스트림!!
		try {
			SAXParser parser = factory.newSAXParser(); //파서생성
			
			//파싱 시작!!
			try {
				//parser.parse(new File(path+"/aa.xml"),handler);
				//파일이 아닌 그냥 스트링을 대상으로 xml 파싱을 시도해보자~~!!
				InputSource source = new InputSource(new StringReader(xmlArea.getText()));
				parser.parse(source,handler);
				
				//파싱이 완료되면, 엑셀로 출력!!
				int ans = chooser.showSaveDialog(this);
				
				
				
				if(ans==JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					
					//엑셀내용 구성하기!!
					HSSFWorkbook book = new HSSFWorkbook();
					HSSFSheet sheet = book.createSheet(); //쉬트생성
					
					//핸들러가 파싱의 결과로 담아놓은 리스트만큼 row생성!!
					for(int i=0; i<handler.list.size();i++) {
						HSSFRow row = sheet.createRow(i); //엑셀의  한 행을 생성!!
						Lang lang = handler.list.get(i);
						row.createCell(0).setCellValue(lang.getName());; //name
						row.createCell(1).setCellValue(lang.getVersion());; //version
						row.createCell(2).setCellValue(lang.getVendor());; //vendor
					}
					
					//엑셀을 파일로 출력!!
					book.write(fos= new FileOutputStream(file));
				}
				JOptionPane.showMessageDialog(this, "생성완료");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}finally {
			if(fos!=null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public void convertJsonToExcel() {
		//json 파싱은 외부라이브러리인 google json simple 이용하겠다!!
		JSONParser jsonParser = new JSONParser();
		FileOutputStream fos = null;
		try {
			//파싱 한 이후부터는 스트링에 불과한 데이터가 객체화 되서 객체처럼 사용가능
			JSONObject jsonObject = (JSONObject)jsonParser.parse(jsonArea.getText());
			String name = (String)jsonObject.get("name");
			long price = (Long)jsonObject.get("price"); //unBoxing
			String color = (String)jsonObject.get("color");
			
			ArrayList<Flower> list = new ArrayList<Flower>();
			Flower flower = new Flower();
			flower.setName(name);
			flower.setPrice((int)price);
			flower.setColor(color);
			
			list.add(flower);
			
			//엑셀 생성하기!!
			HSSFWorkbook book = new HSSFWorkbook();
			HSSFSheet sheet = book.createSheet();//쉬트생성
			for(int i=0; i<list.size();i++) {
				Flower obj =list.get(i); //리스트에서 VO꺼내기
				HSSFRow row = sheet.createRow(i); //row 생성!!
				row.createCell(0).setCellValue(obj.getName());;
				row.createCell(1).setCellValue(obj.getPrice());;
				row.createCell(2).setCellValue(obj.getColor());
			}
			//내용을 모두 채운 시점이므로 엑셀을 파일로 생성하자!!
			int ans = chooser.showSaveDialog(this);
			if(ans==JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				book.write(file);
				JOptionPane.showMessageDialog(this, "파일생성함");
				//만들어진 파일의 디렉토리를 열어주는 친절함을 베풀자!!
				chooser.showOpenDialog(this);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(fos!=null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static void main(String[] args) {
		new ExchangeData();
	}
}
