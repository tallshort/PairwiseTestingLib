package pairwisetesting.complex.parametervisitor;

import pairwisetesting.complex.ComplexParameter;
import pairwisetesting.complex.DefaultParameterVisitor;
import pairwisetesting.complex.SimpleParameter;

/**
 * This class acts as the visitor to print the information of parameters.
 */
public class PrintParameterVisitor extends DefaultParameterVisitor {

	public void visit(SimpleParameter p) {
		for (int i = 0; i < p.getDepth(); i++) {
			System.out.print("\t");
		}
		System.out.println(p);
	}
	
	public void visit(ComplexParameter p) {
		for (int i = 0; i < p.getDepth(); i++) {
			System.out.print("\t");
		}
		System.out.println(p);
	}

}
