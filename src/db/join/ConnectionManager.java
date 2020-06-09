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

//중복된 데이터베이스 연결, 헤제코드를 재사용성을 높이기 위해 별도의 클래스로 정의한다!!


	public class ConnectionManager {
	static ConnectionManager instance;
	String url;
	//="jdbc:oracle:thin:@localhost:1521:XE";
	String user;
	//="c##java";
	String password;
	//="1234";
	
	//생성시점부터, 데이터베이스에 대한 정보를 프로퍼티에서 가져오자!!
	//왜 하나? 유지보수성 때문에..
	Properties props;
	FileInputStream  fis;
	//외부의 클래스가 접근못하게
	private ConnectionManager() {
		props = new Properties(); //key-value의 쌍으로 되어있는 데이터를 전담하는 객체!!
		//얘는 이 형태 이외에 데이터는 관섬도 없고 읽지도 못한다!!

		//스트림 생성시 패키지로부터 파일 접근하는 방법
		InputStream is = this.getClass().getResourceAsStream("/database/dbms.db");
		try {
			props.load(is);
			System.out.println(is);
			
			//key값으로 데이터 읽어오기!
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
	//데이터베이스 접속
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
	//데이터베이스 자원해제
	//윈도우 창 닫을때 호출
	public void closeDB(Connection con) {
		if(con!=null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//DML(insert,update,delete)만 수행한 경우
	//Connection과 PreparedStatement만 반납	
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
	
	//select문 경우 Preparedstatement, ResultSet닫음
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
	
	//인스턴스를 반환하는 메서드 정의
	//인스턴스 변수에 대한 getter!
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
