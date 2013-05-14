package pairwisetesting.engine.pict;

import java.util.ArrayList;
import java.util.Arrays;

import pairwisetesting.coredomain.Engine;
import pairwisetesting.coredomain.EngineException;
import pairwisetesting.coredomain.Factor;
import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.engine.PTSInterface;

/**
 * The engine based on the tool
 * <a href="http://msdn.microsoft.com/en-us/testing/bb980925.aspx">PICT<a>.
 * 
 */
public class PICTEngine extends Engine {

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
		
		ArrayList<String> iNames = new ArrayList<String>(Arrays.asList(mp.getFactorNames()));
		
		ArrayList<ArrayList<String>> iValues = new ArrayList<ArrayList<String>>();
		
		for(Factor factor:mp.getFactors()){
			iValues.add(factor.getLevelList());
		}
		
		int nWay = mp.getStrength();
		
		PTSInterface pict = new PICT();
		
		pict.initEngine(iNames, iValues, nWay);
		
		pict.startAlgorithm();
		ArrayList<String> resultArrayList = pict.getOutputList();
		
		String[][] testData = new String[resultArrayList.size()][];
		
		for(int i = 0; i<resultArrayList.size();i++){
			testData[i] = resultArrayList.get(i).trim().split(",");	
		}
		
		return testData;
	}
}
