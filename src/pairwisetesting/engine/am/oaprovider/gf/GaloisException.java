/**
	GALOISFIELD

	This Package is written by Jan Struyf
	URL: http://ace.ulyssis.student.kuleuven.ac.be/~jeans
	EMail: jan.struyf@student.kuleuven.ac.be
	Post:	Jan Struyf, Hoogstraat 47, 3360 Bierbeek, BELGIUM

	You can do anything with it, if you leave this comment field unmodified.
 */
package pairwisetesting.engine.am.oaprovider.gf;

public class GaloisException extends RuntimeException {

	private static final long serialVersionUID = 4674463899172146968L;

	private String outstr;

	@SuppressWarnings("unused")
	private int code;

	public static final int POLYNOMIAL = 0;
	public static final int DIVIDE = 1;
	public static final int MODULOPOLY = 2;
	public static final int PRIMITIVE = 3;
	public static final int PARSE = 4;
	public static final int FIELDCONV = 5;
	public static final int GENERAL = 6;
	public static final int CODE = 7;

	public GaloisException(String newstr, int newcode) {
		outstr = newstr;
		code = newcode;
	}

	public String toString() {
		return "GaloisException: " + outstr;
	}
}