package utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

/**
 * This class contains the General Utilities that are required to support the
 * testing
 * 
 * @author Gurman
 *
 */
public class General {
	/**
	 * This method is used to encode a String using Base64 encoding mechanism
	 * 
	 * @param text - text to be encoded
	 * @return Base64 encoded String
	 */
	public static String encode(String text) {
		return Base64.getEncoder().encodeToString(text.getBytes());
	}

	/**
	 * This method is used to decode a Base64-encoded String
	 * 
	 * @param encodedText - encoded text to be decoded
	 * @return decoded String
	 */
	public static String decode(String encodedText) {
		return new String(Base64.getDecoder().decode(encodedText));
	}

	/**
	 * This method is used to test the given text against the given regular
	 * expression and return the matches and groups
	 * 
	 * @param text  - text to be tested against the regular expression
	 * @param regex - regular expression pattern
	 * @return List<List<String>> containing the matches and their captured-groups
	 */
	public static List<List<String>> getRegexMatches(String text, String regex) {
		List<List<String>> matchesAndGroups = new ArrayList<List<String>>();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			List<String> currentMatchAndGroups = new ArrayList<String>();
			for (int i = 0; i <= matcher.groupCount(); i++)
				currentMatchAndGroups.add(matcher.group(i));
			matchesAndGroups.add(currentMatchAndGroups);
		}
		return matchesAndGroups;
	}

	/**
	 * This method is used to create a directory
	 * 
	 * @param dirPath - path to the directory
	 * @return true, if the directory is created; else false
	 */
	public static boolean createDirectory(String dirPath) {
		File file = new File(dirPath);
		return file.mkdirs();
	}

	/**
	 * This method is used to create a file
	 * 
	 * @param filePath - path to the file to be created
	 * @return true, if the file is created; else false
	 */
	public static boolean createFile(String filePath) {
		File file = new File(filePath);
		try {
			FileUtils.touch(file);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		return file.exists();
	}

	/**
	 * This method is used to clean up a directory without deleting the directory.
	 * It will delete all the subfiles and subfolders.
	 * 
	 * @param dirPath - path to the directory to be cleaned
	 * @return true if the directory is cleaned; else false
	 */
	public static boolean cleanUpDirectory(String dirPath) {
		try {
			FileUtils.cleanDirectory(new File(dirPath));
			return true;
		} catch (IOException exception) {
			return false;
		}
	}
}
