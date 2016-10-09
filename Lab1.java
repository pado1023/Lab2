import java.io.File;
import java.io.FileNotFoundException;
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
 * Put a short phrase describing the program here.
 *
 * @author Vincent Young, Alex Sarrouh
 *
 */
public final class Lab1 {

	public static String[] stopwords = {"a", "as", "able", "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards", "again", "against", "aint", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "arent", "around", "as", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "cmon", "cs", "came", "can", "cant", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldnt", "course", "currently", "definitely", "described", "despite", "did", "didnt", "different", "do", "does", "doesnt", "doing", "dont", "done", "down", "downwards", "during", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "far", "few", "ff", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadnt", "happens", "hardly", "has", "hasnt", "have", "havent", "having", "he", "hes", "hello", "help", "hence", "her", "here", "heres", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "id", "ill", "im", "ive", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isnt", "it", "itd", "itll", "its", "its", "itself", "just", "keep", "keeps", "kept", "know", "knows", "known", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "right", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldnt", "since", "six", "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "ts", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "thats", "thats", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "theres", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "theyd", "theyll", "theyre", "theyve", "think", "third", "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "viz", "vs", "want", "wants", "was", "wasnt", "way", "we", "wed", "well", "were", "weve", "welcome", "well", "went", "were", "werent", "what", "whats", "whatever", "when", "whence", "whenever", "where", "wheres", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whos", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", "wont", "wonder", "would", "would", "wouldnt", "yes", "yet", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves", "zero"};
	
    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private Lab1() {
    }
    
    //Keeps track of each term occurring in corpus and how many articles it appears in
    private static Map<String,Integer> termFreq;
    
    //This is a nested map that has each article ID mapped to its feature vector
    private static Map<String, Map<String, Integer>> tf_idf_featureMap;
    
    private static ArrayList<String> corpusTerms;
    private static ArrayList<String> heavyFilteredTerms;
    private static ArrayList<String> allTopics;
    
    //Alex's featureMap
    private static Map<String, String[]> places_map;
    private static Map<String, String[]> topics_map;
    private static Map<String, Map<String, Integer>> heavy_filtered_map;
    
    //Keeps track of total number of documents
    private static int numOfDoc = 0;
    
    
    /**
     * Stem a term
     * @param term
     * @return stem of the String term
     */
    public static String stemTerm(String term){
    	PorterStemmer stemmer = new PorterStemmer();
    	stemmer.setCurrent(term);
    	stemmer.stem();
    	return stemmer.getCurrent();
    }
    
    public static HashMap<String,Integer> tokenizeArticle(Elements topic, Elements places, Elements body){
    	
    	StandardTokenizer tokenizer = new StandardTokenizer(AttributeFactory.DEFAULT_ATTRIBUTE_FACTORY);
        OffsetAttribute offsetAttribute = tokenizer.getAttribute(OffsetAttribute.class);
        CharTermAttribute termAttribute = tokenizer.getAttribute(CharTermAttribute.class);
    	
        Elements[] elements = {topic,places,body};
        HashMap<String,Integer> featureVector = new HashMap<String,Integer>();
    	
    	try{
            for (int x = 0; x<elements.length; x++){
            	StringReader reader;
            	if (x==1){
            		reader = new StringReader(elements[x].toString());
            		tokenizer.setReader(reader);
                    tokenizer.reset();
                    while (tokenizer.incrementToken()){
                		int startOffset = offsetAttribute.startOffset();
                	    int endOffset = offsetAttribute.endOffset();
                	    String term = stemTerm(termAttribute.toString().toLowerCase());

                	    if (featureVector.containsKey(term)){
                	    	featureVector.put(term, featureVector.get(term) + 1);
                	    }else{
                	    	if (!Arrays.asList(stopwords).contains(term)&&!term.equals("d")){
                    	    	if (!corpusTerms.contains(term)) corpusTerms.add(term);
                	    	    featureVector.put(term, 1);
                	    	    if (termFreq.containsKey(term)){
                	    		    termFreq.put(term, termFreq.get(term)+1);
                	    	    }else{
                	    	    	
                	    		    termFreq.put(term, 1);
                	    	    }
                	    	}
                	    }
                	}
            	}else{
            		reader = new StringReader(elements[x].text());
            		tokenizer.setReader(reader);
                    tokenizer.reset();
            		while (tokenizer.incrementToken()){
                		int startOffset = offsetAttribute.startOffset();
                	    int endOffset = offsetAttribute.endOffset();
                	    String term = stemTerm(termAttribute.toString().toLowerCase());

                	    if (featureVector.containsKey(term)){
                	    	featureVector.put(term, featureVector.get(term) + 1);
                	    }else{
                	    	if (!Arrays.asList(stopwords).contains(term)){
                	    		if (x==0&&!allTopics.contains(term)) allTopics.add(term);
                    	    	if (!corpusTerms.contains(term)) corpusTerms.add(term);
                	    	    featureVector.put(term, 1);
                	    	    if (termFreq.containsKey(term)){
                	    		    termFreq.put(term, termFreq.get(term)+1);
                	    	    }else{
                	    		    termFreq.put(term, 1);
                	    	    }
                	    	}
                	    }
                	}
            	}
            	tokenizer.close();
            }
            tokenizer.end();
        } catch (IOException ex){
        	System.out.println("Error: "+ex);
        }
    	
    	return featureVector;
        
    }
    
    
    /**
     * Get the TF-IDF value for each word in each vector
     * @return returns the featured map with TF-IDf values
     */
    public static Map<String, Map<String, Double>> convertToTf_Idf(Map<String, Map<String, Integer>> tf_idf_map){
    	
    	Map<String, Map<String, Double>> updatedFeatureMap = new HashMap<String, Map<String, Double>>();
    
        for (Map.Entry<String,Map<String, Integer>> vector: tf_idf_map.entrySet()){
        	
        	//gets individual feature vector by NewID
        	String thisKey = vector.getKey();
        	Map<String, Integer> valueMap = vector.getValue();
        	
        	//Gets total terms to be used to find IDF
        	int totalTerms = totalNumberOfTerms(valueMap);
        	
        	//Temp vector that will be placed in the updated feature map
        	Map<String, Double> tempVector = new HashMap<String, Double>();
        	
        	//iterates through individual feature vector
        	for(Map.Entry<String, Integer> valueVector: valueMap.entrySet()){
        		String keyPair = valueVector.getKey();
        		int valuePair = valueVector.getValue();
        		
        		//Get TF value
        		double tfValue = getTF(keyPair,valuePair , totalTerms);
        		//Get IDF value
        		double idfValue = getIDF(keyPair);
        		// TF*IDF gives TF_IDF value
        		double tf_idf = tfValue * idfValue;
        		
        		if (tf_idf>0.015){
            		tempVector.put(keyPair, tf_idf);
        		}
     
        	}

        	updatedFeatureMap.put(thisKey, tempVector);
        	
        	
        }
        
    	return updatedFeatureMap;
    	
    }
    /**
     * Get total number of terms
     * @return summation of all the frequencies in the vector map
     */
    public static int totalNumberOfTerms(Map<String, Integer> map){
    	int sumOfFreq = 0;
    	for(Map.Entry<String, Integer> vector: map.entrySet()){
    		sumOfFreq += vector.getValue();
    	}
    	
    	return sumOfFreq;
    	    	
    }
    
    /**
     * Get TF value of a given word
     * @return TF value of a word
     */
    public static double getTF(String key, int freq, int numOfTerms){
    	double tf_value = 0;
    	
    	//TF # of times a term appears in a doc / total # of terms in doc
    	tf_value = (double)freq/numOfTerms;
    	
    	return tf_value;
    }
    
    
    /**
     * Get IDF value of a given word
     * @return IDF value of a word
     */
    public static double getIDF(String key){
    	double idf_value = 0;
    	double temp_value = 0;
    	//IDF: natural log of total # of documents divided by # of doc where term appeared
    	temp_value = (double) numOfDoc / termFreq.get(key);
    	
    	idf_value = Math.log(temp_value);
    	
    	
    	return idf_value;
    }

    private static void printCorpusTerms(ArrayList<String> corpus, CsvWriter writer) throws IOException{
    	writer.write("");
    	for (String term: corpus){
    		writer.write(term);
    	}
    	writer.endRecord();
    }
    
    /**
     * Gets the places of each article
     * @param p
     * @return places as an array of strings
     */
    public static String[] getPlaces(Elements p){
    	//Some string manipulation to get it in proper format
    	String[] places = new String[20];
    	String temp = p.toString();
    	String temp_2 = temp.replace("<d>", "");
    	temp = temp_2.replace("</d>", ",");
    	temp_2 = temp.replace("<places>", "");
    	temp = temp_2.replace("</places>", "");
    	
    	//put the split into the array
    	places = temp.split(",");
    	for(int i= 0; i < places.length; i++){
    		String x = places[i];
    		String y = x.trim();
    		places[i] = y;
    	}
    	
    	return places;
    }
    
    public static String[] getTopics(Elements t){
       	//Some string manipulation to get it in proper format
    	String[] topics = new String[20];
    	String temp = t.toString();
    	String temp_2 = temp.replace("<d>", "");
    	temp = temp_2.replace("</d>", ",");
    	temp_2 = temp.replace("<topics>", "");
    	temp = temp_2.replace("</topics>", "");
    	
    	//put the split into the array
    	topics = temp.split(",");
    	for(int i= 0; i < topics.length; i++){
    		String x = topics[i];
    		String y = x.trim();
    		topics[i] = y;
    		
    	}
    	
    	return topics;
    }
    
    public static boolean containNumber(String item){
    	return item.contains("1") || item.contains("2") || item.contains("3")|| item.contains("4")
    			|| item.contains("5")|| item.contains("6")|| item.contains("7")|| item.contains("8")|| item.contains("9");
    }
    
    /**
     * This method filters what terms are to be printed in the feature vector
     * @param heavy_filter
     * @return filtered map
     */
    public static Map<String, Map<String, Integer>> filter(Map<String, Map<String, Integer>> heavy_filter){
    	Map<String, Map<String, Integer>> filtered = new HashMap<String, Map<String, Integer>>();

    	for(Map.Entry<String, Map<String, Integer>> vector: heavy_filter.entrySet()){
    		Map<String, Integer> single_map = new HashMap<String, Integer>();
    		single_map = vector.getValue();
    		
    		// Iterate through and delete items that contain a period in them
    		// Delete all integers/ numbers
    		// Get rid of all place and reuter "they most likely come from <place> and <reuter tag that did not get caught in tokenizer
    		Map<String, Integer> temp_map = new HashMap<String, Integer>();
    		for(Map.Entry<String, Integer> valueVector: single_map.entrySet()){
    			
    			String key = valueVector.getKey();
    			int v = valueVector.getValue();
    			int y = termFreq.get(key);
    			if(!containNumber(key)  && !key.equals("place") && !key.equals("reuter") && !key.contains("\\") && key.length() > 2 && termFreq.get(key) > 100){
    				temp_map.put(valueVector.getKey(), valueVector.getValue());
    	    		if (!heavyFilteredTerms.contains(valueVector.getKey())) heavyFilteredTerms.add(valueVector.getKey());
    			}
    			
    			
    		}
    		
    		filtered.put(vector.getKey(), temp_map);
    	}
    	
    	return filtered;
    }
    
    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
    	
        //Instantiate the tf-idf feature map and heavy_filtered_map
    	tf_idf_featureMap = new HashMap<String, Map<String, Integer>>();
    	heavy_filtered_map = new HashMap<String, Map<String, Integer>>();
    	corpusTerms = new ArrayList<String>();
    	heavyFilteredTerms = new ArrayList<String>();
    	allTopics = new ArrayList<String>();
    	
    	//Places Map
    	places_map = new HashMap<String, String[]>();
    	topics_map = new HashMap<String, String[]>();
    	
    	termFreq = new HashMap<String, Integer>();
        //Iterate through each file
        final File folder = new File("data/");
        for (final File fileEntry : folder.listFiles()) {
            String fileName = fileEntry.getName(); //Get SGM file name
            
            Scanner fileScanner = new Scanner(new File("data/" + fileName));
            //Go through the entire file
            while (fileScanner.hasNextLine()) {
                String singleArticle = fileScanner.nextLine();
                //Get an entire article into a string
                while (!singleArticle.contains("</REUTERS>")
                        && fileScanner.hasNextLine()) {
                    singleArticle = singleArticle + " "
                            + fileScanner.nextLine();
                }
                // Parse single article
                Document article = Jsoup.parse(singleArticle, "",
                        Parser.xmlParser());
                //Get Topics
                Elements topicElement = article.getElementsByTag("topics");
                //Get places
                Elements placesElement = article.getElementsByTag("places");
                // Get Body
                Elements bodyElement = article.getElementsByTag("body");
                // Get Id
                String newID = article.select("reuters").attr("newid");
                
                 
               
                //Get feature vector for article
                HashMap<String,Integer> tokens = tokenizeArticle(topicElement,placesElement,bodyElement);                
                
                // Add feature vector to the tf_idf feature map
                tf_idf_featureMap.put(newID,tokens);
              
                //Add feature vector to heavy_filtered_map
                heavy_filtered_map.put(newID, tokens);
                // Get Places 
                places_map.put(newID, getPlaces(placesElement));
                
                //Get Topics
                topics_map.put(newID, getTopics(topicElement));
                
                //Track number of documents
                numOfDoc++;
                
            }
            fileScanner.close();
        }
       
       //Converting our feature we set aside for tf-idf to vector of tf-idf scores
       Map<String, Map<String, Double>> updated_tf_idf_FeatureMap = convertToTf_Idf(tf_idf_featureMap);
       
       //Convert Alex's feature map to a more refined kind
      Map<String, Map<String, Integer>> updated_heavy_filtered_map = filter(heavy_filtered_map);
       
      
       //Print the feature map/vectors
       //tf-idf printing
       for (int x = 1; x<=numOfDoc; x+=1000){
    	   CsvWriter tf_idf_writer = new CsvWriter("tf-idf part "+ (x/1000+1) + "(TYPE2).csv");
    	   printCorpusTerms(corpusTerms,tf_idf_writer);
    	   for (int y = x; y<x+1000 && y<numOfDoc ; y++){
    		   tf_idf_writer.write("NewID: "+y);
    		   
        	   HashMap<String,Double> currentMap = (HashMap<String, Double>) updated_tf_idf_FeatureMap.get(Integer.toString(y));
        	   
        	   for (String cterm: corpusTerms){
        		   
        		   if (currentMap.containsKey(cterm)){
        			   tf_idf_writer.write(currentMap.get(cterm).toString());
        		   }
        		   else tf_idf_writer.write("0");
        	   }
        	   tf_idf_writer.endRecord();
        	   System.out.println("Article "+y+" printed (TYPE2)");
    	   }
    	   tf_idf_writer.close();
       }
       
       CsvWriter topicsWriter = new CsvWriter("topics.csv");
       printCorpusTerms(allTopics,topicsWriter);
       for (int x = 1; x<=numOfDoc; x++){
    	   topicsWriter.write("NewID: "+x);
    	   HashMap<String,Double> currentMap = (HashMap<String,Double>) updated_tf_idf_FeatureMap.get(Integer.toString(x));
    	   for (String topic: allTopics){
    		   if (currentMap.containsKey(topic)) topicsWriter.write(currentMap.get(topic).toString());
    		   else topicsWriter.write("0");
    	   }
    	   topicsWriter.endRecord();
    	   System.out.println("TOPICS WRITTEN");
       }
       topicsWriter.close();
       
       
       //The heavy filtered feature vector part
       for (int x = 1; x<=numOfDoc; x+=1000){
    	   CsvWriter filter_writer = new CsvWriter("heavy-filtered-part "+ (x/1000+1) + "(TYPE2).csv");
    	   printCorpusTerms(heavyFilteredTerms,filter_writer);
    	   for (int y = x; y<x+1000; y++){
    		   if (y==numOfDoc) break;
    		   filter_writer.write("NewID: "+y);
    		   
        	   HashMap<String,Integer> currentMap = (HashMap<String, Integer>) updated_heavy_filtered_map.get(Integer.toString(y));
        	   
        	   for (String cterm: heavyFilteredTerms){
        		   
        		   if (currentMap.containsKey(cterm)){
        			   filter_writer.write(currentMap.get(cterm).toString());
        		   }
        		   else filter_writer.write("0");
        	   }
        	   filter_writer.endRecord();
        	   System.out.println("Filtered Article "+y+" printed (TYPE2)");
    	   }
    	   filter_writer.close();
       }
       
    }
}

