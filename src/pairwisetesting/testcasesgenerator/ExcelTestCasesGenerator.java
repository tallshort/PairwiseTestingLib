package pairwisetesting.testcasesgenerator;

import java.io.File;

import com.google.common.base.Preconditions;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import pairwisetesting.coredomain.ITestCasesGenerator;
import pairwisetesting.coredomain.MetaParameter;

/**
 * The generator generates test cases with Excel format.
 */
public class ExcelTestCasesGenerator implements ITestCasesGenerator {

	private String filePath;

	/**
	 * Constructs a generator with the specified Excel file path.
	 * 
	 * @param filePath
	 *            the file path to store the test cases
	 * @throws NullPointerException
	 *             if {@code filePath} is null
	 */
	public ExcelTestCasesGenerator(String filePath) {
		Preconditions.checkNotNull(filePath, "Excel file path");
		this.filePath = filePath;
	}

	/**
	 * Returns the test cases same as {@link TXTTestCasesGenerator}. It will
	 * also store the test cases to the specified Excel file path.
	 * 
	 * @see
	 * pairwisetesting.coredomain.ITestCasesGenerator#generate(pairwisetesting
	 * .coredomain.MetaParameter, java.lang.String[][])
	 */
	public String generate(MetaParameter mp, String[][] testData) {
		try {
			WritableWorkbook workbook = Workbook.createWorkbook(new File(
					filePath));
			WritableSheet sheet = workbook.createSheet("Test Cases", 0);

			WritableFont font = new WritableFont(WritableFont.ARIAL, 10);
			WritableCellFormat format = new WritableCellFormat(font);

			String[] factorNames = mp.getFactorNames();
			for (int i = 0; i < factorNames.length; i++) {
				Label factorLabel = new Label(i, 0, factorNames[i], format);
				sheet.addCell(factorLabel);
			}

			for (int i = 0; i < testData.length; i++) {
				for (int j = 0; j < testData[0].length; j++) {
					Label levelLabel = new Label(j, i + 1, testData[i][j],
							format);
					sheet.addCell(levelLabel);
				}
			}
			workbook.write();
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new TXTTestCasesGenerator().generate(mp, testData);
	}

	/**
	 * Returns the file path where stores the test cases.
	 * 
	 * @return the file path where stores the test cases
	 */
	public String getFilePath() {
		return this.filePath;
	}

}
