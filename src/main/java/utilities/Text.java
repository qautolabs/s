package utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains methods to read/write data to text files
 * 
 * @author Gurman
 *
 */
public class Text {
	/**
	 * This method is used to read lines from the given text file
	 * 
	 * @param filePath - path to the text file to be read
	 * @return List<String> containing the lines read from the given text file
	 */
	public static List<String> read(String filePath) {
		List<String> data = new ArrayList<String>();
		try {
			FileReader fileReader = new FileReader(new File(filePath));
			BufferedReader reader = new BufferedReader(fileReader);
			String currentLine;
			while ((currentLine = reader.readLine()) != null)
				data.add(currentLine);
			reader.close();
			fileReader.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return data;
	}

	/**
	 * This method is used to read the specific line from the given text file
	 * 
	 * @param filePath - path to the text file to be read
	 * @param lineNo   - line number to be read
	 * @return String containing the data present in the given line
	 */
	public static String read(String filePath, int lineNo) {
		List<String> data = read(filePath);
		if (lineNo < 1 || lineNo > data.size())
			return null;
		else
			return data.get(lineNo - 1);
	}

	/**
	 * This method is used to write data to the given text file
	 * 
	 * @param filePath - path to the text file where data is to be written
	 * @param data     - text to be written to the file
	 * @param append   - true to append the new data to existing data; false to
	 *                 overwrite existing data with the new data
	 */
	public static void write(String filePath, String data, boolean append) {
		try {
			FileWriter fileWriter = new FileWriter(new File(filePath), append);
			BufferedWriter writer = new BufferedWriter(fileWriter);
			writer.write(data);
			writer.close();
			fileWriter.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
}
