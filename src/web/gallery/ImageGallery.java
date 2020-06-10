package web.gallery;

import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//내가 구축한 톰켓 웹서버로부터 데이터를 가져와 이미지를 출력해보자!!
public class ImageGallery extends JFrame{
	URL url;
	HttpURLConnection httpCon; //원격지의 리소스에 연결!!
	BufferedReader buffr;
	
	public ImageGallery() {
		setLayout(new FlowLayout());
		loadFromURl();
		
		setSize(600,600);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	//웹서버로부터 데이터를 가져오기!!
	public void loadFromURl() {
		try {
			url = new URL("http://127.0.0.1:9090/json");
			httpCon = (HttpURLConnection)url.openConnection();
			//연결된 객체로부터 스트림을 뽑아 데이터 마셔보자!!(읽기)
			InputStream is = httpCon.getInputStream();
			
			buffr = new BufferedReader(new InputStreamReader(is));
			String data = null;
			StringBuilder sb = new StringBuilder();
			while(true) {
				data = buffr.readLine();
				if(data==null) break;
				sb.append(data); //한줄씩 읽어들인 json 스트링을 모으자!!
			}
			System.out.println(sb.toString());
			parseJson(sb.toString()); //모아진 데이터를 파싱하자!!
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(buffr!=null) {
				try {
					buffr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//읽어들인  json스트링을 파싱하여 객체화 하기!!!
	public void parseJson(String data) {
		JSONParser jsonParser = new JSONParser();
		//파싱 한 후엔, 스트링에 불과했던 데이터가 객체가 되버린다..
		//따라서 객체처럼 사용이 가능하다!
		try {
			JSONObject jsonObject = (JSONObject)jsonParser.parse(data); //파싱시작!! 파싱 : 데이터로부터 원하는 정보를 뽑는거
			JSONArray jsonArray = (JSONArray)jsonObject.get("dataList");
			
			for(int i=0; i<jsonArray.size();i++) {
				JSONObject json = (JSONObject)jsonArray.get(i);
				String title = (String)json.get("title");
				String author = (String)json.get("author");
				String filename = (String)json.get("filename");
				GalleryItem item = new GalleryItem(120, 160, title, author,filename);
				//프레임에 아이템패널 부착!!
				this.add(item);
				//프레임 refresh
				this.revalidate(); //프레임 refresh
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		new ImageGallery();
	}
}
