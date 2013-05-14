package pairwisetesting.coredomain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.google.common.base.Preconditions;

/**
 * This class encapsulates the meta data used for Pairwise Testing, including
 * <ul>
 * <li>factors and their levels
 * <li>strength
 * <li>constraints
 * </ul>
 * 
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong>
 * 
 * @see Factor
 */
public class MetaParameter implements Serializable {

	private static final long serialVersionUID = 2799432989700537225L;

	private int strength;

	/**
	 * The usage of LinkedHashMap is to maintain the insertion order so that the
	 * factors' name row order matches the level row order.
	 */
	private LinkedHashMap<String, Factor> factorMap
										= new LinkedHashMap<String, Factor>();

	private ArrayList<String> constraints = new ArrayList<String>();

	/**
	 * Constructs a meta parameter with the specified strength.
	 * 
	 * @param strength
	 *            the meta parameter's strength
	 * @throws IllegalArgumentException
	 *             if {@code strength < 2}
	 */
	public MetaParameter(int strength) {
		setStrength(strength);
	}

	/**
	 * Constructs a meta parameter with strength = 2. 
	 * Strength = 2 is used for most engines.
	 */
	public MetaParameter() {
		setStrength(2);
	}

	/**
	 * Sets meta parameter's strength.
	 * 
	 * @param strength
	 *            the meta parameter's strength
	 * @throws IllegalArgumentException
	 *             if {@code strength < 2}
	 */
	public void setStrength(int strength) {
		Preconditions.checkArgument(strength >= 2, "The strength should >= 2.");
		this.strength = strength;
	}

	/**
	 * Returns the meta parameter's strength.
	 * 
	 * @return the meta parameter's strength
	 */
	public int getStrength() {
		return strength;
	}

	/**
	 * Adds a new factor to the meta parameter.
	 * 
	 * @param factor
	 *            a new factor for the meta parameter
	 * @throws NullPointerException
	 *             if {@code factor} is null
	 */
	public void addFactor(Factor factor) {
		Preconditions.checkNotNull(factor, "factor");
		factorMap.put(factor.getName(), factor);
	}

	/**
	 * Returns the factor with the specified factor's name.
	 * 
	 * @param factorName
	 *            the factor's name
	 * @return the factor with the specified factor's name
	 * @throws NullPointerException
	 *             if {@code factorName} is null
	 */
	public Factor getFactor(String factorName) {
		Preconditions.checkNotNull(factorName, "factor name");
		return factorMap.get(factorName);
	}

	/**
	 * Returns an array containing all the factors.
	 * 
	 * @return an array containing all the factors
	 */
	public Factor[] getFactors() {
		return factorMap.values().toArray(new Factor[0]);
	}

	/**
	 * Returns an array containing all the factors' names.
	 * 
	 * @return an array containing all the factors' names
	 */
	public String[] getFactorNames() {
		return factorMap.keySet().toArray(new String[0]);
	}

	/**
	 * Returns the level with the specified factor's name and the specified
	 * position in the factor's level list.
	 * 
	 * @param factorName
	 *            the factor's name
	 * @param index
	 *            the specified position in the factor's level list
	 * @return the level with the specified factor's name and the specified
	 *         position in the factor's level list
	 * @throws NullPointerException
	 *             if {@code factorName} is null
	 * @throws IndexOutOfBoundsException
	 *             if {@code index} is out of the bounds of the factor's level
	 *             list
	 */
	public String getLevelOfFactor(String factorName, int index) {
		return getFactor(factorName).getLevel(index);
	}

	/**
	 * Returns the number of factors.
	 * 
	 * @return the number of factors
	 */
	public int getNumOfFactors() {
		return factorMap.values().size();
	}

	/**
	 * Returns the max number of levels within all the factors.
	 * 
	 * @return the max number of levels within all the factors
	 */
	public int getMaxNumOfLevels() {
		int max = 0;
		Factor[] factors = this.getFactors();
		for (Factor factor : factors) {
			if (max < factor.getNumOfLevels())
				max = factor.getNumOfLevels();
		}
		return max;
	}

	/**
	 * Adds a new constraint to the meta parameter.
	 * 
	 * @param constraint
	 *            a new constraint for the meta parameter
	 * @throws NullPointerException
	 *             if {@code constraint} is null
	 */
	public void addConstraint(String constraint) {
		Preconditions.checkNotNull(constraint, "constraint");
		this.constraints.add(constraint);
	}

	/**
	 * Returns an array containing all the constraints.
	 * 
	 * @return an array containing all the constraints
	 */
	public String[] getConstraints() {
		return this.constraints.toArray(new String[0]);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((constraints == null) ? 0 : constraints.hashCode());
		result = prime * result
				+ ((factorMap == null) ? 0 : factorMap.hashCode());
		result = prime * result + strength;
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
		MetaParameter other = (MetaParameter) obj;
		if (constraints == null) {
			if (other.constraints != null)
				return false;
		} else if (!constraints.equals(other.constraints))
			return false;
		if (factorMap == null) {
			if (other.factorMap != null)
				return false;
		} else if (!factorMap.equals(other.factorMap))
			return false;
		if (strength != other.strength)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		res.append("Strength: " + this.getStrength()).append("\n");
		res.append("Factors: " + this.factorMap.values()).append("\n");
		res.append("Constraints: " + this.constraints);
		return res.toString();
	}
}
