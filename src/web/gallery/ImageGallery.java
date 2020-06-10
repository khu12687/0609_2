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

//���� ������ ���� �������κ��� �����͸� ������ �̹����� ����غ���!!
public class ImageGallery extends JFrame{
	URL url;
	HttpURLConnection httpCon; //�������� ���ҽ��� ����!!
	BufferedReader buffr;
	
	public ImageGallery() {
		setLayout(new FlowLayout());
		loadFromURl();
		
		setSize(600,600);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	//�������κ��� �����͸� ��������!!
	public void loadFromURl() {
		try {
			url = new URL("http://127.0.0.1:9090/json");
			httpCon = (HttpURLConnection)url.openConnection();
			//����� ��ü�κ��� ��Ʈ���� �̾� ������ ���ź���!!(�б�)
			InputStream is = httpCon.getInputStream();
			
			buffr = new BufferedReader(new InputStreamReader(is));
			String data = null;
			StringBuilder sb = new StringBuilder();
			while(true) {
				data = buffr.readLine();
				if(data==null) break;
				sb.append(data); //���پ� �о���� json ��Ʈ���� ������!!
			}
			System.out.println(sb.toString());
			parseJson(sb.toString()); //����� �����͸� �Ľ�����!!
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
	
	//�о����  json��Ʈ���� �Ľ��Ͽ� ��üȭ �ϱ�!!!
	public void parseJson(String data) {
		JSONParser jsonParser = new JSONParser();
		//�Ľ� �� �Ŀ�, ��Ʈ���� �Ұ��ߴ� �����Ͱ� ��ü�� �ǹ�����..
		//���� ��üó�� ����� �����ϴ�!
		try {
			JSONObject jsonObject = (JSONObject)jsonParser.parse(data); //�Ľ̽���!! �Ľ� : �����ͷκ��� ���ϴ� ������ �̴°�
			JSONArray jsonArray = (JSONArray)jsonObject.get("dataList");
			
			for(int i=0; i<jsonArray.size();i++) {
				JSONObject json = (JSONObject)jsonArray.get(i);
				String title = (String)json.get("title");
				String author = (String)json.get("author");
				String filename = (String)json.get("filename");
				GalleryItem item = new GalleryItem(120, 160, title, author,filename);
				//�����ӿ� �������г� ����!!
				this.add(item);
				//������ refresh
				this.revalidate(); //������ refresh
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		new ImageGallery();
	}
}
