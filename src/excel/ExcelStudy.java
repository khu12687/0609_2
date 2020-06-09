package excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelStudy {
	FileInputStream fis;
	HSSFWorkbook book;
	String fiilename = "C:/web_appDB/oracle_workspace/project0601/src/data/���� ����1.xls";

	public ExcelStudy() {
		try {
			fis = new FileInputStream(fiilename);
			book = new HSSFWorkbook(fis);
			HSSFSheet sheet = book.getSheet("member");
			
			//�� ����� ���� �ִ��� ����
			int rowNum = sheet.getLastRowNum();
			
		
			for(int i=0; i<rowNum;i++) {
				//����Ʈ�ȿ� �ִ� ������ ����
				HSSFRow row = sheet.getRow(i);
				
				HSSFCell name = row.getCell(0);
				HSSFCell phone = row.getCell(1);
				HSSFCell email = row.getCell(2);
				HSSFCell age = row.getCell(3);
				
				//������ ��쿣//
				if(i<1) {
					System.out.println(name.getStringCellValue()+","+phone.getStringCellValue()+","+email.getStringCellValue()+","+age.getStringCellValue());
				}else {
				System.out.println(name.getStringCellValue()+","+phone.getStringCellValue()+","+email.getStringCellValue()+","+(int)age.getNumericCellValue());
				}
			}
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		new ExcelStudy();
	}
}
