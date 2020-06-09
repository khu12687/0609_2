package transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.join.ConnectionManager;

//����Ŭ �����ͺ��̽��� ���� Ʈ����� ó���� �ڹ� �ڵ�� �����غ���!!
//����� jdbc�� Ʈ������� �ڵ� Ŀ�ԵǾ��ִ�!! �׷��� �׵��� ������ ������ commit ������ �������Ŵ�!!
public class TransEx {
	ConnectionManager connectionManager = ConnectionManager.getInstance();
	public TransEx() {
		//�ΰ��� ���̺� ���� insert ��Ȳ�� ������!!
		//�� ��� ���ξ����� 2�� ¥���� Ʈ����� ��Ȳ�̴�!!
		Connection con = connectionManager.getConnection();
		PreparedStatement pstmt = null;
		
		try {
			con.setAutoCommit(false); //�ڵ�Ŀ�� ���ϰڴ�
			String sql="insert into dept(deptno,dname,loc) values(50,?,?)";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, "marketing");
			pstmt.setString(2, "LA");
			pstmt.executeUpdate(); //��������
			
			sql="insert into emp(empno,ename,deptno) values(7777,'batman',50)";
			pstmt=con.prepareStatement(sql);
			pstmt.executeUpdate(); //���� ����  2
			
			//��� ���ξ����� ���������Ƿ�, ��ü�� �������� �����Ѵ�
			//commit
			con.commit();
			System.out.println("�ΰ��� ����Ʈ ��ϿϷ�");
		} catch (Exception e) {
			e.printStackTrace();
			//������ �߻��� ����̹Ƿ�, ��� ���ξ����� ó������ ������������ ���ư����Ѵ�!! rollback
			try {
				con.rollback();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			System.out.println();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if(con!=null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static void main(String[] args) {
		new TransEx();
	}
}
