package pairwisetesting.engine.jenny;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import pairwisetesting.coredomain.Engine;
import pairwisetesting.coredomain.EngineException;
import pairwisetesting.coredomain.Factor;
import pairwisetesting.coredomain.MetaParameter;

/**
 * The engine based on the tool
 * <a href="http://burtleburtle.net/bob/math/jenny.html">Jenny<a>.
 * 
 * @see JennyTestDataTransformer
 */
public class JennyEngine extends Engine {

	public JennyEngine() {
		this.transformer = new JennyTestDataTransformer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pairwisetesting.coredomain.Engine#generateRawTestData(pairwisetesting
	 * .coredomain.MetaParameter)
	 */
	@Override
	protected String[][] generateRawTestData(MetaParameter mp)
			throws EngineException {
		
		StringBuilder command = new StringBuilder();
		command.append("jenny -n").append(mp.getStrength()).append(" ");
		
		for (Factor factor : mp.getFactors()) {
			command.append(factor.getNumOfLevels()).append(" ");
		}
		// System.out.println(command.toString());
		
		String[][] testData = null;
		ArrayList<String[]> testDataList = new ArrayList<String[]>();
                
		try {
			Process p = Runtime.getRuntime().exec(command.toString());						
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
			String strProc = null;
			while ((strProc = in.readLine()) != null) {
				String[] testDataRow = strProc.trim().split(" ");
				testDataList.add(testDataRow);
			}
			in.close();                       					
		} catch (IOException e) {
			e.printStackTrace();
			throw new EngineException("jenny.exe error!");
		}
                
		testData = new String[testDataList.size()][];
		for (int i = 0; i < testDataList.size(); i++) {
			testData[i] = testDataList.get(i);                            
		}
		return testData;
	}

}
