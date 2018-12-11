import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;


public class Kernel {

	 static Mat frame;
	 static Mat frameCopy;
	
	private static BufferedImage matToBufferedImage(Mat frame) { 
		int type = 0; 
		if (frame.channels() == 1) { 
		type = BufferedImage.TYPE_BYTE_GRAY; 
		} else if (frame.channels() == 3) { 
		type = BufferedImage.TYPE_3BYTE_BGR; 
		} 
		BufferedImage image = new BufferedImage(frame.width(), frame.height(), type); 
		WritableRaster raster = image.getRaster(); 
		DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer(); 
		byte[] data = dataBuffer.getData(); 
		frame.get(0, 0, data); 
		return image; 
	} 	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.loadLibrary("opencv_ffmpeg400_64"); 
		byte[] buffer = {1,2,3,4,5,6,7,8,4,1,2,3,4,5,1,1};
		Mat matr = new Mat(4,4,0);
		matr.put(0, 0, buffer);
		System.out.println(matr.dump());
		
		byte[] bufferkernel = {2,0,1,0,3,1,0,3,2};
		Mat matrkernel = new Mat(3,3,0);
		matrkernel.put(0, 0, bufferkernel);
		System.out.println(matrkernel.dump());
		
		Mat conv = new Mat();
		Imgproc.filter2D(matr, conv, 0, matrkernel);
		System.out.println(conv.dump());
		
		VideoCapture grabber = new VideoCapture(); 
		grabber.open("C:\\Users\\SD\\workspace\\Video\\src\\2.jpg"); 
		
		final JFrame frame1 = new JFrame("camera"); 
		frame1.setDefaultCloseOperation(frame1.EXIT_ON_CLOSE); 
		frame1.setSize(900, 900); 
		frame1.setLayout(new GridLayout(3,3));

		JLabel lbl=new JLabel(); 
		JLabel lbl1=new JLabel(); 
		JLabel lbl2=new JLabel(); 
		JLabel lbl3=new JLabel(); 
		JLabel lbl4=new JLabel(); 
		JLabel lbl5=new JLabel(); 
		JLabel lbl6=new JLabel(); 
		JLabel lbl7=new JLabel(); 
		JLabel lbl8=new JLabel(); 
		
		JPanel panel = new JPanel();
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		JPanel panel4 = new JPanel();
		JPanel panel5 = new JPanel();
		JPanel panel6 = new JPanel();
		JPanel panel7 = new JPanel();
		JPanel panel8 = new JPanel();
		
		panel.setSize(frame1.getSize().width/3, frame1.getSize().height/3); 
		panel1.setSize(frame1.getSize().width/3, frame1.getSize().height/3); 
		panel2.setSize(frame1.getSize().width/3, frame1.getSize().height/3); 
		panel3.setSize(frame1.getSize().width/3, frame1.getSize().height/3); 
		panel4.setSize(frame1.getSize().width/3, frame1.getSize().height/3); 
		panel5.setSize(frame1.getSize().width/3, frame1.getSize().height/3); 
		panel6.setSize(frame1.getSize().width/3, frame1.getSize().height/3); 
		panel7.setSize(frame1.getSize().width/3, frame1.getSize().height/3); 
		panel8.setSize(frame1.getSize().width/3, frame1.getSize().height/3); 
		
		panel.add(lbl);
		panel1.add(lbl1);
		panel2.add(lbl2);
		panel3.add(lbl3);
		panel4.add(lbl4);
		panel5.add(lbl5);
		panel6.add(lbl6);
		panel7.add(lbl7);
		panel8.add(lbl8);
		
		frame1.add(panel);
		frame1.add(panel1);
		frame1.add(panel2);
		frame1.add(panel3);
		frame1.add(panel4);
		frame1.add(panel5);
		frame1.add(panel6);
		frame1.add(panel7);
		frame1.add(panel8);
		
		frame1.setVisible(true); 
		
		frame1.repaint(); 
		frame=new Mat();
		frameCopy=new Mat();
		Mat frameBuffer=new Mat();
		
		grabber.read(frame);
		Mat gray = new Mat(); 
		Mat wide = new Mat(); 
		
		Imgproc.resize(frame, frame, new Size(panel.getSize().getWidth(), panel.getSize().getHeight()));
		
		Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY); 
		Imgproc.Canny(gray, wide, 50, 150,3,false); 
		
		ImageIcon image;
		image = new ImageIcon(matToBufferedImage(wide)); 
		lbl.setIcon(image); 
		
		image = new ImageIcon(matToBufferedImage(frame)); 
		lbl1.setIcon(image);
		
		Imgproc.blur(frame, frameCopy, new Size(5,5));
		image = new ImageIcon(matToBufferedImage(frameCopy)); 
		lbl2.setIcon(image);
		
		Core.absdiff(frame, frameCopy, frameBuffer);
		image = new ImageIcon(matToBufferedImage(frameBuffer)); 
		lbl3.setIcon(image);
		
		Imgproc.GaussianBlur(frame, frameCopy, new Size(11,11), 3);
		image = new ImageIcon(matToBufferedImage(frameCopy)); 
		lbl4.setIcon(image);
	
		Imgproc.bilateralFilter(frameCopy, frameBuffer,9 ,75,75);
		image = new ImageIcon(matToBufferedImage(frameBuffer)); 
		lbl7.setIcon(image);
		
		Core.absdiff(frame, frameCopy, frameBuffer);
		image = new ImageIcon(matToBufferedImage(frameBuffer)); 
		lbl5.setIcon(image);
	
		
		Imgproc.sqrBoxFilter(frame, frameCopy, 0, new Size(2,2));
		image = new ImageIcon(matToBufferedImage(frameCopy)); 
		lbl6.setIcon(image);
		
		Imgproc.Sobel(frame, frameCopy, -1, 2, 2);
		image = new ImageIcon(matToBufferedImage(frameCopy)); 
		lbl8.setIcon(image);
		
	}
	

}
