package db.join;

public class SubCategory {
	private int subCategory_id;
	//private int topcategory_id; //���̵�
	private TopCategory topcategory; // Has a���� �������
	private String name;
	public int getSubCategory_id() {
		return subCategory_id;
	}
	public void setSubCategory_id(int subCategory_id) {
		this.subCategory_id = subCategory_id;
	}
	public TopCategory getTopcategory() {
		return topcategory;
	}
	public void setTopcategory(TopCategory topcategory) {
		this.topcategory = topcategory;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

}
