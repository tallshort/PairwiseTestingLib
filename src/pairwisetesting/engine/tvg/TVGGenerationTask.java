package pairwisetesting.engine.tvg;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.math.BigInteger;

import java.util.ArrayList;
import java.util.Date;



public class TVGGenerationTask {


    /** parent Frame of this class. Need an instance to access the methods of the MainFrame. */
    public TVG pMain;
    private ArrayList<ArrayList<String>> dataValues;
    private ArrayList<String> inputNames;
    private ArrayList<ArrayList<String>> iors;
    private ArrayList<ArrayList<String>> iors_xp;
    private ArrayList<String> outputNames;
    private String statMessage;
    private boolean[] uniqueList;
    private long current = 0;
    private long lengthOfTask;


    /**
     * Creates a new PTSGenerationTask object.  The PTSGenerationTask classs is actually a Thread Factory, that will
     * create and return the appropriate Task* class to run the selected algorithm.  All of the Algorithms are run in
     * seperate threads in an attempt to not interfere in the GUI updates and because the progress monitor requires it.
     *
     * @param aFrame PariwiseTestingMain Object passed in
     * @param iN Input names ArrayList
     * @param dV dataValues ArrayList
     * @param oN output names ArrayList
     * @param ir Input output mappings
     * @param file desired output file
     * @param headerComments comments to go at the top of the output file
     */
    public TVGGenerationTask( TVG aMain, ArrayList<String> iN, ArrayList<ArrayList<String>> dV, ArrayList<String> oN, ArrayList<ArrayList<String>> ir) {
        this.inputNames = iN;
        this.dataValues = dV;
        this.outputNames = oN;
        this.iors = ir;
        this.pMain = aMain;
    }


    /**
     * Sets the current progress of the Task.  This value is used by the progress monitor.
     *
     * @param val int value of the current progress in the task.
     */
    public void setCurrent( long val ) {
        this.current = val;
    }

    /**
     * Returns an int value of the current progress of the selected Task.  This method is called by the progress
     * Monitor.
     *
     * @return The current progress of the Task.
     */
    public long getCurrent() {
        return current;
    }

    /**
     * Generates and returns the size of the full combinatorial of the input values.
     *
     * @return int value of the size of the full combinatorial data set.
     */
    public long getFullXPSize() {
        long size = 1;
        for ( int m = 0; m < this.dataValues.size(); m++ ) {
            int val = ( this.dataValues.get( m ) ).size();
            size = ( val > 0 ) ? ( size * val ) : size;
        }
        return size;
    }

    /**
     * Sets the length of the Task.  Used by the task to report how long it will take.
     *
     * @param val int value
     */
    public void setLengthOfTask( long val ) {
        this.lengthOfTask = val;
    }

    /**
     * Returns the length of the selected Task.  Used by the progress monitor along with getCurrent() to
     * show the current progress.
     *
     * @return int value
     */
    public long getLengthOfTask() {
        return lengthOfTask;
    }

    /**
     * Returns the current message for the stats of the progress monitor.
     *
     * @return String message
     */
    public String getMessage() {
        return statMessage;
    }

    /**
     * Sets the start time of the running algorithm, so that it can be used in calculating how long the
     * algorithm could take to run.
     *
     * @param aTime a Date object, usually the current system time when the algorithm started processing.
     */
    public void setStartTime( Date aTime ) {
        this.pMain.setStartTime( aTime );
    }

    /**
     * isUnique() does the job of determining whether the input influences for cIOR are contained wholely in any other
     * IOR contained in IORVector
     *
     * @param subList is the individual IOR to check for uniqueness
     * @param index is the index of the current ior.
     */
    public void isUnique( ArrayList<String> subList, int index ) {
        boolean isunique = true;
        ArrayList<String> tempIOR = null;
        for ( int i = 0; i < this.iors.size(); i++ ) {
            tempIOR = this.iors.get( i );
            if ( i != index ) {
                if ( tempIOR.size() > subList.size() ) {
                    isunique = isunique && !( tempIOR.containsAll( subList ) );
                } else if ( ( tempIOR.size() == subList.size() ) && ( index < i ) ) {
                    isunique = isunique && !( tempIOR.containsAll( subList ) );
                }
            }
        }
        this.uniqueList[index] = isunique;
    }

    /**
     * Checks the uniqueness of the iors in the IORList.
     */
    public void checkUnique() {
        this.uniqueList = new boolean[this.iors.size()];
        for ( int i = 0; i < this.uniqueList.length; i++ ) {
            this.uniqueList[i] = true;
        }
        ArrayList<String> cIOR = null;
        for ( int i = 0; i < this.iors.size(); i++ ) {
            cIOR = this.iors.get( i );
            cIOR.remove( 0 );
            isUnique( cIOR, i );
        }
    }

    /**
     * When the current value is equal to or larger than the length of the task, true is
     * returned.  This allows the tracking of progress and allows for a stopping point
     * when running algorithms that otherwise would not have a determinite endding point.
     *
     * @return True if the current value is greater than or equal to the length of the task.
     */
    public boolean done() {
        if ( current >= lengthOfTask ) {
            return true;
        }
        return false;
    }

    /**
     * Generates the IOR cross product to be used in the calculation of the Reduced
     * sets.<br>
     * The IOR cross product is generated from the point of view of the program outputs.<br>
     * Only unique IORs are included in the generation process.<br>
     * The basic structure is as follows:<br>
     * <P>The First output variable that represents a unique IOR is selected.<br>
     * The combination of the Data Values associated with the input Variables that are determined to
     * influence the value of the output Variables. <br>
     * Any input variables not included in the IOR are left null and considered to be 'don't cares'<br>
     * The lists are stored in one ArrayList and represent all the necessary combinations to be tested
     * according to the IOR.
     * </P>
     */
    public void genIorXP() {
        this.checkUnique();
        if ( TVG.isLogging ) {
            System.out.print( "Output names list: " + this.outputNames.size() );
            for ( int i = 0; i < this.outputNames.size(); i++ ) {
                System.out.print( "  [" + i + "]" + this.outputNames.get( i ) );
            }
            System.out.print( "\nIOR list: " + this.iors.size() );
            for ( int i = 0; i < this.iors.size(); i++ ) {
                System.out.print( "\n[" + i + "]" );
                ArrayList<String> tmp = this.iors.get( i );
                for ( int x = 0; x < tmp.size(); x++ ) {
                    System.out.print( " " + tmp.get( x ) );
                }
            }
            System.out.println( "\nInput names list: " + this.inputNames.size() );
            for ( int i = 0; i < this.inputNames.size(); i++ ) {
                System.out.print( "  [" + i + "]" + this.inputNames.get( i ) );
            }
            System.out.print( "\nTest data list: " + this.dataValues.size() );
            for ( int i = 0; i < this.dataValues.size(); i++ ) {
                System.out.print( "\n[" + i + "]" );
                ArrayList<String> tmp = this.dataValues.get( i );
                for ( int x = 0; x < tmp.size(); x++ ) {
                    System.out.print( " " + tmp.get( x ) );
                }
            }
            System.out.println();
        }
        iors_xp = new ArrayList<ArrayList<String>>();
        int[] inputIndexes = null;
        int x = 1;
        int totalLines = 0;
        int[] numLines = new int[this.outputNames.size()];
        for ( int i = 0; i < this.outputNames.size(); i++ ) {
        	ArrayList<String> tempIOR = this.iors.get( i );
            if ( this.uniqueList[i] ) {
                int iorInputs = tempIOR.size();
                for ( int z = 0; z < iorInputs; z++ ) {

                    /* tempTestData is the list of dataValues that is associated with the input name at location z
                     * in the ior list of input names.  */
                    int nameIndex = this.inputNames.indexOf( (String)tempIOR.get( z ) );
                    String name = null;
                    try {
                        name = (String)tempIOR.get( z );
                    } catch ( Exception ex4 ) {}
                    if ( TVG.isLogging ) {
                        System.out.println( "nameIndex: " + nameIndex + "  name: " + name );
                    }
                    ArrayList<String> tempTestData = this.dataValues.get( this.inputNames.indexOf( tempIOR.get( z ) ) );
                    if ( tempTestData != null ) {
                        int y = tempTestData.size();
                        x = ( y > 0 ) ? ( x * y ) : x;
                    }
                }
            }
            numLines[i] = x;
            totalLines = ( x > 1 ) ? ( totalLines + x ) : totalLines;
            x = 1;
        }
        for ( int i = 0; i < this.iors.size(); i++ ) {
        	ArrayList<String> tempIOR = this.iors.get( i );
            if ( this.uniqueList[i] ) {    // if it is not a unique input output relationship, we really don't need to see it.
                if ( TVG.isLogging ) {
                    System.out.println( "Including " + this.outputNames.get( i ) + " in the ior_xp file.  It has "
                                        + ( tempIOR.size() ) + " influences." );
                }
                int iorInputs = tempIOR.size();
                inputIndexes = new int[iorInputs];
                int[] maxIndexes = new int[iorInputs];
                int[] valIndexes = new int[iorInputs];
                TVGModIndexer modInc = new TVGModIndexer( valIndexes, maxIndexes );
                ArrayList<ArrayList<String>> testDataVec = new ArrayList<ArrayList<String>>();
                for ( int z = 0; z < iorInputs; z++ ) {
                	ArrayList<String> tempTestData = this.dataValues.get( this.inputNames.indexOf( tempIOR.get( z ) ) );
                    if ( tempTestData != null ) {
                        testDataVec.add( tempTestData );
                        inputIndexes[z] = this.inputNames.indexOf( tempIOR.get( z ) );
                        maxIndexes[z] = tempTestData.size();
                        valIndexes[z] = 1;
                    }
                }
                int index = 1;
                while ( index <= numLines[i] ) {
                    ArrayList<String> iors_xp_set = new ArrayList<String>(this.inputNames.size());
                    for(int iors_xp_setIndex=0;iors_xp_setIndex<this.inputNames.size();iors_xp_setIndex++){
                    	iors_xp_set.add(null);
                    }
                    for ( int r = 0; r < testDataVec.size(); r++ ) {
                        String currValue = ( testDataVec.get( r ) ).get( ( valIndexes[r] - 1 ) );
                        iors_xp_set.set(inputIndexes[r], currValue);
                    }
                    valIndexes = modInc.increment();
                    index++;
                    iors_xp.add( iors_xp_set );
                }
            }
        }
        this.lengthOfTask = this.iors_xp.size();
        // below this line is only about printing to the IOR_XP.log file ///////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////
        if ( TVG.isLogging ) {
            File currFile = new File( "C:\\ior_info.log" );
            BufferedWriter writ2 = null;
            try {
                writ2 = new BufferedWriter( new FileWriter( currFile ) );
            } catch ( IOException ex1 ) {}
            try {
                writ2.write( "\nWhich elements are Unique?\n\n" );
            } catch ( Exception ex ) {
                ex.printStackTrace();
            }
            ArrayList<String> temp = null;
            for ( int i = 0; i < this.iors.size(); i++ ) {
                temp = this.iors.get( i );
                try {
                    writ2.write( "iors_xp[" + i + "] = " + this.outputNames.get( i ) + " and is Unique? "
                                 + this.uniqueList[i] + "\n" + "and has " + temp.size() + " values." );
                    writ2.newLine();
                } catch ( Exception ex ) {
                    ex.printStackTrace();
                }
                for ( int x1 = 0; x1 < temp.size(); x1++ ) {
                    try {
                        writ2.write( "[" + x1 + "]-" + temp.get( x1 ) );
                    } catch ( IOException ex2 ) {}
                }
                try {
                    writ2.newLine();
                    writ2.newLine();
                } catch ( IOException ex3 ) {}
            }
            try {
                writ2.write( "Following is the cross product of the iors." );
                writ2.newLine();
                writ2.newLine();
            } catch ( IOException ex9 ) {}
            for ( int i = 0; i < this.iors_xp.size(); i++ ) {
                ArrayList<String> tempA = this.iors_xp.get( i );
                try {
                    writ2.write( ( i + 1 ) + "\t" );
                } catch ( IOException ex5 ) {}
                for ( int j = 0; j < tempA.size(); j++ ) {
                    if ( tempA.get(j) == null ) {
                        try {
                            writ2.write( " ~" );
                        } catch ( IOException ex6 ) {}
                    } else {
                        try {
                            writ2.write( " " + tempA.get(j) );
                        } catch ( IOException ex7 ) {}
                    }
                }
                try {
                    writ2.newLine();
                } catch ( IOException ex8 ) {}
            }
            try {
                writ2.flush();
                writ2.close();
            } catch ( Exception ex ) {}
        }
    }

    /**
     * Through use of the BigInt Factorial, this method calcluates the number of combinations and
     * sets the length of the task accordingly.  ( Number of Inputs choose size of NWay)  C(a,b)
     *
     */
    public void genNWayTaskSize() {
        BigInteger num = factorial( (long)this.inputNames.size() );
        BigInteger den = ( factorial( (long)this.pMain.getNforNway() ) )
                             .multiply( ( factorial( (long)this.inputNames.size() - (long)this.pMain.getNforNway() ) ) );
        BigInteger len = num.divide( den );
        System.out.println( "Setting length of NWay task at: " + len );
        this.setLengthOfTask( (int)( len.longValue() ) );
    }

    /**
     * Creates a Swingworker thread that contains a new instance of the class.
     * Creates and returns a new instance of the TaskNWay class.  This class generates the NWay IOR.
     * Starts the new thread.
     */
    

    

    public void genNWaygo() {
        this.setCurrent( 0 );
        new TVGTaskNWay( TVGGenerationTask.this, TVGGenerationTask.this.inputNames,
                                     TVGGenerationTask.this.pMain.getNforNway() );

    }

    /**
     * Calculates how many days, hours, minutes and seconds the time represents.
     *
     * @param time The amount of time in millis.
     *
     * @return A String representation of the length of time.
     */
    public String makeTime( int time ) {
        String currTime = "";
        int days = time / 86400000;
        int left = time % 86400000;
        int hours = left / 3600000;
        left = left % 3600000;
        int minutes = left / 60000;
        double seconds = (double)( left % 60000 ) / 1000;
        if ( days > 0 ) {
            currTime = currTime + days + " day";
            if ( days > 1 ) {
                currTime += "s";
            }
            currTime += ", ";
        }
        if ( ( days > 0 ) || ( hours > 0 ) ) {
            currTime += ( hours + " hour" );
            if ( hours > 1 ) {
                currTime += "s";
            }
            currTime += ", ";
        }
        if ( ( days > 0 ) || ( hours > 0 ) || ( minutes > 0 ) ) {
            currTime += ( minutes + " minute" );
            if ( minutes > 1 ) {
                currTime += "s";
            }
            currTime += ", ";
        }
        currTime += ( seconds + " seconds" );
        return currTime;
    }

    /**
     * Generates how long the task took to complete.
     *
     * @return a call to the makeTime method with the int value of the time.  makeTime returns a string
     * representation of the length of time it took to complete the task.
     */
    public String processEndTime() {
        long t1 = this.pMain.getStartTime();
        long t2 = System.currentTimeMillis();
        long time = t2 - t1;
        return this.makeTime( (int)time );
    }

    /**
     * Creates a Swingworker thread that contains a new instance of the class.
     * Creates and returns a new instance of the TaskRedT class.  This class generates the T Reduced set.
     * Starts the new thread.
     */

    public ArrayList<String> redTgo() {
        this.setCurrent( 0 );
        TVGTaskRedT temp = new TVGTaskRedT( TVGGenerationTask.this, TVGGenerationTask.this.iors_xp, TVGGenerationTask.this.dataValues);
        return temp.getOutputList();

    }


    /**
     * Stop() sets the current value of the task to be equal to the length of the task
     * effectively causing the done() method to return true.
     */
    public void stop() {
        this.setCurrent( this.lengthOfTask );
    }

    /**
     * My BigInt factorial method used because MAX_LONG just wasn't large enough.
     *
     * @param m The operand of the factorial. (as in m!)
     *
     * @return The value of the factorial evaluation.
     */
    private BigInteger factorial( long m ) {
        BigInteger r = BigInteger.valueOf( m );
        return ( r.compareTo( BigInteger.ZERO ) == 0 ) ? BigInteger.ONE : r.multiply( factorial( r.longValue() - 1 ) );
    }
}