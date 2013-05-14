package pairwisetesting.complex.parametervisitor;

import pairwisetesting.complex.ComplexParameter;
import pairwisetesting.complex.DefaultParameterVisitor;
import pairwisetesting.complex.SimpleParameter;

/**
 * This class acts as the visitor to count the parameters.
 */
public class CountParameterVisitor extends DefaultParameterVisitor {

	private int branchCount = 0;
	private int leafCount = 0;
	
	public void visit(ComplexParameter p) {
		++branchCount;
	}
	
	public void visit(SimpleParameter p) {
		++leafCount;
	}

	/**
	 * Returns the count of nodes.
	 * 
	 * @return the count of nodes
	 */
	public int getNodeCount() {
		return branchCount + leafCount;
	}

	/**
	 * Returns the count of leaves.
	 * 
	 * @return the count of leaves
	 */
	public int getLeafCount() {
		return leafCount;
	}
	
}
