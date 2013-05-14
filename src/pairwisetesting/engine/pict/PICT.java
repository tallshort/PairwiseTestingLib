package pairwisetesting.engine.pict;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import pairwisetesting.engine.PTSInterface;


public class PICT implements PTSInterface {
	ArrayList<String> inputNamesList;
	ArrayList<ArrayList<String>> inputValuesList;
	ArrayList<String> outputList;
	int nWay;
	File outFile;
	
	public PICT(){
		
	}

	public PICT(ArrayList<String> iNames,
			ArrayList<ArrayList<String>> iValues,int nWay) {
		initEngine(iNames,iValues,nWay);
		
	}
	
	public void initEngine(ArrayList<String> iNames,
			ArrayList<ArrayList<String>> iValues,int nWay){
		this.inputNamesList = iNames;
		this.inputValuesList = iValues;
		this.nWay = nWay;
		
	}

	public ArrayList<String> getOutputList() {
		return this.outputList;
	}
	
	private void buildInputFile(){
		
        outFile = new File("input.txt");

        BufferedWriter fout = null;
        try {
            fout = new BufferedWriter( new FileWriter( outFile ) );
        } catch ( IOException ex ) {
            System.out.println( "failed to create input.txt" );
        }
        String iName = "";
        ArrayList<String> tempArray;

        for ( int i = 0; i < this.inputNamesList.size(); i++ ) {
            iName = this.inputNamesList.get( i );
            tempArray = this.inputValuesList.get( i );
            try {
                fout.write( iName +":" );
            } catch ( IOException ex1 ) {}
            String dVal = "";
            for ( int j = 0; j < tempArray.size(); j++ ) {
                dVal = tempArray.get( j );
                if ( j == tempArray.size()-1 ) {
                    try {
                        fout.write( dVal);
                    } catch ( IOException ex5 ) {}
                } else {
                    try {
                        fout.write(dVal + ",");
                    } catch ( IOException ex2 ) {}
                }
            }
            try {
                fout.newLine();
            } catch ( IOException ex3 ) {}
        }
        
        try {
            fout.flush();
            fout.close();
        } catch ( IOException ex8 ) {}
	}

	public boolean startAlgorithm() {
		buildInputFile();

		try {
			String command = "pict input.txt"+" /o:" + this.nWay;
			Process p = Runtime.getRuntime().exec(command);
			
			outputList = new ArrayList<String>();
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String strProc;
			in.readLine();
			while ((strProc = in.readLine()) != null) {
				strProc = strProc.replaceAll("\t", ",");
				outputList.add(strProc);
			}
			in.close();

		} catch (IOException e) {
			System.out.println("IOException ");
		}
		if(outputList.isEmpty()){
			return false;
		}else{
			return true;
		}
	}
}
