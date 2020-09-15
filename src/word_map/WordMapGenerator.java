package word_map;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class WordMapGenerator {
	
	private static long startTime, endTime;
	
	
	public static void main(String[] args) {
		String[] inputFiles = {"lorax.txt", "horton.txt", "cat_hat.txt"};
		WordMapGenerator mapGen = new WordMapGenerator("c:/users/kanan/desktop/wm/word_map", inputFiles, "c:/users/kanan/desktop/wm/inputs");
		startTime = System.currentTimeMillis();
		mapGen.generatePartialFiles();
		mapGen.generateCondensedFile();
		endTime = System.currentTimeMillis();
		
		System.out.printf("\nTotal Elapsed Time: %.2fmin [%.2fs]", (endTime - startTime) / 60000.f, (endTime - startTime) / 1000.f);
	}
	
	
	private Path mainDir, subDir;
	private Path condensedFile;
	private String[] inputFiles;
	private String inputDirectory;
	
	HashMap<String, Integer> wordList;
	
	
	public WordMapGenerator(String directory, String[] inputFiles, String inputDirectory) {
		mainDir = Paths.get(directory);
		subDir = Paths.get(mainDir.toString(), "partial_files");
		condensedFile = Paths.get(mainDir.getParent().toString(), "cdata.wmp");
		
		if (Files.notExists(subDir)) {
			try {
				Files.createDirectories(subDir);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		this.inputFiles = inputFiles;
		this.inputDirectory = inputDirectory;
		
		wordList = new HashMap<String, Integer>();
	}
	
	
	private static String cleanFile(String input) {
		String cleaned = input.replaceFirst("[.]", "_clean.");
			
		System.out.printf("Cleaning input file: %s\n", input);
		try {
			FileInputStream fis = new FileInputStream(input);
			FileOutputStream fos = new FileOutputStream(cleaned);
			
			int curChar, lastChar = fis.read(), nextChar;
			if (lastChar != '\'') fos.write(lastChar);
			while ((curChar = fis.read()) != -1) {
				if (curChar == '_') continue;
				if (Character.isAlphabetic(curChar) && Character.isDigit(lastChar)) continue;
				
				if (curChar == '\'') {
					if (lastChar == ' ' || lastChar == ',' || lastChar == '.') curChar = lastChar;
					else {
						if ((nextChar = fis.read()) == -1) continue;
						else if (Character.isWhitespace(nextChar) || nextChar == ',' || nextChar == '.') curChar = nextChar;
						if (nextChar != '\'') fis.skip(-1);
					}
				}
				
				if (curChar == '!' || curChar == '?') curChar = '.';
				// else if (curChar == '\n') curChar = '.';
				else if (Character.isWhitespace(curChar)) {
					if (lastChar == ' ') continue;
					if (fis.read() != '\'') fis.skip(-1);
					curChar = ' ';
				}
				
				if (((lastChar == '.' || lastChar == ',') && curChar != ' ') ||
					((curChar == '.' || curChar == ',') && lastChar != ' ')) {
					fos.write(' ');
				}
				
				lastChar = curChar;
				fos.write(curChar);
			}
			
			fis.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		System.out.println("Cleaning complete.");
		
		return cleaned;
	}
	
	private static String combineInputs(String inputDirectory, String[] inputFiles) throws IOException {
		Path combined = Paths.get(inputDirectory, "combined_input.txt");
		Files.deleteIfExists(combined);
		makeFile(combined);
		
		System.out.println("Combining inputs...");
		for (int i = 0; i < inputFiles.length; ++i) {
			byte[] bytes = Files.readAllBytes(Paths.get(inputDirectory, inputFiles[i]));
			Files.write(combined, bytes, StandardOpenOption.APPEND);
		}
		System.out.println("Combination complete.");
		
		return cleanFile(combined.toString());
	}
	
	private static void makeFile(Path path) {
		if (Files.notExists(path)) {
			try {
				Files.createFile(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static boolean fileContains(Path path, int n) {
		try {
			Scanner reader = new Scanner(path.toFile());
			
			while (reader.hasNextInt()) {
				if (reader.nextInt() == n) {
					reader.close();
					return true;
				}
			}
			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	private static void writeInt(Path path, int n) {
		try {
			Files.write(path, ByteBuffer.allocate(4).putInt(n).array(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void generatePartialFiles() {
		String file = null;
		try {
			file = combineInputs(inputDirectory, inputFiles);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to sanitize input file.");
			System.exit(1);
		}
		
		Scanner reader = null;
		try {
			reader = new Scanner(new File(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		String curWord = null, lastWord = null;
		Path lastFile = null;
		int bytesRead = 0, totalBytes = (int)new File(file).length();
		float percentage = 0.025f, percentageIncrement = percentage;
		int extra = 1;
		
		makeFile(Paths.get(subDir.toString(), 0 + "_._"));
		wordList.put(".", 0); // make sure the period is first so we can efficiently find a word to start sentences when using this data
		System.out.println("Generating partial files...");
		long startTime = System.currentTimeMillis();
		while (reader.hasNext() || extra-- > 0) { // using this hacky extra-- trick to get one more iteration after reader.hasNext() is false (ensures the last word in the input file is accounted for)
			if (reader.hasNext()) {
				bytesRead += (curWord = reader.next()).length() + 1;
				curWord = curWord.toLowerCase().replaceAll("[^a-zA-Z.,']", "");
			}
			else lastWord = null;
			
			if ((float)bytesRead / totalBytes >= percentage) {
				System.out.printf("%.2f%% [%.2fmin] complete...\n", ((float)bytesRead / totalBytes) * 100, (System.currentTimeMillis() - startTime) / 60000.f);
				percentage += percentageIncrement;
			}
			
			if (lastWord != null) {
				lastFile = Paths.get(subDir.toString(), wordList.get(lastWord) + "_" + lastWord + "_");
				makeFile(lastFile);
			}
			if (!curWord.isEmpty()) {
				if (!wordList.containsKey(curWord)) wordList.put(curWord, wordList.size());
				if (lastWord != null && !(lastWord.contentEquals(".") && curWord.contentEquals(",")) && !(lastWord.contentEquals(",") && curWord.contentEquals("."))) {
					if (!lastWord.contentEquals(curWord) && !fileContains(lastFile, wordList.get(curWord))) {
						writeInt(lastFile, wordList.get(curWord));
					}
				}
				lastWord = curWord;
			}
		}
		if (wordList.size() > 1) writeInt(Paths.get(subDir.toString(), 0 + "_._"), 1); // make sure first word in the document acts like it came after a period
		System.out.println("Generation complete.");
		
		
		reader.close();
	}
	
	
	// ugh, sadly this takes 4 for-loops to condense all of the data into a single file
	// if this code needs to run faster, then the files can be separated into 2 or 3 to reduce the number of necessary for-loops
	public void generateCondensedFile() {
		try {
			Files.deleteIfExists(condensedFile);
			Files.createFile(condensedFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Generating condensed file...");
		File directory = subDir.toFile();
		File[] files = directory.listFiles();
		Arrays.parallelSort(files, new PartialFileComparator()); // want to make sure that we go in ascending order of id values
		if (files == null) {
			System.out.println("An error occured while loading the prepared files.");
			System.exit(1);
		}
		
		// first walk through each file to gather data
		int totalWords = 0;
		int listSectionSize = 0;
		for (int i = 0; i < files.length; ++i) {
			if (!files[i].isDirectory()) {
				++totalWords;
				listSectionSize += files[i].length();
			}
		}
		
		// to optimize file-size, we can reduce index blocks by only including start indices and then include delimiters in the list and word sections
		int indexBlockSize = 36, indexSectionSize = totalWords * indexBlockSize;
		int listSectionSum = 0, wordSectionSum = 0;
		ByteBuffer buffer = ByteBuffer.allocate(indexBlockSize);
		
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(condensedFile.toFile(), true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		for (File file : files) { // first for-loop just fills in the index section
			if (file.isDirectory()) continue;
			
			String[] fileNameData = file.getName().split("_");
			
			int id = Integer.parseInt(fileNameData[0]);
			long listIndexStart = indexSectionSize + listSectionSum;
			long listIndexEnd = listIndexStart + file.length();
			long wordIndexStart = indexSectionSize + listSectionSize + wordSectionSum;
			long wordIndexEnd = wordIndexStart + fileNameData[1].length();
			
			buffer.putInt(id);
			buffer.putLong(listIndexStart);
			buffer.putLong(listIndexEnd);
			buffer.putLong(wordIndexStart);
			buffer.putLong(wordIndexEnd);
			
			try {
				output.write(buffer.array());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			listSectionSum += file.length();
			wordSectionSum += fileNameData[1].length();
			buffer.clear();
		}
		
		FileInputStream fis = null;
		for (File file : files) { // second for-loop fills in the list section
			if (file.isDirectory()) continue;
			
			try {
				fis = new FileInputStream(file);
				output.write(fis.readAllBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		for (File file : files) { // final for-loop fills in the word section
			if (file.isDirectory()) continue;
			
			try {
				output.write(file.getName().split("_")[1].getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Generation complete.");
	}
}
