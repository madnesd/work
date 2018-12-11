import java.awt.BorderLayout; 
import java.util.List; 
import java.util.ArrayList; 

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import java.awt.Image; 
import java.awt.image.BufferedImage; 
import java.awt.image.DataBufferByte; 
import java.awt.image.WritableRaster; 
import java.io.File; 

//import javafx.scene.layout.Background;

import javax.swing.*; 


import org.opencv.core.Core; 
import org.opencv.core.CvType; 
import org.opencv.core.Mat; 
import org.opencv.core.MatOfInt4;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs; 
import org.opencv.imgproc.Imgproc; 
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.videoio.VideoCapture; 
import org.opencv.videoio.Videoio; 
import org.opencv.core.MatOfPoint; 

public class Zaxvat_video {
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
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.loadLibrary("opencv_ffmpeg400_64"); 
		VideoCapture grabber = new VideoCapture(); 

		grabber.open("C:\\Users\\SD\\workspace\\Video\\src\\2.jpg"); 
		
	//	grabber.open(0); 
		System.out.println("connected: " + grabber.isOpened()); 
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
		
		JPanel panel = new JPanel();
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		JPanel panel4 = new JPanel();
		JPanel panel5 = new JPanel();
		
		panel.setSize(300, 300); 
		panel1.setSize(300, 300); 
		panel2.setSize(300, 300); 
		panel3.setSize(300, 300); 
		panel4.setSize(300, 300); 
		panel5.setSize(300, 300); 
		
		panel.add(lbl);
		panel1.add(lbl1);
		panel2.add(lbl2);
		panel3.add(lbl3);
		panel4.add(lbl4);
		panel5.add(lbl5);
		
		frame1.add(panel);
		frame1.add(panel1);
		frame1.add(panel2);
		frame1.add(panel3);
		frame1.add(panel4);
		frame1.add(panel5);
		
		int i=1; 
	
		frame1.setVisible(true); 
		frame1.repaint(); 
		
		frame=new Mat();
		//grabber.set(1, 11000);
		
		while (true) { 
		if (grabber.read(frame)) { 
			
			 if(frameCopy == null) { 
				 frameCopy = frame.clone(); 
				 System.out.println("first frame"); 
				  } 
			 
		System.out.println("Start"+i+"frames"); 
		i++; 
		Mat gray = new Mat(); 
		Mat grayCopy = new Mat(); 
		Mat wide = new Mat(); 
		Mat cdst = new Mat(); 
		Mat cdstP = new Mat(); 
		
		Imgproc.resize(frame, frame, new Size(panel.getSize().getWidth(),panel.getSize().getHeight()));
		Imgproc.resize(frameCopy, frameCopy, new Size(panel.getSize().getWidth(),panel.getSize().getHeight()));
		
		Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY); 
		Imgproc.Canny(gray, wide, 50, 150,3,false); 
		
		
	
		int mWidth = frame.width(); 
		int mHeight = frame.height(); 
		 frameCopy = frame; 
		
		List <MatOfPoint> border = new ArrayList<MatOfPoint>(); 
		
		Point aP1 = new Point(mWidth*0.01, 0.95*mHeight); 
		Point aP2 = new Point(mWidth*0.99, 0.95*mHeight); 
		Point aP3 = new Point(mWidth*0.50, mHeight*0.5); 
		Point aP4 = new Point(mWidth*0.4, mHeight*0.5); 
		border.add(new MatOfPoint(aP1, aP2, aP3, aP4)); 
		Mat filtermat = new Mat(); 
		Mat mask= new Mat(frameCopy.rows(), frameCopy.cols(), CvType.CV_8U, Scalar.all(0)); 
		Imgproc.drawContours(mask, border, 0 ,new Scalar(255), -1); 
		frameCopy.copyTo(filtermat, mask); 

		Imgproc.cvtColor(filtermat, gray, Imgproc.COLOR_BGR2GRAY); 
		Imgproc.Canny(gray, wide, 50, 150,3,false); 

		Mat lines = new Mat(); 

		Imgproc.HoughLinesP(wide, lines, 1, Math.PI / 360, 80, 80, 80); 

		for (int j = 0; j < lines.rows(); j++) { 
		for (int ii = 0; ii < lines.cols(); ii++) { 
		double[] points = lines.get(j, ii); 
		double x1, y1, x2, y2; 
		double delta = 10*frame.height()/100; 

		x1 = points[0]; 
		y1 = points[1]; 
		x2 = points[2]; 
		y2 = points[3]; 
		Point pt1 = new Point(x1, y1); 
		Point pt2 = new Point(x2, y2); 

		if (x1>aP1.x+delta & x1<aP2.x-delta&x2>aP1.x+delta & x2<aP2.x-delta& Math.abs(y1-y2)>delta) { 
	//	Imgproc.line(frame, pt1, pt2, new Scalar(255, 255, 0), 4); 
		} 
		} 
		Imgproc.putText(filtermat, Integer.toString(j), new Point(50,50), 1, 2, new Scalar(255, 0, 0)); 
		} 

		
		
//		Imgproc.HoughLinesP(wide, lines, 1, Math.PI / 360, 80, 80, 80); 
//
//		for (int j = 0; j < lines.rows(); j++) { 
//		for (int ii = 0; ii < lines.cols(); ii++) { 
//		double[] points = lines.get(j, ii); 
//		double x1, y1, x2, y2; 
//		double delta = 10*frame.height()/100; 
//
//		x1 = points[0]; 
//		y1 = points[1]; 
//		x2 = points[2]; 
//		y2 = points[3]; 
//		Point pt1 = new Point(x1, y1); 
//		Point pt2 = new Point(x2, y2); 
//
//		if (x1>aP1.x+delta & x1<aP2.x-delta&x2>aP1.x+delta & x2<aP2.x-delta& Math.abs(y1-y2)>delta) { 
//		Imgproc.line(frame, pt1, pt2, new Scalar(255, 255, 0), 4); 
//		}
//		
//		}}
//		
		
		int ddepth = CvType.CV_16S; 
	
		ImageIcon image;
		
		image = new ImageIcon(matToBufferedImage(frame)); 
		lbl4.setIcon(image); 
		
		image = new ImageIcon(matToBufferedImage(wide)); 
		lbl.setIcon(image); 
		
	    image = new ImageIcon(matToBufferedImage(frame)); 
		lbl5.setIcon(image); 
		

		
		Imgproc.blur(gray, wide, new Size(5,5));
		image = new ImageIcon(matToBufferedImage(wide)); 
		lbl1.setIcon(image); 

		Mat grad_x = new Mat();
	    Mat grad_y = new Mat();
	    Mat abs_grad_x = new Mat();
	    Mat abs_grad_y = new Mat();
	 
	    
	    Imgproc.Sobel(gray, grad_x, ddepth, 1, 0);
	    Core.convertScaleAbs(grad_x, abs_grad_x);
	    Imgproc.Sobel(gray, grad_y, ddepth, 0, 1);
	    Core.convertScaleAbs(grad_y, abs_grad_y);
	    Core.addWeighted(abs_grad_x, 0.5, abs_grad_y, 0.5, 0, wide);
	   
		image = new ImageIcon(matToBufferedImage(wide)); 
		lbl2.setIcon(image); 
		
		image = new ImageIcon(matToBufferedImage(frame)); 
		lbl3.setIcon(image); 
		
		
		frameCopy=frame;
			
		frame1.repaint(); 
		try { 
		Thread.sleep(20); 
		} catch (InterruptedException e) { 
		// TODO Auto-generated catch block 
		e.printStackTrace(); 
		} 
		} 

		if (frame.empty()) { 
		System.out.println("Empty"+i+"frames"); 
		break;} 
	} 
	
	
		//System.exit(0);

	
	}
}
