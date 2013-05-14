package pairwisetesting.complex;

/**
 * This class acts as the helper to do XML manipulations for method under test.
 * 
 * @see XStreamMethodUnderTestXMLHelper
 */
public interface IMethodUnderTestXMLHelper {

	/**
	 * Returns the XML representation of the specified method under test.
	 * 
	 * @param m
	 *            the specified method under test
	 * @return the XML representation of {@code m}
	 * @throws NullPointerException
	 *             if {@code m} is null
	 */
	String toXML(MethodUnderTest m);

	/**
	 * Returns a method under test with the specified XML data
	 * 
	 * @param xml
	 *            the specified XML data
	 * @return a method under test with the specified XML data
	 * @throws NullPointerException
	 *             if {@code xml} is null
	 */
	MethodUnderTest fromXML(String xml);

	/**
	 * Returns the instances of the method under test's parameters and return
	 * value.
	 * 
	 * @param methodUnderTestXmlData
	 *            the specified XML representation of the method under test
	 * @param values
	 *            the string representations of the values assigned to simple
	 *            type, both parameters and return value
	 * @return the instances of the method under test's parameters and return
	 *         value
	 * @throws NullPointerException
	 *             if {@code methodUnderTestXmlData} or {@code values} is null
	 */
	Object[] assign(String methodUnderTestXmlData, String[] values);

}
