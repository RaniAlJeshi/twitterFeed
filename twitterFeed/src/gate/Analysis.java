package gate;

import gate.gui.MainFrame;
import gate.util.GateException;

import java.io.File;
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

	long lastTweetID = 0;
	
	private List getTweets()
	{
		
		List<Status> tweets = null; 
		
		//Twitter Start
		twitterFeed twitterStream = new twitterFeed();
		
		Twitter twitter = new TwitterFactory().getInstance();
		
		try {
			tweets = twitter.getHomeTimeline();
			lastTweetID = tweets.get(tweets.size()-1).getId(); 
			twitterFeed feed = new twitterFeed(); 
			twitterStream.getStreamS(lastTweetID);
			
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error 001: " + e.getMessage());
		}
		
		return tweets; 
	}
	
	private void initGate()
	{
		
	}
	public static void main(String[] args) throws GateException, TwitterException
	{
		try {
			//GateCore GC = new GateCore();
			File plugins = new File("lib/plugins");
			gate.Gate.setPluginsHome(plugins);
			gate.Gate.init();
			MainFrame.getInstance().setVisible(true);
		} catch (GateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
}