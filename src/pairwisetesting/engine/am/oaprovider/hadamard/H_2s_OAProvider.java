package pairwisetesting.engine.am.oaprovider.hadamard;

import com.google.common.base.Preconditions;

import pairwisetesting.engine.am.OAProvider;
import pairwisetesting.engine.am.oaprovider.util.MathUtil;

/**
 * The OA provider based on Hadamard Matrix and its runs is 2^s.
 * 
 * @see Matrix
 */
public class H_2s_OAProvider extends OAProvider {

	// 2-order Hadamard Matrix
	private static Matrix H2; 
	static {
		H2 = new Matrix(2, 2);
		H2.setElement(1, 1, 1);
		H2.setElement(1, 2, 1);
		H2.setElement(2, 1, 1);
		H2.setElement(2, 2, -1);
	}

	/**
	 * Constructs an OA provider based on Hadamard Matrix.
	 */
	public H_2s_OAProvider() {
		
	}

	/**
	 * @param m
	 *            the number of factors and it should be 2^s - 1
	 * @see pairwisetesting.engine.am.OAProvider#get(int)
	 * @throws IllegalArgumentException
	 *             if {@code m} is not 2^s - 1
	 */
	public int[][] get(int m) {
		Preconditions.checkArgument(MathUtil.is_2sMinusOne(m), 
				"The number of factors should be 2^s - 1.");
		
		// Remove the all 1 column (happen to be the first column)
		int[][] res = getH2s(m + 1).to2DArray(2);

		// -1 -> 2
		for (int column = 0; column < res[0].length; column++) {
			for (int row = 0; row < res.length; row++) {
				if (res[row][column] == -1) {
					res[row][column] = 2;
				}
			}
		}
		return res;
	}

	/**
	 * Generates H2^s Matrix.
	 * 
	 * @param numOfColumns
	 *            the number of columns and it should be 2^s
	 * @return H2^s Matrix
	 */
	private Matrix getH2s(int numOfColumns) {
		if (numOfColumns == 2) {
			return H2;
		} else {
			return H2.directProduct(getH2s(numOfColumns / 2));
		}
	}

}
