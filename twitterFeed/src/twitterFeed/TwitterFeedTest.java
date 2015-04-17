package twitterFeed;


import java.util.List;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TwitterFeedTest {
	/*
	 * 1- Forward engineer the Database and test again. - Success 
	 * 2- populate the database from Gate and twitter. 
	 * 3- Apply what u get to the demo account 
	 */
	
	public static void main(String[] args) throws TwitterException //throws GateException, TwitterException
	{
		
		long lastTweetID = 0;
		List<Status> tweets; 
		
		//Twitter Start
		twitterFeed twitterStream = new twitterFeed();
		
		Twitter twitter = new TwitterFactory().getInstance();
		
		tweets = twitter.getHomeTimeline();
		System.out.println("Last Tweet: " + tweets.get(tweets.size()-1).getId()+ "User: "+twitter.getScreenName());  
		lastTweetID = tweets.get(tweets.size()-1).getId(); 
		twitterStream.getStreamS(lastTweetID);
		System.out.println(lastTweetID);
		
		//Twitter End
			
			//Gate Start
			//Gate.init(); 
			//MainFrame.getInstance().setVisible(true); 
			//Factory.newDocument("This is a document");
			//Gate End
			
			//MySQL Start
			//MySQL test = new MySQL(); 
			//test.testConn();
			//MySQL End
		//	break; 
		
		//GateCore GC = new GateCore(); 
		
	}
}
