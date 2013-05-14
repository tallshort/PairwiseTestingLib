package pairwisetesting.coredomain;

/**
 * This interface acts as the provider of meta parameter.
 * 
 * @see pairwisetesting.metaparameterparser.FileMetaParameterProvider
 * @see pairwisetesting.metaparameterparser.XMLMetaParameterProvider
 * @see pairwisetesting.metaparameterparser.TextMetaParameterProvider
 * @see pairwisetesting.metaparameterparser.WordMetaParameterProvider
 * @see pairwisetesting.metaparameterparser.ExcelMetaParameterProvider
 */
public interface IMetaParameterProvider {

	/**
	 * Returns a meta parameter provided by the provider
	 * 
	 * @return a meta parameter provided by the provider
	 * @throws MetaParameterException
	 *             if the provider has problems in providing the meta parameter
	 */
	MetaParameter get() throws MetaParameterException;

}
