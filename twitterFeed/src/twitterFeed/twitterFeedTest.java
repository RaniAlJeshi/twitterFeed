package twitterFeed;

import java.util.List;

import twitter4j.Status;

public class twitterFeedTest {
	/*
	 * 1- Forward engineer the Database and test again. - Success 
	 * 2- populate the database from Gate and twitter. 
	 * 3- Apply what u get to the demo account 
	 */
	
	public static void main(String[] args) //throws GateException, TwitterException
	{
		
		long lastTweetID = 0;
		List<Status> tweets; 
		//while(true)
		//{
			//Twitter Start
			twitterFeed twitterStream = new twitterFeed(); 
			tweets = twitterStream.getStreamS(lastTweetID);
			System.out.println("Last Tweet: " + tweets.get(tweets.size()-1).getId());  
			lastTweetID = tweets.get(tweets.size()-1).getId(); 
			twitterFeed feed = new twitterFeed(); 
			//feed.getAccess(Key, Secret); 
			feed.getStream(); 
			feed.getHistoryStream(); 
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
		//}
		
		//GateCore GC = new GateCore(); 
		
	}
}
