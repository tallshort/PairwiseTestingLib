package pairwisetesting.complex;

/**
 * The default implementation for {@code IParameterVisitor}.
 */
public class DefaultParameterVisitor implements IParameterVisitor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pairwisetesting.complex.IParameterVisitor#visit(pairwisetesting.complex
	 * .SimpleParameter)
	 */
	public void visit(SimpleParameter p) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pairwisetesting.complex.IParameterVisitor#visit(pairwisetesting.complex
	 * .ComplexParameter)
	 */
	public void visit(ComplexParameter p) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pairwisetesting.complex.IParameterVisitor#endVisit(pairwisetesting.complex
	 * .SimpleParameter)
	 */
	public void endVisit(SimpleParameter p) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pairwisetesting.complex.IParameterVisitor#endVisit(pairwisetesting.complex
	 * .ComplexParameter)
	 */
	public void endVisit(ComplexParameter p) {
	}

}
