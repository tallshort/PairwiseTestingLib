package pairwisetesting.engine.am.oaprovider.util;

/**
 * This utility class provides some useful methods to do Mathematics related
 * calculations.
 * 
 */
public class MathUtil {

	// Suppress default constructor for noninstantiability
	private MathUtil() {
		throw new AssertionError();
	}

	/**
	 * Returns <tt>true</tt> if the specified number is 2^s - 1.
	 * 
	 * @param n
	 *            the specified number
	 * @return <tt>true</tt> if {@code n} is 2^s - 1
	 */
	public static boolean is_2sMinusOne(int n) {
		int s = 1;
		while (true) {
			int res = (int) Math.pow(2, s) - 1;
			if (n == res) {
				return true;
			} else if (res > n) {
				return false;
			} else {
				s++;
			}
		}
	}

	/**
	 * Returns <tt>true</tt> if the specified number is a prime.
	 * 
	 * @param n
	 *            the specified number
	 * @return <tt>true</tt> if {@code n} is a prime
	 */
	public static boolean isPrime(int n) {
		boolean prime = true;
		for (int i = 3; i <= Math.sqrt(n); i += 2) {
			if (n % i == 0) {
				prime = false;
				break;
			}
		}
		if ((n % 2 != 0 && prime && n > 2) || n == 2) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns [p, m] if the specified number = p^m, otherwise returns null.
	 * 
	 * @param n
	 *            the specified number
	 * @return [p, m] if {@code n} = p^m, otherwise null
	 */
	public static int[] partOfPrimePower(int n) {
		if (isPrime(n)) {
			return new int[] { n, 1 };
		}

		int p = 1;
		while (true) {
			if (isPrime(p)) {
				int i = 1;
				while (true) {
					int res = (int) Math.pow(p, i);
					if (res == n) {
						return new int[] { p, i };
					} else if (res > n) {
						break;
					}
					i++;
				}
			}
			p++;
			if (p > n) {
				return null;
			}
		}
	}

	/**
	 * Returns the next prime power larger than the specified number.
	 * 
	 * @param start
	 *            the start number to find the next prime power
	 * @return the next prime power larger than {@code start}
	 */
	public static int nextPrimePower(int start) {
		int next = start + 1;
		while (true) {
			if (partOfPrimePower(next) != null) {
				return next;
			} else {
				next++;
			}
		}
	}
}
