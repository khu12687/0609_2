package excel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import db.join.ConnectionManager;

//������ �ѰǾ� ����ϴ� �������� �����ϱ� ���� ���� ���� ������ ���α׷�
public class DumpApp extends JFrame{
	JPanel p_west;
	JTextField t_name;
	JTextField t_phone;
	JTextField t_email;
	JTextField t_age;
	JButton bt_excel;
	JButton bt_regist;
	JTable table;
	JScrollPane scroll;
	JFileChooser chooser;
	ConnectionManager connectionManger;
	Connection con;
	PreparedStatement pstmt;
	ResultSet rs=null;
	FileInputStream fis = null;
	Thread thread; //������ �о���̴� �ӵ��� ���� ���û��� �����̹Ƿ� ����������!!
					//������ ���� �����͸� �������� ������ ��Ʈ�p���� DB�� �����ϴ¾����� �ð��� �� ���ɸ���
					//�� ���̷� ���� ������ �����Ͱ� �������ֱ� ������ �Ϻη� ������� �ӵ��� ���� ���������� ���纸��!
	AnimalModel model;
	public DumpApp() {
		p_west = new JPanel();
		t_name = new JTextField("����",10);
		t_phone = new JTextField("123123123",10);
		t_email = new JTextField("������������@naver.com",10);
		t_age = new JTextField("23",10);
	
		bt_excel = new JButton("�������� ����");
		bt_regist = new JButton("DB�� ���");
		
		table = new JTable(model = new AnimalModel());
		scroll = new JScrollPane(table);
		chooser = new JFileChooser("C:/web_appDB/oracle_workspace/project0601/src/data");
		
		//��Ÿ�� ����
		p_west.setBackground(Color.YELLOW);
		p_west.setPreferredSize(new Dimension(120,600));
		
		//����
		p_west.add(t_name);
		p_west.add(t_phone);
		p_west.add(t_email);
		p_west.add(t_age);
		p_west.add(bt_excel);
		p_west.add(bt_regist);
		
		add(p_west,BorderLayout.WEST);
		add(scroll);
		
		//�����ͺ��̽� ����
		connectionManger = new ConnectionManager();
		con = connectionManger.getConnection();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				connectionManger.closeDB(con);
				System.exit(0);
			}
		});
		
		setSize(520, 500);
		setVisible(true);
		setLocationRelativeTo(null);
		
		//��ư�� ������ ����
		bt_excel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseFile(); //���ε��ϰ���� �������� ����!!
			}
		});
		
		bt_regist.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				regist();
			}
		});
	}
	public void chooseFile() {
		int result = chooser.showOpenDialog(this);
		
		if(result == JFileChooser.APPROVE_OPTION) {			
			try {
				File file = chooser.getSelectedFile();
				fis = new FileInputStream(file);
				this.setTitle(file.getAbsolutePath());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	//��Ϲ�ư!!
	public void regist() {
		
		try {
			//���� ��ü ����!!
			HSSFWorkbook book = new HSSFWorkbook(fis);
			
			//��Ʈ�� ���� ����!!
			HSSFSheet sheet=book.getSheet("member");
			
			int rowNum = sheet.getLastRowNum(); //�� ���ڵ� ���� ��ȯ ���!!
			thread = new  Thread() {
				public void run() {
					//row�� ����
					for(int i=1; i<=rowNum; i++) {
						HSSFRow row = sheet.getRow(i);
						
						String name = row.getCell(0).getStringCellValue();
						String phone = row.getCell(1).getStringCellValue();
						String email = row.getCell(2).getStringCellValue();
						int age = (int)row.getCell(3).getNumericCellValue();
						
						//����Ŭ�� �ֱ�!!
						String sql="insert into animal(animal_id, name, phone, email, age)";
						sql+=" values(seq_animal.nextval,?,?,?,?)";
						
						try {
							pstmt = con.prepareStatement(sql);
							
							//���ε� ���� ����
							pstmt.setString(1, name);
							pstmt.setString(2, phone);
							pstmt.setString(3, email);
							pstmt.setInt(4, age);
							
							//���� ����
							int n = pstmt.executeUpdate();
							
							//�ð�����
							Thread.sleep(10);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					JOptionPane.showMessageDialog(DumpApp.this, "��ϿϷ�");
					
					getList();
				}
			};
			
			thread.start();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(fis!=null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	public void getList() {
		String sql = "select * from animal";
		
		try {
			pstmt = con.prepareStatement(sql);
			rs =pstmt.executeQuery(); //select���� ����!!
			//rs�� �ݱ����� List �� ��ȯ����!!
			List list = new ArrayList(); //Animal���� ���� ����Ʈ!!
			while(rs.next()) {
				Animal animal = new Animal();
				animal.setAnimal_id(rs.getInt("animal_id"));
				animal.setName(rs.getString("name"));
				animal.setPhone(rs.getString("phone"));
				animal.setEmail(rs.getString("email"));
				animal.setAge(rs.getInt("age"));
				
				list.add(animal);
			}
			//���� ����Ʈ�� ���� ������ ����Ʈ�� ��ü!
			model.list = list;
			table.updateUI();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static void main(String[] args) {
		new DumpApp();
	}
}
