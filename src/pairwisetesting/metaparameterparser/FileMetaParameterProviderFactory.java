package pairwisetesting.metaparameterparser;

import com.google.common.base.Preconditions;

/**
 * The factory that creates the proper meta parameter provider based on the
 * suffix of the file path.
 * 
 * @see FileMetaParameterProvider
 * @see TextMetaParameterProvider
 * @see WordMetaParameterProvider
 * @see ExcelMetaParameterProvider
 */
public class FileMetaParameterProviderFactory {

	/**
	 * Returns a meta parameter provider based on the suffix of the file path if
	 * the related meta parameter is available, otherwise returns null.
	 * 
	 * @param filePath
	 *            the path of the file that contains the meta parameter
	 * @return a meta parameter provider based on the suffix of the file path if
	 *         the related meta parameter is available, otherwise null
	 * @throws NullPointerException
	 *             if {@code filePath} is null
	 */
	public FileMetaParameterProvider create(String filePath) {
		Preconditions.checkNotNull(filePath, 
				"the path of the file that contains the meta parameter");
		if (filePath.endsWith("doc")) {
			return new WordMetaParameterProvider(filePath);
		} else if (filePath.endsWith("xls")) {
			return new ExcelMetaParameterProvider(filePath);
		} else if (filePath.endsWith("txt")) {
			return new TextMetaParameterProvider(filePath);
		} else {
			return null;
		}
	}

}
