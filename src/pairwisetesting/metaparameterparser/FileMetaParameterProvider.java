package pairwisetesting.metaparameterparser;

import java.io.File;

import com.google.common.base.Preconditions;

import pairwisetesting.coredomain.Factor;
import pairwisetesting.coredomain.IMetaParameterProvider;
import pairwisetesting.coredomain.MetaParameter;

/**
 * This abstract class acts as the provider of meta parameter from file.
 * 
 * @see pairwisetesting.metaparameterparser.TextMetaParameterProvider
 * @see pairwisetesting.metaparameterparser.WordMetaParameterProvider
 * @see pairwisetesting.metaparameterparser.ExcelMetaParameterProvider
 * @see MetaParameter
 */
public abstract class FileMetaParameterProvider 
							implements IMetaParameterProvider {

	protected String filePath;

	/**
	 * Constructs a meta parameter provider with the specified file that
	 * contains the meta parameter.
	 * 
	 * @param filePath
	 *            the path of the file that contains the meta parameter
	 * @throws NullPointerException
	 *             if {@code filePath} is null
	 */
	public FileMetaParameterProvider(String filePath) {
		Preconditions.checkNotNull(filePath,
				"the path of the file that contains the meta parameter");
		this.filePath = new File(filePath).getAbsolutePath();
	}

	/**
	 * Extracts factor and its levels from one line.
	 * 
	 * @param mp
	 *            meta parameter to fill data
	 * @param line
	 *            one string that contains the factor information
	 * @throws NullPointerException
	 *             if {@code mp} or {@code line} is null
	 */
	protected void extractFactorLevels(MetaParameter mp, String line) {
		Preconditions.checkNotNull(mp, "meta parameter");
		Preconditions.checkNotNull(line, "line");
		// System.out.println(line);
		String[] factorAndLevels = line.split("\\s*[,:;]\\s*");
		Factor factor = new Factor(factorAndLevels[0]);
		for (int i = 1; i < factorAndLevels.length; i++) {
			factor.addLevel(factorAndLevels[i]);
		}
		mp.addFactor(factor);
	}

}
