package pairwisetesting.engine.am.oaprovider.ols;

import java.util.ArrayList;
import java.util.List;

import pairwisetesting.engine.am.oaprovider.util.MathUtil;

import com.google.common.base.Preconditions;

/**
 * The Orthogonal Latin Square (OLS) provider based on Rp Finite Field.
 * 
 * @see Poly_OLS_Provider
 */
public class Rp_OLS_Provider implements OLS_Provider {

	/**
	 * Returns the generated OLS based on Rp Finite Field.
	 * 
	 * @param t
	 *            the order of the OLS and it should be a prime
	 * @param n
	 *            the number of OLS and it should be at most {@code t-1}
	 * @return the generated OLS based on Rp Finite Field
	 * @throws IllegalArgumentException
	 *             if {@code t} is not a prime or {@code n < 0 || n >= t}
	 */
	public List<int[][]> generate_OLS(int t, int n) {
		Preconditions.checkArgument(MathUtil.isPrime(t), 
				"The order of the OLS should be a prime.");
		Preconditions.checkArgument((n >= 0 && n <= t-1), 
				"The number of OLS should be >= 0 and at most t-1.");
		return generateRp_OLS(t, n);
	}

	/**
	 * Returns the generated OLS based on Rp Finite Field.
	 * 
	 * @param p
	 *            the order of the OLS and it should be a prime
	 * @param n
	 *            the number of OLS and it should be at most {@code p-1}
	 * @return the generated OLS based on Rp Finite Field
	 */
	private List<int[][]> generateRp_OLS(int p, int n) {
		ArrayList<int[][]> OLS_list = new ArrayList<int[][]>(n);

		// generate n OLS
		for (int i = 0; i < n; i++) {
			int interval = i + 1;
			OLS_list.add(generateRp_LS(p, interval));
		}

		return OLS_list;
	}

	/**
	 * Returns the generated LS based on Rp Finite Field.
	 * 
	 * @param p
	 *            the order of the LS and it should be a prime
	 * @param interval
	 *            the interval used to generate LS
	 * @return the generated LS based on Rp Finite Field
	 */
	private int[][] generateRp_LS(int p, int interval) {
		int[][] ls = new int[p][p];
		for (int row = 0; row < p; row++) {
			int value = (row * interval) % p + 1; // start value
			for (int column = 0; column < p; column++) {
				ls[row][column] = value;

				// next value
				value = (value + 1) % p;
				if (value == 0)
					value = p;
			}
		}
		return ls;
	}

}
