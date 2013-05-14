package pairwisetesting.complex;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Preconditions;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * This class acts as the helper to do XML manipulations for method under test
 * with the help of XStream.
 */
public class XStreamMethodUnderTestXMLHelper implements
		IMethodUnderTestXMLHelper {

	private XStream xstream = new XStream(new DomDriver());

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pairwisetesting.complex.IMethodUnderTestXMLHelper#fromXML(java.lang.String
	 * )
	 */
	public MethodUnderTest fromXML(String xml) {
		Preconditions.checkNotNull(xml, "xml data");
		return (MethodUnderTest) xstream.fromXML(xml);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pairwisetesting.complex.IMethodUnderTestXMLHelper#toXML(pairwisetesting
	 * .complex.MethodUnderTest)
	 */
	public String toXML(MethodUnderTest m) {
		Preconditions.checkNotNull(m, "method under test");
		return xstream.toXML(m);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pairwisetesting.complex.IMethodUnderTestXMLHelper#assign(java.lang.String
	 * , java.lang.String[])
	 */
	public Object[] assign(String methodUnderTestXmlData, String[] values) {
		MethodUnderTest m = fromXML(methodUnderTestXmlData);
		ParameterAssignmentVisitor pv = new ParameterAssignmentVisitor(values);
		m.accept(pv);
		m.getReturnValueParameter().accept(pv);
		ArrayList<Object> objects = new ArrayList<Object>();
		// System.out.println(Arrays.toString(pv.getXMLParameters()));
		for (String xmlParameter : pv.getXMLParameters()) {
			objects.add(xstream.fromXML(xmlParameter));
		}
//		System.out.println(xstream.toXML(objects.get(0)));
//		System.out.println(xstream.toXML(objects.get(1)));
//		System.out.println(xstream.toXML(objects.get(2)));
		return objects.toArray();
	}
}

/**
 * This class acts as the visitor to do parameter assignments.
 */
class ParameterAssignmentVisitor implements IParameterVisitor {

	private String[] values;
	private int next;
	private ArrayList<String> xmlParameters = new ArrayList<String>();
	private StringBuilder xmlParameter = new StringBuilder();
	
	// Used to store the concrete type for endVisit usage
	private String currentConcreteType;

	/**
	 * Constructs a parameter assignment visitor with the specified values.
	 * 
	 * @param values the specified values
	 * @throws NullPointerException
	 *             if {@code values} is null
	 */
	ParameterAssignmentVisitor(String[] values) {
		Preconditions.checkNotNull(values, "values");
		this.values = values;
	}

	public void visit(SimpleParameter p) {
		beginTag(p);
		if (isSimpleArrayType(p)) {
			appendSimpleArrayValue(p);
		} else if (isSimpleSetOrListType(p)) {
			appendSimpleSetOrListValue(p);
		} else if (isSimpleMapType(p)) {
			appendSimpleMapValue(p);
		} else {
			xmlParameter.append(values[next++]);
		}
	}

	public void endVisit(SimpleParameter p) {
		endTag(p);
	}

	public void visit(ComplexParameter p) {
		beginTag(p);
	}

	public void endVisit(ComplexParameter p) {
		endTag(p);
	}

	/**
	 * Returns an array of the XML representations of the parameters.
	 * 
	 * @return an array of the XML representations of the parameters
	 */
	public String[] getXMLParameters() {
		return this.xmlParameters.toArray(new String[0]);
	}

	/**
	 * Builds the XML data of begin tag for the specified simple parameter.
	 * 
	 * @param p the specified simple parameter
	 */
	private void beginTag(SimpleParameter p) {
		if (p.getDepth() == 0) {
			xmlParameter = new StringBuilder();
			if (isSimpleArrayType(p)) {
				xmlParameter.append(
						"<" + getXStreamArrayType(p.getType()) + ">");
			} else if (isSimpleContainerType(p)) {
				xmlParameter.append(
						"<" + getXStreamContainerType(p.getType()) + ">");
			} else {
				xmlParameter.append("<" + p.getType() + ">");
			}
		} else {
			xmlParameter.append("<" + p.getName() + ">");
		}
	}

	/**
	 * Builds the XML data of begin tag for the specified complex parameter.
	 * 
	 * @param p the specified complex parameter
	 */
	private void beginTag(ComplexParameter p) {
		if (p.getDepth() == 0) {
			xmlParameter = new StringBuilder();
			if (p.isAbstract()) {
				this.currentConcreteType = this.values[next++];
				xmlParameter.append("<" + this.currentConcreteType + ">");
			} else {
				xmlParameter.append("<" + p.getType() + ">");
			}
		} else {
			xmlParameter.append("<" + p.getName() + ">");
		}
	}

	/**
	 * Builds the XML data of end tag for the specified parameter.
	 * 
	 * @param p the specified parameter
	 */
	private void endTag(Parameter p) {
		if (p.getDepth() == 0) {
			if (p.isAbstract()) {
				xmlParameter.append("</" + this.currentConcreteType + ">");
			} else {
				if (isSimpleArrayType(p)) {
					xmlParameter.append(
							"</" + getXStreamArrayType(p.getType()) + ">");
				} else if (isSimpleContainerType(p)) {
					xmlParameter.append(
							"</" + getXStreamContainerType(p.getType()) + ">");
				} else {
					xmlParameter.append("</" + p.getType() + ">");
				}
			}
			xmlParameters.add(xmlParameter.toString());
		} else {
			xmlParameter.append("</" + p.getName() + ">");
		}
	}
	
	/**
	 * Returns <tt>true</tt> if the specified parameter is a simple array type.
	 * Currently all array types are considered simple.
	 * 
	 * @param p
	 *            the specified parameter
	 * @return <tt>true</tt> if {@code p} is a simple array type
	 */
	private boolean isSimpleArrayType(Parameter p) {
		return p.getType().endsWith("[]");
	}
	
	/**
	 * Returns <tt>true</tt> if the specified parameter is a simple container
	 * type.
	 * Currently all container types are considered simple.
	 * 
	 * @param p
	 *            the specified parameter
	 * @return <tt>true</tt> if {@code p} is a simple container type
	 */
	private boolean isSimpleContainerType(Parameter p) {
		return (isSimpleSetOrListType(p) || isSimpleMapType(p));
	}
	
	/**
	 * Returns <tt>true</tt> if the specified parameter is a simple set or list
	 * type.
	 * Currently all set or list types are considered simple.
	 * 
	 * @param p
	 *            the specified parameter
	 * @return <tt>true</tt> if {@code p} is a simple set or list type
	 */
	private boolean isSimpleSetOrListType(Parameter p) {
		return (p.getType().startsWith("java.util.ArrayList")
				|| p.getType().startsWith("java.util.LinkedList")
				|| p.getType().startsWith("java.util.HashSet"));
	}
	
	/**
	 * Returns <tt>true</tt> if the specified parameter is a simple map
	 * type.
	 * Currently all map types are considered simple.
	 * 
	 * @param p
	 *            the specified parameter
	 * @return <tt>true</tt> if {@code p} is a simple map type
	 */
	private boolean isSimpleMapType(Parameter p) {
		return (p.getType().startsWith("java.util.HashMap"));
	}

	/**
	 * Returns the element type of the specified simple array type.
	 * 
	 * @param arrayType
	 *            the specified simple array type
	 * @return the element type of {@code arrayType}
	 */
	private String getSimpleArrayElementType(String arrayType) {
		return arrayType.replace("[]", "");
	}

	/**
	 * Returns the XStream array type of the specified simple array type.
	 * 
	 * @param arrayType
	 *            the specified simple array type
	 * @return the XStream array type of {@code arrayType}
	 */
	private String getXStreamArrayType(String arrayType) {
		return arrayType.replace("[]", "-array");
	}
	
	/**
	 * Returns the element type of the specified simple set or list type.
	 * 
	 * @param setOrListType
	 *            the specified simple set or list type
	 * @return the element type of {@code setOrListType}
	 */
	private String getSimpleSetOrListElementType(String setOrListType) {
		String regex = "<(.+)>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(setOrListType);
		String setOrListElementType = null;
		if (matcher.find()) {
			setOrListElementType = matcher.group(1).toLowerCase();
		} else {
			setOrListElementType = "object";
		}
		// System.out.println(setOrListType);
		return setOrListElementType;
	}
	
	/**
	 * Returns the map entry type of the specified simple map type.
	 * 
	 * @param mapType
	 *            the specified simple map type
	 * @return the map entry type of {@code mapType}
	 */
	private String[] getSimpleMapEntryType(String mapType) {
		String regex = "<(.+)\\s*,\\s*(.+)>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(mapType);
		String[] mapEntryType = new String[2];
		if (matcher.find()) {
			mapEntryType[0] = matcher.group(1).toLowerCase();
			mapEntryType[1] = matcher.group(2).toLowerCase();
		} else {
			mapEntryType[0] = "object";
			mapEntryType[1] = "object";
		}
		// System.out.println(mapType);
		return mapEntryType;
	}
	
	/**
	 * Returns the XStream container type of the specified simple container 
	 * type.
	 * 
	 * @param containerType
	 *            the specified simple container type
	 * @return the XStream container type of {@code containerType}
	 */
	private String getXStreamContainerType(String containerType) {
		if (containerType.startsWith("java.util.ArrayList")) {
			return "list";
		} else if (containerType.startsWith("java.util.HashMap")) {
			return "map";
		} else if (containerType.startsWith("java.util.LinkedList")) {
			return "linked-list";
		} else if (containerType.startsWith("java.util.HashSet")) {
			return "set";
		} else {
			return null;
		}
	}

	/**
	 * Returns the container elements of the specified simple container value.
	 * 
	 * @param simpleContainerValue
	 *            the specified simple container value
	 * @return the container elements of {@code simpleContainerValue}
	 */
	public String[] getContainerElements(String simpleContainerValue) {
		simpleContainerValue = simpleContainerValue.trim();
		if (simpleContainerValue.length() <= 2) { // [], {}, ()
			return new String[0];
		} else {
			String elements
				= simpleContainerValue.substring(1, 
						simpleContainerValue.length() - 1).trim();
			if (elements.equals("")) { // [  ], {  }, (  )
				return new String[0];
			} else {
				return elements.split("\\s*[,:]\\s*");
			}
		}
	}

	/**
	 * Appends the values of set or list elements.
	 * 
	 * @param setOrListElementType
	 *            the specified set or list element type
	 * @param elements
	 *            the specified set or list elements
	 */
	private void appendSetOrListElements(String setOrListElementType, 
			String[] elements) {
		for (String element : elements) {
			xmlParameter.append("<" + setOrListElementType + ">");
			xmlParameter.append(element);
			xmlParameter.append("</" + setOrListElementType + ">");
		}
	}
	
	/**
	 * Appends the values of map elements.
	 * 
	 * @param mapEntryType
	 *            the specified map entry type
	 * @param elements
	 *            the specified map elements
	 */
	private void appendMapElements(String[] mapEntryType, String[] elements) {
		for (int i = 0; i < elements.length; i += 2) {
			xmlParameter.append("<entry>");
			xmlParameter.append("<" + mapEntryType[0] + ">");
			xmlParameter.append(elements[i]);
			xmlParameter.append("</" + mapEntryType[0] + ">");
			xmlParameter.append("<" + mapEntryType[1] + ">");
			xmlParameter.append(elements[i+1]);
			xmlParameter.append("</" + mapEntryType[1] + ">");
			xmlParameter.append("</entry>");
		}
	}
	
	/**
	 * Appends the simple array value for the specified simple parameter.
	 * 
	 * @param p the specified simple parameter
	 */
	private void appendSimpleArrayValue(SimpleParameter p) {
		String elementType = getSimpleArrayElementType(p.getType());
		String[] elements = getContainerElements(values[next++]);
		// System.out.println(Arrays.toString(elements));
		appendSetOrListElements(elementType, elements);
	}
	
	/**
	 * Appends the simple set or list value for the specified simple parameter.
	 * 
	 * @param p the specified simple parameter
	 */
	private void appendSimpleSetOrListValue(SimpleParameter p) {
		String elementType = getSimpleSetOrListElementType(p.getType());
		String[] elements = getContainerElements(values[next++]);
		appendSetOrListElements(elementType, elements);
	}
	
	/**
	 * Appends the simple map value for the specified simple parameter.
	 * 
	 * @param p the specified simple parameter
	 */
	private void appendSimpleMapValue(SimpleParameter p) {
		String[] mapEntryType = getSimpleMapEntryType(p.getType());
		String[] elements = getContainerElements(values[next++]);
		appendMapElements(mapEntryType, elements);
	}

}
