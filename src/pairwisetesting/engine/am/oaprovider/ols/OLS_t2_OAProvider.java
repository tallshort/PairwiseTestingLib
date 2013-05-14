package pairwisetesting.engine.am.oaprovider.ols;

import java.util.List;

import com.google.common.base.Preconditions;

/**
 * The OA provider based on Orthogonal Latin Square (OLS). 
 * Its OA contains t^2 runs.
 * 
 * @see OLS_tu_OAProvider
 */
public class OLS_t2_OAProvider extends OLS_OAProvider {

	/**
	 * Constructs an OLS OA Provider (t^2 runs) with the specified number 
	 * of levels.
	 * 
	 * @param t
	 *            the number of levels
	 * @throws IllegalArgumentException
	 *             if {@code t} < 1 or {@code t} is not a prime or prime power
	 */
	public OLS_t2_OAProvider(int t) {
		super(t);
	}

	/**
	 * @see pairwisetesting.engine.am.OAProvider#get(int)
	 * 
	 * @throws IllegalArgumentException
	 *             if {@code m < 2 || m > t+1}
	 */
	@Override
	public int[][] get(int m) {
		Preconditions.checkArgument(m >= 2 && m <= t+1, 
				"The number of factors should be at least 2 and at most t+1.");

		// OA Lt^2(t^m)
		int[][] oa = new int[t * t][m];

		generateFirstColumn(oa);
		generateSecondColumn(oa);

		List<int[][]> OLS_list = getOLS_Provider().generate_OLS(t, m - 2);

		// the start column for the OLS is the third one
		int startColumnForOLS = 2;
		fillOLSInOA(oa, OLS_list, startColumnForOLS, 1);

		return oa;
	}

	// 111.. 222.. 333.. ...
	private void generateFirstColumn(int[][] oa) {
		for (int value = 1; value <= this.t; value++) {
			for (int i = 0; i < this.t; i++) {
				oa[(value - 1) * this.t + i][0] = value;
			}
		}
	}

	// 123.. 123.. 123.. ...
	private void generateSecondColumn(int[][] oa) {
		for (int i = 0; i < this.t; i++) {
			for (int value = 1; value <= this.t; value++) {
				oa[i * this.t + value - 1][1] = value;
			}
		}
	}

}
