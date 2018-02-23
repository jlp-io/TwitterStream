import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;

//this file isn't used anymore
public class TwitterPaginator extends TimerTask {
    private String URL;
    private String USER;
    private String PASS;
    
	public static void main(String[] args) {
		Timer timer = new Timer();
		timer.schedule(new TwitterPaginator(), 0, 2000);
	}
	
	@Override
	public void run() {
		try {
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setDebugEnabled(true)
			  .setOAuthConsumerKey("")
			  .setOAuthConsumerSecret("")
			  .setOAuthAccessToken("")
			  .setOAuthAccessTokenSecret("");			
			Configuration builder = cb.build();
			TwitterFactory tf = new TwitterFactory(builder);
			Twitter twitter = tf.getInstance();
			Connection myConn = DriverManager.getConnection(URL, USER, PASS);
			
			ArrayList<Status> recentTweets = new ArrayList<>(twitter.getHomeTimeline());
			for (int i = 0; i < recentTweets.size(); i++) {	    
	        	Tweet tweet1 = new Tweet("@" + recentTweets.get(i).getUser().getScreenName(), recentTweets.get(i).getText(), recentTweets.get(i).getId(), recentTweets.get(i).getFavoriteCount(), recentTweets.get(i).getRetweetCount(), TwitterStreamingAPI.getMediaURL(recentTweets.get(i).toString()), TwitterStreamingAPI.getPermalink(recentTweets.get(i).toString()));
	    		System.out.println(tweet1.toString());
	        	Connection newConn = myConn;
				Statement myStmt = newConn.createStatement();
				String SQL = "INSERT INTO Twitter_Memes(Name, Text, ID, Favorites, Retweets, Media, Permalink, Time) VALUES ('" + tweet1.getName() + "'," + " '" + tweet1.getText() + "'," + " '" + tweet1.getId() + "'," + " '" + tweet1.getFavorites() + "'," + " '" + tweet1.getRetweets() + "'," + " '" + tweet1.getMedia() + "'," + " '" + tweet1.getPermalink() + "'," + " '" + tweet1.getTime() + "')";
				myStmt.executeUpdate(SQL);
			}
			Timestamp time = new Timestamp(System.currentTimeMillis());
			System.out.println("Time: " + time);
		}catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}