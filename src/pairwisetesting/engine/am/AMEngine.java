package pairwisetesting.engine.am;

import com.google.common.base.Preconditions;

import pairwisetesting.coredomain.Engine;
import pairwisetesting.coredomain.EngineException;
import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.engine.am.oaprovider.CascadeOAProviderFactory;

/**
 * The engine implementation based on algebra methods. Current implementation
 * only supports Orthogonal Arrays (OA).
 * 
 * @see OATestDataTransformer
 * @see IOAProviderFactory
 * @see OAProvider
 */
public class AMEngine extends Engine {

	/**
	 * The factory used to create OA provider.
	 */
	private IOAProviderFactory factory;

	/**
	 * Constructs an AM engine using an OAProviderFactory as factory.
	 */
	public AMEngine() {
		this(new CascadeOAProviderFactory());
	}

	/**
	 * Constructs an AM engine using the specified OA provider factory.
	 * 
	 * @param factory
	 *            the specified OA provider factory
	 * @throws NullPointerException
	 *             if {@code factory} is null
	 */
	public AMEngine(IOAProviderFactory factory) {
		Preconditions.checkNotNull(factory, "OA provider factory");
		this.transformer = new OATestDataTransformer();
		this.factory = factory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pairwisetesting.coredomain.Engine#generateRawTestData(pairwisetesting
	 * .coredomain.MetaParameter)
	 */
	@Override
	protected String[][] generateRawTestData(MetaParameter mp)
			throws EngineException {
		int numOfLevels = mp.getMaxNumOfLevels();
		OAProvider provider = factory.create(numOfLevels, mp.getNumOfFactors());
		int[][] rawTestData = provider.get(mp.getNumOfFactors());
		return int2DArrayToString2DArray(rawTestData);
	}

	/**
	 * Transform int 2D array to string 2D array
	 * 
	 * @param int2DArray the specified int 2D array
	 * @return string 2D array
	 */
	private String[][] int2DArrayToString2DArray(int[][] int2DArray) {
		String[][] res = new String[int2DArray.length][int2DArray[0].length];
		for (int column = 0; column < int2DArray[0].length; column++) {
			for (int row = 0; row < int2DArray.length; row++) {
				res[row][column] = "" + int2DArray[row][column];
			}
		}
		return res;
	}

}
