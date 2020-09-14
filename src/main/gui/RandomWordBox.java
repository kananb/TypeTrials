package main.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import main.App;
import main.WordGenerator;


/* To-do
 * Sometimes old words show up again for some reason
 * begWord doesn't work as intended because it treats the middle of the box as the end
 * Keep track of characters typed by user to compare against generated words (for error tracking)
 * Figure out how I wanna change the font color and stuff when a character is typed incorrectly
 * 		(perhaps store a bit along with each display character and write them all individually)
 */

public class RandomWordBox {
	
	private static final Color
		UNTYPED_COLOR = Color.WHITE,
		TYPED_RIGHT_COLOR = Color.LIGHT_GRAY,
		TYPED_WRONG_COLOR = new Color(160, 50, 50);
	private static final Color
		CURSOR_COLOR = new Color(230, 230, 230),
		FRAME_COLOR = new Color(100, 100, 100),
		BG_COLOR = new Color(180, 180, 180);
	private static Font font = new Font("Courier", Font.BOLD, 40);
	
	
	private StringBuilder generatedText;
	private ArrayList<Boolean> charStates;
	private int textIndex, numMissedChars;
	private String[] displayChars;
	private Color[] displayColors;
	
	private int curWord;
	private boolean capitalize;
	
	
	public RandomWordBox(int displayLength) {
		if (displayLength % 2 == 0) ++displayLength;
		curWord = 0;
		capitalize = true;
		
		generatedText = new StringBuilder(128);
		// generatedText.append(WordGenerator.instance().getWord(curWord = WordGenerator.instance().getNextWord(curWord)));
		charStates = new ArrayList<Boolean>(128);
		
		textIndex = 0;
		numMissedChars = 0;
		
		displayChars = new String[displayLength];
		displayColors = new Color[displayLength];
		generateDisplayData();
	}
	
	private void generateDisplayData() {
		for (int i = 0; i < displayChars.length; ++i) {			
			int index = i + textIndex - displayChars.length / 2;
			if (index >= generatedText.length()) {
				String nextWord = WordGenerator.instance().getWord(curWord = WordGenerator.instance().getNextWord(curWord));
				if (capitalize) nextWord = Character.toUpperCase(nextWord.charAt(0)) + nextWord.substring(1);
				else if (nextWord.equals("i")) nextWord = "I";
				capitalize = nextWord.equals(".");
				generatedText.append(((!nextWord.equals(".") && !nextWord.equals(",") && generatedText.length() != 0) ? " " : "") + nextWord);
			}
			if (index < 0) {
				displayChars[i] = " ";
				displayColors[i] = TYPED_RIGHT_COLOR;
			}
			else {
				displayChars[i] = generatedText.charAt(index) + "";
				displayColors[i] = (i < displayChars.length / 2) ? ((charStates.get(index)) ? TYPED_RIGHT_COLOR : TYPED_WRONG_COLOR) : UNTYPED_COLOR;
			}
		}
	}
	
	public void render(Graphics2D g) {
		FontMetrics metric = g.getFontMetrics(font);
		int charWidth = metric.stringWidth(" "),
			width = charWidth * displayChars.length,
			height = metric.getHeight(),
			x = App.instance().getWidth() / 2 - charWidth * displayChars.length / 2,
			y = App.instance().getHeight() / 2;
		int padding = 20;
		
		g.setColor(FRAME_COLOR);
		g.fillRect(x - padding * 2, y - height + metric.getDescent() - padding * 2, width + padding * 4, height + padding * 4);
		g.setColor(BG_COLOR);
		g.fillRect(x - padding, y - height + metric.getDescent() - padding, width + padding * 2, height + padding * 2);
		g.setColor(CURSOR_COLOR);
		g.fillRect(x + charWidth * (displayChars.length / 2), y - height + metric.getDescent(), charWidth, height);
		
		g.setFont(font);
		for (int i = 0; i < displayChars.length; ++i) {
			g.setColor(displayColors[i]);
			g.drawString(displayChars[i], x, y);
			x += charWidth;
		}
	}

	public void type(char c) {
		boolean correct = c == displayChars[displayChars.length / 2].charAt(0);
		charStates.add(correct);
		if (!correct) ++numMissedChars;
		
		++textIndex;
		generateDisplayData();
	}
	
	public void untype() {
		if (textIndex <= 0) return;
		
		boolean state = charStates.get(charStates.size() - 1);
		charStates.remove(charStates.size() - 1);
		if (!state) --numMissedChars;
		
		--textIndex;
		generateDisplayData();
	}
	
	public int getNumWordsTyped() {
		return charStates.size() / 5;
	}
	
	public int getNumMissedChars() {
		return numMissedChars;
	}
}
