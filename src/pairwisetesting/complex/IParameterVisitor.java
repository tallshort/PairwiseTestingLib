package pairwisetesting.complex;

/**
 * The class acts as the visitor to visit parameter.
 * 
 * @see DefaultParameterVisitor
 * @see pairwisetesting.complex.parametervisitor.CountParameterVisitor
 * @see pairwisetesting.complex.parametervisitor.PrintParameterVisitor
 */
public interface IParameterVisitor {

	/**
	 * Visits the specified simple parameter.
	 * 
	 * @param p the specified simple parameter
	 */
	public void visit(SimpleParameter p);

	/**
	 * Visits the specified complex parameter.
	 * 
	 * @param p the specified complex parameter
	 */
	public void visit(ComplexParameter p);
	
	/**
	 * Performs after visiting the specified simple parameter.
	 * 
	 * @param p the specified simple parameter
	 */
	public void endVisit(SimpleParameter p);
	
	/**
	 * Performs after visiting the specified complex parameter.
	 * 
	 * @param p the specified complex parameter
	 */
	public void endVisit(ComplexParameter p);

}