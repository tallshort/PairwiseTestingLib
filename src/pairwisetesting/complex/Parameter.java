package pairwisetesting.complex;

import com.google.common.base.Preconditions;

/**
 * This class encapsulates the parameter related information of some method.
 * 
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong>
 * 
 * @see SimpleParameter
 * @see ComplexParameter
 */
public abstract class Parameter {

	private String type = "";
	private String name = "";
	private String fullNamePrefix = "";
	private int depth;

	/**
	 * Constructs a parameter with the specified type and name.
	 * 
	 * @param type
	 *            the specified type
	 * @param name
	 *            the specified name
	 * @throws NullPointerException
	 *             if {@code type} or {@code name} is null
	 */
	public Parameter(String type, String name) {
		setType(type);
		setName(name);
	}

	/**
	 * Returns the type of the parameter.
	 * 
	 * @return the type of the parameter
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type of the parameter.
	 * 
	 * @param type
	 *            the type of the parameter
	 * @throws NullPointerException
	 *             if {@code type} is null
	 */
	public void setType(String type) {
		Preconditions.checkNotNull(type, "type");
		this.type = type;
	}

	/**
	 * Returns the name of the parameter.
	 * 
	 * @return the name of the parameter
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the parameter.
	 * 
	 * @param name
	 *            the name of the parameter
	 * @throws NullPointerException
	 *             if {@code name} is null
	 */
	public void setName(String name) {
		Preconditions.checkNotNull(name, "name");
		this.name = name;
	}

	/**
	 * Returns the depth of the parameter in the parameter tree.
	 * 
	 * @return the depth of the parameter in the parameter tree
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * Sets the depth of the parameter in the parameter tree.
	 * 
	 * @param depth
	 *            the depth of the parameter in the parameter tree
	 * @throws IllegalArgumentException
	 *             if {@code depth < 0}
	 */
	public void setDepth(int depth) {
		Preconditions.checkArgument(depth >= 0, "The depth should >= 0.");
		this.depth = depth;
	}

	/**
	 * Returns the full name of the parameter.
	 * 
	 * @return the full name of the parameter
	 */
	public String getFullName() {
		return this.fullNamePrefix + this.name;
	}

	/**
	 * Add a prefix to the parameter.
	 * 
	 * @param prefix
	 *            a prefix to the parameter
	 * @throws NullPointerException
	 *             if {@code prefix} is null
	 */
	public void addFullNamePrefix(String prefix) {
		Preconditions.checkNotNull(prefix, "prefix");
		this.fullNamePrefix = prefix + "." + this.fullNamePrefix;
	}

	@Override
	public String toString() {
		return String.format("[%s %s]", type, getFullName());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + depth;
		result = prime * result
				+ ((fullNamePrefix == null) ? 0 : fullNamePrefix.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Parameter other = (Parameter) obj;
		if (depth != other.depth)
			return false;
		if (fullNamePrefix == null) {
			if (other.fullNamePrefix != null)
				return false;
		} else if (!fullNamePrefix.equals(other.fullNamePrefix))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	/**
	 * Accepts a specified parameter visitor to visit the parameter and its
	 * children if it has.
	 * 
	 * @param pv
	 *            the specified parameter visitor
	 * @throws NullPointerException
	 *             if {@code pv} is null
	 */
	public abstract void accept(IParameterVisitor pv);

	/**
	 * Returns <tt>true</tt> if it is a complex parameter.
	 * 
	 * @return <tt>true</tt> if it is a complex parameter
	 */
	public abstract boolean isComplex();

	/**
	 * Returns <tt>true</tt> if it is Interface or Abstract Class type.
	 * 
	 * @return <tt>true</tt> if it is Interface or Abstract Class type
	 */
	public abstract boolean isAbstract();

}
