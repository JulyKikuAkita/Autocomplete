package lyft.autocomplete;

public class WordRank {
    String mWord;
    Integer mRank; // 1 larest, INTEGER.MAX_VALUE smallest

    public WordRank() {
        mWord = null;
        mRank = -1;
    }

    public WordRank(String word, Integer rank) {
        this.mWord = word;
        this.mRank = rank;
    }
    
    @Override
    public String toString() {
        return mWord + " (" + mRank + ") ";
    }
}
