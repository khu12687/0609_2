package web.gallery;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JPanel;

//하나의 겔러리 컴포넌트를 정의한다!!
//참고) 컴포넌트란? 일반적인 IT분야에서 독립적 기능을 갖춘 재사용가능한 단위를 말함!
//안드로이드분야에서는 위젯(Widget)
public class GalleryItem extends JPanel{
	JPanel p_canvas;
	JLabel la_title;
	JLabel la_author;
	Image img;
	String filename;
	//패널 생성시 그 크기를 개발자가 지정할 수 있도록!!
	public GalleryItem(int width, int height,String title, String author, String filename) {
		this.filename = filename;
		this.setPreferredSize(new Dimension(width,height));
		this.setBackground(Color.YELLOW);
		
		p_canvas = new JPanel() {
			@Override
			public void paint(Graphics g) {
				g.drawImage(img,0,0,width,height*(70/100),p_canvas);
			}
		};
		la_title = new JLabel("제목 :"+title);
		la_author = new JLabel("작성자 : "+author);
		
		//스타일
		p_canvas.setPreferredSize(new Dimension(width,height*(70/100)));
		p_canvas.setBackground(Color.RED);
		la_title.setPreferredSize(new Dimension(width,40));
		la_author.setPreferredSize(new Dimension(width,40));
		
		//조립
		add(p_canvas);
		add(la_title);
		add(la_author);
	}
	//url로 부터 이미지 생성해야 하므로, ImageIO.read() 할 예정..
	public void createImage() {
		
	}
}
