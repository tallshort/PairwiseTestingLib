package pairwisetesting.engine.am;

import com.google.common.base.Preconditions;

import pairwisetesting.coredomain.Factor;
import pairwisetesting.coredomain.ITestDataTransformer;
import pairwisetesting.coredomain.MetaParameter;

/**
 * The test data transformer used for {@link AMEngine}.
 * It handles the missing values by fill the valid levels in turn.
 *
 * @see AMEngine
 */
public class OATestDataTransformer implements ITestDataTransformer {

	/* (non-Javadoc)
	 * @see pairwisetesting.coredomain.ITestDataTransformer#transform(pairwisetesting.coredomain.MetaParameter, java.lang.String[][])
	 */
	public String[][] transform(MetaParameter mp, String[][] rawTestData) {
		Preconditions.checkNotNull(mp, "meta parameter");
		Preconditions.checkNotNull(rawTestData, "raw test data");
		String[][] testData = rawTestData;
		for (int columnIndex = 0; columnIndex < rawTestData[0].length; columnIndex++) {
			
			String factorName = mp.getFactorNames()[columnIndex];
			Factor factor = mp.getFactor(factorName);
			
			// start index of the level to fill the missing value
			int nextIndexForEmpty = 0;
			
			for (int rowIndex = 0; rowIndex < rawTestData.length; rowIndex++) {
				int index = Integer.parseInt(rawTestData[rowIndex][columnIndex]) - 1;
				if (index < factor.getNumOfLevels()) {
					testData[rowIndex][columnIndex] = factor.getLevel(index);
				} else {
					testData[rowIndex][columnIndex] = factor.getLevel(nextIndexForEmpty);
					// next index of the level to fill the missing value
					nextIndexForEmpty = (nextIndexForEmpty + 1) % factor.getNumOfLevels();
				}					
			}
		}
		return testData;
	}
}
