package gate;

import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;

import gate.util.GateException;
import gate.util.persistence.PersistenceManager;
import gate.Document;

import java.io.File;
import java.io.IOException;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitterFeed.twitterFeed;
/*
 * This class to get the tweets form twitter with lastID and analysis them one by one before getting the next patch of 
 * tweets. 
 */
public class Analysis {

	static long lastTweetID;
	
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
			twitterStream.getStreamS(lastTweetID);
			
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error 001: " + e.getMessage());
		}
		
		return tweets; 
	}
	
	private static Document createDoc(List<Status> list)
	{
		StringBuffer sb = new StringBuffer();
		Document doc = null; 
		for(int i = 0; i< list.size(); i++)
		{
			Status tweet = list.get(i);
			sb.append("@" + "ID: "+tweet.getId() + "- User: " +tweet.getUser().getScreenName() + " - Text: " + tweet.getText() +"\r\n");
		}
		try {
			doc = Factory.newDocument(sb.toString());
			//System.out.println(sb.toString());			
			
		} catch (ResourceInstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc;
	}
	/*private void initGate()
	{
		
	}*/
	public static void main(String[] args) throws GateException, TwitterException, IOException
	{
		try {
			
			Gate.setUserConfigFile(new File("user-gate.xml")); 
	        Gate.setSiteConfigFile(new File("gate.xml"));
	        GateCore gateCore = new GateCore();
	        gateCore.makeVisible(); 
	        
			File gappFile = new File("Resources/Golder.gapp");
			Corpus cor = Factory.newCorpus("Cor1");
			Document d = createDoc(getTweets());
			cor.add(d);
			
			SerialAnalyserController pipeLine = (SerialAnalyserController)PersistenceManager.loadObjectFromFile(gappFile);
			pipeLine.setName("ReadyMade");
			pipeLine.setCorpus(cor);
			Gate.setExecutable(pipeLine);
			Gate.getExecutable().execute();
			
			
			
		} catch (GateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
}