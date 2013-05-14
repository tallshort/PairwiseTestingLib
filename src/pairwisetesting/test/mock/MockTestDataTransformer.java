package pairwisetesting.test.mock;

import pairwisetesting.coredomain.ITestDataTransformer;
import pairwisetesting.coredomain.MetaParameter;

public class MockTestDataTransformer implements ITestDataTransformer{

	public String[][] transform(MetaParameter mp, String[][] rawTestData) {
		String[][] testData = rawTestData;
		for (int columnIndex = 0; columnIndex < rawTestData[0].length; columnIndex++) {
			String factorName = mp.getFactorNames()[columnIndex];
			for (int rowIndex = 0; rowIndex < rawTestData.length; rowIndex++) {
				testData[rowIndex][columnIndex] = mp.getLevelOfFactor(factorName, Integer.parseInt(rawTestData[rowIndex][columnIndex]) - 1);
			}
		}
		
		return testData;
	}

}
