package twitterFeed;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Properties;

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class twitterFeed {
	public List<Status> getStreamS(long lastId)
	{
		List<Status> statuses = null; 
		 try 
		 {
	            // gets Twitter instance with default credentials
	            Twitter twitter = new TwitterFactory().getInstance();
	            User user = twitter.verifyCredentials();
	           //Paging p = new Paging(10); 
	            long sinceId = lastId; 
	            
	            if(lastId == 0)
	            {
	            	statuses = twitter.getHomeTimeline(new Paging(1, 10)); 
	            }
	            else
	            {
	            	 statuses = twitter.getHomeTimeline(new Paging().sinceId(sinceId)); //new Paging().since(sinceId));
	            }
	            //ResponseList<Status> statuses = twitter.getUserTimeline();
	            System.out.println("Showing @" + user.getScreenName() + "'s home timeline.");
	            for (Status status : statuses)
	            {
	                //System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText()+"\r\n");
	            	System.out.println("@" + status.getUser().getScreenName() + " - Text: " + status.getText()+ " - Date: "+ status.getCreatedAt()+ " - Source: " 
	                		+status.getSource() + "- ID: "+status.getId()+"\r\n");
	            	
	            }
	        } catch (TwitterException te)
	        {
	            te.printStackTrace();
	            System.out.println("Failed to get timeline: " + te.getMessage());
	            //System.exit(-1);
	        }
		 
		return statuses;
	}
	
	public void getStream()
	{
		 try 
		 {
	            // gets Twitter instance with default credentials
	            Twitter twitter = new TwitterFactory().getInstance();
	            User user = twitter.verifyCredentials();
	      //      Paging p = new Paging(10); 
	            
	            List<Status> statuses = twitter.getHomeTimeline();
	            
	            //ResponseList<Status> statuses = twitter.getUserTimeline();
	            System.out.println("Showing @" + user.getScreenName() + "'s home timeline.");
	            for (Status status : statuses)
	            {
	                //System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText()+"\r\n");
	            	System.out.println("@" + status.getUser().getScreenName() + " - Text: " + status.getText()+ " - Date: "+ status.getCreatedAt()+ " - Source: " 
	                		+status.getSource() + "- ID: "+status.getId()+"\r\n");
	            }
	        } catch (TwitterException te)
	        {
	            te.printStackTrace();
	            System.out.println("Failed to get timeline: " + te.getMessage());
	            System.exit(-1);
	        }
	}
	
	public void getHistoryStream()
	{
		 try 
		 {
			 int n = 0; 
            // gets Twitter instance with default credentials
            Twitter twitter = new TwitterFactory().getInstance();
            User user = twitter.verifyCredentials();
            Paging p = new Paging(1,20); 
            for(n = 1; n<25; n++)
            {
            	p.setPage(n); 
	            List<Status> statuses = twitter.getHomeTimeline(p);
	            
	            //ResponseList<Status> statuses = twitter.getUserTimeline();
	            System.out.println("Showing @" + user.getScreenName() + "'s home timeline.");
	            for (Status status : statuses)
	            {
	            	//I can add these to the database directly and then Analyse only the text in Gate. The ID will facilitate the analysis of the text.
	            	//The Id will be attached within an annotation with the text to gate. 
	                System.out.println( status.getText()+"\r\n");
	            }
            }
        } catch (TwitterException te)
        {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit(-1);
        }
	}
	
	private void getAccess(String key, String secret)
	{
		File file = new File("twitter4j.properties");
        Properties prop = new Properties();
        
        InputStream is = null;
        OutputStream os = null;
        try {
            if (file.exists()) {
                is = new FileInputStream(file);
                prop.load(is);
            }
            if (key.isEmpty()) {
                if (null == prop.getProperty("oauth.consumerKey")
                        && null == prop.getProperty("oauth.consumerSecret")) {
                    // consumer key/secret are not set in twitter4j.properties
                    System.out.println(
                            "Usage: java twitter4j.examples.oauth.GetAccessToken [consumer key] [consumer secret]");
                    System.exit(-1);
                }
            } else {
                prop.setProperty("oauth.consumerKey", key);
                prop.setProperty("oauth.consumerSecret", secret);
                os = new FileOutputStream("twitter4j.properties");
                prop.store(os, "twitter4j.properties");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(-1);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignore) {
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ignore) {
                }
            }
        }
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            RequestToken requestToken = twitter.getOAuthRequestToken();
            System.out.println("Got request token.");
            System.out.println("Request token: " + requestToken.getToken());
            System.out.println("Request token secret: " + requestToken.getTokenSecret());
            AccessToken accessToken = null;

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (null == accessToken) {
                System.out.println("Open the following URL and grant access to your account:");
                System.out.println(requestToken.getAuthorizationURL());
                try {
                    Desktop.getDesktop().browse(new URI(requestToken.getAuthorizationURL()));
                } catch (UnsupportedOperationException ignore) {
                } catch (IOException ignore) {
                } catch (URISyntaxException e) {
                    throw new AssertionError(e);
                }
                System.out.print("Enter the PIN(if available) and hit enter after you granted access.[PIN]:");
                String pin = br.readLine();
                try {
                    if (pin.length() > 0) {
                        accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                    } else {
                        accessToken = twitter.getOAuthAccessToken(requestToken);
                    }
                } catch (TwitterException te) {
                    if (401 == te.getStatusCode()) {
                        System.out.println("Unable to get the access token.");
                    } else {
                        te.printStackTrace();
                    }
                }
            }
            System.out.println("Got access token.");
            System.out.println("Access token: " + accessToken.getToken());
            System.out.println("Access token secret: " + accessToken.getTokenSecret());

            try {
                prop.setProperty("oauth.accessToken", accessToken.getToken());
                prop.setProperty("oauth.accessTokenSecret", accessToken.getTokenSecret());
                os = new FileOutputStream(file);
                prop.store(os, "twitter4j.properties");
                os.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.exit(-1);
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException ignore) {
                    }
                }
            }
            System.out.println("Successfully stored access token to " + file.getAbsolutePath() + ".");
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get accessToken: " + te.getMessage());
            System.exit(-1);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Failed to read the system input.");
            System.exit(-1);
        }
	
	}
	
}
