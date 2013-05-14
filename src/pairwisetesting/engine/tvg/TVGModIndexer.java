package pairwisetesting.engine.tvg;

public class TVGModIndexer {
    private int[] currIndex = null;
    private int[] maxIndex = null;

    /**
     * Creates a new ModIndexer object.
     *
     * @param curr DOCUMENT ME!
     * @param max DOCUMENT ME!
     */
    public TVGModIndexer( int[] curr, int[] max ) {
        currIndex = curr;
        maxIndex = max;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getCurrIndex() {
        return this.currIndex;
    }

    /**
     * DOCUMENT ME!
     *
     * @param max DOCUMENT ME!
     */
    public void setMaxIndex( int[] max ) {
        maxIndex = max;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] increment() {
        int i = 1;
        for ( i = currIndex.length - 1; i >= 0; i-- ) {
            currIndex[i] += 1;
            if ( currIndex[i] > maxIndex[i] ) {
                currIndex[i] = 1;
            } else {
                break;
            }
        }

        return currIndex;
    }

    /**
     * DOCUMENT ME!
     *
     * @param val DOCUMENT ME!
     */
    public void resetCurrIndex( int val ) {
        if ( currIndex != null ) {
            for ( int i = 0; i < currIndex.length; i++ ) {
                currIndex[i] = val;
            }
        }
    }
}
