import java.util.Arrays;
import java.io.*;  
import java.util.Scanner;
import java.util.ArrayList;  
import java.util.Map; 
import java.util.Collections; 
import java.util.HashMap;




public class Driver {  

    //Creating ArrayList of Hashmaps indicating how often a word appears
    public static HashMap<String, Integer> printTopWords(ArrayList<Sentence> sentences){
        ArrayList <String> allwords = new ArrayList<>();
        HashMap<String, Integer> countMap = new HashMap<>();
        for(int i = 0; i < sentences.size(); i++){
            allwords = sentences.get(i).splitSentence();
            for(int j = 0; j < allwords.size(); j++){  
                if (countMap.containsKey(allwords.get(j))){
                    Integer count = countMap.get(allwords.get(j));
                    countMap.put(allwords.get(j), count + 1);

                }    
                else{
                    countMap.put(allwords.get(j), 1);
                }
            
            }
        
     
        }
        return countMap;

    }
    
public static void main(String[] args) throws Exception  {  
    //parsing a CSV file into Scanner class constructor  
    Scanner sc = new Scanner(new File("testdata.manual.2009.06.14.csv"));   
    ArrayList <Sentence> sentencelist = new ArrayList<>();
    ArrayList <Sentence> Tweets_in_Timeframe = new ArrayList<>();
    ArrayList <Sentence> Tweet_Experiments = new ArrayList<>();
    while (sc.hasNext()){  
        
        Sentence sentence = Sentence.convertLine(sc.nextLine());
        sentencelist.add(sentence);
        //if a tweet contains the person of interest, we keep the tweets and add it into an array of tweets to apply sentiment analysis
        /* 
        if (sentence.keepLebron() == true){
            Tweet_Experiments.add(sentence);
        } 
        */
        /* 
        if (sentence.keepObama() == true){
            Tweet_Experiments.add(sentence);
        }
        */

         if (sentence.keepBobbyFlay() == true){
            Tweet_Experiments.add(sentence);
        }
        
        /* 
        if (sentence.keepPelosi() == true){
            Tweet_Experiments.add(sentence);
        }
        */

        //arbitrary temporal range to keep 

       if (sentence.keep("May 31 2009-Jun 02 2009") == true){
            Tweets_in_Timeframe.add(sentence);
        }
      
        //System.out.println(sc.nextLine());  //find and returns the next complete token from this scanner  

      // sc.hasnextline
       // sc.nextline
      
    }
    //System.out.println(Tweets_in_Timeframe);
    System.out.println(Tweet_Experiments);
    // sentiment analysis 
    // for all tweets
    /* 
    for (Sentence sent : sentencelist) {
        sent.setSentiment(sent.getSentiment(sent.getText()));
        String s = "" + sent.getSentiment();
        System.out.println(sent.getText() + " | sentiment rating: " + s);
    }
    */
    // sentiment analysis for experiments 
  
    for (Sentence sent : Tweet_Experiments) {
        sent.setSentiment(sent.getSentiment(sent.getText()));
        String s = "" + sent.getSentiment();
        System.out.println(sent.getText() + " | sentiment rating: " + s);
    }
    
    HashMap<String, Integer> WordCounts = printTopWords(sentencelist);
    
    Map.Entry<String, Integer> maxEntry = null;
    for (Map.Entry<String, Integer> entry : WordCounts.entrySet())
        if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
            maxEntry = entry;
    int maxValueLen = maxEntry.getValue().toString().length();
    ArrayList <String> results = new ArrayList<String>();
    for (Map.Entry set : WordCounts.entrySet()){
        String value = set.getValue().toString();
        while(value.length() < maxValueLen)
            value = " " + value;
        results.add(value + " of " + set.getKey());
    }
    Collections.sort(results);
    Collections.reverse(results);
 //   for (int i = 0; i < results.size() && i < 100; i++)
 //       System.out.println(results.get(i));
     

  
   

    
    sc.close();  //closes the scanner  
    }

}  


