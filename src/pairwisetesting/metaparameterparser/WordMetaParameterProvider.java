package pairwisetesting.metaparameterparser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.coredomain.MetaParameterException;

/**
 * This class acts as the provider of meta parameter from Word file.
 * 
 * @see MetaParameter 
 */
public class WordMetaParameterProvider extends FileMetaParameterProvider {

	/**
	 * Constructs a meta parameter provider with the specified Word file that
	 * contains the meta parameter.
	 * 
	 * @param filePath
	 *            the path of the Word file that contains the meta parameter
	 * @throws NullPointerException
	 *             if {@code filePath} is null
	 */
	public WordMetaParameterProvider(String filePath) {
		super(filePath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pairwisetesting.coredomain.IMetaParameterProvider#get()
	 */
	public MetaParameter get() throws MetaParameterException {
		MetaParameter mp = new MetaParameter(2);
		String command
			= String.format("python word_extractor.py %s %s", filePath, ":");
		try {
			Process p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream("word_extractor_output.txt"), "utf-8"));
			String line = null;
			while ((line = in.readLine()) != null) {
				extractFactorLevels(mp, line);
			}
			in.close();
			return mp;
		} catch (Exception e) {
			throw new MetaParameterException(e);
		}
	}

}
