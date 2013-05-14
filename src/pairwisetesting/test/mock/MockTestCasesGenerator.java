package pairwisetesting.test.mock;

import pairwisetesting.coredomain.ITestCasesGenerator;
import pairwisetesting.coredomain.MetaParameter;

public class MockTestCasesGenerator implements ITestCasesGenerator {

	public String generate(MetaParameter mp, String[][] testData) {
		StringBuilder res = new StringBuilder();
		for (String factorName : mp.getFactorNames()) {
			res.append(factorName).append("\t");
		}
		res.append("\n");
		for (String[] row : testData) {
			for (String value : row) {
				res.append(value).append("\t");
			}
			res.append("\n");
		}
		return res.toString();
	}

}
