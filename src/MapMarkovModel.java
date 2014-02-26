import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;


public class MapMarkovModel extends AbstractModel {
    private String myString;
    private Random myRandom;
    public static final int DEFAULT_COUNT = 100;
    
    private HashMap<String, ArrayList<String>> markovChain;
    private int markovK;
    
    public MapMarkovModel() {
    	markovK = -1;
        myRandom = new Random(1234);
        myString = "bbbabbabbbbaba";
    }
    
    /**
     * Create a new training text for this model based on the information read
     * from the scanner.
     * @param s is the source of information
     */
    public void initialize(Scanner s) {
        double start = System.currentTimeMillis();
        int count = readChars(s);
        double end = System.currentTimeMillis();
        double time = (end - start) / 1000.0;
        super.messageViews("#read: " + count + " chars in: " + time + " secs");
    }

    protected int readChars(Scanner s) {
        myString = s.useDelimiter("\\Z").next();
        s.close();      
        return myString.length();
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
    
    private void generateMarkovChain(int k, String lexicon) {
    	System.out.println("Generating Markov Chain...");
    	markovK = k;
    	markovChain = new HashMap<String, ArrayList<String>>();
    	int len = lexicon.length();
    	for (int i = 0; i < len - 1 - k; i++) {
			String key = lexicon.substring(i, i + k);
			ArrayList<String> outs = markovChain.get(key);
			if (outs == null) {  // first occurrence
				outs = new ArrayList<String>();
				markovChain.put(key, outs);
			}
			String next = lexicon.substring(i + 1, i + 1 + k);
			outs.add(next);
		}
//    	printMarkovChain();
    }
    
    public void markov(int k, int numLetters) {
    	if (k != markovK || markovChain == null) {
            double stime = System.currentTimeMillis();
            String wrapAroundString = myString + myString.substring(0, k);
        	generateMarkovChain(k, wrapAroundString);
            double etime = System.currentTimeMillis();
            double time = (etime - stime) / 1000.0;
            System.out.println("Time used to generate Markov Chain: " + time);
        }
    	
        int start = myRandom.nextInt(myString.length() - k + 1);
        String seed = myString.substring(start, start + k);
        
        StringBuilder build = new StringBuilder();
        ArrayList<String> list;
        double stime = System.currentTimeMillis();
        for (int i = 0; i < numLetters; i++) {
			list = markovChain.get(seed);
            int pick = myRandom.nextInt(list.size());
			String st = list.get(pick);
			char ch = st.charAt(st.length() - 1);
			build.append(ch);
			seed = st;
		}
        double etime = System.currentTimeMillis();
        double time = (etime - stime) / 1000.0;
        this.messageViews("Time to generate: " + time);
        this.notifyViews(build.toString());
    }
}
