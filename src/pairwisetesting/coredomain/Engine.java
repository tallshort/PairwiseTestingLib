package pairwisetesting.coredomain;

import com.google.common.base.Preconditions;

/**
 * This abstract class encapsulates the logic of generating test data with the
 * Pairwise Testing technique.
 * 
 * @see MetaParameter
 * @see ITestDataTransformer
 * @see pairwisetesting.engine.am.AMEngine
 * @see pairwisetesting.engine.jenny.JennyEngine
 * @see pairwisetesting.engine.pict.PICTEngine
 * @see pairwisetesting.engine.tvg.TVGEngine
 */
public abstract class Engine {

	/**
	 * The transformer used to transform the raw test data.
	 */
	protected ITestDataTransformer transformer = ITestDataTransformer.NULL;

	/**
	 * Generates the raw test data based on the meta parameter.
	 * 
	 * @param mp
	 *            the meta parameter used by the engine
	 * @return the raw test data
	 * @throws EngineException
	 *             if the engine can not handle the meta parameter
	 */
	protected abstract String[][] generateRawTestData(MetaParameter mp)
			throws EngineException;

	/**
	 * Generates the test data based on the meta parameter.
	 * 
	 * @param mp
	 *            the meta parameter used by the engine
	 * @return the test data
	 * @throws NullPointerException
	 *             if {@code mp} is null
	 * @throws EngineException
	 *             if the engine can not handle the meta parameter
	 */
	public String[][] generateTestData(MetaParameter mp) throws EngineException {
		Preconditions.checkNotNull(mp, "meta parameter");
		String[][] rawTestData = generateRawTestData(mp);
		return transformer.transform(mp, rawTestData);
	}

}
