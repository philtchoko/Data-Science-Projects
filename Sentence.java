import java.util.Arrays;
import java.util.ArrayList;
import java.util.Properties;
import org.ejml.simple.SimpleMatrix;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.sql.Timestamp;
import java.util.Date;
import java.text.ParseException;


public class Sentence{
    private String sentence;
    private String author;
    private String timestamp;
    private int sentiment;
    
    public Sentence(String text, String author, String timestamp){
        this.sentence = text;
        this.author = author;
        this.timestamp = timestamp;

    }
    
    public int getSentiment() {
		return sentiment;
	}
	
	public void setSentiment(int sentiment) {
		this.sentiment = sentiment;
	}


    public String getText(){
        return sentence;

    }
    public void setText(String text){
       this.sentence = text;
    }
    
    public String getTimestamp(){
        return timestamp;
    }
    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }
    
    public String getAuthor(){
        return author;
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public String toString(){
        String result = "";
        result = "{author:" + author + "," + " sentence:" + "\"" + sentence + "\"" + ", timestamp:" + "\"" + timestamp + "\"" + "}";
        return result;


    }
    
    // convertLine removes punctuation and undesired parts of tweets contained from the raw data 
    public static Sentence convertLine(String line){
		String[] stringarray = line.split("\",\"");
        System.out.println(stringarray[2]);
        //System.out.println(stringarray);
        String timestamp = formatDate(stringarray[2]);
        String author = stringarray[4];
        String text = removePunct(stringarray[5]);
        Sentence s = new Sentence(text, author, timestamp);
        return s;
     }
    // formatDate keeps the day, month, and year
    private static String formatDate(String timestamp){
        String[] date = timestamp.split(" ");
        return date[1] + " " + date[2] + " " + date[5].replaceAll("\"","");
    }

	private static String removePunct(String line){
        return line.replaceAll("[\\p{Punct}&&[^@]]+", "");
    }
    
    // splitSentence makes every word lowercase and removes stopwords to make room for more meaningful words in hashmapping
    public ArrayList <String> splitSentence(){
        String[] stopwords = {"a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "aren't", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "can't", "cannot", "could", "couldn't", "did", "didn't", "do", "does", "doesn't", "doing", "don't", "down", "during", "each", "few", "for", "from", "further", "had", "hadn't", "has", "hasn't", "have", "haven't", "having", "he", "he'd", "he'll", "he's", "her", "here", "here's", "hers", "herself", "him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is", "isn't", "it", "it's", "its", "itself", "let's", "me", "more", "most", "mustn't", "my", "myself", "no", "nor", "not", "of", "off", "on", "once", "only", "or", "other", "ought", "our", "ours ourselves", "out", "over", "own", "same", "shan't", "she", "she'd", "she'll", "she's", "should", "shouldn't", "so", "some", "such", "than", "that", "that's", "the", "their", "theirs", "them", "themselves", "then", "there", "there's", "these", "they", "they'd", "they'll", "they're", "they've", "this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "wasn't", "we", "we'd", "we'll", "we're", "we've", "were", "weren't", "what", "what's", "when", "when's", "where", "where's", "which", "while", "who", "who's", "whom", "why", "why's", "with", "won't", "would", "wouldn't", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves"};
        ArrayList <String> words = new ArrayList();
        String [] r = sentence.split(" ");
        for (int i = 0; i < r.length; i++) {
            r[i] = r[i].toLowerCase();
            boolean stopword = false;
            for (int j = 0; j < stopwords.length; j++){
                if (r[i].equals(stopwords[j])){
                    stopword = true;
                    break;
                }
                
            }
            
           if(!stopword){
            words.add(r[i]);
           }
        }
        return words;
    }
    public int getSentiment(String text){
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Annotation annotation = pipeline.process(text);
        CoreMap sentence = annotation.get(CoreAnnotations.SentencesAnnotation.class).get(0);
        Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
        return RNNCoreAnnotations.getPredictedClass(tree);
    }
    
    public boolean keepBobbyFlay(){
        if(getText().contains("Bobby Flay") || getText().contains("bobby flay")){
            return true;
        }
        return false;
    }
    public boolean keepLebron(){
        if(getText().contains("lebron") || (getText().contains("Lebron")) || (getText().contains("LeBron"))) {
            return true;
        }
       return false;

    }
    public boolean keepObama(){
        if(getText().contains("Obama") || (getText().contains("obama"))){
            return true;
        }
        return false;
    }
    
    public boolean keepPelosi(){
        if(getText().contains("Pelosi") || (getText().contains("pelosi"))){
            return true;
        }
        return false;
    }
    
    
   

     
    public boolean keep(String temporalRange){
      try{
        String dates[] = temporalRange.split("-");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");
        Date beforeStamp = dateFormat.parse(dates[0]);
        Date afterStamp = dateFormat.parse(dates[1]);
        Date timestamp = dateFormat.parse(getTimestamp());
        Timestamp beforeTimestamp = new Timestamp(beforeStamp.getTime());
        Timestamp afterTimestamp = new Timestamp(afterStamp.getTime());
        Timestamp newTimestamp = new Timestamp(timestamp.getTime());
        if (newTimestamp.after(afterTimestamp) || newTimestamp.before(beforeTimestamp)){
            return false;
            
        }

       

        } catch (ParseException e){
          System.out.println("Exception :" + e);
          

        }
        return true;
    }

}