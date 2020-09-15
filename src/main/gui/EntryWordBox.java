package main.gui;

import java.awt.Color;
import java.util.ArrayList;

import main.WordGenerator;

public class EntryWordBox extends WordBox {
	
	private static final Color
		TYPED_COLOR = new Color(150, 150, 150),
		TYPED_RIGHT_COLOR = new Color(0.1f, 0.7f, 0.15f, 0.2f),
		TYPED_WRONG_COLOR = new Color(0.6f, 0.2f, 0.2f, 0.75f);
	
	
	private StringBuilder generatedText;
	private ArrayList<Boolean> charStates;
	private int textIndex, numMissedChars;
	
	private int curWord;
	private boolean capitalize;
	
	
	public EntryWordBox(int centerX, int centerY, int fontSize, int displayLength) {
		super(centerX, centerY, fontSize, displayLength);
		
		generatedText = new StringBuilder(128);
		charStates = new ArrayList<Boolean>(128);
		
		textIndex = 0;
		numMissedChars = 0;
		curWord = 0;
		capitalize = true;
		cursorPos = this.displayLength / 2;
		
		generateDisplayData();
	}
	
	public EntryWordBox(int centerX, int centerY, int fontSize, String displayText) {
		super(centerX, centerY, fontSize, displayText);

		curWord = 0;
		capitalize = true;
		
		generatedText = new StringBuilder(128);
		generatedText.append(displayText);
		charStates = new ArrayList<Boolean>(128);
		cursorPos = this.displayLength / 2;
		
		textIndex = 0;
		numMissedChars = 0;
		
		generateDisplayData();
	}
	
	private void generateDisplayData() {
		for (int i = 0; i < displayLength; ++i) {			
			int index = i + textIndex - displayLength / 2;
			if (index >= generatedText.length()) {
				String nextWord = WordGenerator.instance().getWord(curWord = WordGenerator.instance().getNextWord(curWord));
				if (capitalize || (nextWord.length() > 1 && nextWord.charAt(0) == 'i' && nextWord.charAt(1) == '\'')) {
					nextWord = Character.toUpperCase(nextWord.charAt(0)) + nextWord.substring(1);
				}
				else if (nextWord.contentEquals("i")) nextWord = "I";
				
				capitalize = nextWord.equals(".");
				generatedText.append(((!nextWord.equals(".") && !nextWord.equals(",") && generatedText.length() != 0) ? " " : "") + nextWord);
			}
			// this allows text to start at the middle of the wordbox
			if (index < 0) {
				displayChars[i] = ' ';
				displayColors[i][0] = TYPED_COLOR;
				displayColors[i][1] = DEFAULT_BG_COLOR;
			}
			else {
				displayChars[i] = generatedText.charAt(index);
				if (i < displayLength && charStates.size() > index)
					if (charStates.get(index)) displayColors[i][1] = TYPED_RIGHT_COLOR;
					else displayColors[i][1] = TYPED_WRONG_COLOR;
				else displayColors[i][1] = DEFAULT_BG_COLOR;
			}
		}
	}
	
	public void type(char c) {
		boolean correct = c == displayChars[cursorPos];
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
