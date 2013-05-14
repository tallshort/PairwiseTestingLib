package pairwisetesting.metaparameterparser;

import com.google.common.base.Preconditions;

import nu.xom.Document;
import nu.xom.Element;
import pairwisetesting.coredomain.Factor;
import pairwisetesting.coredomain.MetaParameter;

/**
 * This class serialises meta parameter to XML.
 * 
 * @see MetaParameter
 */
public class MetaParameterXMLSerializer {

	/**
	 * Returns the XML data of the specified meta parameter.
	 * 
	 * @param mp
	 *            the specified meta parameter
	 * @return the XML data of the specified meta parameter
	 * @throws NullPointerException
	 *             if {@code mp} is null
	 */
	public String serialize(MetaParameter mp) {
		Preconditions.checkNotNull(mp, "meta parameter");
		Element root = new Element("metaparameter");

		// Strength
		Element strengthElement = new Element("strength");
		strengthElement.appendChild("" + mp.getStrength());
		root.appendChild(strengthElement);

		// Factors
		for (Factor factor : mp.getFactors()) {

			Element factorElement = new Element("factor");

			// Name
			Element nameElement = new Element("name");
			nameElement.appendChild(factor.getName());
			factorElement.appendChild(nameElement);

			// Levels
			for (String level : factor.getLevels()) {
				Element levelElement = new Element("level");
				levelElement.appendChild(level);
				factorElement.appendChild(levelElement);
			}

			root.appendChild(factorElement);
		}

		// Constraints
		for (String constraint : mp.getConstraints()) {
			Element constraintElement = new Element("constraint");
			constraintElement.appendChild(constraint);
			root.appendChild(constraintElement);
		}

		Document doc = new Document(root);
		return doc.toXML();
	}
}
