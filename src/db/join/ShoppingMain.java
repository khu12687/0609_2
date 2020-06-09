package db.join;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class ShoppingMain extends JFrame{
	JPanel p_west;
	Choice ch_top; // top카테고리
	Choice ch_sub; // sub카테고리
	//리스트는 배열과 흡사하지만, 고무줄처럼 크기가 유동적이며, 오직 객체만을 넣을 수 있는다는 점이 틀리다
	ArrayList<TopCategory> topList = new ArrayList<TopCategory>(); //크기 0
	ArrayList<SubCategory> subList = new ArrayList<SubCategory>(); //크기 0
	int[] top_pk; //최상위 카테고리의 pk
	int[] sub_pk; //하위 카테고리의 pk
	
	JTextField t_name;
	JTextField t_price;
	JTextField t_brand;

	JButton bt_regist; //등록 버튼
	JButton bt_list; //목록 버튼

	JTable table;
	JScrollPane scroll;
	
	JoinModel joinModel;
	
	//데이터베이스 연동 관련
	ConnectionManager connectionManger;
	Connection con; //프로그램과 가동과 동시에 생성하고, 윈도우창 닫으면 닫자
	public ShoppingMain() {
		p_west = new JPanel();
		ch_top = new Choice();
		ch_sub = new Choice();
		
		t_name = new JTextField(10);
		t_price = new JTextField(10);
		t_brand = new JTextField(10);
		
		bt_regist = new JButton("등록");
		bt_list = new JButton("목록");
		table = new JTable(joinModel = new JoinModel());
		scroll = new JScrollPane(table);
		
		//서쪽 패널에 컴포넌트 부착
		p_west.add(ch_top);
		p_west.add(ch_sub);
		p_west.add(t_name);
		p_west.add(t_price);
		p_west.add(t_brand);
		p_west.add(bt_regist);
		p_west.add(bt_list);
		
		//스타일 적용
		p_west.setPreferredSize(new Dimension(150,500));
		p_west.setBackground(Color.CYAN);
		ch_top.setPreferredSize(new Dimension(140,25));
		ch_sub.setPreferredSize(new Dimension(140,25));
		//서쪽에 패널 부착
		add(p_west,BorderLayout.WEST);
		
		//샌터에 스크롤 부착
		add(scroll);
		
		//미리 가져올거 가져오자!!
		connectionManger = new ConnectionManager();
		con = connectionManger.getConnection();
		getTopCategoryList();
		TopCategory topcategory = topList.get(0);
		getSubCategoryList(topcategory.getTopcategory_id());
		setVisible(true);
		setSize(700,500);
		setLocationRelativeTo(null); //화면 가운데 위치
		
		//프레임과 리스너 연결
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				connectionManger.closeDB(con);
				System.exit(0);
			}
		});
		
		//상위 카테고리 초이스에 리스너 연결
		ch_top.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				System.out.println("값 바꿧어?");
				//유저가 선택한 topcategory_id를 추출해보자!!
				//topList 컬렉션 프레임웤에서..
				int index = ch_top.getSelectedIndex();
				System.out.println("당신이 선택한 옵션은"+index);
				
				TopCategory topCategory=topList.get(index); //VO 끄집어내기
				//최상위 카테고리의 pk를 넘기자!!
				getSubCategoryList(topCategory.getTopcategory_id());
				
			}
		});
		
		//등록버튼과 리스너 연결
		bt_regist.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				regist();
			}
		});
		//목록버튼과 리스너 연결
		bt_list.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getList();
			}
		});
	}
	
	public void getTopCategoryList() {
		String sql ="select * from topcategory order by topcategory_id asc";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = con.prepareStatement(sql); //쿼리 객체생성
			rs=pstmt.executeQuery(); //쿼리실행
			
			//top_pk = new int[];
			//rs의 모든 레코드를 Choice에 반영하기!!
			while(rs.next()) {
				//레코드 한건마다, 자바 VO객체의 인스턴스 1개로 받는다
				TopCategory topCategory = new TopCategory();
				topCategory.setTopcategory_id(rs.getInt("topcategory_id"));
				topCategory.setName(rs.getString("name"));
				
				topList.add(topCategory); //리스트에 VO넣기!!
				
				//아래의 코드는 유저가 보게될 카테고리 제목
				ch_top.add(rs.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			connectionManger.closeDB(pstmt,rs);
		}
	}
	
	public void getSubCategoryList(int topcategory_id) {
		String sql = "select * from subcategory where topcategory_id=?";
		//System.out.println(sql);
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = con.prepareStatement(sql);
			//쿼리 수행전에 바이드 변수의 값을 결정!!
			pstmt.setInt(1, topcategory_id);
			rs=pstmt.executeQuery();
			
			subList = new ArrayList<SubCategory>();
			
			while(rs.next()) {
				//VO로 rs 값등을 옮겨심기!!
				SubCategory subCategory = new SubCategory();
				subCategory.setSubCategory_id(rs.getInt("subcategory_id"));
				//자식이 부모의 객체자료형을 has a 관계로 보유한경우
				//TopCategory topCategory = new TopCategory(); 방치
				//subCategory.setTopcategory_id(rs.getInt("topcategory_id"));
				subCategory.setName(rs.getString("name"));
				
				//subList에 채워넣기!!
				subList.add(subCategory);
			}
			
			//기존의 아이템들을 모두 비우기!!
			ch_sub.removeAll();
			
			//생서된 리스트를 이용하여, 화면에 반영!! 즉 UI에 반영
			for(int i=0; i<subList.size(); i++) {
				SubCategory subCategory = subList.get(i);
				ch_sub.add(subCategory.getName());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			connectionManger.closeDB(pstmt,rs);
		}
	}
	
	//등록
	public void regist() {
		
		//상품테이블은 하위카테고리를 외래키로 보유하므로, 현재 유저가 보고있는 하위카테고리의 pk를 조사하자!!
		
		int index = ch_sub.getSelectedIndex();
		SubCategory subCategory = subList.get(index);
		
		String sql = "insert into goods(goods_id,subcategory_id,name,price,brand)";
		sql +=" values(seq_goods.nextval,?,?,?,?)";
		
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sql);//쿼리 생성
			//바인드 변수 값 지정
			pstmt.setInt(1,subCategory.getSubCategory_id());
			pstmt.setString(2, t_name.getText()); //제품명
			pstmt.setInt(3,Integer.parseInt(t_price.getText()));
			pstmt.setString(4, t_brand.getText());
			
			//쿼리수행!!
			int result = pstmt.executeUpdate();
			if(result ==0) {
				JOptionPane.showMessageDialog(this, "등록실패");
			}else {
				JOptionPane.showMessageDialog(this, "등록성공");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			connectionManger.closeDB(pstmt); //자원 해제!!
		}
		
	}
	//목록가져오기(3개의 테이블을 조인하여 가져올것임)
	public void getList() {
		StringBuilder sql = new StringBuilder();
		sql.append("select t.topcategory_id as topcategory_id, t.name as top_name");
		sql.append(", s.subcategory_id as subcategory_id, s.name as sub_name");
		sql.append(", goods_id,g.name as goods_name,price,brand");
		sql.append(" from topcategory t, subcategory s, goods g");
		sql.append(" where t.topcategory_id = s.topcategory_id");
		sql.append(" and s.subcategory_id = g.subcategory_id");
		System.out.println(sql.toString());
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			rs=pstmt.executeQuery();
			
			//rs에는 3개의 테이블이 합쳐진 레코드가 있기 때문에, 이 데이터들을 우리가 정의한 VO에 찢어서 넣어야 한다!!
			
			List list = new ArrayList();
			
			while(rs.next()) {
				TopCategory topCategory = new TopCategory();
				SubCategory subCategory = new SubCategory();
				Goods goods = new Goods();
				
				//서로간의 관계부터 설정하자!!
				subCategory.setTopcategory(topCategory); //(1)
				goods.setSubCategory(subCategory);
				
				//rs의 데이터를 VO에 적절히 옮겨심자!!
				topCategory.setTopcategory_id(rs.getInt("topcategory_id"));
				topCategory.setName(rs.getString("top_name"));
				
				subCategory.setSubCategory_id(rs.getInt("subcategory_id"));
				//subCategory.setTopcategory(rs.getInt("t.topcategory_id")); 이미위에서 넣어서 필요없음(1)
				subCategory.setName(rs.getString("sub_name"));
				
				goods.setGoods_id(rs.getInt("goods_id"));
				goods.setName(rs.getString("goods_name"));
				goods.setPrice(rs.getInt("price"));
				goods.setBrand(rs.getString("brand"));
				
				
				//완성된 Goods VO를 리스트에 추가!!
				list.add(goods);
			}
			//완성된 리스트를 JoinModel이 보유한 리스트에 대입!!
			joinModel.goodsList=(ArrayList)list;
			table.updateUI(); //테이블 새로고침!!
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			connectionManger.closeDB(pstmt,rs);
		}
	}
	
	public static void main(String[] args) {
		new ShoppingMain();
	}
}
