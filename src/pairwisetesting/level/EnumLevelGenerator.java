package pairwisetesting.level;

import java.util.ArrayList;

import com.google.common.base.Preconditions;

/**
 * The generator generates levels for factor that contains enum values.
 */
public class EnumLevelGenerator implements ILevelGenerator {
	private Class<? extends Enum<?>> enumType;

	/**
	 * Constructs a generator with the specified enum type.
	 * 
	 * @param enumType
	 *            the specified enum type
	 * @throws NullPointerException
	 *             if {@code enumType} is null
	 */
	public EnumLevelGenerator(Class<? extends Enum<?>> enumType) {
		Preconditions.checkNotNull(enumType, "enumType");
		this.enumType = enumType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pairwisetesting.level.ILevelGenerator#generateLevels()
	 */
	public String[] generateLevels() {
		Enum<?>[] enumConstants = enumType.getEnumConstants();
		ArrayList<String> levels = new ArrayList<String>();
		for (Enum<?> e : enumConstants) {
			levels.add(e.name());
		}
		return levels.toArray(new String[0]);
	}

}
