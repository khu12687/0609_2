package db.join;

import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class JoinModel extends AbstractTableModel{
	ArrayList<Goods> goodsList = new ArrayList<Goods>();
	
	//컬럼제목 배열
	String[] column = {
		"topcategory_id"
		,"상위카테고리명"
		,"subcategory_id"
		,"하위카테고리명"
		,"goods_id"
		,"상품명"
		,"가격"
		,"브랜드"
	};
	@Override
	public int getRowCount() {
		return goodsList.size();
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
		//ArrayList 리시트에 들어있는 데이터를 출력!!
		Goods goods = goodsList.get(row);
		String data = null;
		if(col==0) { //topcategory_id
			data = Integer.toString(goods.getSubCategory().getTopcategory().getTopcategory_id());
		}else if(col==1) { //top name
			data = goods.getSubCategory().getTopcategory().getName();
		}else if(col==2) { //subcategory_id
			data = Integer.toString(goods.getSubCategory().getSubCategory_id());
 		}else if(col==3) { //sub name
			data = goods.getSubCategory().getName();
		}else if(col==4) { //goods_id
			data = Integer.toString(goods.getGoods_id());
		}else if(col==5) { //상품명
			data = goods.getName();
		}else if(col==6) { //가격
			data = Integer.toString(goods.getPrice());
		}else if(col==7) { //브랜드
			data = goods.getBrand();
		}
		return data;
	}

		
}
