import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;


public class WordMarkovModel extends AbstractModel {
    private String myWords[];
    private Random myRandom;
    public static final int DEFAULT_COUNT = 100;
    
    private HashMap<WordNgram, ArrayList<WordNgram>> markovChain;
    private int markovK;
    
    public WordMarkovModel() {
    	markovK = -1;
        myRandom = new Random(1234);
    }
    
    /**
     * Create a new training text for this model based on the information read
     * from the scanner.
     * @param s is the source of information
     */
    public void initialize(Scanner s) {
        double start = System.currentTimeMillis();
        int count = readWords(s);
        double end = System.currentTimeMillis();
        double time = (end - start) / 1000.0;
        super.messageViews("#read: " + count + " chars in: " + time + " secs");
    }

    protected int readWords(Scanner s) {
        String str = s.useDelimiter("\\Z").next();
        s.close();
        myWords = str.split("\\s+");
        return myWords.length;
    }
    
    /**
     * Generate N letters using an order-K markov process where
     * the parameter is a String containing K and N separated by
     * whitespace with K first. If N is missing it defaults to some
     * value.
     */
    public void process(Object o) {
        String temp = (String) o;
        String[] nums = temp.split("\\s+");
        int k = Integer.parseInt(nums[0]);
        int numLetters = DEFAULT_COUNT;
        if (nums.length > 1) {
            numLetters = Integer.parseInt(nums[1]);
        }
        markov(k, numLetters);
    }
    
    private void generateMarkovChain(int k, String[] lexicon) {
    	System.out.println("Generating Markov Chain...");
    	markovK = k;
    	markovChain = new HashMap<WordNgram, ArrayList<WordNgram>>();
    	int len = lexicon.length;
    	for (int i = 0; i < len - 1 - k; i++) {
    		WordNgram key = new WordNgram(lexicon, i, k);
			ArrayList<WordNgram> outs = markovChain.get(key);
			if (outs == null) {  // first occurrence
				outs = new ArrayList<WordNgram>();
				markovChain.put(key, outs);
			}
			WordNgram next = new WordNgram(lexicon, i + 1, k);
			outs.add(next);
		}
    }
    
    public void markov(int k, int numLetters) {
    	if (k != markovK || markovChain == null) {
            double stime = System.currentTimeMillis();
            int len = myWords.length;
            String wrappedWords[] = new String[len + k];
            System.arraycopy(myWords, 0, wrappedWords, 0, len);
            for (int i = 0; i < k; i++) {
				wrappedWords[len + i] = myWords[i];
			}
        	generateMarkovChain(k, wrappedWords);
            double etime = System.currentTimeMillis();
            double time = (etime - stime) / 1000.0;
            System.out.println("Time used to generate Markov Chain: " + time);
        }
    	
        int start = myRandom.nextInt(myWords.length - k + 1);
        WordNgram seed = new WordNgram(myWords, start, k);
        
        StringBuilder build = new StringBuilder();
        ArrayList<WordNgram> list;
        double stime = System.currentTimeMillis();
        for (int i = 0; i < numLetters; i++) {
			list = markovChain.get(seed);
            int pick = myRandom.nextInt(list.size());
            WordNgram wg = list.get(pick);
            String st = wg.get(wg.getLength() - 1);
			build.append(st + " ");
			seed = wg;
		}
        double etime = System.currentTimeMillis();
        double time = (etime - stime) / 1000.0;
        this.messageViews("Time to generate: " + time);
        this.notifyViews(build.toString());
    }
    
}
