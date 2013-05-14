package pairwisetesting.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

/**
 * The utility class facilitates the way of reading/writing file.
 * Borrowed from Bruce Eckel's Thinking in Java 4th.
 * 
 * @see ArrayList
 */
public class TextFile extends ArrayList<String> {
	
	private static final long serialVersionUID = -20207102858888293L;

	/**
	 * Read a file as a single string.
	 * 
	 * @param fileName
	 *            the specified file name
	 * @throws NullPointerException
	 *             if {@code fileName} is null
	 * @throws RuntimeException
	 *             if something wrong during processing
	 */
	public static String read(String fileName) {
		Preconditions.checkNotNull(fileName, "file name");
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(fileName).getAbsoluteFile()));
			try {
				String s = null;
				while ((s = in.readLine()) != null) {
					sb.append(s).append("\n");
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return sb.toString();
	}

	/**
	 * Write text to a single file.
	 * 
	 * @param fileName
	 *            the specified file name
	 * @param text
	 *            the text to write
	 * @throws NullPointerException
	 *             if {@code fileName} or {@code text} is null
	 * @throws RuntimeException
	 *             if something wrong during processing
	 */
	public static void write(String fileName, String text) {
		Preconditions.checkNotNull(fileName, "file name");
		Preconditions.checkNotNull(text, "text to write");
		try {
			File outFile = new File(fileName).getAbsoluteFile();
			outFile.getParentFile().mkdirs();
			PrintWriter out = new PrintWriter(outFile);
			
			try {
				out.print(text);
				//out.write(text);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Constructs an array list from a file splitted by a regular expression.
	 * 
	 * @param fileName
	 *            the specified file name
	 * @param splitter
	 *            a regular expression as the splitter
	 * @throws NullPointerException
	 *             if {@code fileName} or {@code splitter} is null
	 */
	public TextFile(String fileName, String splitter) {
		super(Arrays.asList(read(fileName).split(
				Objects.nonNull(splitter, "splitter"))));
		// Regular expression split() often leaves an empty String at the first
		// position
		if (get(0).equals("")) remove(0);
	}
	
	/**
	 * Constructs an array list from a file splitted by {@literal \n}
	 * 
	 * @param fileName
	 *            the specified file name
	 * @throws NullPointerException
	 *             if {@code fileName} is null
	 */
	public TextFile(String fileName) {
		this(fileName, "\n");
	}
	
	/**
	 * Write contents to a single file.
	 * 
	 * @param fileName
	 *            the specified file name
	 * @throws NullPointerException
	 *             if {@code fileName} is null
	 * @throws RuntimeException
	 *             if something wrong during processing
	 */
	public void write(String fileName) {
		Preconditions.checkNotNull(fileName, "file name");
		try {
			File outFile = new File(fileName).getAbsoluteFile();
			outFile.getParentFile().mkdirs();
			PrintWriter out = new PrintWriter(outFile);
			
			try {
				for (String item : this)
					out.println(item);
					//out.write(item);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
