package main.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class WordBox {
	
	protected static final Color
		DEFAULT_FONT_COLOR = new Color(70, 70, 70),
		DEFAULT_BG_COLOR = new Color(0, 0, 0, 0);
	private static final Color
		CURSOR_COLOR = new Color(250, 250, 250),
		FRAME_COLOR = new Color(180, 180, 180),
		BG_COLOR = new Color(220, 220, 220);
	
	private static final int frameSize = 40;
	private static final int padding = 20;
	
	
	private int x, y;
	private Font font;
	
	protected char[] displayChars;
	protected Color[][] displayColors;
	protected int displayLength;
	
	protected int cursorPos;
	
	
	public WordBox(int centerX, int centerY, int fontSize, int displayLength) {
		x = centerX;
		y = centerY;
		font = new Font("Courier", Font.BOLD, fontSize);
		this.displayLength = displayLength;
		if (this.displayLength < 1) displayLength = 1;
		
		displayChars = new char[this.displayLength];
		displayColors = new Color[this.displayLength][2];
		for (int i = 0; i < displayColors.length; ++i) {
			displayColors[i][0] = DEFAULT_FONT_COLOR;
			displayColors[i][1] = DEFAULT_BG_COLOR;
		}
		
		cursorPos = 0;
	}
	
	public WordBox(int centerX, int centerY, int fontSize, String displayText) {
		x = centerX;
		y = centerY;
		font = new Font("Courier", Font.BOLD, fontSize);
		this.displayLength = displayText.length();
		if (this.displayLength < 1) displayLength = 1;
		
		displayChars = new char[displayLength];
		displayColors = new Color[displayLength][2];
		for (int i = 0; i < this.displayLength; ++i) {
			displayChars[i] = displayText.charAt(i);
			displayColors[i][0] = DEFAULT_FONT_COLOR;
			displayColors[i][1] = DEFAULT_BG_COLOR;
		}
		
		cursorPos = displayText.length();
	}
	
	public void render(Graphics2D g) {
		FontMetrics metric = g.getFontMetrics(font);
		int charWidth = metric.stringWidth(" "),
			width = charWidth * displayLength,
			height = metric.getHeight(),
			adjustX = x - charWidth * displayLength / 2,
			adjustY = y - height / 2;
		
		g.setColor(FRAME_COLOR);
		g.fillRect(adjustX - frameSize, adjustY - height + metric.getDescent() - frameSize, width + frameSize * 2, height + frameSize * 2);
		g.setColor(BG_COLOR);
		g.fillRect(adjustX - padding, adjustY - height + metric.getDescent() - padding, width + padding * 2, height + padding * 2);
		if (cursorPos > -1) {
			g.setColor(CURSOR_COLOR);
			g.fillRect(adjustX + charWidth * cursorPos, adjustY - height + metric.getDescent(), charWidth, height);
		}
		
		g.setFont(font);
		for (int i = 0; i < displayLength; ++i) {
			g.setColor(displayColors[i][1]);
			g.fillRect(adjustX, adjustY - height + metric.getDescent(), charWidth, height);
			g.setColor(displayColors[i][0]);
			g.drawString(displayChars[i] + "", adjustX, adjustY);
			adjustX += charWidth;
		}
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public int getFontSize() {
		return font.getSize();
	}
	public void setFontSize(int fontSize) {
		font = new Font(font.getFontName(), font.getStyle(), fontSize);
	}
	
	public String getText() {
		return new String(displayChars);
	}
	public void setText(String displayText) {
		for (int i = 0; i < displayText.length(); ++i) {
			displayChars[i] = displayText.charAt(i);
		}
	}
	
	public Color getCharFontColor(int index) {
		return displayColors[index][0];
	}
	public void setCharFontColor(int index, Color color) {
		displayColors[index][0] = color;
	}
	
	public Color getCharFontBackground(int index) {
		return displayColors[index][1];
	}
	public void setCharFontColorBackground(int index, Color color) {
		displayColors[index][1] = color;
	}
	
	public Color[][] getDisplayColors() {
		return displayColors;
	}
	public void setDisplayColors(Color[][] displayColors) {
		if (displayColors.length >= displayLength && displayColors[0].length == 2) {
			this.displayColors = displayColors;
		}
		
	}
	
	public int getCursorPos() {
		return cursorPos;
	}
	public void setCursorPos(int cursorPos) {
		this.cursorPos = cursorPos;
	}
	
	public int getHeight() {
		return font.getSize() + frameSize * 2;
	}
}
