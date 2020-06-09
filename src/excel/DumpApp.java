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

//일일이 한건씩 등록하는 불편함을 개선하기 위한 엑셀 덤프 데이터 프로그램
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
	Thread thread; //엑셀을 읽어들이는 속도는 현재 로컬상의 업무이므로 무지빠르다!!
					//하지만 읽은 데이터를 원격지에 떨어진 네트웤상의 DB에 접근하는업무는 시간이 좀 더걸린다
					//이 차이로 인한 누락된 데이터가 있을수있기 때문에 일부로 실행부의 속도를 조금 안정적으로 늦춰보자!
	AnimalModel model;
	public DumpApp() {
		p_west = new JPanel();
		t_name = new JTextField("수달",10);
		t_phone = new JTextField("123123123",10);
		t_email = new JTextField("ㄴㅁㅇㅁㅁㄴ@naver.com",10);
		t_age = new JTextField("23",10);
	
		bt_excel = new JButton("엑셀파일 선택");
		bt_regist = new JButton("DB에 등록");
		
		table = new JTable(model = new AnimalModel());
		scroll = new JScrollPane(table);
		chooser = new JFileChooser("C:/web_appDB/oracle_workspace/project0601/src/data");
		
		//스타일 적용
		p_west.setBackground(Color.YELLOW);
		p_west.setPreferredSize(new Dimension(120,600));
		
		//조립
		p_west.add(t_name);
		p_west.add(t_phone);
		p_west.add(t_email);
		p_west.add(t_age);
		p_west.add(bt_excel);
		p_west.add(bt_regist);
		
		add(p_west,BorderLayout.WEST);
		add(scroll);
		
		//데이터베이스 연결
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
		
		//버튼과 리스너 연결
		bt_excel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseFile(); //업로드하고싶은 엑셀파일 선택!!
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
	
	//등록버튼!!
	public void regist() {
		
		try {
			//엑셀 객체 생성!!
			HSSFWorkbook book = new HSSFWorkbook(fis);
			
			//쉬트에 먼저 접근!!
			HSSFSheet sheet=book.getSheet("member");
			
			int rowNum = sheet.getLastRowNum(); //총 레코드 갯수 반환 얻기!!
			thread = new  Thread() {
				public void run() {
					//row에 접근
					for(int i=1; i<=rowNum; i++) {
						HSSFRow row = sheet.getRow(i);
						
						String name = row.getCell(0).getStringCellValue();
						String phone = row.getCell(1).getStringCellValue();
						String email = row.getCell(2).getStringCellValue();
						int age = (int)row.getCell(3).getNumericCellValue();
						
						//오라클에 넣기!!
						String sql="insert into animal(animal_id, name, phone, email, age)";
						sql+=" values(seq_animal.nextval,?,?,?,?)";
						
						try {
							pstmt = con.prepareStatement(sql);
							
							//바인드 변수 매핑
							pstmt.setString(1, name);
							pstmt.setString(2, phone);
							pstmt.setString(3, email);
							pstmt.setInt(4, age);
							
							//쿼리 수행
							int n = pstmt.executeUpdate();
							
							//시간지연
							Thread.sleep(10);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					JOptionPane.showMessageDialog(DumpApp.this, "등록완료");
					
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
			rs =pstmt.executeQuery(); //select쿼리 수행!!
			//rs를 닫기전에 List 로 변환하자!!
			List list = new ArrayList(); //Animal들을 담을 리스트!!
			while(rs.next()) {
				Animal animal = new Animal();
				animal.setAnimal_id(rs.getInt("animal_id"));
				animal.setName(rs.getString("name"));
				animal.setPhone(rs.getString("phone"));
				animal.setEmail(rs.getString("email"));
				animal.setAge(rs.getInt("age"));
				
				list.add(animal);
			}
			//모델의 리스트를 현재 생성한 리스트로 교체!
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
