package pairwisetesting.level;

/**
 * The generator generates levels for factor that contains boolean values.
 */
public class BooleanLevelGenerator implements ILevelGenerator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see pairwisetesting.level.ILevelGenerator#generateLevels()
	 */
	public String[] generateLevels() {
		return new String[] { "true", "false" };
	}

}
