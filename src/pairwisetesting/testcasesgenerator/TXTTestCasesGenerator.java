package pairwisetesting.testcasesgenerator;

import pairwisetesting.coredomain.ITestCasesGenerator;
import pairwisetesting.coredomain.MetaParameter;

import com.google.common.base.Join;

/**
 * The generator generates test cases with plain text format.
 */
public class TXTTestCasesGenerator implements ITestCasesGenerator {

	/**
	 * Returns test cases with plain text format.
	 * 
	 * @see
	 * pairwisetesting.coredomain.ITestCasesGenerator#generate(pairwisetesting
	 * .coredomain.MetaParameter, java.lang.String[][])
	 */
	public String generate(MetaParameter mp, String[][] testData) {
		StringBuilder res = new StringBuilder();
		res.append(Join.join("\t", mp.getFactorNames()));
		res.append("\n");
		for (String[] row : testData) {
			res.append(Join.join("\t", row));
			res.append("\n");
		}
		return res.toString();
	}

}
