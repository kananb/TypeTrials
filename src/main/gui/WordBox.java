package main.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import main.App;

public class WordBox {
	
	protected static final Color
		DEFAULT_FONT_COLOR = new Color(70, 70, 70),
		DEFAULT_BG_COLOR = new Color(0, 0, 0, 0);
	private static final Color
		CURSOR_COLOR = new Color(250, 250, 250),
		FRAME_COLOR = new Color(180, 180, 180),
		BG_COLOR = new Color(220, 220, 220);
	
	private static final float framePercentage = 0.35f;
	private static final float paddingPercentage = framePercentage / 2;
	
	
	private Rectangle dimensions;
	private Font font;
	
	private FontMetrics fontMetrics;
	private int charWidth;
	
	protected char[] displayChars;
	protected Color[][] displayColors;
	protected int displayLength;
	
	protected int cursorPos;
	
	
	public WordBox(Rectangle dimensions, int fontSize) {
		this.dimensions = dimensions;
		font = new Font("Courier", Font.BOLD, fontSize);
		fontMetrics = App.instance().getFontMetrics(font);
		charWidth = fontMetrics.stringWidth(" ");
		
		this.displayLength = (int )(dimensions.getWidth() - dimensions.getHeight() * framePercentage - dimensions.getHeight() * paddingPercentage) / fontMetrics.stringWidth(" ");
		
		displayChars = new char[this.displayLength];
		displayColors = new Color[this.displayLength][2];
		for (int i = 0; i < displayColors.length; ++i) {
			displayColors[i][0] = DEFAULT_FONT_COLOR;
			displayColors[i][1] = DEFAULT_BG_COLOR;
		}
		
		cursorPos = 0;
	}
	
	public WordBox(Rectangle dimensions, int fontSize, String displayText) {
		this.dimensions = dimensions;
		font = new Font("Courier", Font.BOLD, fontSize);
		fontMetrics = App.instance().getFontMetrics(font);
		charWidth = fontMetrics.stringWidth(" ");
		
		this.displayLength = (int )(dimensions.getWidth() - dimensions.getHeight() * framePercentage - dimensions.getHeight() * paddingPercentage) / fontMetrics.stringWidth(" ");
		if (displayLength > displayText.length()) displayLength = displayText.length();
		
		displayChars = new char[displayLength];
		displayColors = new Color[displayLength][2];
		setText(displayText);
		for (int i = 0; i < this.displayLength; ++i) {
			displayColors[i][0] = DEFAULT_FONT_COLOR;
			displayColors[i][1] = DEFAULT_BG_COLOR;
		}
		
		cursorPos = displayText.length();
	}
	
	public void render(Graphics2D g) {
		g.setColor(FRAME_COLOR);
		g.fillRect((int)dimensions.getX(), (int)dimensions.getY(), (int)dimensions.getWidth(), (int)dimensions.getHeight());
		
		int bgx = (int)(dimensions.getX() + dimensions.getHeight() * framePercentage / 2),
			bgy = (int)(dimensions.getY() + dimensions.getHeight() * framePercentage / 2),
			bgwidth = (int)(dimensions.getWidth() - dimensions.getHeight() * framePercentage),
			bgheight = (int)(dimensions.getHeight() - dimensions.getHeight() * framePercentage);
		g.setColor(BG_COLOR);
		g.fillRect(bgx, bgy, bgwidth, bgheight);
		
		int fontx = (int)(dimensions.getX() + dimensions.getWidth() / 2) - charWidth * displayLength / 2,
			fonty = (int)(dimensions.getY() + dimensions.getHeight() / 2) - fontMetrics.getDescent() + (font.getSize() + fontMetrics.getDescent()) / 2;
		if (cursorPos > -1) {
			g.setColor(CURSOR_COLOR);
			g.fillRect(fontx + cursorPos * charWidth, fonty - fontMetrics.getHeight() + fontMetrics.getDescent(), charWidth, fontMetrics.getHeight());
		}
		
		g.setFont(font);
		for (int i = 0; i < displayLength; ++i) {
			g.setColor(displayColors[i][1]);
			g.fillRect(fontx, fonty - fontMetrics.getHeight() + fontMetrics.getDescent(), charWidth, fontMetrics.getHeight());
			g.setColor(displayColors[i][0]);
			g.drawString(displayChars[i] + "", fontx, fonty);
			fontx += charWidth;
		}
	}
	
	public Rectangle getDimensions() {
		return dimensions;
	}
	public void setDimensions(Rectangle dimensions) {
		this.dimensions = dimensions;
	}
	
	public int getFontSize() {
		return font.getSize();
	}
	
	public String getText() {
		return new String(displayChars);
	}
	public void setText(String displayText) {
		int size = Math.max(displayLength, displayText.length());
		
		for (int i = 0; i < size; ++i) {
			if (i >= displayLength) break;
			else if (i >= displayText.length()) displayChars[i] = ' ';
			else displayChars[i] = displayText.charAt(i);
		}
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
}
