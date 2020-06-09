package web.gallery;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JPanel;

//�ϳ��� �ַ��� ������Ʈ�� �����Ѵ�!!
//����) ������Ʈ��? �Ϲ����� IT�о߿��� ������ ����� ���� ���밡���� ������ ����!
//�ȵ���̵�о߿����� ����(Widget)
public class GalleryItem extends JPanel{
	JPanel p_canvas;
	JLabel la_title;
	JLabel la_author;
	Image img;
	String filename;
	//�г� ������ �� ũ�⸦ �����ڰ� ������ �� �ֵ���!!
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
		la_title = new JLabel("���� :"+title);
		la_author = new JLabel("�ۼ��� : "+author);
		
		//��Ÿ��
		p_canvas.setPreferredSize(new Dimension(width,height*(70/100)));
		p_canvas.setBackground(Color.RED);
		la_title.setPreferredSize(new Dimension(width,40));
		la_author.setPreferredSize(new Dimension(width,40));
		
		//����
		add(p_canvas);
		add(la_title);
		add(la_author);
	}
	//url�� ���� �̹��� �����ؾ� �ϹǷ�, ImageIO.read() �� ����..
	public void createImage() {
		
	}
}
