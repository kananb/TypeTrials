package main.gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import main.App;

public class InputWordBox extends WordBox {
	
	protected static Color curCharColor = new Color(230, 230, 230);
	
	
	public InputWordBox(int displayLength, Color fontColor) {
		super(displayLength, fontColor);
	}
	public InputWordBox(String displayString, Color fontColor) {
		super(displayString, fontColor);
	}

	public void render(Graphics2D g) {
		// optimize this
		FontMetrics metric = g.getFontMetrics(font);
		int x = App.instance().getWidth() / 2 - metric.stringWidth(displayString.toString()) / 2,
			y = App.instance().getHeight() / 2,
			width = metric.stringWidth(displayString.toString()),
			height = metric.getHeight(),
			charWidth = metric.stringWidth(" ");
		int padding = 20;
		
		g.setColor(frameColor);
		g.fillRect(x - padding * 2, y - height + metric.getDescent() - padding * 2, width + padding * 4, height + padding * 4);
		g.setColor(backgroundColor);
		g.fillRect(x - padding, y - height + metric.getDescent() - padding, width + padding * 2, height + padding * 2);
		g.setColor(curCharColor);
		g.fillRect(x + width / 2, y - height + metric.getDescent(), charWidth, height);
		
		g.setColor(fontColor);
		g.setFont(font);
		g.drawString(displayString.toString(), x, y);
	}
}
