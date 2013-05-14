package pairwisetesting.engine.am.oaprovider.ols;

import java.util.List;

import com.google.common.base.Preconditions;

import pairwisetesting.engine.am.OAProvider;
import pairwisetesting.engine.am.oaprovider.util.MathUtil;

/**
 * The OA provider based on Orthogonal Latin Square (OLS).
 * 
 * @see OLS_t2_OAProvider
 * @see OLS_tu_OAProvider
 * @see OLS_Provider
 * @see Rp_OLS_Provider
 * @see Poly_OLS_Provider
 */
public abstract class OLS_OAProvider extends OAProvider {

	/**
	 * The Orthogonal Latin Square provider.
	 */
	private OLS_Provider ols_Provider;

	/**
	 * Constructs an OLS OA provider with the specified number of levels.
	 * 
	 * @param t
	 *            the number of levels
	 * @throws IllegalArgumentException
	 *             if {@code t} < 1 or {@code t} is not a prime or prime power
	 */
	public OLS_OAProvider(int t) {
		super(t);
		int[] parts = MathUtil.partOfPrimePower(t);
		Preconditions.checkArgument(parts != null,  
				"The number of levels should be a prime power.");
		if (parts[1] == 1) {
			this.setOLS_Provider(new Rp_OLS_Provider());
		} else {
			this.setOLS_Provider(new Poly_OLS_Provider());
		}
	}

	/**
	 * Fill the specified OLS list into OA 2D array.
	 * 
	 * @param oa
	 *            the OA to be filled
	 * @param OLS_list
	 *            the OLS list
	 * @param startColumnForOLS
	 *            the start column for OLS (begin with 0)
	 * @param numOfLSRepetitions
	 *            the number of repetitions for LS
	 * @throws NullPointerException
	 *             if {@code oa} or {@code OLS_list} is null
	 * @throws IllegalArgumentException
	 *             if {@code startColumnForOLS < 2} 
	 *             or {@code numOfLSRepetitions < 1}
	 */
	protected void fillOLSInOA(int[][] oa, List<int[][]> OLS_list,
			int startColumnForOLS, int numOfLSRepetitions) {
		Preconditions.checkNotNull(oa, "the OA to be filled");
		Preconditions.checkNotNull(OLS_list, "the OLS list");
		Preconditions.checkArgument(startColumnForOLS >= 2, 
				"The start column for OLS should >= 2.");
		Preconditions.checkArgument(numOfLSRepetitions >= 1, 
				"The number of repetitions for LS should >= 1.");
		
		int columnForNextLS = startColumnForOLS;
		for (int[][] square : OLS_list) {
			for (int i = 0; i < numOfLSRepetitions; i++) {

				int nextRepetitionStartIndex
					= i * square.length * square.length;

				for (int row = 0; row < square.length; row++) {
					for (int column = 0; column < square.length; column++) {
						oa[nextRepetitionStartIndex + row * square.length 
						   + column][columnForNextLS] = square[row][column];
					}
				}
			}
			// the column for the next LS
			columnForNextLS++;
		}
	}

	/**
	 * Returns the Orthogonal Latin Square provider.
	 * 
	 * @return the Orthogonal Latin Square provider
	 */
	protected OLS_Provider getOLS_Provider() {
		return ols_Provider;
	}

	/**
	 * Sets the Orthogonal Latin Square provider.
	 * 
	 * @param ols_Provider the specified Orthogonal Latin Square provider
	 */
	private void setOLS_Provider(OLS_Provider ols_Provider) {
		this.ols_Provider = ols_Provider;
	}

}
