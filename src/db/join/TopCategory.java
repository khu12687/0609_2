package db.join;

//DB의 Table의 레코드 1건을 보관할 객체 정의
//이 객체는 로직을 담고 있는 것이 아니라, 오직 데이터만을 보관한다고 하여 어플리케이션 설계 분야에서는 VO(value object),
//DTO(Data Transfer object)
public class TopCategory {
	private int topcategory_id;
	private String name;
	public int getTopcategory_id() {
		return topcategory_id;
	}
	public void setTopcategory_id(int topcategory_id) {
		this.topcategory_id = topcategory_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
