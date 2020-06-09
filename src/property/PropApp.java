package property;
//자바의 api 중 , key-value의 쌍으로 이루어진 데이터인 
//property 파일을 전담해서 읽고 검색하는 객체가 있다!!
//Properties 클래스임!!

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropApp {
	Properties props;
	FileReader reader; //데이터에 한글이 포함되어 있으므로, 문자기반의 입력스트림을 이용하자!!
	//FileReader is a InputSstream!!
	public PropApp() {
		props = new Properties();
		
		//하드디스크의 파일을 읽어야 하므로 해석에 앞서 파일에 대한 접근!!
		try {
			props.load(reader = new FileReader("C:/web_appDB/oracle_workspace/project0601/data/fruit.data"));
			System.out.println("파일 접근 완료!!");
			
			//이 시점부터는 파일을 해석하자!!(일종의 파싱..)
			//복잡도로 판단한다면 xml > json > properties
			String data=(String)props.get("apple");
			System.out.println(data);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(reader!=null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static void main(String[] args) {
		new PropApp();
	}
}
