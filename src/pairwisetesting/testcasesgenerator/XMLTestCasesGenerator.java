package pairwisetesting.testcasesgenerator;

import nu.xom.Document;
import nu.xom.Element;
import pairwisetesting.coredomain.Factor;
import pairwisetesting.coredomain.ITestCasesGenerator;
import pairwisetesting.coredomain.MetaParameter;

/**
 * The generator generates test cases with XML format.
 */
public class XMLTestCasesGenerator implements ITestCasesGenerator {

	/**
	 * Returns test cases with XML format.
	 * 
	 * @see
	 * pairwisetesting.coredomain.ITestCasesGenerator#generate(pairwisetesting
	 * .coredomain.MetaParameter, java.lang.String[][])
	 */
	public String generate(MetaParameter mp, String[][] testData) {

		Element root = new Element("testcases");

		// Factors
		for (Factor factor : mp.getFactors()) {
			Element factorElement = new Element("factor");
			factorElement.appendChild(factor.getName());
			root.appendChild(factorElement);
		}

		// Runs
		for (String[] row : testData) {
			Element runElement = new Element("run");
			// Levels
			for (String level : row) {
				Element levelElement = new Element("level");
				levelElement.appendChild(level);
				runElement.appendChild(levelElement);
			}
			root.appendChild(runElement);
		}

		Document doc = new Document(root);
		return doc.toXML();
	}

}
