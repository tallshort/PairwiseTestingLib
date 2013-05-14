package pairwisetesting.engine.tvg;

import java.util.ArrayList;
import java.util.Date;

import pairwisetesting.engine.PTSInterface;




public class TVG implements PTSInterface{


    /** 1000 milliseconds or one second in time */
    public static final int ONE_SECOND = 1000;
    /** TRUE if logging is desirable, FALSE if no logging is required */
    public static boolean isLogging = false;
    /** TRUE if partial logging is desired, FALSE if verbose logging is required */
    public static boolean isPartLog = false;


    /**
     * An ArrayList to hold seperate lists of Data Values for the individual input Variables. (2 dimensional ArrayList)
     */
    public ArrayList<ArrayList<String>> dataValues = new ArrayList<ArrayList<String>>();
    /**
     * An ArrayList to hold the seperate lists of Input Variables associated with each output Variable. (2 dimensional
     * ArrayList)
     */
    public ArrayList<ArrayList<String>> iorList = new ArrayList<ArrayList<String>>();
    
    public ArrayList<String> outputList;
    
    /** The list of names of the the input variables */
    private ArrayList<String> inputNamesList = new ArrayList<String>();
    /** The list of names of the output variables */
    private ArrayList<String> outputNamesList = new ArrayList<String>();
    /** The starting time associated with the beginning of an algorithmic run. */
    private Date startTime;
    /** A SwingWorker subclass used to control the thread running the algorithm and to control the progress monitor */
    private TVGGenerationTask task;
    /**
     * In coordination with the GenerationTask this is used to monitor the progress of a given running algorithmic
     * thread
     */
    //private ProgressMonitor progMon;
    /** This is used with the ProgressMonitor to update the progress of a given algorithmic thread */
    /** Used as N for the N-Way algorithm. */
    private int nforNWay = 2;
    
    private boolean checkForInputData =true;
    
    
    public TVG(){
    	
    }
    
    public TVG(int n){
    	this.setNforNway(n);
    }
    
    public TVG(ArrayList<String> inputNamesList,ArrayList<ArrayList<String>> dataValues){
    	this.inputNamesList = inputNamesList;
    	this.dataValues = dataValues;    	
    }
    
    
    public TVG(ArrayList<String> inputNamesList,ArrayList<ArrayList<String>> dataValues,int nWay){
    	initEngine(inputNamesList,dataValues,nWay);
    }

    public void initEngine(ArrayList<String> iNames,
			ArrayList<ArrayList<String>> iValues,int nWay){
		this.inputNamesList = iNames;
		this.dataValues = iValues;
		this.setNforNway(nWay);
		
	}
    
    
    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     */
    public void setNforNway( int value ) {
        if ( value < 0 ) {
            value = value * -1;
        }
        if ( value > this.inputNamesList.size() ) {
            this.nforNWay = this.inputNamesList.size();
        } else {
            this.nforNWay = value;
        }
        if ( value == 0 ) {
            this.nforNWay = 1;
        }
    }

    /**
     * Inspector used to return the int value for the n-way matching
     *
     * @return int value used for n-way matching
     */
    public int getNforNway() {
        return this.nforNWay;
    }

    

    /**
     * DOCUMENT ME!
     *
     * @param aStart DOCUMENT ME!
     */
    public void setStartTime( Date aStart ) {
        this.startTime = aStart;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public long getStartTime() {
        if ( this.startTime == null ) {
            return -1;
        }
        return this.startTime.getTime();
    }

    

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean checkForInputData() {
        boolean check = true;
        String errorName = null;
        for ( int i = 0; i < this.dataValues.size(); i++ ) {
            ArrayList<String> tempList = this.dataValues.get( i );
            if ( tempList.size() < 1 ) {
                errorName = (String)this.inputNamesList.get( i );
                System.out.println( "Input Variable: " + errorName + " must have at least 1 (one) data Value." );
                check = false;
            }
        }
        if ( !check ) {
            this.checkForInputData = false;
        }
        return check;
    }

    /**
     * DOCUMENT ME!
     */
    private void genNWay() {
        task = new TVGGenerationTask(this,this.inputNamesList,null,null,null);
        task.genNWayTaskSize();
        task.genNWaygo();
    }

    public void genNWayPart2( TVGTaskNWay nwayTask ) {
        this.outputNamesList = nwayTask.getOutputList();
        this.iorList = nwayTask.getIORList();
        System.out.println("Finished Generating "+this.nforNWay+"-Way IOR.");
    }

    

    /**
     * DOCUMENT ME!
     */
    private void genTRed() {
		if (checkForInputData()) {
			this.checkForInputData = true;
			System.out.println("T-reduction started");
			task = new TVGGenerationTask(this, this.inputNamesList,
					this.dataValues, this.outputNamesList, this.iorList);
			task.genIorXP();
			System.out.println("length of task is: " + task.getLengthOfTask());
			System.out.println("max int is: " + Integer.MAX_VALUE);
			this.outputList = task.redTgo();
		}
	}


    public void buildIorList(){
    	for(int index = 0;index<iorList.size();index++){
    		ArrayList<String> tempList = iorList.get(index);
    		tempList.add(0, this.outputNamesList.get(index));
    	}
    	
    }
    
        
    public boolean startAlgorithm(){
		genNWay();
		buildIorList();
		genTRed();
		return this.checkForInputData;
    }
    
    public boolean startAlgorithm(int n){
		setNforNway(n);
		genNWay();
		buildIorList();
		genTRed();
		return this.checkForInputData;
    }
	
    public void setdataValues(ArrayList<ArrayList<String>> tempArray){
    	this.dataValues = tempArray;    	
    }
    
    public void setinputNamesList(ArrayList<String> tempArray){
    	this.inputNamesList = tempArray;
    }
    
    public ArrayList<ArrayList<String>> getIorList(){
    	return iorList;
    }
    
    public ArrayList<String> getOutputList(){
    	ArrayList<String> tempList = this.outputList;
    	for(int i=0;i<tempList.size();i++){
    		String temp = tempList.get(i).replaceAll(":", ",");
    		temp = temp.substring(0, temp.length()-1);
    		tempList.set(i, temp);
    	}
    	
    	return tempList;
    }
    
    public ArrayList<String> getOutputNameList(){
    	return this.outputNamesList;
    }
    
    public int getNforNway(int n){
    	return this.nforNWay;
    }
    
    
	
	
}
