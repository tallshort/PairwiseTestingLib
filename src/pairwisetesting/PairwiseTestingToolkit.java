package pairwisetesting;

import com.google.common.base.Preconditions;

import pairwisetesting.coredomain.Engine;
import pairwisetesting.coredomain.EngineException;
import pairwisetesting.coredomain.IMetaParameterProvider;
import pairwisetesting.coredomain.ITestCasesGenerator;
import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.coredomain.MetaParameterException;

/**
 * The facade for core domain logic of Pairwise Testing
 * 
 * @see pairwisetesting.coredomain.IMetaParameterProvider
 * @see pairwisetesting.coredomain.Engine
 * @see pairwisetesting.coredomain.ITestCasesGenerator
 */
public class PairwiseTestingToolkit {
	
	private IMetaParameterProvider provider;
	private Engine engine;
	private ITestCasesGenerator generator;

	/**
	 * Sets the meta parameter provider.
	 * 
	 * @param provider
	 *            the specified meta parameter provider
	 * @throws NullPointerException
	 *             if the specified meta parameter provider is null
	 */
	public void setMetaParameterProvider(IMetaParameterProvider provider) {
		Preconditions.checkNotNull(provider, "meta parameter provider");
		this.provider = provider;
	}

	/**
	 * Sets the engine used to generate test data.
	 * 
	 * @param engine
	 *            the specified engine used to generate test data
	 * @throws NullPointerException
	 *             if the specified engine is null
	 */
	public void setEngine(Engine engine) {
		Preconditions.checkNotNull(engine, "engine");
		this.engine = engine;
	}

	/**
	 * Sets the test cases generator.
	 * 
	 * @param generator
	 *            the specified test cases generator
	 * @throws NullPointerException
	 *             if the specified test cases generator is null
	 */
	public void setTestCasesGenerator(ITestCasesGenerator generator) {
		Preconditions.checkNotNull(generator, "test cases generator");
		this.generator = generator;
	}

	/**
	 * Generates the test data.
	 * 
	 * @return the test data
	 * @throws MetaParameterException
	 *             if the provider has problems in providing the meta parameter
	 * @throws EngineException
	 *             if the engine can not handle the meta parameter
	 */
	public String[][] generateTestData() throws MetaParameterException,
			EngineException {
		MetaParameter mp = provider.get();
		return engine.generateTestData(mp);
	}

	/**
	 * Generates the test cases (without expected result).
	 * 
	 * @return the test cases (without expected result)
	 * @throws MetaParameterException
	 *             if the provider has problems in providing the meta parameter
	 * @throws EngineException
	 *             if the engine can not handle the meta parameter
	 */	
	public String generateTestCases() throws MetaParameterException,
			EngineException {
		MetaParameter mp = provider.get();
		String[][] testData = engine.generateTestData(mp);
		return generator.generate(mp, testData);
	}

}
