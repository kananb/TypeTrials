package main.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import main.App;

public class WordBox {
	
	protected static Font font = new Font("Courier", Font.BOLD, 40);
	protected static Color frameColor = new Color(100, 100, 100);
	protected static Color backgroundColor = new Color(180, 180, 180);
	
	
	protected int displayLength;
	protected StringBuilder displayString;
	
	protected Color fontColor;
	
	
	public WordBox(int displayLength, Color fontColor) {
		this.displayLength = displayLength;
		displayString = new StringBuilder(displayLength);
		this.fontColor = fontColor;
	}
	public WordBox(String displayString, Color fontColor) {
		displayLength = displayString.length();
		this.displayString = new StringBuilder(displayLength);
		this.displayString.append(displayString);
		this.fontColor = fontColor;
	}
	
	public void render(Graphics2D g) {
		FontMetrics metric = g.getFontMetrics(font);
		int x = App.instance().getWidth() / 2 - metric.stringWidth(displayString.toString()) / 2,
			y = App.instance().getHeight() / 2,
			width = metric.stringWidth(displayString.toString()),
			height = metric.getHeight();
		int padding = 20;
		
		g.setColor(frameColor);
		g.fillRect(x - padding * 2, y - height + metric.getDescent() - padding * 2, width + padding * 4, height + padding * 4);
		g.setColor(backgroundColor);
		g.fillRect(x - padding, y - height + metric.getDescent() - padding, width + padding * 2, height + padding * 2);
		
		g.setColor(fontColor);
		g.setFont(font);
		g.drawString(displayString.toString(), x, y);
	}
}
