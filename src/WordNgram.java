/*
 * This class encapsulates N words/strings so that the
 * group of N words can be treated as a key in a map or an
 * element in a set, or an item to be searched for in an array.
 * <P>
 * @author YOU,COMPSCI 201 STUDENT
 */

public class WordNgram implements Comparable<WordNgram>{
    
    private String[] myWords;
    
    /*
     * Store the n words that begin at index start of array list as
     * the N words of this N-gram.
     * @param list contains at least n words beginning at index start
     * @start is the first of the N worsd to be stored in this N-gram
     * @n is the number of words to be stored (the n in this N-gram)
     */
    public WordNgram(String[] list, int start, int n) {
        myWords = new String[n];
        System.arraycopy(list, start, myWords, 0, n);
    }
    
    /**
     * Return value that meets criteria of compareTo conventions.
     * @param wg is the WordNgram to which this is compared
     * @return appropriate value less than zero, zero, or greater than zero
     */
    public int compareTo(WordNgram wg) {
    	int len = getLength();
    	int cmp = len - wg.getLength();
    	if (cmp != 0)
    		return cmp;
    	
    	for (int i = 0; i < len; i++) {
			int comp = myWords[i].compareTo(wg.get(i));
			if (comp != 0)
				return comp;
		}

        return 0;
    }
    
    /**
     * Return true if this N-gram is the same as the parameter: all words the same.
     * @param o is the WordNgram to which this one is compared
     * @return true if o is equal to this N-gram
     */
    public boolean equals(Object o){
        WordNgram wg = (WordNgram) o;
        if (getLength() != wg.getLength())
        	return false;
        
        int len = getLength();
        for (int i = 0; i < len; i++)
			if (!(myWords[i].equals(wg.get(i)) ))
				return false;
      
        return true;
    }
    
    /**
     * Returns a good value for this N-gram to be used in hashing.
     * @return value constructed from all N words in this N-gram
     */
    public int hashCode(){
    	String s = "";
    	for (int i = 0; i < myWords.length; i++) {
			s += myWords[i];
		}
        return s.hashCode();
    }
    
    public int getLength() {
    	return myWords.length;
    }
    
    public String get(int n) {
    	return myWords[n];
    }
}
