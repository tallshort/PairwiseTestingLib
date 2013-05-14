package pairwisetesting.engine.jenny;

import pairwisetesting.coredomain.ITestDataTransformer;
import pairwisetesting.coredomain.MetaParameter;

/**
 * The test data transformer used for {@link JennyEngine}.
 *
 * @see JennyEngine
 */
public class JennyTestDataTransformer implements ITestDataTransformer {

	/* (non-Javadoc)
	 * @see pairwisetesting.coredomain.ITestDataTransformer#transform(pairwisetesting.coredomain.MetaParameter, java.lang.String[][])
	 */
	public String[][] transform(MetaParameter mp, String[][] rawTestData) {
		String[][] testData = rawTestData;
		for (int columnIndex = 0; columnIndex < rawTestData[0].length; columnIndex++) {
			String factorName = mp.getFactorNames()[columnIndex];
			for (int rowIndex = 0; rowIndex < rawTestData.length; rowIndex++) {
				String feature = rawTestData[rowIndex][columnIndex];
				int index = feature.charAt(feature.length() - 1) - 'a';
				testData[rowIndex][columnIndex] 
				                   = mp.getLevelOfFactor(factorName, index);
			}
		}
		return testData;
	}

}
