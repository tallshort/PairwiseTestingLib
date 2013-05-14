package pairwisetesting.complex;

import com.google.common.base.Preconditions;

/**
 * This class encapsulates the simple type parameter related information of some
 * method.
 * 
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong>
 */
public class SimpleParameter extends Parameter {

	/**
	 * Constructs a simple parameter with the specified type and name.
	 * 
	 * @param type
	 *            the specified type
	 * @param name
	 *            the specified name
	 * @throws NullPointerException
	 *             if {@code type} or {@code name} is null
	 */
	public SimpleParameter(String type, String name) {
		super(type, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seepairwisetesting.complex.Parameter#accept(pairwisetesting.complex.
	 * IParameterVisitor)
	 */
	@Override
	public void accept(IParameterVisitor pv) {
		Preconditions.checkNotNull(pv, "parameter visitor");
		pv.visit(this);
		pv.endVisit(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pairwisetesting.complex.Parameter#isComplex()
	 */
	@Override
	public boolean isComplex() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pairwisetesting.complex.Parameter#isAbstract()
	 */
	@Override
	public boolean isAbstract() {
		return false;
	}

}
