package pairwisetesting.engine.am.oaprovider.ols;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.google.common.base.Preconditions;

import pairwisetesting.engine.am.oaprovider.gf.ExtendedGaloisField;
import pairwisetesting.engine.am.oaprovider.gf.GaloisField;
import pairwisetesting.engine.am.oaprovider.gf.GaloisPolynomial;
import pairwisetesting.engine.am.oaprovider.util.MathUtil;

/**
 * The Orthogonal Latin Square (OLS) provider based on Polynomial Finite Field.
 * 
 * @see Rp_OLS_Provider
 */
public class Poly_OLS_Provider implements OLS_Provider {

	/**
	 * Returns the generated OLS based on Polynomial Finite Field.
	 * 
	 * @param t
	 *            the order of the OLS and it should be a prime power
	 * @param n
	 *            the number of OLS and it should be at most {@code t-1}
	 * @return the generated OLS based on Polynomial Finite Field
	 * @throws IllegalArgumentException
	 *             if {@code t} is not a prime power 
	 *             or {@code n < 0 || n >= t}
	 */
	public List<int[][]> generate_OLS(int t, int n) {
		Preconditions.checkArgument(MathUtil.partOfPrimePower(t) != null, 
				"The order of the OLS should be a prime power.");
		Preconditions.checkArgument((n >= 0 && n <= t-1), 
				"The number of OLS should be >= 0 and at most t-1.");
		return generatePoly_OLS(t, n);
	}
	
	/**
	 * Returns the generated OLS based on Polynomial Finite Field.
	 * 
	 * @param pm
	 *            the order of the OLS and it should be a prime power
	 * @param n
	 *            the number of OLS and it should be at most {@code pm-1}
	 * @return the generated OLS based on Polynomial Finite Field
	 */
	private List<int[][]> generatePoly_OLS(int pm, int n) {

		ArrayList<int[][]> OLS_list = new ArrayList<int[][]>(n);
		
		if (n == 0) {
			return OLS_list;
		}
		
		 // p & m
		int[] parts = MathUtil.partOfPrimePower(pm);
		int[][] firstPoly_LS = generatePoly_LS(parts[0], parts[1]);
		OLS_list.add(firstPoly_LS);
		
		int[][] nextPoly_LS = Arrays.copyOf(firstPoly_LS, firstPoly_LS.length);
		for (int i = 1; i < n; i++) {
			
			int[] secondRow = nextPoly_LS[1];
			
			// 3->2, 4->3, 5->4 ...
			for (int row = 1; row < nextPoly_LS.length - 1; row++) {
				nextPoly_LS[row] = nextPoly_LS[row + 1];
			}
			// second row -> last row
			nextPoly_LS[nextPoly_LS.length - 1] = secondRow;
			
			// add and copy a new LS with raw data to process
			OLS_list.add(nextPoly_LS);
			nextPoly_LS = Arrays.copyOf(nextPoly_LS, nextPoly_LS.length);
		}
		
		return OLS_list;
	}
	
	/**
	 * Returns the generated LS based on Polynomial Finite Field.
	 * 
	 * @param p
	 *            the prime part of the order of the OLS
	 * @param m
	 *            the power part of the order of the OLS
	 * @return the generated LS based on Polynomial Finite Field
	 */
	private int[][] generatePoly_LS(int p, int m) {
		GaloisField field = new GaloisField(p);
		field.setDisplayMode(GaloisField.ALFAPOWER);
		ExtendedGaloisField extendedField = new ExtendedGaloisField(field, 'X', m);
		GaloisPolynomial[] alfaPowers = extendedField.getAlfaPowers();
		
		// Map alfaPowers to number in OA
		HashMap<String, Integer> alfaPowerNumberMap = new HashMap<String, Integer>();
		alfaPowerNumberMap.put("0", 1);
		int nextNumber = 2;
		for (GaloisPolynomial alfaPower : alfaPowers) {
			if (!alfaPowerNumberMap.containsKey(alfaPower.toString())) {
				alfaPowerNumberMap.put(alfaPower.toString(), nextNumber);
				nextNumber++;
			}
		}
		// System.out.println(alfaPowerNumberMap);
		
		// the order of the OLS
		int n = (int)Math.pow(p, m);
		int[][] ls = new int[n][n];
		
		// left-top
		ls[0][0] = 1;
		
		// first row
		for (int j = 1; j < n; j++) {
			ls[0][j] = alfaPowerNumberMap.get(alfaPowers[j-1].toString());
		}
		// first column
		for (int i = 1; i < n; i++) {
			ls[i][0] = alfaPowerNumberMap.get(alfaPowers[i-1].toString());
		}
		
		// addition table
		GaloisPolynomial moduloPoly = extendedField.getModuloPoly();
		for (int i = 1; i < n; i++) {
			for (int j = 1; j < n; j++) {
				GaloisPolynomial remains =  alfaPowers[i-1].sum(alfaPowers[j-1]).divide(moduloPoly)[1];
				ls[i][j] = alfaPowerNumberMap.get(remains.toString());
			}
		}
		
		return ls;
	}

}
