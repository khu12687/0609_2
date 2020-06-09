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
		bt_test = new JButton("�������ϸ����");
		bt_db = new JButton("DB�� ������");
		bt_xml= new JButton("XML�� ������ ����");
		bt_json= new JButton("JSON�� ������ ����");
		chooser = new JFileChooser(path);
		
		//��Ÿ�� ����
		table.setPreferredSize(new Dimension(400,500));
		xmlArea.setPreferredSize(new Dimension(300,500));
		jsonArea.setPreferredSize(new Dimension(300,500));
		jsonArea.setFont(new Font("����",Font.BOLD,20));
		//����
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
		
		//��ư�� ������ ����
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
	//���������� �ڹ����α׷��������� �����غ���!!(���뵵 �����ϰ�..)
	//POI ���̺귯���� �䱸�ȴ�
	public void createExcel() {
		//����ִ� ��ũ�� ����!!
		HSSFWorkbook book = new HSSFWorkbook();
		
		//������ ���뱸��
		HSSFSheet sheet = book.createSheet(); //��Ʈ����
		HSSFRow row = sheet.createRow(0); //row ����
		row.createCell(0).setCellValue("������ȣ"); //car_id
		row.createCell(1).setCellValue("���̸�"); //name
		row.createCell(2).setCellValue("�귣��"); //brand
		row.createCell(3).setCellValue("����"); //price
		
		//������ �����ϱ�
		HSSFRow data1 = sheet.createRow(1);
		data1.createCell(0).setCellValue("1");
		data1.createCell(1).setCellValue("Audi");
		data1.createCell(2).setCellValue("�ƿ��");
		data1.createCell(3).setCellValue("8500");
		
		//���ϰ� ���丮�� �����ϴ� / ������, �������ø� �˾Ƽ� ����
		String sp = File.pathSeparator;
		FileOutputStream fos = null;
		try {
			//���������� �������� �����Ѵ�!!
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
		JOptionPane.showMessageDialog(this, "���ϻ����߾��");
		
	}
	
	//EMP �פӺ� �ε�!!
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
			
			//���� list�� ����!!
			model.list=list;
			table.updateUI();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			connectionManger.closeDB(pstmt,rs);
		}
	}
	
	//�����ͺ��̽��� ������ ����
	public void convertDBToExcel() {
		int ans = chooser.showSaveDialog(this);
		FileOutputStream fos = null; //������ ����� ���� ������½�Ʈ��
		
		if(ans==JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile(); //������ ������ ���ϸ�!!
			
			//���� ���ϸ����� ������������!!
			
			//����ִ� ���� ����!!
			HSSFWorkbook book = new HSSFWorkbook();
			
			//���Ϸ� �����ϱ� ����, ������ ä������!!
			HSSFSheet sheet = book.createSheet(); //����ִ� ��Ʈ ����!!
			
			//ArrayList�� ����ִ� ������ ����ŭ ������ row�� ����
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
			
			//ä���� ������ ���� ������ ���Ϸ� ����!!
			try {
				book.write(fos=new FileOutputStream(file));
				JOptionPane.showMessageDialog(this, "���ϻ����Ϸ�");
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
	//XML�� ������ ��ȯ�Ͽ� ����!!
	public void convertXMLtoExcel() {
		//javaSE���� ��ü �����ϴ� �ļ��� �ִ�!!
		SAXParserFactory factory = SAXParserFactory.newInstance();
		MyXMLHandler handler = new MyXMLHandler();
		FileOutputStream fos = null; //�� ���� ������ ��Ʈ��!!
		try {
			SAXParser parser = factory.newSAXParser(); //�ļ�����
			
			//�Ľ� ����!!
			try {
				//parser.parse(new File(path+"/aa.xml"),handler);
				//������ �ƴ� �׳� ��Ʈ���� ������� xml �Ľ��� �õ��غ���~~!!
				InputSource source = new InputSource(new StringReader(xmlArea.getText()));
				parser.parse(source,handler);
				
				//�Ľ��� �Ϸ�Ǹ�, ������ ���!!
				int ans = chooser.showSaveDialog(this);
				
				
				
				if(ans==JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					
					//�������� �����ϱ�!!
					HSSFWorkbook book = new HSSFWorkbook();
					HSSFSheet sheet = book.createSheet(); //��Ʈ����
					
					//�ڵ鷯�� �Ľ��� ����� ��Ƴ��� ����Ʈ��ŭ row����!!
					for(int i=0; i<handler.list.size();i++) {
						HSSFRow row = sheet.createRow(i); //������  �� ���� ����!!
						Lang lang = handler.list.get(i);
						row.createCell(0).setCellValue(lang.getName());; //name
						row.createCell(1).setCellValue(lang.getVersion());; //version
						row.createCell(2).setCellValue(lang.getVendor());; //vendor
					}
					
					//������ ���Ϸ� ���!!
					book.write(fos= new FileOutputStream(file));
				}
				JOptionPane.showMessageDialog(this, "�����Ϸ�");
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
		//json �Ľ��� �ܺζ��̺귯���� google json simple �̿��ϰڴ�!!
		JSONParser jsonParser = new JSONParser();
		FileOutputStream fos = null;
		try {
			//�Ľ� �� ���ĺ��ʹ� ��Ʈ���� �Ұ��� �����Ͱ� ��üȭ �Ǽ� ��üó�� ��밡��
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
			
			//���� �����ϱ�!!
			HSSFWorkbook book = new HSSFWorkbook();
			HSSFSheet sheet = book.createSheet();//��Ʈ����
			for(int i=0; i<list.size();i++) {
				Flower obj =list.get(i); //����Ʈ���� VO������
				HSSFRow row = sheet.createRow(i); //row ����!!
				row.createCell(0).setCellValue(obj.getName());;
				row.createCell(1).setCellValue(obj.getPrice());;
				row.createCell(2).setCellValue(obj.getColor());
			}
			//������ ��� ä�� �����̹Ƿ� ������ ���Ϸ� ��������!!
			int ans = chooser.showSaveDialog(this);
			if(ans==JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				book.write(file);
				JOptionPane.showMessageDialog(this, "���ϻ�����");
				//������� ������ ���丮�� �����ִ� ģ������ ��Ǯ��!!
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
