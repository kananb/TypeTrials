package main.gui;

import java.awt.Canvas;

import javax.swing.JFrame;

public class Display {
	
	private JFrame frame;
	private Canvas canvas;
	

	public Display(int width, int height, String title) {
		frame = new JFrame(title);
		canvas = new Canvas();
		
		canvas.setSize(width, height);
		canvas.setFocusable(true);
		
		frame.setSize(width, height);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(canvas);
		frame.pack();
		frame.setVisible(true);
	}
	
	
	public int getWidth() {
		return canvas.getWidth();
	}
	public void setWidth(int width) {
		frame.setSize(width, frame.getHeight());
		canvas.setSize(width, canvas.getHeight());
	}
	
	public int getHeight() {
		return canvas.getHeight();
	}
	public void setHeight(int height) {
		frame.setSize(frame.getWidth(), height);
		canvas.setSize(canvas.getWidth(), height);
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
}
