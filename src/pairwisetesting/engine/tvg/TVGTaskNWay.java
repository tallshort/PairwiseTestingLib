package pairwisetesting.engine.tvg;

import java.util.ArrayList;

public class TVGTaskNWay {


    TVGGenerationTask task;
    /** ArrayList of all of the input Names */
    private ArrayList<String> inputNames;
    private ArrayList<ArrayList<String>> iorList;
    private ArrayList<String> outputList;
    private int n = -1;


    /**
     * Creates a new TaskNWay object.
     *
     * @param tsk GenerationTask parent Object used to update current task position for the progress monitor.
     * @param iNames ArrayList of input names
     * @param nForNWay the int value associated with the N in N-Way.
     */
    public TVGTaskNWay( TVGGenerationTask tsk, ArrayList<String> iNames, int nForNWay ) {
        this.task = tsk;
        this.inputNames = iNames;
        this.n = nForNWay;
        this.iorList = new ArrayList<ArrayList<String>>();
        this.outputList = new ArrayList<String>();
        if ( this.n == 3 ) {
            try {
                this.gen3Way();
            } catch ( Exception ex ) {
                System.out.print( "error in 3-Way: " + ex );
            }
        } else if ( this.n == 4 ) {
            try {
                this.gen4Way();
            } catch ( Exception ex1 ) {
                System.out.print( "error in 4-Way: " + ex1 );
            }
        } else if ( this.n == 5 ) {
            try {
                this.gen5Way();
            } catch ( Exception ex2 ) {
                System.out.print( "error in 5-Way: " + ex2 );
            }
        } else if ( this.n == 6 ) {
            try {
                this.gen6Way();
            } catch ( Exception ex3 ) {
                System.out.print( "error in 6-Way: " + ex3 );
            }
        } else if ( this.n == 7 ) {
            try {
                this.gen7Way();
            } catch ( Exception ex4 ) {
                System.out.print( "error in 7-Way: " + ex4 );
            }
        } else if ( this.n == 8 ) {
            try {
                this.gen8Way();
            } catch ( Exception ex5 ) {
                System.out.print( "error in 8-Way: " + ex5 );
            }
        } else if ( this.n == 9 ) {
            try {
                this.gen9Way();
            } catch ( Exception ex6 ) {
                System.out.print( "error in 9-Way: " + ex6 );
            }
        } else if ( this.n == 10 ) {
            try {
                this.gen10Way();
            } catch ( Exception ex7 ) {
                System.out.print( "error in 10-Way: " + ex7 );
            }
        } else {
            try {
                this.gen2Way();
            } catch ( Exception ex8 ) {
                System.out.print( "error in 2-Way: " + ex8 );
            }
        }
        this.task.setCurrent( this.task.getLengthOfTask() );
        this.task.pMain.genNWayPart2( this );
    }

    /**
     * Returns the IORList ArrayList
     *
     * @return IORList
     */
    public ArrayList<ArrayList<String>> getIORList() {
        return this.iorList;
    }

    /**
     * Returns the OUtput list ArrayList
     *
     * @return Output List
     */
    public ArrayList<String> getOutputList() {
        return this.outputList;
    }

    /**
     * Generates a 10 way matching.  using combinatorial M choose N.
     */
    private void gen10Way() {
        int p = 1;
        for ( int a = 0; a < ( this.inputNames.size() - 9 ); a++ ) {
            for ( int b = a + 1; b < ( this.inputNames.size() - 8 ); b++ ) {
                for ( int c = b + 1; c < ( this.inputNames.size() - 7 ); c++ ) {
                    for ( int d = c + 1; d < ( this.inputNames.size() - 6 ); d++ ) {
                        for ( int e = d + 1; e < ( this.inputNames.size() - 5 ); e++ ) {
                            for ( int f = e + 1; f < ( this.inputNames.size() - 4 ); f++ ) {
                                for ( int g = f + 1; g < ( this.inputNames.size() - 3 ); g++ ) {
                                    for ( int h = g + 1; h < ( this.inputNames.size() - 2 ); h++ ) {
                                        for ( int i = h + 1; i < ( this.inputNames.size() - 1 ); i++ ) {
                                            for ( int j = i + 1; j < this.inputNames.size(); j++ ) {
                                            	ArrayList<String> tempArray = new ArrayList<String>();
                                                this.outputList.add( "OUT_" + p );
                                                tempArray.add( this.inputNames.get( a ) );
                                                tempArray.add( this.inputNames.get( b ) );
                                                tempArray.add( this.inputNames.get( c ) );
                                                tempArray.add( this.inputNames.get( d ) );
                                                tempArray.add( this.inputNames.get( e ) );
                                                tempArray.add( this.inputNames.get( f ) );
                                                tempArray.add( this.inputNames.get( g ) );
                                                tempArray.add( this.inputNames.get( h ) );
                                                tempArray.add( this.inputNames.get( i ) );
                                                tempArray.add( this.inputNames.get( j ) );
                                                this.iorList.add( tempArray );
                                                this.task.setCurrent( p );
                                                p++;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Generates a 2 way matching.  using combinatorial M choose N.
     */
    private void gen2Way() {
        int p = 1;
        for ( int a = 0; a < ( this.inputNames.size() - 1 ); a++ ) {
            for ( int b = a + 1; b < this.inputNames.size(); b++ ) {
            	ArrayList<String> tempArray = new ArrayList<String>();
                this.outputList.add( "OUT_" + p );
                tempArray.add( this.inputNames.get( a ) );
                tempArray.add( this.inputNames.get( b ) );
                this.iorList.add( tempArray );
                this.task.setCurrent( p );
                p++;
            }
        }
    }

    /**
     * Generates a 3 way matching.  using combinatorial M choose N.
     */
    private void gen3Way() {
        int p = 1;
        for ( int a = 0; a < ( this.inputNames.size() - 2 ); a++ ) {
            for ( int b = a + 1; b < ( this.inputNames.size() - 1 ); b++ ) {
                for ( int c = b + 1; c < this.inputNames.size(); c++ ) {
                	ArrayList<String> tempArray = new ArrayList<String>();
                    this.outputList.add( "OUT_" + p );
                    tempArray.add( this.inputNames.get( a ) );
                    tempArray.add( this.inputNames.get( b ) );
                    tempArray.add( this.inputNames.get( c ) );
                    this.iorList.add( tempArray );
                    this.task.setCurrent( p );
                    p++;
                }
            }
        }
    }

    /**
     * Generates a 4 way matching.  using combinatorial M choose N.
     */
    private void gen4Way() {
        int p = 1;
        for ( int a = 0; a < ( this.inputNames.size() - 3 ); a++ ) {
            for ( int b = a + 1; b < ( this.inputNames.size() - 2 ); b++ ) {
                for ( int c = b + 1; c < ( this.inputNames.size() - 1 ); c++ ) {
                    for ( int d = c + 1; d < this.inputNames.size(); d++ ) {
                    	ArrayList<String> tempArray = new ArrayList<String>();
                        this.outputList.add( "OUT_" + p );
                        tempArray.add( this.inputNames.get( a ) );
                        tempArray.add( this.inputNames.get( b ) );
                        tempArray.add( this.inputNames.get( c ) );
                        tempArray.add( this.inputNames.get( d ) );
                        this.iorList.add( tempArray );
                        this.task.setCurrent( p );
                        p++;
                    }
                }
            }
        }
    }

    /**
     * Generates a 5 way matching.  using combinatorial M choose N.
     */
    private void gen5Way() {
        int p = 1;
        for ( int a = 0; a < ( this.inputNames.size() - 4 ); a++ ) {
            for ( int b = a + 1; b < ( this.inputNames.size() - 3 ); b++ ) {
                for ( int c = b + 1; c < ( this.inputNames.size() - 2 ); c++ ) {
                    for ( int d = c + 1; d < ( this.inputNames.size() - 1 ); d++ ) {
                        for ( int e = d + 1; e < this.inputNames.size(); e++ ) {
                            ArrayList<String> tempArray = new ArrayList<String>();
                            this.outputList.add( "OUT_" + p );
                            tempArray.add( this.inputNames.get( a ) );
                            tempArray.add( this.inputNames.get( b ) );
                            tempArray.add( this.inputNames.get( c ) );
                            tempArray.add( this.inputNames.get( d ) );
                            tempArray.add( this.inputNames.get( e ) );
                            this.iorList.add( tempArray );
                            this.task.setCurrent( p );
                            p++;
                        }
                    }
                }
            }
        }
    }

    /**
     * Generates a 6 way matching.  using combinatorial M choose N.
     */
    private void gen6Way() {
        int p = 1;
        for ( int a = 0; a < ( this.inputNames.size() - 5 ); a++ ) {
            for ( int b = a + 1; b < ( this.inputNames.size() - 4 ); b++ ) {
                for ( int c = b + 1; c < ( this.inputNames.size() - 3 ); c++ ) {
                    for ( int d = c + 1; d < ( this.inputNames.size() - 2 ); d++ ) {
                        for ( int e = d + 1; e < ( this.inputNames.size() - 1 ); e++ ) {
                            for ( int f = e + 1; f < this.inputNames.size(); f++ ) {
                            	ArrayList<String> tempArray = new ArrayList<String>();
                                this.outputList.add( "OUT_" + p );
                                tempArray.add( this.inputNames.get( a ) );
                                tempArray.add( this.inputNames.get( b ) );
                                tempArray.add( this.inputNames.get( c ) );
                                tempArray.add( this.inputNames.get( d ) );
                                tempArray.add( this.inputNames.get( e ) );
                                tempArray.add( this.inputNames.get( f ) );
                                this.iorList.add( tempArray );
                                this.task.setCurrent( p );
                                p++;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Generates a 7 way matching.  using combinatorial M choose N.
     */
    private void gen7Way() {
        int p = 1;
        for ( int a = 0; a < ( this.inputNames.size() - 6 ); a++ ) {
            for ( int b = a + 1; b < ( this.inputNames.size() - 5 ); b++ ) {
                for ( int c = b + 1; c < ( this.inputNames.size() - 4 ); c++ ) {
                    for ( int d = c + 1; d < ( this.inputNames.size() - 3 ); d++ ) {
                        for ( int e = d + 1; e < ( this.inputNames.size() - 2 ); e++ ) {
                            for ( int f = e + 1; f < ( this.inputNames.size() - 1 ); f++ ) {
                                for ( int g = f + 1; g < this.inputNames.size(); g++ ) {
                                	ArrayList<String> tempArray = new ArrayList<String>();
                                    this.outputList.add( "OUT_" + p );
                                    tempArray.add( this.inputNames.get( a ) );
                                    tempArray.add( this.inputNames.get( b ) );
                                    tempArray.add( this.inputNames.get( c ) );
                                    tempArray.add( this.inputNames.get( d ) );
                                    tempArray.add( this.inputNames.get( e ) );
                                    tempArray.add( this.inputNames.get( f ) );
                                    tempArray.add( this.inputNames.get( g ) );
                                    this.iorList.add( tempArray );
                                    this.task.setCurrent( p );
                                    p++;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Generates a 8 way matching.  using combinatorial M choose N.
     */
    private void gen8Way() {
        int p = 1;
        for ( int a = 0; a < ( this.inputNames.size() - 7 ); a++ ) {
            for ( int b = a + 1; b < ( this.inputNames.size() - 6 ); b++ ) {
                for ( int c = b + 1; c < ( this.inputNames.size() - 5 ); c++ ) {
                    for ( int d = c + 1; d < ( this.inputNames.size() - 4 ); d++ ) {
                        for ( int e = d + 1; e < ( this.inputNames.size() - 3 ); e++ ) {
                            for ( int f = e + 1; f < ( this.inputNames.size() - 2 ); f++ ) {
                                for ( int g = f + 1; g < ( this.inputNames.size() - 1 ); g++ ) {
                                    for ( int h = g + 1; h < this.inputNames.size(); h++ ) {
                                    	ArrayList<String> tempArray = new ArrayList<String>();
                                        this.outputList.add( "OUT_" + p );
                                        tempArray.add( this.inputNames.get( a ) );
                                        tempArray.add( this.inputNames.get( b ) );
                                        tempArray.add( this.inputNames.get( c ) );
                                        tempArray.add( this.inputNames.get( d ) );
                                        tempArray.add( this.inputNames.get( e ) );
                                        tempArray.add( this.inputNames.get( f ) );
                                        tempArray.add( this.inputNames.get( g ) );
                                        tempArray.add( this.inputNames.get( h ) );
                                        this.iorList.add( tempArray );
                                        this.task.setCurrent( p );
                                        p++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Generates a 9 way matching.  using combinatorial M choose N.
     */
    private void gen9Way() {
        int p = 1;
        for ( int a = 0; a < ( this.inputNames.size() - 8 ); a++ ) {
            for ( int b = a + 1; b < ( this.inputNames.size() - 7 ); b++ ) {
                for ( int c = b + 1; c < ( this.inputNames.size() - 6 ); c++ ) {
                    for ( int d = c + 1; d < ( this.inputNames.size() - 5 ); d++ ) {
                        for ( int e = d + 1; e < ( this.inputNames.size() - 4 ); e++ ) {
                            for ( int f = e + 1; f < ( this.inputNames.size() - 3 ); f++ ) {
                                for ( int g = f + 1; g < ( this.inputNames.size() - 2 ); g++ ) {
                                    for ( int h = g + 1; h < ( this.inputNames.size() - 1 ); h++ ) {
                                        for ( int i = h + 1; i < this.inputNames.size(); i++ ) {
                                        	ArrayList<String> tempArray = new ArrayList<String>();
                                            this.outputList.add( "OUT_" + p );
                                            tempArray.add( this.inputNames.get( a ) );
                                            tempArray.add( this.inputNames.get( b ) );
                                            tempArray.add( this.inputNames.get( c ) );
                                            tempArray.add( this.inputNames.get( d ) );
                                            tempArray.add( this.inputNames.get( e ) );
                                            tempArray.add( this.inputNames.get( f ) );
                                            tempArray.add( this.inputNames.get( g ) );
                                            tempArray.add( this.inputNames.get( h ) );
                                            tempArray.add( this.inputNames.get( i ) );
                                            this.iorList.add( tempArray );
                                            this.task.setCurrent( p );
                                            p++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}