package excel.exchange;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

//태그를 파싱하는 과정에서, 이벤트마다 메서드를 호출하는 객체!!
public class MyXMLHandler extends DefaultHandler{
	ArrayList<Lang> list;
	//실행부가 어디를 지나치고 있는지를 체크해주는 변수
	boolean isName;
	boolean isVersion;
	boolean isVendor;
	Lang lang; //VO선언
	@Override
	public void startDocument() throws SAXException {
		list = new ArrayList<Lang>();
	}
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		System.out.print("<"+qName+">");
		
		if(qName.equals("lang")) { //emp vo
			lang = new Lang();
		}else if(qName.equals("name")) {
			isName = true;
		}else if(qName.equals("vesion")) {
			isVersion=true;
		}else if(qName.equals("vendor")) {
			isVendor=true;
		}
	}
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String data = new String(ch,start,length);
		System.out.print(data);
		
		if(isName) { //name 태그를 지나치고 있다면
			lang.setName(data);
		}else if(isVersion) { //vesion 태그를 지나치고있다면
			lang.setVersion(data);
		}else if(isVendor) { // vendor 태그를 지나치고있다면
			lang.setVendor(data);
		}
	}
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		System.out.println("<"+qName+">");
		if(qName.equals("lang")) { //emp vo
			list.add(lang); //리스트에 VO담기 !!
		}else if(qName.equals("name")) {
			isName = false;
		}else if(qName.equals("vesion")) {
			isVersion=false;
		}else if(qName.equals("vendor")) {
			isVendor=false;
		}
	}
	@Override
	public void endDocument() throws SAXException {
		System.out.println("담겨진 언어의 수는 "+list.size());
	}
}
