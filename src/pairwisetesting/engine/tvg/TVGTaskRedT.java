package pairwisetesting.engine.tvg;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


public class TVGTaskRedT {
    Random rVal;
    private ArrayList<ArrayList<String>> dataValues;
    private ArrayList<ArrayList<String>> iors_xp;
    private ArrayList<ArrayList<String>> masterList;
    private BufferedWriter logger;
    private TVGGenerationTask task;
    private boolean[] covered;
    private boolean[] iArray;
    private int current = 0;
    private int setCount = 0;
    private ArrayList<String> outputList = new ArrayList<String>();

    /**
     * Creates a new RedTtask object.
     *
     * @param gent the Generation task object passed in so that 'current' can be updated
     * @param aior_xp the cross product of the values associated with the iors
     * @param adValues the list of dataValues for the inputs.
     * @param aFile the file used for output.
     * @param header the info to be written to the top of the output file.
     */
    public TVGTaskRedT( TVGGenerationTask gent, ArrayList<ArrayList<String>> aior_xp, ArrayList<ArrayList<String>> adValues) {
        rVal = new Random( System.currentTimeMillis() );
        this.task = gent;
        this.iors_xp = aior_xp;
        this.dataValues = adValues;
        buildCoveredArray();
        String fName = "output.log";
        if( TVG.isLogging || TVG.isPartLog ) {
            System.out.println( "LogFile name: " + fName );
            try {
                logger = new BufferedWriter(
                                 new FileWriter(
                                         new File(fName ) ) );
            } catch( IOException ex ) {
                System.out.println( "Failed to create the log file." );
            }
        }

        Date now = new Date( System.currentTimeMillis() );
        this.task.setStartTime( now );


        System.out.println( "Reduction started: " + now );
        if( TVG.isLogging || TVG.isPartLog ) {
            try {
                this.logger.write( "# File started: " + now );
                this.logger.newLine();
            } catch( IOException ex3 ) {
            }
            logDataValues();
        }
        genRedT();
    }

    /**
     * DOCUMENT ME!
     *
     * @param restrictions DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private ArrayList<String> getMaximalElement( ArrayList<String> restrictions ) {
        // element 0 will be the index, element 1 will be the actual value.
        // initially set the values to -1 and "" so that anything will be larger
    	ArrayList<String> result = new ArrayList<String>();
    	result.add("-1");
    	result.add("");
        int currBest = -1;
        int bestListSize = -1;
        for( int i = 0; i < this.dataValues.size(); i++ ) {
            if( restrictions.indexOf( "" + i ) < 0 ) {
                // dataList is the list of dataValues for an individual input
            	ArrayList<String> dataList = this.dataValues.get( i );
                // masterPart is the list of values left in the ior_xp for that input.
            	ArrayList<String> masterPart = this.masterList.get( i );
                // if the dataList is empty or null, no sense in even trying to get a max value.
                if( ( dataList != null ) && ( dataList.size() > 0 ) ) {
                    // start by getting each element of the dataList in sequence
                    for( int j = 0; j < dataList.size(); j++ ) {
                        String currVal = dataList.get( j );
                        int numVal = 0;
                        // compare it to each element still in the masterPart
                        for( int x = 0; x < masterPart.size(); x++ ) {
                            // if it is a match, then increment the number of Values
                            if( currVal.compareTo( masterPart.get( x ) ) == 0 ) {
                                numVal++;
                            }
                        }
                        // if the current Value has more incidents than the best, make the current Value the Best
                        if( numVal > currBest ) {
                            if( TVG.isLogging ) {
                                try {
                                    logger.write( numVal + " > " + currBest
                                                  + "  so '" + currVal
                                                  + "' replaces '" + result.get(1)
                                                  + "' as the best choice and it is at index: " + i );
                                    logger.newLine();
                                } catch( IOException ex ) {
                                }
                            }
                            result.set(0,  "" + i);
                            result.set(1, currVal);
                            currBest = numVal;
                            bestListSize = masterList.size();
                            // if the current Value has the same number as the Best value, choose one randomly. (flip a coin)
                        } else if( numVal == currBest ) {
                            if( masterList.size() > bestListSize ) {    // if the list has fewer nulls.
                                if( TVG.isLogging ) {
                                    try {
                                        logger.write( numVal + " == "
                                                      + currBest + "  but "
                                                      + masterList.size()
                                                      + " > " + bestListSize );
                                        logger.newLine();
                                    } catch( IOException ex1 ) {
                                    }
                                }
                                result.set(0,  "" + i);
                                result.set(1, currVal);
                                currBest = numVal;
                                bestListSize = masterList.size();
                            } else if( masterList.size() == bestListSize ) {
                                long curTM = rVal.nextLong();
                                if( curTM < 0 ) {
                                    curTM = -curTM;
                                }
                                boolean replace = ( ( curTM % 2 ) == 1 );
                                if( TVG.isLogging ) {
                                    try {
                                        logger.write( numVal + " == "
                                                      + currBest + "  and "
                                                      + masterList.size()
                                                      + " == " + bestListSize
                                                      + "  " );
                                    } catch( IOException ex1 ) {
                                    }
                                }
                                if( replace ) {
                                    if( TVG.isLogging ) {
                                        try {
                                            logger.write( "'" + currVal
                                                          + "' will replace '"
                                                          + result.get(1)
                                                          + "' as the best choice for index: " + i );
                                            logger.newLine();
                                        } catch( IOException ex2 ) {
                                        }
                                    }
                                    result.set(0,  "" + i);
                                    result.set(1, currVal);
                                    currBest = numVal;
                                    bestListSize = masterList.size();
                                } else {
                                    if( TVG.isLogging ) {
                                        try {
                                            logger.write( "'" + currVal
                                                          + "' will NOT replace '"
                                                          + result.get(1)
                                                          + "' as the best choice for index: " + i );
                                            logger.newLine();
                                        } catch( IOException ex3 ) {
                                        }
                                    }
                                }
                            } else {
                                if( TVG.isLogging ) {
                                    try {
                                        logger.write( numVal + " == "
                                                      + currBest + "  but "
                                                      + masterList.size()
                                                      + " < " + bestListSize
                                                      + " so it will NOT be replaced." );
                                        logger.newLine();
                                    } catch( IOException ex1 ) {
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if( TVG.isLogging ) {
                    try {
                        logger.write( "Skipping index : " + i );
                        logger.newLine();
                    } catch( IOException ex4 ) {
                    }
                }
            }
            if( TVG.isLogging ) {
                try {
                    logger.write( "Done looking at index: " + i );
                    logger.newLine();
                } catch( IOException ex6 ) {
                }
            }
        }
        if( TVG.isLogging || TVG.isPartLog ) {
            try {
                logger.write( "returning a maximal value of: " + result.get(0)
                              + "  " + result.get(1) );
                logger.newLine();
            } catch( IOException ex5 ) {
            }
        }
        return result;
    }

    
    /**
     * Builds an array of booleans the same size as the ArrayList ior_xp initially set to false. The values will get
     * changed to true when the corresponding index in the iors_xp ArrayList is 'covered'.
     */
    private void buildCoveredArray() {
        this.covered = new boolean[this.iors_xp.size()];
        this.iArray = new boolean[this.iors_xp.size()];
        for( int i = 0; i < this.covered.length; i++ ) {
            this.covered[i] = false;
            this.iArray[i] = true;
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void buildMasterLists() {
        this.masterList = new ArrayList<ArrayList<String>>();
        // if the iors_xp Arraylist is null or empty, bail early
        if( ( this.iors_xp == null ) || this.iors_xp.isEmpty() ) {
            return;
        }
        // first we need to initialize the masterList.
        // get the first element of the ior_xp and create a new Arraylist for each index in it.
        ArrayList<String> tempArray = this.iors_xp.get( 0 );
        for( int i = 0; i < tempArray.size(); i++ ) {
        	ArrayList<String> tList = new ArrayList<String>();
            this.masterList.add( tList );
        }
        // now we need to insert all the elements from each String[] into the corresponding ArrayList but only
        // if it is not null.
        for( int i = 0; i < this.iors_xp.size(); i++ ) {
            // we only add the elements if the ior_xp has not been covered.
            if( this.iArray[i] ) {
                tempArray = this.iors_xp.get( i );
                for( int j = 0; j < tempArray.size(); j++ ) {
                    if( tempArray.get(j) != null ) {
                        ( this.masterList.get( j ) ).add(
                                tempArray.get(j) );
                    }
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param tset the test set to be compared with vals to determine coverage.
     * @param vals the vlaues used to determine coverage.
     *
     * @return true if tset covers vals
     */
    private boolean covers( ArrayList<String> tset, ArrayList<String> vals ) {
        if( ( ( tset == null ) || ( vals == null ) )
                || ( tset.size() != vals.size() ) ) {
            return false;
        }
        for( int i = 0; i < tset.size(); i++ ) {
            if( ( vals.get(i) != null ) && ( tset.get(i).compareTo( vals.get(i) ) != 0 ) ) {
                return false;
            }
        }
        return true;
    }

    
    
    private void genRedT() {
        //this.task.pMain.setCursor( Cursor.getPredefinedCursor(
                                            //Cursor.WAIT_CURSOR ) );
        final int sizeOfset = this.dataValues.size();
        while( this.current < this.iors_xp.size() && !task.done()) {
            if( TVG.isLogging || TVG.isPartLog ) {
                try {
                    logger.write( "current: " + this.current + "  iors_xp: "
                                  + this.iors_xp.size() );
                    logger.newLine();
                } catch( IOException ex8 ) {
                }
            }
            ArrayList<ArrayList<String>> maxList = new ArrayList<ArrayList<String>>();
            int numIndexes = 0;
            ArrayList<String> bestIndexList = new ArrayList<String>();
            ArrayList<String> bestSet = new ArrayList<String>(sizeOfset);
            for(int bestSetIndex = 0; bestSetIndex<sizeOfset;bestSetIndex++){
            	bestSet.add(null);
            }
            int bestIndex = -1;
            ArrayList<String> max = null;
            while( numIndexes < sizeOfset ) {
                this.parseIncludeList( maxList );
                this.buildMasterLists();
                if( TVG.isLogging ) {
                    this.logMasterList();
                }
                max = this.getMaximalElement( bestIndexList );
                bestIndex = Integer.parseInt( max.get(0) );
                bestIndexList.add( max.get(0) );
                maxList.add( max );
                //System.out.println("index # " + numIndexes + " = " + bestIndex);
                if( TVG.isLogging ) {
                    try {
                        logger.write( "index # " + numIndexes + " = "
                                      + bestIndex );
                        logger.newLine();
                    } catch( IOException ex ) {
                    }
                }
                bestSet.set(bestIndex, max.get(1));
                if( TVG.isLogging ) {
                    try {
                        logger.write( "BestSet looks like: " );
                    } catch( IOException ex1 ) {
                    }
                    for( int m = 0; m < bestSet.size(); m++ ) {
                        try {
                            logger.write( bestSet.get(m) + "  " );
                        } catch( IOException ex3 ) {
                        }
                    }
                    try {
                        logger.newLine();
                        logger.newLine();
                    } catch( IOException ex2 ) {
                    }
                }
                numIndexes++;
            }
            this.setCount++;
            String tempString = "";
            for( int r = 0; r < bestSet.size(); r++ ) {
            	tempString = tempString +bestSet.get(r) + ":";
            }
            outputList.add(tempString);
            this.markIndexes( bestSet );
            if( this.task.done() ) {
                numIndexes = sizeOfset;
            }
        }
        if( TVG.isLogging || TVG.isPartLog ) {
            try {
                logger.newLine();
                logger.write( "Total sets = " + this.setCount );
                logger.newLine();
                String end = this.task.processEndTime();
                System.out.println( "took: " + end );
                logger.write( "This process took: " + end );
                logger.newLine();
                this.logger.flush();
                this.logger.close();
            } catch( IOException ex7 ) {
            }
        }

        System.out.println( "Final set count = " + this.setCount );
        
        this.task.stop();
       // this.task.mFrame.setCursor( Cursor.getDefaultCursor() );
    }
    
    public ArrayList<String> getOutputList(){
    	return this.outputList;
    }

    /**
     * DOCUMENT ME!
     */
    private void logDataValues() {
        for( int i = 0; i < this.dataValues.size(); i++ ) {
            ArrayList<String> tempList = this.dataValues.get( i );
            try {
                logger.write( "dataValues[" + i + "] = " );
            } catch( IOException ex ) {
            }
            for( int j = 0; j < tempList.size(); j++ ) {
                try {
                    logger.write( tempList.get( j ) + " " );
                } catch( IOException ex1 ) {
                }
            }
            try {
                logger.newLine();
            } catch( IOException ex2 ) {
            }
        }
        try {
            logger.newLine();
        } catch( IOException ex3 ) {
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void logMasterList() {
        for( int i = 0; i < this.masterList.size(); i++ ) {
            ArrayList<String> tempList = this.masterList.get( i );
            try {
                logger.write( "masterList[" + i + "]" );
            } catch( IOException ex ) {
            }
            for( int j = 0; j < tempList.size(); j++ ) {
                try {
                    logger.write( ": " + tempList.get( j ) );
                } catch( IOException ex1 ) {
                }
            }
            try {
                logger.newLine();
            } catch( IOException ex2 ) {
            }
        }
        try {
            logger.newLine();
        } catch( IOException ex3 ) {
        }
    }


    /**
     * DOCUMENT ME!
     *
     * @param best the list of values to mark against
     */
    private void markIndexes( ArrayList<String> best ) {
        int count = 0;
        int count2 = 0;
        for( int i = 0; i < this.covered.length; i++ ) {
            if( !this.covered[i] ) {
                ArrayList<String> temp = iors_xp.get( i );
                this.covered[i] = covers( best, temp );
                if( this.covered[i] ) {
                    count++;
                    count2++;
                }
            } else {
                count++;
            }
        }
        if( TVG.isLogging || TVG.isPartLog ) {
            try {
                logger.newLine();
                logger.write( "this vector covered " + count2
                              + " new elements." );
                logger.newLine();
                logger.write( count + " indexes are covered out of "
                              + this.iors_xp.size() );
                logger.newLine();
                logger.newLine();
            } catch( IOException ex ) {
            }
        }
        this.current = count;
        this.task.setCurrent( this.current );
    }

    /**
     * DOCUMENT ME!
     *
     * @param maxList DOCUMENT ME!
     */
    private void parseIncludeList( ArrayList<ArrayList<String>> maxList ) {
        for( int i = 0; i < this.covered.length; i++ ) {
            this.iArray[i] = !this.covered[i];
        }
        for( int i = 0; i < maxList.size(); i++ ) {
            ArrayList<String> maxArray = maxList.get( i );
            int maxIndex = Integer.parseInt( maxArray.get(0) );
            for( int j = 0; j < this.iors_xp.size(); j++ ) {
                if( this.iArray[j] ) {
                    ArrayList<String> tempSet = this.iors_xp.get( j );
                    if( tempSet.get(maxIndex) == null ) {
                        //this.iArray[j] = true;
                    } else if( (tempSet.get(maxIndex)).compareTo( maxArray.get(1) ) != 0 ) {
                        this.iArray[j] = false;
                    }
                }
            }
        }
    }
}