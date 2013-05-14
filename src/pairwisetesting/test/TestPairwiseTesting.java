package pairwisetesting.test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import pairwisetesting.PairwiseTestingToolkit;
import pairwisetesting.coredomain.Engine;
import pairwisetesting.coredomain.EngineException;
import pairwisetesting.coredomain.Factor;
import pairwisetesting.coredomain.IMetaParameterProvider;
import pairwisetesting.coredomain.ITestCasesGenerator;
import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.coredomain.MetaParameterException;
import pairwisetesting.engine.am.AMEngine;
import pairwisetesting.engine.am.OAProvider;
import pairwisetesting.engine.am.oaprovider.CascadeOAProviderFactory;
import pairwisetesting.engine.am.oaprovider.SelfRuleOAProviderFactory;
import pairwisetesting.engine.am.oaprovider.hadamard.H_2s_OAProvider;
import pairwisetesting.engine.am.oaprovider.hadamard.Matrix;
import pairwisetesting.engine.am.oaprovider.ols.OLS_Provider;
import pairwisetesting.engine.am.oaprovider.ols.OLS_t2_OAProvider;
import pairwisetesting.engine.am.oaprovider.ols.OLS_tu_OAProvider;
import pairwisetesting.engine.am.oaprovider.ols.Poly_OLS_Provider;
import pairwisetesting.engine.am.oaprovider.ols.Rp_OLS_Provider;
import pairwisetesting.engine.am.oaprovider.util.MathUtil;
import pairwisetesting.engine.jenny.JennyEngine;
import pairwisetesting.engine.pict.PICTEngine;
import pairwisetesting.level.AbstractTypeLevelGenerator;
import pairwisetesting.level.BooleanLevelGenerator;
import pairwisetesting.level.EP_BVA_IntegerLevelGenerator;
import pairwisetesting.level.EnumLevelGenerator;
import pairwisetesting.level.ILevelGenerator;
import pairwisetesting.metaparameterparser.ExcelMetaParameterProvider;
import pairwisetesting.metaparameterparser.FileMetaParameterProvider;
import pairwisetesting.metaparameterparser.FileMetaParameterProviderFactory;
import pairwisetesting.metaparameterparser.MetaParameterXMLSerializer;
import pairwisetesting.metaparameterparser.TextMetaParameterProvider;
import pairwisetesting.metaparameterparser.WordMetaParameterProvider;
import pairwisetesting.metaparameterparser.XMLMetaParameterProvider;
import pairwisetesting.test.mock.MockMetaParameterProvider;
import pairwisetesting.test.mock.MockOAEngine;
import pairwisetesting.test.mock.MockOAProviderFactory;
import pairwisetesting.test.mock.MockTestCasesGenerator;
import pairwisetesting.testcasesgenerator.ExcelTestCasesGenerator;
import pairwisetesting.testcasesgenerator.TXTTestCasesGenerator;
import pairwisetesting.testcasesgenerator.XMLTestCasesGenerator;
import pairwisetesting.util.Converter;
import pairwisetesting.util.TextFile;

public class TestPairwiseTesting extends TestCase {
	private Factor f1, f2, f3, f4, f5;
	
	enum AccountType { 
		Student, Internal, Normal;
	}

	protected void setUp() throws Exception {
		super.setUp();
		f1 = new Factor("OS");
		f1.addLevel("Windows XP");
		f1.addLevel("Solaris 10");
		f1.addLevel("Red Hat 9");
		f2 = new Factor("Browser", new String[] { "IE", "Firefox", "Opera" });
		f3 = new Factor("Memory", new String[] { "255M", "1G", "2G" });
		f4 = new Factor("DB", new String[] { "MySQL", "Oracle", "DB2" });
		f5 = new Factor("Server", new String[] { "WebLogic", "JBoss", "Tomcat",
				"GlassFish" });
	}

	public void testFactor() {
		assertEquals("OS", f1.getName());

		assertEquals("Solaris 10", f1.getLevel(1));

		assertEquals(3, f1.getNumOfLevels());

		String[] expectedLevels = new String[3];
		expectedLevels[0] = "Windows XP";
		expectedLevels[1] = "Solaris 10";
		expectedLevels[2] = "Red Hat 9";
		assertTrue(Arrays.equals(expectedLevels, f1.getLevels()));

		assertEquals("Browser", f2.getName());

		expectedLevels[0] = "IE";
		expectedLevels[1] = "Firefox";
		expectedLevels[2] = "Opera";
		assertTrue(Arrays.equals(expectedLevels, f2.getLevels()));
		
		Factor ft = new Factor();
		try {
			ft.addLevel(null);
			fail("It should not accept null value");
		} catch (NullPointerException e) {
			
		}
		
		try {
			new Factor(null);
			fail("It should not accept null value");
		} catch (NullPointerException e) {
			
		}
		
		try {
			new Factor(null, null);
			fail("It should not accept null value");
		} catch (NullPointerException e) {
			
		}
	}

	public void testMetaParameter() {
		MetaParameter mp = new MetaParameter();

		mp.setStrength(2);
		assertEquals(2, mp.getStrength());
		
		try {
			mp.setStrength(1);
			fail("It should not accept strength < 2");
		} catch (IllegalArgumentException e) {
			
		}
		
		try {
			mp.setStrength(-1);
			fail("It should not accept strength < 2");
		} catch (IllegalArgumentException e) {
			
		}
		
		try {
			mp.addFactor(null);
			fail("It should not accept null value");
		} catch (NullPointerException e) {
			
		}
		
		try {
			Factor ff = mp.getFactor(null);
			fail("It should not accept null value");
		} catch (NullPointerException e) {
			
		}
		
		try {
			mp.addConstraint(null);
			fail("It should not accept null value");
		} catch (NullPointerException e) {
			
		}

		mp.addFactor(f1);
		mp.addFactor(f2);
		mp.addFactor(f3);
		assertEquals(f1, mp.getFactor("OS"));
		assertEquals(f3, mp.getFactor("Memory"));

		assertEquals(f1, mp.getFactors()[0]);
		assertEquals(f3, mp.getFactors()[2]);

		assertEquals("OS", mp.getFactorNames()[0]);
		assertEquals("Memory", mp.getFactorNames()[2]);

		assertEquals("Windows XP", mp.getLevelOfFactor("OS", 0));

		assertEquals(3, mp.getNumOfFactors());

		assertEquals(3, mp.getMaxNumOfLevels());
		mp.addFactor(f5);
		assertEquals(4, mp.getMaxNumOfLevels());

		mp.addConstraint("IF [File system] = \"FAT\" THEN [Size] <= 4096");
		mp
				.addConstraint("IF [OS_2] = \"WinXP\" THEN [SKU_2] = \"Professional\"");
		assertEquals("IF [File system] = \"FAT\" THEN [Size] <= 4096", mp
				.getConstraints()[0]);
		assertEquals("IF [OS_2] = \"WinXP\" THEN [SKU_2] = \"Professional\"",
				mp.getConstraints()[1]);

		MetaParameter mp2 = new MetaParameter(3);
		assertEquals(3, mp2.getStrength());

	}

	public void testPairwiseTestingToolkit() {
		PairwiseTestingToolkit toolkit = new PairwiseTestingToolkit();
		toolkit.setMetaParameterProvider(new MockMetaParameterProvider());
		toolkit.setEngine(new MockOAEngine());
		toolkit.setTestCasesGenerator(new MockTestCasesGenerator());

		String[][] testData = null;
		try {
			testData = toolkit.generateTestData();
		} catch (MetaParameterException e) {
			fail("Should not throw MetaParameterException" + e);
		} catch (EngineException e) {
			fail("Should not throw EngineException" + e);
		}
		assertEquals(f1.getLevel(0), testData[0][0]);
		assertEquals(f1.getLevel(1), testData[3][0]);
		assertEquals(f1.getLevel(2), testData[6][0]);
		assertEquals(f2.getLevel(0), testData[6][1]);
		assertEquals(f2.getLevel(1), testData[7][1]);
		assertEquals(f2.getLevel(2), testData[8][1]);
		assertEquals(f3.getLevel(1), testData[3][2]);
		assertEquals(f3.getLevel(2), testData[4][2]);
		assertEquals(f3.getLevel(0), testData[5][2]);
		assertEquals(f4.getLevel(0), testData[0][3]);
		assertEquals(f4.getLevel(1), testData[1][3]);
		assertEquals(f4.getLevel(2), testData[2][3]);

		String testCases = null;
		try {
			testCases = toolkit.generateTestCases();
			// System.out.println(testCases);
		} catch (MetaParameterException e) {
			fail("Should not throw MetaParameterException" + e);
		} catch (EngineException e) {
			fail("Should not throw EngineException" + e);
		}
		String expectedTestCases = "OS	Browser	Memory	DB	\n"
				+ "Windows XP	IE	255M	MySQL	\n"
				+ "Windows XP	Firefox	1G	Oracle	\n"
				+ "Windows XP	Opera	2G	DB2	\n" + "Solaris 10	IE	1G	DB2	\n"
				+ "Solaris 10	Firefox	2G	MySQL	\n"
				+ "Solaris 10	Opera	255M	Oracle	\n"
				+ "Red Hat 9	IE	2G	Oracle	\n" + "Red Hat 9	Firefox	255M	DB2	\n"
				+ "Red Hat 9	Opera	1G	MySQL	\n";
		assertEquals(expectedTestCases, testCases);
	}

	public void testPICTEngine() throws MetaParameterException {
		Engine engine = new PICTEngine();
		IMetaParameterProvider provider = new MockMetaParameterProvider();
		MetaParameter mp = provider.get();
		String[][] testData = null;
		try {
			testData = engine.generateTestData(mp);
			assertNotNull(testData);
		} catch (EngineException e) {
			e.printStackTrace();
			fail("Should not throw EngineException" + e);
		}
	}

	public void testJennyEngine() throws MetaParameterException {
		Engine engine = new JennyEngine();
		IMetaParameterProvider provider = new MockMetaParameterProvider();
		ITestCasesGenerator generator = new MockTestCasesGenerator();
		MetaParameter mp = provider.get();
		String[][] testData = null;
		try {
			testData = engine.generateTestData(mp);
			assertNotNull(testData);
			String testCases = generator.generate(mp, testData);
			String expectedTestCases = "OS	Browser	Memory	DB	\n"
					+ "Windows XP\tOpera\t2G\tMySQL\t\n"
					+ "Solaris 10\tFirefox\t255M\tDB2\t\n"
					+ "Red Hat 9\tIE\t1G\tOracle\t\n"
					+ "Windows XP\tIE\t255M\tOracle\t\n"
					+ "Solaris 10\tIE\t2G\tDB2\t\n"
					+ "Red Hat 9\tFirefox\t1G\tMySQL\t\n"
					+ "Solaris 10\tOpera\t1G\tDB2\t\n"
					+ "Red Hat 9\tOpera\t255M\tDB2\t\n"
					+ "Windows XP\tFirefox\t2G\tOracle\t\n"
					+ "Solaris 10\tIE\t255M\tMySQL\t\n"
					+ "Windows XP\tIE\t1G\tDB2\t\n"
					+ "Red Hat 9\tOpera\t2G\tOracle\t\n"
					+ "Solaris 10\tOpera\t1G\tOracle\t\n";
			assertEquals(expectedTestCases, testCases);
		} catch (EngineException e) {
			e.printStackTrace();
			fail("Should not throw EngineException" + e);
		}
	}

	public void testMatrix() {
		Matrix m = new Matrix(2, 2);
		m.setElement(1, 1, 2);
		m.setElement(1, 2, 3);
		m.setElement(2, 1, 1);
		m.setElement(2, 2, 0);

		assertEquals(2, m.getElement(1, 1));
		assertEquals(1, m.getElement(2, 1));

		assertEquals(2, m.getNumOfRows());
		assertEquals(2, m.getNumOfColumns());

		Matrix m1 = new Matrix(2, 2);
		m1.setElement(1, 1, 2);
		m1.setElement(1, 2, 3);
		m1.setElement(2, 1, 1);
		m1.setElement(2, 2, 0);

		assertEquals(m1, m);

		Matrix m2 = new Matrix(3, 2);
		m2.setElement(1, 1, -1);
		m2.setElement(1, 2, 1);
		m2.setElement(2, 1, 2);
		m2.setElement(2, 2, -3);
		m2.setElement(3, 1, 1);
		m2.setElement(3, 2, 0);

		Matrix expected = new Matrix(6, 4);
		expected.setElement(1, 1, -2);
		expected.setElement(1, 2, 2);
		expected.setElement(1, 3, -3);
		expected.setElement(1, 4, 3);
		expected.setElement(2, 1, 4);
		expected.setElement(2, 2, -6);
		expected.setElement(2, 3, 6);
		expected.setElement(2, 4, -9);
		expected.setElement(3, 1, 2);
		expected.setElement(3, 2, 0);
		expected.setElement(3, 3, 3);
		expected.setElement(3, 4, 0);
		expected.setElement(4, 1, -1);
		expected.setElement(4, 2, 1);
		expected.setElement(4, 3, 0);
		expected.setElement(4, 4, 0);
		expected.setElement(5, 1, 2);
		expected.setElement(5, 2, -3);
		expected.setElement(5, 3, 0);
		expected.setElement(5, 4, 0);
		expected.setElement(6, 1, 1);
		expected.setElement(6, 2, 0);
		expected.setElement(6, 3, 0);
		expected.setElement(6, 4, 0);
		

		Matrix res = m1.directProduct(m2);
		// System.out.println(res);
		assertEquals(expected, res);
		
		try {
			m1.directProduct(null);
			fail("It should not accept null value");
		} catch (NullPointerException e) {
			
		}
		
		try {
			m1.to2DArray(0);
			fail("It should not be out of bounds");
		} catch (IndexOutOfBoundsException e) {
			
		}

		int[][] expected1 = new int[][] { { 2, -3, 3 }, { -6, 6, -9 },
				{ 0, 3, 0 }, { 1, 0, 0 }, { -3, 0, 0 }, { 0, 0, 0 } };
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected1,
				res.to2DArray(2)));

		Matrix H2 = new Matrix(2, 2);
		H2.setElement(1, 1, 1);
		H2.setElement(1, 2, 1);
		H2.setElement(2, 1, 1);
		H2.setElement(2, 2, -1);
		Matrix H4 = H2.directProduct(H2);
		Matrix H8 = H2.directProduct(H4);
		assertEquals(1, H8.getElement(3, 1));
		assertEquals(1, H8.getElement(3, 2));
		assertEquals(-1, H8.getElement(3, 3));
		assertEquals(-1, H8.getElement(3, 4));
		assertEquals(1, H8.getElement(3, 5));
		assertEquals(1, H8.getElement(3, 6));
		assertEquals(-1, H8.getElement(3, 7));
		assertEquals(-1, H8.getElement(3, 8));
		assertEquals(1, H8.getElement(6, 1));
		assertEquals(-1, H8.getElement(6, 2));
		assertEquals(1, H8.getElement(6, 3));
		assertEquals(-1, H8.getElement(3, 4));
		assertEquals(-1, H8.getElement(6, 5));
		assertEquals(1, H8.getElement(3, 6));
		assertEquals(-1, H8.getElement(3, 7));
		assertEquals(1, H8.getElement(6, 8));
	}

	public void testH_2S_OAProvider() {
		OAProvider provider = new H_2s_OAProvider();

		int[][] rawTestData = provider.get(3);
		int[][] expected = { { 1, 1, 1 }, { 2, 1, 2 }, { 1, 2, 2 }, { 2, 2, 1 } };
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected,
				rawTestData));
		// System.out.println(Arrays.deepToString(rawTestData));

		rawTestData = provider.get(7);
		int[][] expected2 = { { 1, 1, 1, 1, 1, 1, 1 }, { 2, 1, 2, 1, 2, 1, 2 },
				{ 1, 2, 2, 1, 1, 2, 2 }, { 2, 2, 1, 1, 2, 2, 1 },
				{ 1, 1, 1, 2, 2, 2, 2 }, { 2, 1, 2, 2, 1, 2, 1 },
				{ 1, 2, 2, 2, 2, 1, 1 }, { 2, 2, 1, 2, 1, 1, 2 } };
		// System.out.println(Arrays.deepToString(rawTestData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected2,
				rawTestData));
		
		try {
			provider.get(-2);
			fail("The number of factors should be 2^s - 1.");
		} catch (IllegalArgumentException e) {
			
		}
		
		try {
			provider.get(5);
			fail("The number of factors should be 2^s - 1.");
		} catch (IllegalArgumentException e) {
			
		}
	}
	
	public void testRp_OLS_Provider() {
		OLS_Provider provider = new Rp_OLS_Provider();
		int[][] expected1 = {
				{1, 2, 3, 4, 5}, 
				{2, 3, 4, 5, 1}, 
				{3, 4, 5, 1, 2}, 
				{4, 5, 1, 2, 3}, 
				{5, 1, 2, 3, 4}
		};
		int[][] expected2 = {
				{1, 2, 3, 4, 5}, 
				{3, 4, 5, 1, 2}, 
				{5, 1, 2, 3, 4}, 
				{2, 3, 4, 5, 1},
				{4, 5, 1, 2, 3}
		};
		int[][] expected3 = {
				{1, 2, 3, 4, 5}, 
				{4, 5, 1, 2, 3}, 
				{2, 3, 4, 5, 1}, 
				{5, 1, 2, 3, 4}, 
				{3, 4, 5, 1, 2}
		};
		int[][] expected4 = {
				{1, 2, 3, 4, 5}, 
				{5, 1, 2, 3, 4}, 
				{4, 5, 1, 2, 3},
				{3, 4, 5, 1, 2},
				{2, 3, 4, 5, 1}
		};
		List<int[][]> OLS_list = provider.generate_OLS(5, 4);
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected1,
				OLS_list.get(0)));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected2,
				OLS_list.get(1)));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected3,
				OLS_list.get(2)));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected4,
				OLS_list.get(3)));
		
		expected1 = new int[][] {
				{1, 2, 3},
				{2, 3, 1},
				{3, 1, 2},
		};
		expected2 = new int[][] {
				{1, 2, 3},
				{3, 1, 2},
				{2, 3, 1},
		};
		OLS_list = provider.generate_OLS(3, 2);
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected1,
				OLS_list.get(0)));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected2,
				OLS_list.get(1)));
		
		OLS_list = provider.generate_OLS(3, 0);
		assertTrue("OLS list should be empty", OLS_list.isEmpty());
		
		try {
			OLS_list = provider.generate_OLS(3, -1);
			fail("The number of OLS should be >= 0 and at most t-1.");
		} catch (IllegalArgumentException e) {
			
		}
		
		try {
			OLS_list = provider.generate_OLS(3, 3);
			fail("The number of OLS should be >= 0 and at most t-1.");
		} catch (IllegalArgumentException e) {
			
		}
		
		try {
			OLS_list = provider.generate_OLS(3, 4);
			fail("The number of OLS should be >= 0 and at most t-1.");
		} catch (IllegalArgumentException e) {
			
		}
	}
	
	public void testPoly_OLS_Provider() {
		OLS_Provider provider = new Poly_OLS_Provider();
		int[][] expected1 = {
				{1, 2, 3, 4}, 
				{2, 1, 4, 3}, 
				{3, 4, 1, 2}, 
				{4, 3, 2, 1},
		};
		int[][] expected2 = {
				{1, 2, 3, 4}, 
				{3, 4, 1, 2}, 
				{4, 3, 2, 1},
				{2, 1, 4, 3},
		};
		int[][] expected3 = {
				{1, 2, 3, 4}, 
				{4, 3, 2, 1},
				{2, 1, 4, 3},
				{3, 4, 1, 2}, 
		};
	
		List<int[][]> OLS_list = provider.generate_OLS(4, 3);
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected1,
				OLS_list.get(0)));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected2,
				OLS_list.get(1)));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected3,
				OLS_list.get(2)));
		
		expected1 = new int[][] {
				{1, 2, 3, 4, 5, 6, 7, 8},
				{2, 1, 5, 8, 3, 7, 6, 4},
				{3, 5, 1, 6, 2, 4, 8, 7},
				{4, 8, 6, 1, 7, 3, 5, 2},
				{5, 3, 2, 7, 1, 8, 4, 6},
				{6, 7, 4, 3, 8, 1, 2, 5},
				{7, 6, 8, 5, 4, 2, 1, 3},
				{8, 4, 7, 2, 6, 5, 3, 1},
		};
		expected2 = new int[][] {
				{1, 2, 3, 4, 5, 6, 7, 8},
				{3, 5, 1, 6, 2, 4, 8, 7},
				{4, 8, 6, 1, 7, 3, 5, 2},
				{5, 3, 2, 7, 1, 8, 4, 6},
				{6, 7, 4, 3, 8, 1, 2, 5},
				{7, 6, 8, 5, 4, 2, 1, 3},
				{8, 4, 7, 2, 6, 5, 3, 1},
				{2, 1, 5, 8, 3, 7, 6, 4},
		};
		expected3 = new int[][] {
				{1, 2, 3, 4, 5, 6, 7, 8},
				{4, 8, 6, 1, 7, 3, 5, 2},
				{5, 3, 2, 7, 1, 8, 4, 6},
				{6, 7, 4, 3, 8, 1, 2, 5},
				{7, 6, 8, 5, 4, 2, 1, 3},
				{8, 4, 7, 2, 6, 5, 3, 1},
				{2, 1, 5, 8, 3, 7, 6, 4},
				{3, 5, 1, 6, 2, 4, 8, 7},
		};
		int[][] expected4 = new int[][] {
				{1, 2, 3, 4, 5, 6, 7, 8},
				{5, 3, 2, 7, 1, 8, 4, 6},
				{6, 7, 4, 3, 8, 1, 2, 5},
				{7, 6, 8, 5, 4, 2, 1, 3},
				{8, 4, 7, 2, 6, 5, 3, 1},
				{2, 1, 5, 8, 3, 7, 6, 4},
				{3, 5, 1, 6, 2, 4, 8, 7},
				{4, 8, 6, 1, 7, 3, 5, 2},
		};
		int[][] expected5 = new int[][] {
				{1, 2, 3, 4, 5, 6, 7, 8},
				{6, 7, 4, 3, 8, 1, 2, 5},
				{7, 6, 8, 5, 4, 2, 1, 3},
				{8, 4, 7, 2, 6, 5, 3, 1},
				{2, 1, 5, 8, 3, 7, 6, 4},
				{3, 5, 1, 6, 2, 4, 8, 7},
				{4, 8, 6, 1, 7, 3, 5, 2},
				{5, 3, 2, 7, 1, 8, 4, 6},
		};
		
		int[][] expected6 = new int[][] {
				{1, 2, 3, 4, 5, 6, 7, 8},
				{7, 6, 8, 5, 4, 2, 1, 3},
				{8, 4, 7, 2, 6, 5, 3, 1},
				{2, 1, 5, 8, 3, 7, 6, 4},
				{3, 5, 1, 6, 2, 4, 8, 7},
				{4, 8, 6, 1, 7, 3, 5, 2},
				{5, 3, 2, 7, 1, 8, 4, 6},
				{6, 7, 4, 3, 8, 1, 2, 5},
		};
		
		int[][] expected7 = new int[][] {
				{1, 2, 3, 4, 5, 6, 7, 8},
				{8, 4, 7, 2, 6, 5, 3, 1},
				{2, 1, 5, 8, 3, 7, 6, 4},
				{3, 5, 1, 6, 2, 4, 8, 7},
				{4, 8, 6, 1, 7, 3, 5, 2},
				{5, 3, 2, 7, 1, 8, 4, 6},
				{6, 7, 4, 3, 8, 1, 2, 5},
				{7, 6, 8, 5, 4, 2, 1, 3},
		};
		
		OLS_list = provider.generate_OLS(8, 7);
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected1,
				OLS_list.get(0)));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected2,
				OLS_list.get(1)));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected3,
				OLS_list.get(2)));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected4,
				OLS_list.get(3)));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected5,
				OLS_list.get(4)));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected6,
				OLS_list.get(5)));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected7,
				OLS_list.get(6)));
		
		OLS_list = provider.generate_OLS(8, 0);
		assertEquals("OLS list should be empty", 0, OLS_list.size());
		
		try {
			OLS_list = provider.generate_OLS(8, -1);
			fail("The number of OLS should be >= 0 and at most t-1.");
		} catch (IllegalArgumentException e) {
			
		}
		
		try {
			OLS_list = provider.generate_OLS(8, 8);
			fail("The number of OLS should be >= 0 and at most t-1.");
		} catch (IllegalArgumentException e) {
			
		}
		
		try {
			OLS_list = provider.generate_OLS(8, 9);
			fail("The number of OLS should be >= 0 and at most t-1.");
		} catch (IllegalArgumentException e) {
			
		}
	}

	public void testRp_OLS_t2_OAProvider() {

		OAProvider provider = new OLS_t2_OAProvider(3);

		// L9(3^4)
		int[][] rawTestData = provider.get(4);
		int[][] expected = { { 1, 1, 1, 1 }, { 1, 2, 2, 2 }, { 1, 3, 3, 3 },
				{ 2, 1, 2, 3 }, { 2, 2, 3, 1 }, { 2, 3, 1, 2 }, { 3, 1, 3, 2 },
				{ 3, 2, 1, 3 }, { 3, 3, 2, 1 } };
		// System.out.println(Arrays.deepToString(rawTestData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected,
				rawTestData));

		// L9(3^3)
		rawTestData = provider.get(3);
		int[][] expected2 = { { 1, 1, 1 }, { 1, 2, 2 }, { 1, 3, 3 },
				{ 2, 1, 2 }, { 2, 2, 3 }, { 2, 3, 1 }, { 3, 1, 3 },
				{ 3, 2, 1 }, { 3, 3, 2 } };
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected2,
				rawTestData));

		provider = new OLS_t2_OAProvider(5);
		// L25(5^6)
		rawTestData = provider.get(6);
		int[][] expected3 = { { 1, 1, 1, 1, 1, 1 }, { 1, 2, 2, 2, 2, 2 },
				{ 1, 3, 3, 3, 3, 3 }, { 1, 4, 4, 4, 4, 4 },
				{ 1, 5, 5, 5, 5, 5 }, { 2, 1, 2, 3, 4, 5 },
				{ 2, 2, 3, 4, 5, 1 }, { 2, 3, 4, 5, 1, 2 },
				{ 2, 4, 5, 1, 2, 3 }, { 2, 5, 1, 2, 3, 4 },
				{ 3, 1, 3, 5, 2, 4 }, { 3, 2, 4, 1, 3, 5 },
				{ 3, 3, 5, 2, 4, 1 }, { 3, 4, 1, 3, 5, 2 },
				{ 3, 5, 2, 4, 1, 3 }, { 4, 1, 4, 2, 5, 3 },
				{ 4, 2, 5, 3, 1, 4 }, { 4, 3, 1, 4, 2, 5 },
				{ 4, 4, 2, 5, 3, 1 }, { 4, 5, 3, 1, 4, 2 },
				{ 5, 1, 5, 4, 3, 2 }, { 5, 2, 1, 5, 4, 3 },
				{ 5, 3, 2, 1, 5, 4 }, { 5, 4, 3, 2, 1, 5 },
				{ 5, 5, 4, 3, 2, 1 } };
		// System.out.println(Arrays.deepToString(rawTestData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected3,
				rawTestData));

		// L25(5^4)
		rawTestData = provider.get(4);
		int[][] expected4 = { { 1, 1, 1, 1 }, { 1, 2, 2, 2 }, { 1, 3, 3, 3 },
				{ 1, 4, 4, 4 }, { 1, 5, 5, 5 }, { 2, 1, 2, 3 }, { 2, 2, 3, 4 },
				{ 2, 3, 4, 5 }, { 2, 4, 5, 1 }, { 2, 5, 1, 2 }, { 3, 1, 3, 5 },
				{ 3, 2, 4, 1 }, { 3, 3, 5, 2 }, { 3, 4, 1, 3 }, { 3, 5, 2, 4 },
				{ 4, 1, 4, 2 }, { 4, 2, 5, 3 }, { 4, 3, 1, 4 }, { 4, 4, 2, 5 },
				{ 4, 5, 3, 1 }, { 5, 1, 5, 4 }, { 5, 2, 1, 5 }, { 5, 3, 2, 1 },
				{ 5, 4, 3, 2 }, { 5, 5, 4, 3 } };
		// System.out.println(Arrays.deepToString(rawTestData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected4,
				rawTestData));

		provider = new OLS_t2_OAProvider(2);
		// L4(2^3)
		rawTestData = provider.get(3);
		int[][] expected5 = { { 1, 1, 1 }, { 1, 2, 2 }, { 2, 1, 2 },
				{ 2, 2, 1 } };
		// System.out.println(Arrays.deepToString(rawTestData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected5,
				rawTestData));

		// L4(2^2)
		rawTestData = provider.get(2);
		int[][] expected6 = { { 1, 1 }, { 1, 2 }, { 2, 1 }, { 2, 2 } };
		// System.out.println(Arrays.deepToString(rawTestData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected6,
				rawTestData));

	}

	public void testRp_OLS_tu_OAProvider() {
		OAProvider provider = new OLS_tu_OAProvider(3);

		// L9(3^4)
		int[][] rawTestData = provider.get(4);
		int[][] expected = { { 1, 1, 1, 1 }, { 1, 2, 2, 2 }, { 1, 3, 3, 3 },
				{ 2, 1, 2, 3 }, { 2, 2, 3, 1 }, { 2, 3, 1, 2 }, { 3, 1, 3, 2 },
				{ 3, 2, 1, 3 }, { 3, 3, 2, 1 } };
		// System.out.println(Arrays.deepToString(rawTestData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected,
				rawTestData));
		
		

		// L27(3^5)
		rawTestData = provider.get(5);
		int[][] expected2 = { { 1, 1, 1, 1, 1 }, { 1, 1, 2, 2, 2 },
				{ 1, 1, 3, 3, 3 }, { 1, 2, 1, 2, 3 }, { 1, 2, 2, 3, 1 },
				{ 1, 2, 3, 1, 2 }, { 1, 3, 1, 3, 2 }, { 1, 3, 2, 1, 3 },
				{ 1, 3, 3, 2, 1 }, { 2, 1, 1, 1, 1 }, { 2, 1, 2, 2, 2 },
				{ 2, 1, 3, 3, 3 }, { 2, 2, 1, 2, 3 }, { 2, 2, 2, 3, 1 },
				{ 2, 2, 3, 1, 2 }, { 2, 3, 1, 3, 2 }, { 2, 3, 2, 1, 3 },
				{ 2, 3, 3, 2, 1 }, { 3, 1, 1, 1, 1 }, { 3, 1, 2, 2, 2 },
				{ 3, 1, 3, 3, 3 }, { 3, 2, 1, 2, 3 }, { 3, 2, 2, 3, 1 },
				{ 3, 2, 3, 1, 2 }, { 3, 3, 1, 3, 2 }, { 3, 3, 2, 1, 3 },
				{ 3, 3, 3, 2, 1 } };
		// System.out.println(Arrays.deepToString(rawTestData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected2,
				rawTestData));

		provider = new OLS_tu_OAProvider(5);
		// L25(5^6)
		rawTestData = provider.get(6);
		int[][] expected3 = { { 1, 1, 1, 1, 1, 1 }, { 1, 2, 2, 2, 2, 2 },
				{ 1, 3, 3, 3, 3, 3 }, { 1, 4, 4, 4, 4, 4 },
				{ 1, 5, 5, 5, 5, 5 }, { 2, 1, 2, 3, 4, 5 },
				{ 2, 2, 3, 4, 5, 1 }, { 2, 3, 4, 5, 1, 2 },
				{ 2, 4, 5, 1, 2, 3 }, { 2, 5, 1, 2, 3, 4 },
				{ 3, 1, 3, 5, 2, 4 }, { 3, 2, 4, 1, 3, 5 },
				{ 3, 3, 5, 2, 4, 1 }, { 3, 4, 1, 3, 5, 2 },
				{ 3, 5, 2, 4, 1, 3 }, { 4, 1, 4, 2, 5, 3 },
				{ 4, 2, 5, 3, 1, 4 }, { 4, 3, 1, 4, 2, 5 },
				{ 4, 4, 2, 5, 3, 1 }, { 4, 5, 3, 1, 4, 2 },
				{ 5, 1, 5, 4, 3, 2 }, { 5, 2, 1, 5, 4, 3 },
				{ 5, 3, 2, 1, 5, 4 }, { 5, 4, 3, 2, 1, 5 },
				{ 5, 5, 4, 3, 2, 1 } };
		// System.out.println(Arrays.deepToString(rawTestData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected3,
				rawTestData));

		provider = new OLS_tu_OAProvider(2);
		// L8(2^4)
		rawTestData = provider.get(4);
		int[][] expected4 = { { 1, 1, 1, 1 }, { 1, 1, 2, 2 }, { 1, 2, 1, 2 },
				{ 1, 2, 2, 1 }, { 2, 1, 1, 1 }, { 2, 1, 2, 2 }, { 2, 2, 1, 2 },
				{ 2, 2, 2, 1 } };
		// System.out.println(Arrays.deepToString(rawTestData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected4,
				rawTestData));
	}
	
	public void testPoly_OLS_t2_OAProvider() {
		OAProvider provider = new OLS_t2_OAProvider(4);
		
		// L16(4^5)
		int[][] rawTestData = provider.get(5);
		int[][] expected = { 
				{ 1, 1, 1, 1, 1}, 
				{ 1, 2, 2, 2, 2}, 
				{ 1, 3, 3, 3 ,3},
				{ 1, 4, 4, 4 ,4},
				{ 2, 1, 2, 3, 4}, 
				{ 2, 2, 1, 4, 3}, 
				{ 2, 3, 4, 1, 2}, 
				{ 2, 4, 3, 2, 1},
				{ 3, 1, 3, 4, 2}, 
				{ 3, 2, 4, 3, 1},
				{ 3, 3, 1, 2, 4},
				{ 3, 4, 2, 1, 3},
				{ 4, 1, 4, 2, 3},
				{ 4, 2, 3, 1, 4},
				{ 4, 3, 2, 4, 1},
				{ 4, 4, 1, 3, 2} };
		// System.out.println(Arrays.deepToString(rawTestData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected,
				rawTestData));
		
		// L16(4^3)
		rawTestData = provider.get(3);
		expected = new int[][] { 
				{ 1, 1, 1}, 
				{ 1, 2, 2}, 
				{ 1, 3, 3},
				{ 1, 4, 4},
				{ 2, 1, 2}, 
				{ 2, 2, 1}, 
				{ 2, 3, 4}, 
				{ 2, 4, 3},
				{ 3, 1, 3}, 
				{ 3, 2, 4},
				{ 3, 3, 1},
				{ 3, 4, 2},
				{ 4, 1, 4},
				{ 4, 2, 3},
				{ 4, 3, 2},
				{ 4, 4, 1} };
		// System.out.println(Arrays.deepToString(rawTestData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected,
				rawTestData));
		
		// L64(8^5)
		 provider = new OLS_t2_OAProvider(8);
		rawTestData = provider.get(5);
		expected = new int[][] { 
				{1, 1, 1, 1, 1}, 
				{1, 2, 2, 2, 2}, 
				{1, 3, 3, 3, 3}, 
				{1, 4, 4, 4, 4}, 
				{1, 5, 5, 5, 5}, 
				{1, 6, 6, 6, 6}, 
				{1, 7, 7, 7, 7}, 
				{1, 8, 8, 8, 8}, 
				{2, 1, 2, 3, 4}, 
				{2, 2, 1, 5, 8}, 
				{2, 3, 5, 1, 6}, 
				{2, 4, 8, 6, 1}, 
				{2, 5, 3, 2, 7}, 
				{2, 6, 7, 4, 3}, 
				{2, 7, 6, 8, 5}, 
				{2, 8, 4, 7, 2}, 
				{3, 1, 3, 4, 5}, 
				{3, 2, 5, 8, 3}, 
				{3, 3, 1, 6, 2}, 
				{3, 4, 6, 1, 7}, 
				{3, 5, 2, 7, 1}, 
				{3, 6, 4, 3, 8}, 
				{3, 7, 8, 5, 4}, 
				{3, 8, 7, 2, 6}, 
				{4, 1, 4, 5, 6}, 
				{4, 2, 8, 3, 7}, 
				{4, 3, 6, 2, 4}, 
				{4, 4, 1, 7, 3}, 
				{4, 5, 7, 1, 8}, 
				{4, 6, 3, 8, 1}, 
				{4, 7, 5, 4, 2}, 
				{4, 8, 2, 6, 5}, 
				{5, 1, 5, 6, 7}, 
				{5, 2, 3, 7, 6}, 
				{5, 3, 2, 4, 8}, 
				{5, 4, 7, 3, 5}, 
				{5, 5, 1, 8, 4}, 
				{5, 6, 8, 1, 2}, 
				{5, 7, 4, 2, 1}, 
				{5, 8, 6, 5, 3}, 
				{6, 1, 6, 7, 8}, 
				{6, 2, 7, 6, 4}, 
				{6, 3, 4, 8, 7}, 
				{6, 4, 3, 5, 2}, 
				{6, 5, 8, 4, 6}, 
				{6, 6, 1, 2, 5}, 
				{6, 7, 2, 1, 3}, 
				{6, 8, 5, 3, 1}, 
				{7, 1, 7, 8, 2}, 
				{7, 2, 6, 4, 1}, 
				{7, 3, 8, 7, 5}, 
				{7, 4, 5, 2, 8}, 
				{7, 5, 4, 6, 3}, 
				{7, 6, 2, 5, 7}, 
				{7, 7, 1, 3, 6}, 
				{7, 8, 3, 1, 4}, 
				{8, 1, 8, 2, 3}, 
				{8, 2, 4, 1, 5}, 
				{8, 3, 7, 5, 1}, 
				{8, 4, 2, 8, 6}, 
				{8, 5, 6, 3, 2}, 
				{8, 6, 5, 7, 4}, 
				{8, 7, 3, 6, 8}, 
				{8, 8, 1, 4, 7}
				};
		// System.out.println(Arrays.deepToString(rawTestData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected,
				rawTestData));
		
		provider = new OLS_t2_OAProvider(3);
		try {
			rawTestData = provider.get(5);
			fail("The number of factors should be at least 2 and at most t+1.");
		} catch (IllegalArgumentException e) {
			
		}
		try {
			rawTestData = provider.get(1);
			fail("The number of factors should be at least 2 and at most t+1.");
		} catch (IllegalArgumentException e) {
			
		}
		
		try {
			provider = new OLS_t2_OAProvider(0);
			fail("The number of levels should be a prime or prime power.");
		} catch (IllegalArgumentException e) {
			
		}
		try {
			provider = new OLS_t2_OAProvider(6);
			fail("The number of levels should be a prime or prime power.");
		} catch (IllegalArgumentException e) {
			
		}
	}
	
	public void testPoly_OLS_tu_OAProvider() {
		OAProvider provider = new OLS_tu_OAProvider(4);
		
		// L64(4^6)
		int[][] rawTestData = provider.get(6);
		int[][] expected = { 
				{ 1, 1, 1, 1, 1, 1}, 
				{ 1, 1, 2, 2, 2, 2}, 
				{ 1, 1, 3, 3, 3 ,3},
				{ 1, 1, 4, 4, 4 ,4},
				{ 1, 2, 1, 2, 3, 4}, 
				{ 1, 2, 2, 1, 4, 3}, 
				{ 1, 2, 3, 4, 1, 2}, 
				{ 1, 2, 4, 3, 2, 1},
				{ 1, 3, 1, 3, 4, 2}, 
				{ 1, 3, 2, 4, 3, 1},
				{ 1, 3, 3, 1, 2, 4},
				{ 1, 3, 4, 2, 1, 3},
				{ 1, 4, 1, 4, 2, 3},
				{ 1, 4, 2, 3, 1, 4},
				{ 1, 4, 3, 2, 4, 1},
				{ 1, 4, 4, 1, 3, 2}, 
				{ 2, 1, 1, 1, 1, 1}, 
				{ 2, 1, 2, 2, 2, 2}, 
				{ 2, 1, 3, 3, 3 ,3},
				{ 2, 1, 4, 4, 4 ,4},
				{ 2, 2, 1, 2, 3, 4}, 
				{ 2, 2, 2, 1, 4, 3}, 
				{ 2, 2, 3, 4, 1, 2}, 
				{ 2, 2, 4, 3, 2, 1},
				{ 2, 3, 1, 3, 4, 2}, 
				{ 2, 3, 2, 4, 3, 1},
				{ 2, 3, 3, 1, 2, 4},
				{ 2, 3, 4, 2, 1, 3},
				{ 2, 4, 1, 4, 2, 3},
				{ 2, 4, 2, 3, 1, 4},
				{ 2, 4, 3, 2, 4, 1},
				{ 2, 4, 4, 1, 3, 2}, 
				{ 3, 1, 1, 1, 1, 1}, 
				{ 3, 1, 2, 2, 2, 2}, 
				{ 3, 1, 3, 3, 3 ,3},
				{ 3, 1, 4, 4, 4 ,4},
				{ 3, 2, 1, 2, 3, 4}, 
				{ 3, 2, 2, 1, 4, 3}, 
				{ 3, 2, 3, 4, 1, 2}, 
				{ 3, 2, 4, 3, 2, 1},
				{ 3, 3, 1, 3, 4, 2}, 
				{ 3, 3, 2, 4, 3, 1},
				{ 3, 3, 3, 1, 2, 4},
				{ 3, 3, 4, 2, 1, 3},
				{ 3, 4, 1, 4, 2, 3},
				{ 3, 4, 2, 3, 1, 4},
				{ 3, 4, 3, 2, 4, 1},
				{ 3, 4, 4, 1, 3, 2}, 
				{ 4, 1, 1, 1, 1, 1}, 
				{ 4, 1, 2, 2, 2, 2}, 
				{ 4, 1, 3, 3, 3 ,3},
				{ 4, 1, 4, 4, 4 ,4},
				{ 4, 2, 1, 2, 3, 4}, 
				{ 4, 2, 2, 1, 4, 3}, 
				{ 4, 2, 3, 4, 1, 2}, 
				{ 4, 2, 4, 3, 2, 1},
				{ 4, 3, 1, 3, 4, 2}, 
				{ 4, 3, 2, 4, 3, 1},
				{ 4, 3, 3, 1, 2, 4},
				{ 4, 3, 4, 2, 1, 3},
				{ 4, 4, 1, 4, 2, 3},
				{ 4, 4, 2, 3, 1, 4},
				{ 4, 4, 3, 2, 4, 1},
				{ 4, 4, 4, 1, 3, 2}, 
		};
		// System.out.println(Arrays.deepToString(rawTestData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected,
				rawTestData));
		
		try {
			rawTestData = provider.get(4);
			fail("The number of factors should be at least t+1.");
		} catch (IllegalArgumentException e) {
			
		}
		
		try {
			rawTestData = provider.get(3);
			fail("The number of factors should be at least t+1.");
		} catch (IllegalArgumentException e) {
			
		}
		
	}

	public void testOAProviderFactory() throws EngineException {
		CascadeOAProviderFactory factory = new CascadeOAProviderFactory();
		assertTrue("It should create H_2s_OAProvider object", factory.create(2,
				3) instanceof H_2s_OAProvider);
		assertTrue("It should create OLS_tu_OAProvider object", factory
				.create(2, 4) instanceof OLS_tu_OAProvider);
		
		assertTrue("It should create OLS_t2_OAProvider object", factory
				.create(3, 3) instanceof OLS_t2_OAProvider);
		assertTrue("It should create OLS_t2_OAProvider object", factory
				.create(3, 4) instanceof OLS_t2_OAProvider);
		assertTrue("It should create OLS_tu_OAProvider object", factory
				.create(3, 5) instanceof OLS_tu_OAProvider);
		
		assertTrue("It should create OLS_t2_OAProvider object", factory
				.create(4, 4) instanceof OLS_t2_OAProvider);
		assertTrue("It should create OLS_t2_OAProvider object", factory
				.create(4, 5) instanceof OLS_t2_OAProvider);
		assertTrue("It should create OLS_tu_OAProvider object", factory
				.create(4, 6) instanceof OLS_tu_OAProvider);
		
		assertTrue("It should create OLS_t2_OAProvider object", factory
				.create(8, 8) instanceof OLS_t2_OAProvider);
		assertTrue("It should create OLS_t2_OAProvider object", factory
				.create(8, 9) instanceof OLS_t2_OAProvider);
		assertTrue("It should create OLS_tu_OAProvider object", factory
				.create(8, 10) instanceof OLS_tu_OAProvider);
		
		try {
			factory.create(0, 2);
			fail("The number of levels should >= 1");
		} catch (IllegalArgumentException e) {
			
		}
		
		try {
			factory.create(1, 1);
			fail("The number of factors should >= 2");
		} catch (IllegalArgumentException e) {
			
		}
		
		SelfRuleOAProviderFactory factory2 = new SelfRuleOAProviderFactory();
		OAProvider provider = factory2.create(2, 4);
		assertEquals(9, provider.get(4).length);
		provider = factory2.create(2, 5);
		assertEquals(16, provider.get(5).length);
		
		provider = factory2.create(3, 5);
		assertEquals(16, provider.get(5).length);
		provider = factory2.create(3, 6);
		assertEquals(25, provider.get(6).length);
		
		provider = factory2.create(4, 6);
		assertEquals(25, provider.get(6).length);
		provider = factory2.create(4, 7);
		assertEquals(49, provider.get(7).length);
		
		provider = factory2.create(6, 7);
		assertEquals(49, provider.get(7).length);
		provider = factory2.create(6, 9);
		assertEquals(64, provider.get(9).length);
		
		provider = factory2.create(10, 7);
		assertEquals(121, provider.get(7).length);
		provider = factory2.create(10, 19);
		assertEquals(361, provider.get(19).length);
		
		provider = factory2.create(20, 19);
		assertEquals(529, provider.get(19).length);
		
		try {
			factory2.create(0, 2);
			fail("The number of levels should >= 1");
		} catch (IllegalArgumentException e) {
			
		}
		
		try {
			factory2.create(1, 1);
			fail("The number of factors should >= 2");
		} catch (IllegalArgumentException e) {
			
		}
	}

	public void testAMEngine() throws EngineException {
		Engine engine = new AMEngine(new MockOAProviderFactory());
		String[][] testData = engine
				.generateTestData(new MockMetaParameterProvider().get());

		String[][] expected = { { "Windows XP", "IE", "255M", "MySQL" },
				{ "Windows XP", "Firefox", "1G", "Oracle" },
				{ "Windows XP", "Opera", "2G", "DB2" },
				{ "Solaris 10", "IE", "1G", "DB2" },
				{ "Solaris 10", "Firefox", "2G", "MySQL" },
				{ "Solaris 10", "Opera", "255M", "Oracle" },
				{ "Red Hat 9", "IE", "2G", "Oracle" },
				{ "Red Hat 9", "Firefox", "255M", "DB2" },
				{ "Red Hat 9", "Opera", "1G", "MySQL" } };
		// System.out.println(Arrays.deepToString(testData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected,
				testData));

		engine = new AMEngine();
		//
		// AMEngine with H_2s_OAProvider
		//
		Factor f1 = new Factor("OS");
		f1.addLevel("Windows XP");
		f1.addLevel("Solaris 10");
		Factor f2 = new Factor("Browser", new String[] { "IE", "Firefox" });
		Factor f3 = new Factor("Memory", new String[] { "1G", "2G" });
		Factor f4 = new Factor("DB", new String[] { "MySQL", "Oracle" });
		Factor f5 = new Factor("PC", new String[] { "DELL", "HP" });
		Factor f6 = new Factor("Server", new String[] { "WebLogic", "Tomcat" });
		Factor f7 = new Factor("HardDisk", new String[] { "40G", "80G" });
		MetaParameter mp = new MetaParameter(2);
		mp.addFactor(f1);
		mp.addFactor(f2);
		mp.addFactor(f3);
		mp.addFactor(f4);
		mp.addFactor(f5);
		mp.addFactor(f6);
		mp.addFactor(f7);

		testData = engine.generateTestData(mp);
		String[][] expected1 = {
				{ "Windows XP", "IE", "1G", "MySQL", "DELL", "WebLogic", "40G" },
				{ "Solaris 10", "IE", "2G", "MySQL", "HP", "WebLogic", "80G" },
				{ "Windows XP", "Firefox", "2G", "MySQL", "DELL", "Tomcat",
						"80G" },
				{ "Solaris 10", "Firefox", "1G", "MySQL", "HP", "Tomcat", "40G" },
				{ "Windows XP", "IE", "1G", "Oracle", "HP", "Tomcat", "80G" },
				{ "Solaris 10", "IE", "2G", "Oracle", "DELL", "Tomcat", "40G" },
				{ "Windows XP", "Firefox", "2G", "Oracle", "HP", "WebLogic",
						"40G" },
				{ "Solaris 10", "Firefox", "1G", "Oracle", "DELL", "WebLogic",
						"80G" } };
		// System.out.println(Arrays.deepToString(testData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected1,
				testData));

		//
		// AMEngine with Rp_OLS_t2_OAProvider
		//
		mp = new MockMetaParameterProvider().get();
		testData = engine.generateTestData(mp);
		String[][] expected2 = { { "Windows XP", "IE", "255M", "MySQL" },
				{ "Windows XP", "Firefox", "1G", "Oracle" },
				{ "Windows XP", "Opera", "2G", "DB2" },
				{ "Solaris 10", "IE", "1G", "DB2" },
				{ "Solaris 10", "Firefox", "2G", "MySQL" },
				{ "Solaris 10", "Opera", "255M", "Oracle" },
				{ "Red Hat 9", "IE", "2G", "Oracle" },
				{ "Red Hat 9", "Firefox", "255M", "DB2" },
				{ "Red Hat 9", "Opera", "1G", "MySQL" } };
		// System.out.println(Arrays.deepToString(testData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected2,
				testData));

		// with missing values
		f1 = new Factor("OS");
		f1.addLevel("Windows XP");
		f1.addLevel("Solaris 10");
		f2 = new Factor("Browser", new String[] { "IE", "Firefox" });
		f3 = new Factor("Memory", new String[] { "255M", "1G", "2G" });
		f4 = new Factor("DB", new String[] { "MySQL", "Oracle" });
		mp = new MetaParameter(2);
		mp.addFactor(f1);
		mp.addFactor(f2);
		mp.addFactor(f3);
		mp.addFactor(f4);

		testData = engine.generateTestData(mp);
		String[][] expected3 = { { "Windows XP", "IE", "255M", "MySQL" },
				{ "Windows XP", "Firefox", "1G", "Oracle" },
				{ "Windows XP", "IE", "2G", "MySQL" },
				{ "Solaris 10", "IE", "1G", "Oracle" },
				{ "Solaris 10", "Firefox", "2G", "MySQL" },
				{ "Solaris 10", "Firefox", "255M", "Oracle" },
				{ "Windows XP", "IE", "2G", "Oracle" },
				{ "Solaris 10", "Firefox", "255M", "MySQL" },
				{ "Windows XP", "IE", "1G", "MySQL" } };
		// System.out.println(Arrays.deepToString(testData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected3,
				testData));

		//
		// AMEngine with Rp_OLS_tu_OAProvider
		//
		f1 = new Factor("OS");
		f1.addLevel("Windows XP");
		f1.addLevel("Solaris 10");
		f2 = new Factor("Browser", new String[] { "IE", "Firefox" });
		f3 = new Factor("Memory", new String[] { "255M", "1G" });
		f4 = new Factor("DB", new String[] { "MySQL", "Oracle" });
		mp = new MetaParameter(2);
		mp.addFactor(f1);
		mp.addFactor(f2);
		mp.addFactor(f3);
		mp.addFactor(f4);

		testData = engine.generateTestData(mp);
		String[][] expected4 = { { "Windows XP", "IE", "255M", "MySQL" },
				{ "Windows XP", "IE", "1G", "Oracle" },
				{ "Windows XP", "Firefox", "255M", "Oracle" },
				{ "Windows XP", "Firefox", "1G", "MySQL" },
				{ "Solaris 10", "IE", "255M", "MySQL" },
				{ "Solaris 10", "IE", "1G", "Oracle" },
				{ "Solaris 10", "Firefox", "255M", "Oracle" },
				{ "Solaris 10", "Firefox", "1G", "MySQL" } };
		// System.out.println(Arrays.deepToString(testData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected4,
				testData));
		
		//
		// AMEngine with Poly_OLS_t2_OAProvider
		//
		f1 = new Factor("OS");
		f1.addLevel("Windows XP");
		f1.addLevel("Solaris 10");
		f1.addLevel("Vista");
		f1.addLevel("Red Hat Linux 9");
		f2 = new Factor("Browser", new String[] { "IE", "Firefox", "Opera", "MyIE" });
		f3 = new Factor("Memory", new String[] { "1G", "2G" , "256M", "512M"});
		f4 = new Factor("DB", new String[] { "MySQL", "Oracle", "DB2", "SQL Server"});
		f5 = new Factor("PC", new String[] { "DELL", "HP", "IBM", "Apple"});
		mp = new MetaParameter(2);
		mp.addFactor(f1);
		mp.addFactor(f2);
		mp.addFactor(f3);
		mp.addFactor(f4);
		mp.addFactor(f5);
		testData = engine.generateTestData(mp);
		String[][] expected5 = {
				{ "Windows XP", "IE", "1G", "MySQL", "DELL"}, 
				{ "Windows XP", "Firefox", "2G", "Oracle", "HP"}, 
				{ "Windows XP", "Opera", "256M", "DB2" ,"IBM"},
				{ "Windows XP", "MyIE", "512M", "SQL Server" ,"Apple"},
				{ "Solaris 10", "IE", "2G", "DB2", "Apple"}, 
				{ "Solaris 10", "Firefox", "1G", "SQL Server", "IBM"}, 
				{ "Solaris 10", "Opera", "512M", "MySQL", "HP"}, 
				{ "Solaris 10", "MyIE", "256M", "Oracle", "DELL"},
				{ "Vista", "IE", "256M", "SQL Server", "HP"}, 
				{ "Vista", "Firefox", "512M", "DB2", "DELL"},
				{ "Vista", "Opera", "1G", "Oracle", "Apple"},
				{ "Vista", "MyIE", "2G", "MySQL", "IBM"},
				{ "Red Hat Linux 9", "IE", "512M", "Oracle", "IBM"},
				{ "Red Hat Linux 9", "Firefox", "256M", "MySQL", "Apple"},
				{ "Red Hat Linux 9", "Opera", "2G", "SQL Server", "DELL"},
				{ "Red Hat Linux 9", "MyIE", "1G", "DB2", "HP"},
		};
		// System.out.println(Arrays.deepToString(testData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected5,
				testData));
		
		//
		// AMEngine with Poly_OLS_tu_OAProvider
		//
		f1 = new Factor("Server", new String[] { "WebLogic", "WebSphere", "JBoss", "GlassFish"});
		f2 = new Factor("OS");
		f2.addLevel("Windows XP");
		f2.addLevel("Solaris 10");
		f2.addLevel("Vista");
		f2.addLevel("Red Hat Linux 9");
		f3 = new Factor("Browser", new String[] { "IE", "Firefox", "Opera", "MyIE" });
		f4 = new Factor("Memory", new String[] { "1G", "2G" , "256M", "512M"});
		f5 = new Factor("DB", new String[] { "MySQL", "Oracle", "DB2", "SQL Server"});
		f6 = new Factor("PC", new String[] { "DELL", "HP", "IBM", "Apple"});
		mp = new MetaParameter(2);
		mp.addFactor(f1);
		mp.addFactor(f2);
		mp.addFactor(f3);
		mp.addFactor(f4);
		mp.addFactor(f5);
		mp.addFactor(f6);
		testData = engine.generateTestData(mp);
		String[][] expected6 = {
				{ "WebLogic", "Windows XP", "IE", "1G", "MySQL", "DELL"}, 
				{ "WebLogic", "Windows XP", "Firefox", "2G", "Oracle", "HP"}, 
				{ "WebLogic", "Windows XP", "Opera", "256M", "DB2" ,"IBM"},
				{ "WebLogic", "Windows XP", "MyIE", "512M", "SQL Server" ,"Apple"},
				{ "WebLogic", "Solaris 10", "IE", "2G", "DB2", "Apple"}, 
				{ "WebLogic", "Solaris 10", "Firefox", "1G", "SQL Server", "IBM"}, 
				{ "WebLogic", "Solaris 10", "Opera", "512M", "MySQL", "HP"}, 
				{ "WebLogic", "Solaris 10", "MyIE", "256M", "Oracle", "DELL"},
				{ "WebLogic", "Vista", "IE", "256M", "SQL Server", "HP"}, 
				{ "WebLogic", "Vista", "Firefox", "512M", "DB2", "DELL"},
				{ "WebLogic", "Vista", "Opera", "1G", "Oracle", "Apple"},
				{ "WebLogic", "Vista", "MyIE", "2G", "MySQL", "IBM"},
				{ "WebLogic", "Red Hat Linux 9", "IE", "512M", "Oracle", "IBM"},
				{ "WebLogic", "Red Hat Linux 9", "Firefox", "256M", "MySQL", "Apple"},
				{ "WebLogic", "Red Hat Linux 9", "Opera", "2G", "SQL Server", "DELL"},
				{ "WebLogic", "Red Hat Linux 9", "MyIE", "1G", "DB2", "HP"},
				{ "WebSphere", "Windows XP", "IE", "1G", "MySQL", "DELL"}, 
				{ "WebSphere", "Windows XP", "Firefox", "2G", "Oracle", "HP"}, 
				{ "WebSphere", "Windows XP", "Opera", "256M", "DB2" ,"IBM"},
				{ "WebSphere", "Windows XP", "MyIE", "512M", "SQL Server" ,"Apple"},
				{ "WebSphere", "Solaris 10", "IE", "2G", "DB2", "Apple"}, 
				{ "WebSphere", "Solaris 10", "Firefox", "1G", "SQL Server", "IBM"}, 
				{ "WebSphere", "Solaris 10", "Opera", "512M", "MySQL", "HP"}, 
				{ "WebSphere", "Solaris 10", "MyIE", "256M", "Oracle", "DELL"},
				{ "WebSphere", "Vista", "IE", "256M", "SQL Server", "HP"}, 
				{ "WebSphere", "Vista", "Firefox", "512M", "DB2", "DELL"},
				{ "WebSphere", "Vista", "Opera", "1G", "Oracle", "Apple"},
				{ "WebSphere", "Vista", "MyIE", "2G", "MySQL", "IBM"},
				{ "WebSphere", "Red Hat Linux 9", "IE", "512M", "Oracle", "IBM"},
				{ "WebSphere", "Red Hat Linux 9", "Firefox", "256M", "MySQL", "Apple"},
				{ "WebSphere", "Red Hat Linux 9", "Opera", "2G", "SQL Server", "DELL"},
				{ "WebSphere", "Red Hat Linux 9", "MyIE", "1G", "DB2", "HP"},
				{ "JBoss", "Windows XP", "IE", "1G", "MySQL", "DELL"}, 
				{ "JBoss", "Windows XP", "Firefox", "2G", "Oracle", "HP"}, 
				{ "JBoss", "Windows XP", "Opera", "256M", "DB2" ,"IBM"},
				{ "JBoss", "Windows XP", "MyIE", "512M", "SQL Server" ,"Apple"},
				{ "JBoss", "Solaris 10", "IE", "2G", "DB2", "Apple"}, 
				{ "JBoss", "Solaris 10", "Firefox", "1G", "SQL Server", "IBM"}, 
				{ "JBoss", "Solaris 10", "Opera", "512M", "MySQL", "HP"}, 
				{ "JBoss", "Solaris 10", "MyIE", "256M", "Oracle", "DELL"},
				{ "JBoss", "Vista", "IE", "256M", "SQL Server", "HP"}, 
				{ "JBoss", "Vista", "Firefox", "512M", "DB2", "DELL"},
				{ "JBoss", "Vista", "Opera", "1G", "Oracle", "Apple"},
				{ "JBoss", "Vista", "MyIE", "2G", "MySQL", "IBM"},
				{ "JBoss", "Red Hat Linux 9", "IE", "512M", "Oracle", "IBM"},
				{ "JBoss", "Red Hat Linux 9", "Firefox", "256M", "MySQL", "Apple"},
				{ "JBoss", "Red Hat Linux 9", "Opera", "2G", "SQL Server", "DELL"},
				{ "JBoss", "Red Hat Linux 9", "MyIE", "1G", "DB2", "HP"},
				{ "GlassFish", "Windows XP", "IE", "1G", "MySQL", "DELL"}, 
				{ "GlassFish", "Windows XP", "Firefox", "2G", "Oracle", "HP"}, 
				{ "GlassFish", "Windows XP", "Opera", "256M", "DB2" ,"IBM"},
				{ "GlassFish", "Windows XP", "MyIE", "512M", "SQL Server" ,"Apple"},
				{ "GlassFish", "Solaris 10", "IE", "2G", "DB2", "Apple"}, 
				{ "GlassFish", "Solaris 10", "Firefox", "1G", "SQL Server", "IBM"}, 
				{ "GlassFish", "Solaris 10", "Opera", "512M", "MySQL", "HP"}, 
				{ "GlassFish", "Solaris 10", "MyIE", "256M", "Oracle", "DELL"},
				{ "GlassFish", "Vista", "IE", "256M", "SQL Server", "HP"}, 
				{ "GlassFish", "Vista", "Firefox", "512M", "DB2", "DELL"},
				{ "GlassFish", "Vista", "Opera", "1G", "Oracle", "Apple"},
				{ "GlassFish", "Vista", "MyIE", "2G", "MySQL", "IBM"},
				{ "GlassFish", "Red Hat Linux 9", "IE", "512M", "Oracle", "IBM"},
				{ "GlassFish", "Red Hat Linux 9", "Firefox", "256M", "MySQL", "Apple"},
				{ "GlassFish", "Red Hat Linux 9", "Opera", "2G", "SQL Server", "DELL"},
				{ "GlassFish", "Red Hat Linux 9", "MyIE", "1G", "DB2", "HP"},
		};
		// System.out.println(Arrays.deepToString(testData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected6,
				testData));
	}

	public void testUtil() {
		assertTrue("It should be 2^s - 1", MathUtil.is_2sMinusOne(1));
		assertTrue("It should be 2^s - 1", MathUtil.is_2sMinusOne(3));
		assertTrue("It should be 2^s - 1", MathUtil.is_2sMinusOne(7));
		assertTrue("It should be 2^s - 1", MathUtil.is_2sMinusOne(63));
		assertTrue("It should be 2^s - 1", MathUtil.is_2sMinusOne(255));

		assertTrue("It should be a prime", MathUtil.isPrime(2));
		assertTrue("It should be a prime", MathUtil.isPrime(3));
		assertTrue("It should be a prime", MathUtil.isPrime(5));
		assertTrue("It should be a prime", MathUtil.isPrime(7));
		assertTrue("It should be a prime", MathUtil.isPrime(11));
		assertTrue("It should be a prime", MathUtil.isPrime(17));
		assertTrue("It should be a prime", MathUtil.isPrime(67));
		assertFalse("It should not be a prime", MathUtil.isPrime(4));
		assertFalse("It should not be a prime", MathUtil.isPrime(6));
		assertFalse("It should not be a prime", MathUtil.isPrime(10));
		
		String text = TextFile.read("testdata/data1.txt");
		assertEquals("This is a string.\n", text);
		
		assertEquals(2, MathUtil.partOfPrimePower(4)[0]);
		assertEquals(2, MathUtil.partOfPrimePower(4)[1]);
		assertEquals(2, MathUtil.partOfPrimePower(8)[0]);
		assertEquals(3, MathUtil.partOfPrimePower(8)[1]);
		assertEquals(3, MathUtil.partOfPrimePower(9)[0]);
		assertEquals(2, MathUtil.partOfPrimePower(9)[1]);
		assertEquals(5, MathUtil.partOfPrimePower(25)[0]);
		assertEquals(2, MathUtil.partOfPrimePower(25)[1]);
		assertEquals(7, MathUtil.partOfPrimePower(49)[0]);
		assertEquals(2, MathUtil.partOfPrimePower(49)[1]);
		
		assertEquals(3, MathUtil.partOfPrimePower(3)[0]);
		assertEquals(1, MathUtil.partOfPrimePower(3)[1]);
		assertEquals(5, MathUtil.partOfPrimePower(5)[0]);
		assertEquals(1, MathUtil.partOfPrimePower(5)[1]);
		
		assertEquals(4, MathUtil.nextPrimePower(3));
		assertEquals(5, MathUtil.nextPrimePower(4));
		assertEquals(7, MathUtil.nextPrimePower(5));
		assertEquals(9, MathUtil.nextPrimePower(8));
		assertEquals(11, MathUtil.nextPrimePower(9));
		assertEquals(13, MathUtil.nextPrimePower(11));
	}

	public void testMetaParameterProvider() throws MetaParameterException {
		String xmlData = "<?xml version='1.0' encoding='UTF-8'?>" +
				"<metaparameter>" +
				"	<strength>2</strength>" +
				"	<factor>" +
				"		<name>OS</name>" +
				"		<level>Windows XP</level>" +
				"		<level>Solaris 10</level>" +
				"		<level>Red Hat 9</level>" +
				"	</factor>" +
				"	<factor>" +
				"		<name>Browser</name>" +
				"		<level>IE</level>" +
				"		<level>Firefox</level>" +
				"		<level>Opera</level>" +
				"	</factor>" +
				"	<factor>" +
				"		<name>Memory</name>" +
				"		<level>255M</level>" +
				"		<level>1G</level>" +
				"		<level>2G</level>" +
				"	</factor>" +
				"	<factor>" +
				"		<name>DB</name>" +
				"		<level>MySQL</level>" +
				"		<level>Oracle</level>" +
				"		<level>DB2</level>" +
				"	</factor>" +
				"</metaparameter>";
		String schemaPath = "schema/MetaParameter.xsd";
		IMetaParameterProvider provider = new XMLMetaParameterProvider(xmlData, schemaPath);
		MetaParameter mp = provider.get();
		MetaParameter expected = new MockMetaParameterProvider().get();
		// System.out.println(mp);
		assertEquals(expected, mp);
		
		String dataFile = "testdata/MetaParameter.xml";
		provider = new XMLMetaParameterProvider(TextFile.read(dataFile), schemaPath);
		mp = provider.get();
		// System.out.println(mp);
		assertEquals(expected, mp);
		
		// Invalid XML
//		for (int i = 3; i <= 3; i++) {
//			dataFile = "testdata/InvalidMetaParameter" + i + ".xml";
//			provider = new XMLMetaParameterProvider(TextFile.read(dataFile));
//			try {			
//				mp = provider.get();
//				fail(dataFile + ": Should throw MetaParameterException");
//			} catch (MetaParameterException e) {
//				System.out.println("fdfd");
//			}
//		}
	}
	
	public void testMetaParameterXMLSerializer() throws MetaParameterException {
		MetaParameter mp = new MockMetaParameterProvider().get();
		
		MetaParameterXMLSerializer serializer = new MetaParameterXMLSerializer();
		// System.out.println(serializer.serialize(mp));

		String schemaPath = "schema/MetaParameter.xsd";
		IMetaParameterProvider provider = new XMLMetaParameterProvider(serializer.serialize(mp), schemaPath);
		
		MetaParameter mp2 = provider.get();
		assertEquals(mp, mp2);
		
		// Escape special characters
		mp = new MetaParameter();
		Factor f = new Factor("<&OS&>", new String[] {"Windows", "Linux"});
		Factor f2 = new Factor("DB", new String[] {"<=MySQL=>", ">=DB2<="});
		mp.addFactor(f);
		mp.addFactor(f2);
		mp.addConstraint("IF [File system] = \"FAT\" THEN [Size] <= 4096");
		String xmlData = serializer.serialize(mp);
		String expected = "<?xml version=\"1.0\"?>\n" + 
		"<metaparameter>" +
		"<strength>2</strength>" +
		"<factor><name>&lt;&amp;OS&amp;&gt;</name>" +
		"<level>Windows</level>" +
		"<level>Linux</level>" +
		"</factor>" +
		"<factor><name>DB</name>" +
		"<level>&lt;=MySQL=&gt;</level>" +
		"<level>&gt;=DB2&lt;=</level>" +
		"</factor>" +
		"<constraint>IF [File system] = \"FAT\" THEN [Size] &lt;= 4096</constraint>" + 
		"</metaparameter>\n";
		assertEquals(expected, xmlData);
		provider = new XMLMetaParameterProvider(xmlData, schemaPath);
		mp2 = provider.get();
		assertEquals(mp, mp2);
	}
	
	public void testTestCasesGenerator() throws EngineException {
		ITestCasesGenerator generator = new TXTTestCasesGenerator();
		MetaParameter mp = new MockMetaParameterProvider().get();
		String[][] testData = new MockOAEngine().generateTestData(mp);
		String testCases = generator.generate(mp, testData);
		String expectedTestCases = "OS	Browser	Memory	DB\n"
			+ "Windows XP	IE	255M	MySQL\n"
			+ "Windows XP	Firefox	1G	Oracle\n"
			+ "Windows XP	Opera	2G	DB2\n"
			+ "Solaris 10	IE	1G	DB2\n"
			+ "Solaris 10	Firefox	2G	MySQL\n"
			+ "Solaris 10	Opera	255M	Oracle\n"
			+ "Red Hat 9	IE	2G	Oracle\n"
			+ "Red Hat 9	Firefox	255M	DB2\n"
			+ "Red Hat 9	Opera	1G	MySQL\n";
		// System.out.println(testCases);
		assertEquals(expectedTestCases, testCases);
		
		generator = new XMLTestCasesGenerator();
		testCases = generator.generate(mp, testData);
		
		String expectedTestCases2 = "<?xml version=\"1.0\"?>\n" +
				"<testcases>" +
				"<factor>OS</factor>" +
				"<factor>Browser</factor>" +
				"<factor>Memory</factor>" +
				"<factor>DB</factor>" +
				"<run>" +
				"<level>Windows XP</level>" +
				"<level>IE</level>" +
				"<level>255M</level>" +
				"<level>MySQL</level>" +
				"</run>" +
				"<run>" +
				"<level>Windows XP</level>" +
				"<level>Firefox</level>" +
				"<level>1G</level>" +
				"<level>Oracle</level>" +
				"</run>" +
				"<run>" +
				"<level>Windows XP</level>" +
				"<level>Opera</level>" +
				"<level>2G</level>" +
				"<level>DB2</level>" +
				"</run>" +
				"<run>" +
				"<level>Solaris 10</level>" +
				"<level>IE</level>" +
				"<level>1G</level>" +
				"<level>DB2</level>" +
				"</run>" +
				"<run>" +
				"<level>Solaris 10</level>" +
				"<level>Firefox</level>" +
				"<level>2G</level>" +
				"<level>MySQL</level>" +
				"</run>" +
				"<run>" +
				"<level>Solaris 10</level>" +
				"<level>Opera</level>" +
				"<level>255M</level>" +
				"<level>Oracle</level>" +
				"</run>" +
				"<run>" +
				"<level>" + "Red Hat 9</level>" +
				"<level>IE</level>" +
				"<level>2G</level>" +
				"<level>Oracle</level>" +
				"</run>" +
				"<run>" +
				"<level>Red Hat 9</level>" +
				"<level>Firefox</level>" +
				"<level>255M</level>" +
				"<level>DB2</level>" +
				"</run>" +
				"<run>" +
				"<level>Red Hat 9</level>" +
				"<level>Opera</level>" +
				"<level>1G</level>" +
				"<level>MySQL</level>" +
				"</run>" +
				"</testcases>\n";
		// System.out.println(testCases);
		assertEquals(expectedTestCases2, testCases);
	}
	
//	public void testPairwiseTestingService() {
//		String xmlData = "<?xml version='1.0' encoding='UTF-8'?>" +
//		"<metaparameter>" +
//		"	<strength>2</strength>" +
//		"	<factor>" +
//		"		<name>OS</name>" +
//		"		<level>Windows XP</level>" +
//		"		<level>Solaris 10</level>" +
//		"	</factor>" +
//		"	<factor>" +
//		"		<name>Browser</name>" +
//		"		<level>IE</level>" +
//		"		<level>Firefox</level>" +
//		"	</factor>" +
//		"	<factor>" +
//		"		<name>Memory</name>" +
//		"		<level>255M</level>" +
//		"		<level>1G</level>" +
//		"	</factor>" +
//		"</metaparameter>";
//		String expectedTestCases = "<?xml version=\"1.0\"?>\n" +
//		"<testcases>" +
//		"<factor>OS</factor>" +
//		"<factor>Browser</factor>" +
//		"<factor>Memory</factor>" +
//		"<run>" +
//		"<level>Windows XP</level>" +
//		"<level>IE</level>" +
//		"<level>255M</level>" +
//		"</run>" +
//		"<run>" +
//		"<level>Solaris 10</level>" +
//		"<level>IE</level>" +
//		"<level>1G</level>" +
//		"</run>" +
//		"<run>" +
//		"<level>Windows XP</level>" +
//		"<level>Firefox</level>" +
//		"<level>1G</level>" +
//		"</run>" +
//		"<run>" +
//		"<level>Solaris 10</level>" +
//		"<level>Firefox</level>" +
//		"<level>255M</level>" +
//		"</run>" +
//		"</testcases>\n";
//		PairwiseTestingServiceImpl service = new PairwiseTestingServiceImpl();
//		assertEquals(expectedTestCases, service.PariwiseTesting(xmlData, "AMEngine"));
//	}
	
	public void testFileMetaParameterProvider() throws MetaParameterException {
		FileMetaParameterProvider provider = new ExcelMetaParameterProvider("testdata/MetaParameter.xls");
		MetaParameter expected = new MetaParameter(2);
		Factor f1 = new Factor("操作系统");
		f1.addLevel("Windows");
		f1.addLevel("Linux");
		f1.addLevel("Mac OS X");
		f1.addLevel("Solaris");
		f1.addLevel("SUSE");
		Factor f2 = new Factor("数据库");
		f2.addLevel("DB2");
		f2.addLevel("Oracle");
		f2.addLevel("MySQL");
		f2.addLevel("SQL Server");
		Factor f3 = new Factor("应用服务器");
		f3.addLevel("JBoss");
		f3.addLevel("WebSphere");
		f3.addLevel("WebLogic");
		f3.addLevel("Tomcat");
		f3.addLevel("GlassFish");
		f3.addLevel("Jetty");
		expected.addFactor(f1);
		expected.addFactor(f2);
		expected.addFactor(f3);
		MetaParameter mp = provider.get();
		// System.out.println(mp);
		assertEquals("ExcelMetaParameterProvider should be OK", expected, mp);
		
		try {
			provider = new ExcelMetaParameterProvider(null);
			fail("It should not accept null value");
		} catch (NullPointerException e) {
			
		}
		
		provider = new TextMetaParameterProvider("testdata/MetaParameter.txt");
		mp = provider.get();
		// System.out.println(mp);
		assertEquals("TextMetaParameterProvider should be OK", expected, mp);
		
		provider = new WordMetaParameterProvider("testdata/MetaParameter_columns.doc");
		mp = provider.get();
		// System.out.println(mp);
		assertEquals("WordMetaParameterProvider(columns) should be OK", expected, mp);
		
		provider = new WordMetaParameterProvider("testdata/MetaParameter_rows.doc");
		mp = provider.get();
		// System.out.println(mp);
		assertEquals("WordMetaParameterProvider(rows) should be OK", expected, mp);
	}
	
	public void testFileMetaParameterProviderFactory() {
		FileMetaParameterProviderFactory factory = new FileMetaParameterProviderFactory();
		assertEquals(ExcelMetaParameterProvider.class, factory.create(
				"testdata/MetaParameter.xls").getClass());
		assertEquals(WordMetaParameterProvider.class, factory.create(
				"testdata/MetaParameter_columns.doc").getClass());
		assertEquals(TextMetaParameterProvider.class, factory.create(
				"testdata/MetaParameter.txt").getClass());
		
		try {
			factory.create(null);
			fail("It should not accept null value");
		} catch (NullPointerException e) {
			
		}
		
		assertNull(factory.create("testdata/MetaParameter.mp3"));
	}
	
	public void testLevelGenerator() {
		ILevelGenerator lg = new EP_BVA_IntegerLevelGenerator(0, 10);
		String[] expected = new String[] {"-10", "-1", "0", "1", "5", "9", "10", "11", "20"};
		assertTrue(Arrays.equals(expected, lg.generateLevels()));
		lg = new EP_BVA_IntegerLevelGenerator(-10, 15);
		expected = new String[] {"-35", "-11", "-10", "-9", "2", "14", "15", "16", "40"};
		assertTrue("EP_BVA_IntegerLevelGenerator should be OK", 
				Arrays.equals(expected, lg.generateLevels()));
		
		lg = new EP_BVA_IntegerLevelGenerator(3, 3);
		expected = new String[] {"0", "2", "3", "4", "6"};
		assertTrue(Arrays.equals(expected, lg.generateLevels()));
		
		try {
			lg = new EP_BVA_IntegerLevelGenerator(8, 7);
			fail("The range start should not larger than the range end.");
		} catch(IllegalArgumentException e) {
			
		}
		
		expected = new String[] {"Student", "Internal", "Normal"};
		lg = new EnumLevelGenerator(AccountType.class);
		assertTrue(Arrays.equals(expected, lg.generateLevels()));
		
		try {
			lg = new EnumLevelGenerator(null);
			fail("It should not accept null value.");
		} catch(NullPointerException e) {
			
		}
		
		expected = new String[] {"true", "false"};
		lg = new BooleanLevelGenerator();
		assertTrue(Arrays.equals(expected, lg.generateLevels()));
		
		lg = new AbstractTypeLevelGenerator("pairwisetesting.test.edu.IEducationManager", "bin");
		expected = new String[] {
				"pairwisetesting.test.edu.EducationManager1", 
				"pairwisetesting.test.edu.EducationManager2", 
				"pairwisetesting.test.edu.EducationManager3"};
		// System.out.println(Arrays.toString(lg.generateLevels()));
		assertTrue(Arrays.equals(expected, lg.generateLevels()));
		
		lg = new AbstractTypeLevelGenerator("pairwisetesting.test.edu.AbstractEducationManager", "bin");
		expected = new String[] {
				"pairwisetesting.test.edu.EducationManager1", 
				"pairwisetesting.test.edu.EducationManager2", 
				"pairwisetesting.test.edu.EducationManager3"};
		// System.out.println(Arrays.toString(lg.generateLevels()));
		assertTrue(Arrays.equals(expected, lg.generateLevels()));
	}
	
	public void testExcelTestCasesGenerator() throws EngineException {
		ITestCasesGenerator generator = new ExcelTestCasesGenerator("testcases.xls");
		MetaParameter mp = new MockMetaParameterProvider().get();
		String[][] testData = new MockOAEngine().generateTestData(mp);
		String testCases = generator.generate(mp, testData);
		assertNotNull(testCases);
	}
	
	public void testConverter() {
		assertEquals(345, Converter.convertTo("345", int.class));
		assertEquals(345L, Converter.convertTo("345", long.class));
		assertEquals(345.7f, Converter.convertTo("345.7f", float.class));
		assertEquals(345.88d, Converter.convertTo("345.88", double.class));
		assertEquals('A', Converter.convertTo("A", Character.class));
		assertEquals(AccountType.Student, Converter.convertTo("Student", AccountType.class));
		assertEquals("Name", Converter.convertTo("Name", String.class));
		assertEquals(true, Converter.convertTo("True", Boolean.class));
		assertEquals(Date.class, Converter.convertTo("2008-4-25", Date.class).getClass());
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
