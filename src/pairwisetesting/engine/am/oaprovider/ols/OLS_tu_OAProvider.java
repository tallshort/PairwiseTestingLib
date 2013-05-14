package pairwisetesting.engine.am.oaprovider.ols;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.base.Preconditions;

/**
 * The OA provider based on Orthogonal Latin Square (OLS). 
 * Its OA contains t^u runs.
 * 
 * @see OLS_t2_OAProvider
 */
public class OLS_tu_OAProvider extends OLS_OAProvider {

	// OA contains t^u runs (rows) and the default is 2.
	private int u = 2; 

	/**
	 * Constructs an OLS OA Provider (t^u runs) with the specified number 
	 * of levels.
	 * 
	 * @param t
	 *            the number of levels
	 * @throws IllegalArgumentException
	 *             if {@code t} < 1 or {@code t} is not a prime or prime power
	 */
	public OLS_tu_OAProvider(int t) {
		super(t);
	}

	/**
	 * @see pairwisetesting.engine.am.OAProvider#get(int)
	 * 
	 * @throws IllegalArgumentException
	 *             if {@code m < t+1}
	 */
	@Override
	public int[][] get(int m) {
		Preconditions.checkArgument(m >= t+1, 
				"The number of factors should be at least t+1.");
		this.u = m - (this.t - 1);

		// OA Lt^u(u^m)
		int[][] oa = new int[getRuns()][m];

		ArrayList<int[]> rows = generate_tuXuRows();
		fill_tuXuRowsInOA(rows, oa);

		List<int[][]> OLS_list
			= getOLS_Provider().generate_OLS(this.t, this.t - 1);

		// the start column for the OLS is the u-th one
		int startColumnForOLS = u;
		int numOfLSRepetitions = getNumOfLSRepetitions();
		fillOLSInOA(oa, OLS_list, startColumnForOLS, numOfLSRepetitions);

		return oa;
	}

	private void fill_tuXuRowsInOA(ArrayList<int[]> rows, int[][] oa) {
		for (int i = 0; i < rows.size(); i++) {
			int[] row = rows.get(i);
			for (int j = 0; j < row.length; j++) {
				oa[i][j] = row[j];
			}
		}
	}

	/**
	 * Returns the first t^u rows and each row contains u factors.
	 * 
	 * @return the first t^u rows and each row contains u factors
	 */
	private ArrayList<int[]> generate_tuXuRows() {
		ArrayList<int[]> resultRows = new ArrayList<int[]>(getRuns());

		int[] row = new int[this.u];
		generate_tuXuRowsBackTrack(0, row, resultRows);

		return resultRows;
	}

	private void generate_tuXuRowsBackTrack(int nextIndex, int[] row,
			ArrayList<int[]> resultRows) {
		if (nextIndex >= row.length) {
			int[] newRow = Arrays.copyOf(row, row.length);
			resultRows.add(newRow);
		} else {
			for (int value = 1; value <= this.t; value++) {
				row[nextIndex] = value;
				generate_tuXuRowsBackTrack(nextIndex + 1, row, resultRows);
			}
		}
	}

	/**
	 * Returns the number of runs.
	 * 
	 * @return the number of runs
	 */
	private int getRuns() {
		return (int) Math.pow(this.t, this.u);
	}

	/**
	 * Returns the number of repetitions.
	 * 
	 * @return the number of repetitions
	 */
	private int getNumOfLSRepetitions() {
		return (int) Math.pow(this.t, this.u - 2);
	}
}
