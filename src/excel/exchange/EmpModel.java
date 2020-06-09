package excel.exchange;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class EmpModel extends AbstractTableModel{
	String[] column = {"empno","ename","job","mgr","hiredate","sal","comm","deptno"};
	ArrayList<Emp> list = new ArrayList<Emp>();
	@Override
	public int getRowCount() {
		return list.size();
	}

	@Override
	public int getColumnCount() {
		return column.length;
	}

	@Override
	public String getColumnName(int col) {
		return column[col];
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		String data = null;
		Emp emp = list.get(row);
		if(col==0) {
			data=Integer.toString(emp.getEmpno());
		}else if(col==1) {
			data=emp.getEname();
		}else if(col==2) {
			data=emp.getJob();
		}else if(col==3) {
			data=Integer.toString(emp.getMgr());
		}else if(col==4) {
			data=emp.getHiredate();
		}else if(col==5) {
			data=Integer.toString(emp.getSal());
		}else if(col==6) {
				data=Integer.toString(emp.getComm());
		}else if(col==7) {
			data=Integer.toString(emp.getDeptno());
		}
		return data;
	}
	
}
