package pairwisetesting.complex;

import java.util.ArrayList;

import com.google.common.base.Preconditions;

import pairwisetesting.util.ClassUtil;

/**
 * This class encapsulates the complex type parameter related information of
 * some method.
 * 
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong>
 */
public class ComplexParameter extends Parameter {

	private ArrayList<Parameter> children = new ArrayList<Parameter>();

	/**
	 * Constructs a complex parameter with the specified type and name.
	 * 
	 * @param type
	 *            the specified type
	 * @param name
	 *            the specified name
	 * @throws NullPointerException
	 *             if {@code type} or {@code name} is null
	 */
	public ComplexParameter(String type, String name) {
		super(type, name);
		// Parameter[] parameters = new
		// ChildParametersExtractor().getParameters(type);
		// children.addAll(Arrays.asList(parameters));
	}

	/**
	 * Add a child parameter to the complex parameter.
	 * 
	 * @param child
	 *            the specified child parameter
	 * @throws NullPointerException
	 *             if {@code child} is null
	 */
	public void add(Parameter child) {
		Preconditions.checkNotNull(child, "child parameter");
		this.children.add(child);
		child.setDepth(this.getDepth() + 1);
		child.addFullNamePrefix(this.getFullName());
	}

	/**
	 * Sets the depth of the complex parameter in the parameter tree.
	 * 
	 * @param depth
	 *            the depth of the complex parameter in the parameter tree
	 * @throws IllegalArgumentException
	 *             if {@code depth < 0}
	 */
	public void setDepth(int newDepth) {
		super.setDepth(newDepth);
		for (Parameter child : children) {
			child.setDepth(this.getDepth() + 1);
		}
	}

	/**
	 * Add a prefix to the complex parameter and its children.
	 * 
	 * @param prefix
	 *            a prefix to the complex parameter and its children
	 * @throws NullPointerException
	 *             if {@code prefix} is null
	 */
	public void addFullNamePrefix(String prefix) {
		super.addFullNamePrefix(prefix);
		for (Parameter child : children) {
			child.addFullNamePrefix(prefix);
		}
	}

	/**
	 * Returns an array of the complex parameter's children.
	 * 
	 * @return an array of the complex parameter's children
	 */
	public Parameter[] getChildren() {
		return this.children.toArray(new Parameter[0]);
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
		for (Parameter child : children) {
			child.accept(pv);
		}
		pv.endVisit(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pairwisetesting.complex.Parameter#isComplex()
	 */
	@Override
	public boolean isComplex() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pairwisetesting.complex.Parameter#isAbstract()
	 */
	@Override
	public boolean isAbstract() {
		Class<?> clazz = ClassUtil.getClass(this.getType());
		return (ClassUtil.isInterface(clazz) 
				|| ClassUtil.isAbstractClass(clazz));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((children == null) ? 0 : children.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComplexParameter other = (ComplexParameter) obj;
		if (children == null) {
			if (other.children != null)
				return false;
		} else if (!children.equals(other.children))
			return false;
		return true;
	}

}
