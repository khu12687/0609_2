package db.join;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

//�ߺ��� �����ͺ��̽� ����, �����ڵ带 ���뼺�� ���̱� ���� ������ Ŭ������ �����Ѵ�!!


	public class ConnectionManager {
	static ConnectionManager instance;
	String url;
	//="jdbc:oracle:thin:@localhost:1521:XE";
	String user;
	//="c##java";
	String password;
	//="1234";
	
	//������������, �����ͺ��̽��� ���� ������ ������Ƽ���� ��������!!
	//�� �ϳ�? ���������� ������..
	Properties props;
	FileInputStream  fis;
	//�ܺ��� Ŭ������ ���ٸ��ϰ�
	private ConnectionManager() {
		props = new Properties(); //key-value�� ������ �Ǿ��ִ� �����͸� �����ϴ� ��ü!!
		//��� �� ���� �̿ܿ� �����ʹ� ������ ���� ������ ���Ѵ�!!

		//��Ʈ�� ������ ��Ű���κ��� ���� �����ϴ� ���
		InputStream is = this.getClass().getResourceAsStream("/database/dbms.db");
		try {
			props.load(is);
			System.out.println(is);
			
			//key������ ������ �о����!
			url = (String)props.get("oracle_url");
			user = (String)props.get("oracle_userid");
			password = (String)props.get("oracle_password");
			System.out.println(url);
			System.out.println(user);
			System.out.println(password);
		} catch (IOException e1) {
			e1.printStackTrace();
		}finally {
			if(is!=null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	//�����ͺ��̽� ����
	public Connection getConnection() {
		Connection con = null;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(url,user,password);
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return con;
	}
	//�����ͺ��̽� �ڿ�����
	//������ â ������ ȣ��
	public void closeDB(Connection con) {
		if(con!=null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//DML(insert,update,delete)�� ������ ���
	//Connection�� PreparedStatement�� �ݳ�	
	public void closeDB(Connection con, PreparedStatement pstmt) {
		if(pstmt!=null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(con!=null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	

	public void closeDB(PreparedStatement pstmt) {
		if(pstmt!=null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//select�� ��� Preparedstatement, ResultSet����
	public void closeDB(PreparedStatement pstmt, ResultSet rs) {
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
	
	public void closeDB(Connection con, PreparedStatement pstmt, ResultSet rs) {
		if(pstmt!=null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(con!=null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(rs!=null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//�ν��Ͻ��� ��ȯ�ϴ� �޼��� ����
	//�ν��Ͻ� ������ ���� getter!
	public static ConnectionManager getInstance() {
		if(instance==null) {
			instance = new ConnectionManager();
		}
		return instance;
	}
	public static void main(String[] args) {
		new ConnectionManager().getConnection();
	}
}
