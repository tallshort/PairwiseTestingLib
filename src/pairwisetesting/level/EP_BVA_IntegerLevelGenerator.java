package pairwisetesting.level;

import com.google.common.base.Preconditions;

/**
 * The generator generates levels for factor that contains integer values based
 * on Equivalence Partitioning and Boundary Value Analysis techniques.
 */
public class EP_BVA_IntegerLevelGenerator implements ILevelGenerator {

	private long rangeStart;
	private long rangeEnd;

	/**
	 * Constructs a generator with the specified range start and range end.
	 * 
	 * @param rangeStart
	 *            the start of the range
	 * @param rangeEnd
	 *            the end of the range
	 * @throws IllegalArgumentException
	 *             if {@code rangeStart > rangeEnd}
	 */
	public EP_BVA_IntegerLevelGenerator(long rangeStart, long rangeEnd) {
		Preconditions.checkArgument(rangeStart <= rangeEnd, 
				"The range start should not larger than the range end.");
		this.rangeStart = rangeStart;
		this.rangeEnd = rangeEnd;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pairwisetesting.level.ILevelGenerator#generateLevels()
	 */
	public String[] generateLevels() {
		if (rangeStart != rangeEnd) {
			return new String[] { "" + (rangeStart - (rangeEnd - rangeStart)),
					"" + (rangeStart - 1), 
					"" + rangeStart, 
					"" + (rangeStart + 1),
					"" + ((rangeStart + rangeEnd) / 2), 
					"" + (rangeEnd - 1),
					"" + rangeEnd, 
					"" + (rangeEnd + 1),
					"" + (rangeEnd + (rangeEnd - rangeStart))};
		} else {
			return new String[] { "" + 0,
					"" + (rangeStart - 1), 
					"" + rangeStart, 
					"" + (rangeStart + 1),
					"" + (rangeStart + rangeStart)};
		}
	}

}
