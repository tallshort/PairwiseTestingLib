package pairwisetesting.metaparameterparser;

import java.util.List;

import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.coredomain.MetaParameterException;
import pairwisetesting.util.TextFile;

/**
 * This class acts as the provider of meta parameter from text file.
 * 
 * @see MetaParameter
 */
public class TextMetaParameterProvider extends FileMetaParameterProvider {

	/**
	 * Constructs a meta parameter provider with the specified text file that
	 * contains the meta parameter.
	 * 
	 * @param filePath
	 *            the path of the text file that contains the meta parameter
	 * @throws NullPointerException
	 *             if {@code filePath} is null
	 */
	public TextMetaParameterProvider(String filePath) {
		super(filePath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pairwisetesting.coredomain.IMetaParameterProvider#get()
	 */
	public MetaParameter get() throws MetaParameterException {
		MetaParameter mp = new MetaParameter(2);
		try {
			List<String> lines = new TextFile(filePath);
			for (String line : lines) {
				extractFactorLevels(mp, line);
			}
			return mp;
		} catch (Exception e) {
			throw new MetaParameterException(e);
		}
	}

}
