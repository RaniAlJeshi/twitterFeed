package gate;

import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;
import gate.Document;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitterFeed.twitterFeed;
/*
 * This class to get the tweets form twitter with lastID and analyze them one by one before getting the next patch of 
 * tweets. 
 */
public class Analysis {

	public static long lastTweetID;
	public List<Signal> signals; 
	static Corpus cor;
	static File gappFile = new File("Resources/Golder.gapp");
	static Boolean gateInit = false; 
	
	public static long getLastTweetID() {
		return lastTweetID;
	}

	public static void setLastTweetID(long lastTweetID) {
		Analysis.lastTweetID = lastTweetID;
	}
	
	/**
	 * Getting tweets from tweeter. 
	 * If lastTweetID exist it will pull tweets from that ID otherwise it will 
	 * pull recent 20 tweets in my time line.  
	 * @return list<status> tweets. 
	 */
	private static List<Status> getTweets()
	{
		
		List<Status> tweets = null; 
		
		//Twitter Start
		twitterFeed twitterStream = new twitterFeed();
		
		Twitter twitter = new TwitterFactory().getInstance();
		
		try {
			tweets = twitter.getHomeTimeline();
			lastTweetID = tweets.get(tweets.size()-1).getId(); 
			//twitterFeed feed = new twitterFeed(); 
			twitterStream.getStreamS(getLastTweetID());
			
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error 001: " + e.getMessage());
		}
		
		return tweets; 
	}
	/**
	 * 
	 * This will take the tweets strip unnessacery information and create a doc 
	 * with ready to process tweets.  
	 * 
	 * @param list
	 * @return gate Document doc
	 */
	private static Document createDoc(List<Status> list)
	{
		StringBuffer sb = new StringBuffer();
		Document doc = null; 
		for(int i = 0; i< list.size(); i++)
		{
			Status tweet = list.get(i);
			sb.append("ID: "+tweet.getId() + " - User: " +tweet.getUser().getScreenName() + " - Text: " + tweet.getText() +".\r\n");
		}
		//Getting last Id in the stream for next stream. 
		setLastTweetID(list.get(list.size()-1).getId()); 
		
		try {
			doc = Factory.newDocument(sb.toString());
			//System.out.println(sb.toString());			
			
		} catch (ResourceInstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc;
	}
	
	/**
	 * Initialize gate for first time. 
	 * @throws GateException
	 */
	private static void initGate() throws GateException
	{
		Gate.setUserConfigFile(new File("user-gate.xml")); 
        Gate.setSiteConfigFile(new File("gate.xml"));
        GateCore gateCore = new GateCore();
        gateCore.makeVisible(); 
        
        cor = Factory.newCorpus("Cor1");
        gateInit = true; 
        
	}
	
	/**
	 * This will analys the patch of tweets from lastTweetedId or
	 * recent 20 tweets. 
	 * 
	 * @throws GateException
	 * @throws TwitterException
	 * @throws IOException
	 */
	public static void NLP_Process() throws GateException, TwitterException, IOException
	{
		if(gateInit == false)
			initGate();
		
		try {//Add a while loop 
			List<Status> tweetsRaw = getTweets(); 
			Document d = createDoc(tweetsRaw);
			cor.add(d);
			SerialAnalyserController pipeLine = (SerialAnalyserController)PersistenceManager.loadObjectFromFile(gappFile);
			pipeLine.setName("ReadyMade");
			pipeLine.setCorpus(cor);
			Gate.setExecutable(pipeLine);
			Gate.getExecutable().execute();
			
			//Take the info from gate and populate Signal List...
			
			AnnotationSet sentenceSignal = d.getAnnotations("SentenceSignal"); 
			for(int j = 0; j > sentenceSignal.size(); j++){
				Signal signal = new Signal();
				Status currentTweet ; 
				Long sentencID  = Long.getLong(sentenceSignal.get(0).getId()+""); 
				//Exclude Repititives 
				while(tweetsRaw.iterator().hasNext()){
					currentTweet = tweetsRaw.iterator().next();
					if(currentTweet.getId() == sentencID)
					{
						signal.setTweetID(sentenceSignal.get(0)+"");
						signal.getDateOfTweet();
						signal.setDateOfTweet(currentTweet.getCreatedAt());
						signal.setTextBody(sentenceSignal.get("PositiveSignal").toString());// or NegativeSignal ..polarity. 
						signal.setCurrency(sentenceSignal.get("CurrencyPair").toString());
						signal.setSignalValue(currentTweet.getRetweetCount());
						//tweetsRaw.remove(currentTweet);
					}
					 
				}
			}
			
			
			
		} catch (GateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	
	/*public static void populateSignals(){
		
	}*/
	
	public static void main(String[] args){
		try {
			NLP_Process();
			//populateSignals(); 
			
		} catch (GateException | TwitterException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
}