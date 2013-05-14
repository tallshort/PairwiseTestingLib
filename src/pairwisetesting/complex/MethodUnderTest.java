package pairwisetesting.complex;

import java.util.ArrayList;

import com.google.common.base.Preconditions;

/**
 * This class encapsulates the method under test.
 * 
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong>
 */
public class MethodUnderTest {

	private ArrayList<Parameter> paramlist = new ArrayList<Parameter>();
	private Parameter returnValueParameter;
	private String name = "";

	/**
	 * Constructs an empty method under test.
	 */
	public MethodUnderTest() {
	}

	/**
	 * Constructs a method under test with the specified return type and method
	 * name.
	 * 
	 * @param returnType
	 *            the specified return type
	 * @param name
	 *            the specified method name
	 * @throws NullPointerException
	 *             if {@code returnType} or {@code name} is null
	 */
	public MethodUnderTest(String returnType, String name) {
		Preconditions.checkNotNull(returnType, "return type");
		this.returnValueParameter = new SimpleParameter(returnType, 
				"ReturnValue");
		setName(name);
	}
	
	/**
	 * Returns the method name.
	 * 
	 * @return the method name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the method name.
	 * 
	 * @param name the specified method name
	 * @throws NullPointerException
	 *             if {@code name} is null
	 */
	public void setName(String name) {
		Preconditions.checkNotNull(name, "method name");
		this.name = name;
	}

	/**
	 * Add an input parameter to the method.
	 * 
	 * @param p the specified input parameter
	 * @throws NullPointerException
	 *             if {@code p} is null
	 */
	public void add(Parameter p) {
		Preconditions.checkNotNull(p, "input parameter");
		paramlist.add(p);
	}

	/**
	 * Returns an array of the method's input parameters.
	 * 
	 * @return an array of the method's input parameters
	 */
	public Parameter[] getParameters() {
		return paramlist.toArray(new Parameter[0]);
	}
	
	/**
	 * Returns the method's return value parameter.
	 * 
	 * @return the method's return value parameter
	 */
	public Parameter getReturnValueParameter() {
		return returnValueParameter;
	}

	/**
	 * Sets the method's return value parameter.
	 * 
	 * @param returnValueParameter
	 *            the method's return value parameter
	 * @throws NullPointerException
	 *             if {@code returnValueParameter} is null
	 */
	public void setReturnValueRarameter(Parameter returnValueParameter) {
		Preconditions.checkNotNull(returnValueParameter, 
				"return value parameter");
		this.returnValueParameter = returnValueParameter;
	}
	
	/**
	 * Returns the method's return type.
	 * 
	 * @return the method's return type
	 */
	public String getReturnType() {
		return this.returnValueParameter.getType();
	}
	
	/**
	 * Accepts a specified parameter visitor to visit the method's input 
	 * parameters.
	 * 
	 * @param pv
	 *            the specified parameter visitor
	 * @throws NullPointerException
	 *             if {@code pv} is null
	 */
	public void accept(IParameterVisitor pv) {
		Preconditions.checkNotNull(pv, "parameter visitor");
		for (Parameter p : this.paramlist) {
			p.accept(pv);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((paramlist == null) ? 0 : paramlist.hashCode());
		result = prime * result
				+ ((returnValueParameter == null)
						? 0 : returnValueParameter.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MethodUnderTest other = (MethodUnderTest) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (paramlist == null) {
			if (other.paramlist != null)
				return false;
		} else if (!paramlist.equals(other.paramlist))
			return false;
		if (returnValueParameter == null) {
			if (other.returnValueParameter != null)
				return false;
		} else if (!returnValueParameter.equals(other.returnValueParameter))
			return false;
		return true;
	}
	
}
