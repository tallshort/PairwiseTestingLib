package pairwisetesting.coredomain;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.common.base.Preconditions;

/**
 * This class encapsulates factor's name and its levels.
 * 
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong>
 */
public class Factor implements Serializable {

	private static final long serialVersionUID = -4146558040253604753L;

	private String name;
	private ArrayList<String> levelList = new ArrayList<String>();

	/**
	 * Constructs a factor with the specified name.
	 * 
	 * @param name
	 *            the factor's name
	 * @throws NullPointerException
	 *             if {@code name} is null
	 */
	public Factor(String name) {
		setName(name);
	}

	/**
	 * Constructs a factor with the name {@literal <unknown>}.
	 */
	public Factor() {
		this("<unknown>");
	}

	/**
	 * Constructs a factor with the specified name and levels.
	 * 
	 * @param name
	 *            the factor's name
	 * @param levels
	 *            the factor's levels
	 * @throws NullPointerException
	 *             if {@code name} or {@code levels} is null
	 */
	public Factor(String name, String[] levels) {
		this(name);
		Preconditions.checkNotNull(levels, "levels");
		for (String level : levels) {
			levelList.add(level);
		}
	}

	/**
	 * Returns the factor's name.
	 * 
	 * @return the factor's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the factor's name.
	 * 
	 * @param name
	 *            the factor's name
	 * @throws NullPointerException
	 *             if {@code name} is null
	 */
	public void setName(String name) {
		Preconditions.checkNotNull(name, "name");
		this.name = name;
	}

	/**
	 * Adds a new level to the factor.
	 * 
	 * @param level
	 *            a new level for the factor
	 * @throws NullPointerException
	 *             if {@code level} is null
	 */
	public void addLevel(String level) {
		Preconditions.checkNotNull(level, "level");
		levelList.add(level);
	}

	/**
	 * Returns the level at the specified position in the level list.
	 * 
	 * @param index
	 *            index of the level to return
	 * @return the level at the specified position in the level list
	 * @throws IndexOutOfBoundsException
	 *             if {@code index} is out of the bounds of the level list
	 */
	public String getLevel(int index) {
		return levelList.get(index);
	}

	/**
	 * Returns the number of levels.
	 * 
	 * @return the number of levels
	 */
	public int getNumOfLevels() {
		return levelList.size();
	}

	/**
	 * Returns an array containing all of the levels in the level list.
	 * 
	 * @return an array containing all of the levels in the level list
	 */
	public String[] getLevels() {
		return levelList.toArray(new String[0]);
	}

	/**
	 * Returns an array list containing all of the levels in the level list.
	 * 
	 * @return an array list containing all of the levels in the level list
	 */
	public ArrayList<String> getLevelList() {
		return levelList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((levelList == null) ? 0 : levelList.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Factor other = (Factor) obj;
		if (levelList == null) {
			if (other.levelList != null)
				return false;
		} else if (!levelList.equals(other.levelList))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		res.append("Name: " + this.getName()).append(" ");
		res.append("Levels: " + this.levelList);
		return res.toString();
	}

}
