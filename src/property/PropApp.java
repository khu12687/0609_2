package property;
//�ڹ��� api �� , key-value�� ������ �̷���� �������� 
//property ������ �����ؼ� �а� �˻��ϴ� ��ü�� �ִ�!!
//Properties Ŭ������!!

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropApp {
	Properties props;
	FileReader reader; //�����Ϳ� �ѱ��� ���ԵǾ� �����Ƿ�, ���ڱ���� �Է½�Ʈ���� �̿�����!!
	//FileReader is a InputSstream!!
	public PropApp() {
		props = new Properties();
		
		//�ϵ��ũ�� ������ �о�� �ϹǷ� �ؼ��� �ռ� ���Ͽ� ���� ����!!
		try {
			props.load(reader = new FileReader("C:/web_appDB/oracle_workspace/project0601/data/fruit.data"));
			System.out.println("���� ���� �Ϸ�!!");
			
			//�� �������ʹ� ������ �ؼ�����!!(������ �Ľ�..)
			//���⵵�� �Ǵ��Ѵٸ� xml > json > properties
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
