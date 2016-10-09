import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.lang.Math;
import java.lang.reflect.Array;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.util.AttributeFactory;
import org.apache.lucene.util.Version;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.tartarus.snowball.ext.PorterStemmer;

import com.csvreader.CsvWriter;




/**
 * This uses vectors from Lab 1 to then create two classifiers
 *
 * @author Vincent Young, Alex Sarrouh
 *
 */
public final class Lab2 {

	public static String[] stopwords = {"a", "as", "able", "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards", "again", "against", "aint", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "arent", "around", "as", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "cmon", "cs", "came", "can", "cant", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldnt", "course", "currently", "definitely", "described", "despite", "did", "didnt", "different", "do", "does", "doesnt", "doing", "dont", "done", "down", "downwards", "during", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "far", "few", "ff", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadnt", "happens", "hardly", "has", "hasnt", "have", "havent", "having", "he", "hes", "hello", "help", "hence", "her", "here", "heres", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "id", "ill", "im", "ive", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isnt", "it", "itd", "itll", "its", "its", "itself", "just", "keep", "keeps", "kept", "know", "knows", "known", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "right", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldnt", "since", "six", "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "ts", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "thats", "thats", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "theres", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "theyd", "theyll", "theyre", "theyve", "think", "third", "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "viz", "vs", "want", "wants", "was", "wasnt", "way", "we", "wed", "well", "were", "weve", "welcome", "well", "went", "were", "werent", "what", "whats", "whatever", "when", "whence", "whenever", "where", "wheres", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whos", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", "wont", "wonder", "would", "would", "wouldnt", "yes", "yet", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves", "zero"};
	
	//Array of words in tf-idf files (in order in which they were printed to .csv files)
	private static ArrayList<String> TFIDFwords;
	
    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private Lab2() {
    }
    
    /**
     *
     * Returns all a .csv file's words in order to the static arraylist for comparison with the lower
     * lines in the .csv file
     * 
     * @param fileName : name of just one of the files that sorted words with the appropriate method (They
     * should have printed the words in the same order since they go through the term list in the same order)
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    private static ArrayList<String> initializeWordArray(String fileName) throws FileNotFoundException, IOException{
    	ArrayList<String> terms = new ArrayList<String>();
    	
    	try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
    	    String line = br.readLine();
    	    
    	    int x = 0;
    	    while (x<line.length()){
    	    	String nextTerm = Character.toString(line.charAt(x));
    	    	int y = x + 1;
    	    	if (line.charAt(x)=='"'){
    	    		while (y<line.length()){
    	    			nextTerm+=line.charAt(y);
    	    			if (line.charAt(y)=='"'){
    	    				terms.add(nextTerm);
    	    				x = y + 2;
    	    				break;
    	    			}
    	    			y++;
    	    		}
    	    	}else{
    	    		if (y!=line.length()){
    	    			while (true){
    	    				if (y==line.length()-1){
    	    					nextTerm+=line.charAt(y);
    	    					terms.add(nextTerm);
    	    					x=y+1;
    	    					break;
    	    				}
    	    				else if (line.charAt(y)!=','){
    	    					nextTerm+=line.charAt(y);
    	    				}else{
    	    					terms.add(nextTerm);
    	    					x = y+1;
    	    					break;
    	    				}
    	    				y++;
    	    			}
    	    		}else{
    	    			terms.add(nextTerm);
    	    		}
    	    	}
    	    }
    	    
    	    br.close();
    	}
    	
    	return terms;
    }
    
    /**
     * 
     * Use a decision tree to predict articles' topics
     * 
     * @param ids
     * @return new HashMap with the number predicted topics linked to their article numbers (int)
     */
    private HashMap<String,Integer> predictTopcis(int[] ids){
    	
    	HashMap<String,Integer> topics = new HashMap<String,Integer>();
    	
    	
    	
    	
    	
    	
    	return topics;
    }
    
    
    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
    	
    	TFIDFwords = new ArrayList<String>();
    	
    	TFIDFwords = initializeWordArray("tf-idf part 1(TYPE2).csv");
    	
    	System.out.println(TFIDFwords);

    }
       
      
   

}

