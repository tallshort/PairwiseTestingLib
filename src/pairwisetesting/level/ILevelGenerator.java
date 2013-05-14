package pairwisetesting.level;

/**
 * This interface acts as the generator to generate levels for some factor.
 *
 * @see EP_BVA_IntegerLevelGenerator
 * @see EnumLevelGenerator
 * @see BooleanLevelGenerator
 * @see AbstractTypeLevelGenerator
 */
public interface ILevelGenerator {

	/**
	 * Returns the generated levels
	 * 
	 * @return the generated levels
	 */
	String[] generateLevels();

}
