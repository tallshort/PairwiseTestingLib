package pairwisetesting.coredomain;

/**
 * This interface acts as the generator of test cases (without expected result)
 * for Pairwise Testing.
 * 
 * @see pairwisetesting.testcasesgenerator.XMLTestCasesGenerator
 * @see pairwisetesting.testcasesgenerator.TXTTestCasesGenerator
 * @see pairwisetesting.testcasesgenerator.ExcelTestCasesGenerator
 */
public interface ITestCasesGenerator {
	
	/**
	 * Generates the test cases (without expected result) based on the meta 
	 * parameter and test data.
	 * 
	 * @param mp
	 *            the meta parameter
	 * @param testData
	 *            the test data generated by engine
	 * @return test cases (without expected result)
	 * @throws NullPointerException
	 *             if {@code mp} or {@code testData} is null
	 */
	String generate(MetaParameter mp, String[][] testData);

}
