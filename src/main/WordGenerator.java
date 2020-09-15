package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

public class WordGenerator {
	
	public static void TestSentenceGeneration() {
		WordGenerator gen = WordGenerator.instance();
		
		int curWord = 0;
		String nextWord = "";
		boolean capitalize = true;
		for (int i = 0; i < 20; ++i) {
			System.out.print(i + 1 + ".");
			while (!nextWord.contentEquals(".")) {
				nextWord = gen.getWord((curWord = gen.getNextWord(curWord)));
				if (capitalize || (nextWord.length() > 1 && nextWord.charAt(0) == 'i' && nextWord.charAt(1) == '\'')) {
					nextWord = Character.toUpperCase(nextWord.charAt(0)) + nextWord.substring(1);
				}
				else if (nextWord.contentEquals("i")) nextWord = "I";
				
				capitalize = nextWord.equals(".");
				System.out.print(((nextWord.equals(".") || nextWord.equals(",")) ? "" : " ") + nextWord);
			}
			System.out.println();
			nextWord = "";
		}
	}
	
	
	private static WordGenerator instance = null;
	private static final File WORD_FILE = new File("cdata.wmp");
	

	private WordGenerator() { }
	public static WordGenerator instance() {
		if (instance == null) instance = new WordGenerator();
		return instance;
	}
	
	public String getWord(int id) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(WORD_FILE);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		ByteBuffer indexBytes = ByteBuffer.allocate(36);
		String word = null;
		try {
			fis.skip(id * 36);
			fis.read(indexBytes.array());
			long wordIndexStart = indexBytes.getLong(20), wordIndexEnd = indexBytes.getLong(28);
			fis.skip(wordIndexStart - (id + 1) * 36);
			byte[] wordBytes = new byte[(int)(wordIndexEnd - wordIndexStart)];
			fis.read(wordBytes);
			word = new String(wordBytes);
			
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return word;
	}
	
	public int getNextWord(int current) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(WORD_FILE);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		ByteBuffer indexBytes = ByteBuffer.allocate(36);
		ByteBuffer wordIdBytes = ByteBuffer.allocate(4);
		int nextWord = -1;
		try {
			fis.skip(current * 36);
			fis.read(indexBytes.array());
			long listIndexStart = indexBytes.getLong(4), listIndexEnd = indexBytes.getLong(12);
			fis.skip((indexBytes.getLong(4) + (int)(Math.random() * ((listIndexEnd - listIndexStart) / 4 - 1) + 0.5) * 4) - (current + 1) * 36);
			fis.read(wordIdBytes.array());
			nextWord = wordIdBytes.getInt();
			
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return nextWord;
	}
}
