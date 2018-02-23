import twitter4j.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.net.URL;

public class TwitterStreamingAPI implements Runnable {
	private static ArrayList<Tweet> streamTweets = new ArrayList<Tweet>();
	private static Connection myConn;
	private static Twitter twitter;
    
	public static void main(String[] args) {
	    String[] userIDs = {"271210628", "132774626", "237634998", "16548023", "759251", "612473"};
 	    String[] neglectedIDs = {"753665145063153664", "726464337510981633", "3945972795", "848234560051703808"};
 	    String[] userNames = {"DankMemePlug", "9GAG", "iFunny", "GirlPosts", "ItsMeowIRL", "someecards", "Lmao", "memeprovider", "ItMeIRL", "FilthyFrank"};
	    String URL = "jdbc:mysql://zwgaqwfn759tj79r.chr7pe7iynqr.eu-west-1.rds.amazonaws.com/igqxgrcy5dw9rxc2";
	    String USER = "gqk0vs5rc6nfbj0w";
	    String PASS = "ib45oys91og5m4q5";
		try {
        myConn = DriverManager.getConnection(URL, USER, PASS);
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("oT8aBKess9jx7Nto8dsUiSeCd")
		  .setOAuthConsumerSecret("URImaSVIVK5nCXNBx1EM2vhebrtbedtR2bv9n8Md8w6lRbJl8I")
		  .setOAuthAccessToken("848234560051703808-dqKD0As0r90oRqEf6LX6G5fHvN4iBpf")
		  .setOAuthAccessTokenSecret("XJNFAwX4qW7lLkKGTuw1Sb8sVICxf9XpQQXl3BAD9nStS");
		
		Configuration builder = cb.build();
					
		TwitterFactory tf = new TwitterFactory(builder);
		twitter = tf.getInstance();
		TwitterStream ts = new TwitterStreamFactory(builder).getInstance();
		
    	StatusListener statusTracker = new StatusListener() {
    		
		        public void onStatus(Status status) {
		        	try {
		        	Tweet tweet1 = new Tweet("@" + status.getUser().getScreenName(), status.getText(), status.getId(), status.getFavoriteCount(), status.getRetweetCount(), getMediaURL(status.toString()), getPermalink(status.toString()));
		        	streamTweets.add(tweet1);
		        	System.out.println(tweet1.toString());		        	
		    		//twitter.updateStatus(tweet1.getText());		
		        	Connection newConn = myConn;
					Statement myStmt = newConn.createStatement();
					String SQL = "INSERT INTO Twitter_Memes(Name, Text, ID, Favorites, Retweets, Media, Permalink, Time) VALUES ('" + tweet1.getName() + "'," + " '" + tweet1.getText() + "'," + " '" + tweet1.getId() + "'," + " '" + tweet1.getFavorites() + "'," + " '" + tweet1.getRetweets() + "'," + " '" + tweet1.getMedia() + "'," + " '" + tweet1.getPermalink() + "'," + " '" + tweet1.getTime() + "')";
					myStmt.executeUpdate(SQL);
		        	}catch(Exception ex) {
		        		System.out.println(ex.getMessage());
		        	}
		        }
		        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
		            System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
		        }
		        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
		            System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
		        }
		        public void onScrubGeo(long userId, long upToStatusId) {
		            System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
		        }
		        public void onException(Exception ex) {
		            ex.printStackTrace();
		        }
				@Override
				public void onStallWarning(StallWarning arg0) {
					// TODO Auto-generated method stub	
				}
		    };

	    UserStreamListener userTracker = new UserStreamListener() {

	    		public void onStatus(Status status) {
		        	try {
		        		
		        	boolean isUser = false;		        	
		        	for (int i = 0; i < userNames.length; i++) {
		        		if (status.getUser().getScreenName().equals(userNames[i])) {
		        			isUser = true;
		        			break;
		        		}
		        	}
		        	
		        	if (isUser) {
		        	Tweet tweet1 = new Tweet("@" + status.getUser().getScreenName(), status.getText(), status.getId(), status.getFavoriteCount(), status.getRetweetCount(), getMediaURL(status.toString()), getPermalink(status.toString()));
		        	streamTweets.add(tweet1);
		        	System.out.println(tweet1.toString());	        	
		    		//twitter.updateStatus(tweet1.getText());		
		        	Connection newConn = myConn;
					Statement myStmt = newConn.createStatement();
					String SQL = "INSERT INTO Twitter_Memes(Name, Text, ID, Favorites, Retweets, Media, Permalink, Time) VALUES ('" + tweet1.getName() + "'," + " '" + tweet1.getText() + "'," + " '" + tweet1.getId() + "'," + " '" + tweet1.getFavorites() + "'," + " '" + tweet1.getRetweets() + "'," + " '" + tweet1.getMedia() + "'," + " '" + tweet1.getPermalink() + "'," + " '" + tweet1.getTime() + "')";
					myStmt.executeUpdate(SQL);
		        	}
		        	}catch(Exception ex) {
		        		System.out.println(ex.getMessage());
		        	}
	            }

	            @Override
	            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
	                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
	            }

	            @Override
	            public void onDeletionNotice(long directMessageId, long userId) {
	                System.out.println("Got a direct message deletion notice id:" + directMessageId);
	            }

	            @Override
	            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
	               System.out.println("Got a track limitation notice:" + numberOfLimitedStatuses);
	            }

	            @Override
	            public void onScrubGeo(long userId, long upToStatusId) {
	               System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
	            }

	            @Override
	            public void onStallWarning(StallWarning warning) {
	               System.out.println("Got stall warning:" + warning);
	            }

	            @Override
	            public void onFriendList(long[] friendIds) {
	               System.out.print("onFriendList");
	                for (long friendId : friendIds) {
	                    System.out.print(" " + friendId);
	                }
	                System.out.println();
	            }

	            @Override
	            public void onFavorite(User source, User target, Status favoritedStatus) {
	                System.out.println("onFavorite source:@"
	                        + source.getScreenName() + " target:@"
	                        + target.getScreenName() + " @"
	                        + favoritedStatus.getUser().getScreenName() + " - "
	                        + favoritedStatus.getText());
	            }

	            @Override
	            public void onUnfavorite(User source, User target, Status unfavoritedStatus) {
	               System.out.println("onUnFavorite source:@"
	                                + source.getScreenName() + " target:@"
	                                + target.getScreenName() + " @"
	                                + unfavoritedStatus.getUser().getScreenName()
	                                + " - " + unfavoritedStatus.getText());
	            }

	            @Override
	            public void onFollow(User source, User followedUser) {
	                System.out.println("onFollow source:@"
	                        + source.getScreenName() + " target:@"
	                        + followedUser.getScreenName());
	            }

	            @Override
	            public void onDirectMessage(DirectMessage directMessage) {
	                System.out.println("onDirectMessage text:"
	                        + directMessage.getText());
	            }

	            @Override
	            public void onUserListMemberAddition(User addedMember, User listOwner, UserList list) {
	                System.out.println("onUserListMemberAddition added member:@"
	                        + addedMember.getScreenName()
	                        + " listOwner:@" + listOwner.getScreenName()
	                        + " list:" + list.getName());
	            }

	            @Override
	            public void onUserListMemberDeletion(User deletedMember, User listOwner, UserList list) {
	                System.out.println("onUserListMemberDeleted deleted member:@"
	                        + deletedMember.getScreenName()
	                        + " listOwner:@" + listOwner.getScreenName()
	                        + " list:" + list.getName());
	            }

	            @Override
	            public void onUserListSubscription(User subscriber, User listOwner, UserList list) {
	                System.out.println("onUserListSubscribed subscriber:@"
	                        + subscriber.getScreenName()
	                        + " listOwner:@" + listOwner.getScreenName()
	                        + " list:" + list.getName());
	            }

	            @Override
	            public void onUserListUnsubscription(User subscriber, User listOwner, UserList list) {
	                System.out.println("onUserListUnsubscribed subscriber:@"
	                        + subscriber.getScreenName()
	                        + " listOwner:@" + listOwner.getScreenName()
	                        + " list:" + list.getName());
	            }

	            @Override
	            public void onUserListCreation(User listOwner, UserList list) {
	                System.out.println("onUserListCreated listOwner:@"
	                        + listOwner.getScreenName()
	                        + " list:" + list.getName());
	            }

	            @Override
	            public void onUserListUpdate(User listOwner, UserList list) {
	                System.out.println("onUserListUpdated listOwner:@"
	                        + listOwner.getScreenName()
	                        + " list:" + list.getName());
	            }

	            @Override
	            public void onUserListDeletion(User listOwner, UserList list) {
	                System.out.println("onUserListDestroyed listOwner:@"
	                        + listOwner.getScreenName()
	                        + " list:" + list.getName());
	            }

	            @Override
	            public void onUserProfileUpdate(User updatedUser) {
	                System.out.println("onUserProfileUpdated user:@" + updatedUser.getScreenName());
	            }

	            @Override
	            public void onBlock(User source, User blockedUser) {
	                System.out.println("onBlock source:@" + source.getScreenName()
	                        + " target:@" + blockedUser.getScreenName());
	            }

	            @Override
	            public void onUnblock(User source, User unblockedUser) {
	                System.out.println("onUnblock source:@" + source.getScreenName()
	                        + " target:@" + unblockedUser.getScreenName());
	            }

	            @Override
	            public void onException(Exception ex) {
	               ex.printStackTrace();
	                System.out.println("onException:" + ex.getMessage());
	            }

				@Override
				public void onFavoritedRetweet(User arg0, User arg1, Status arg2) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onQuotedTweet(User arg0, User arg1, Status arg2) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onRetweetedRetweet(User arg0, User arg1, Status arg2) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onUnfollow(User arg0, User arg1) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onUserDeletion(long arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onUserSuspension(long arg0) {
					// TODO Auto-generated method stub
					
				}
	        };
	        
	    FilterQuery query = new FilterQuery(new long[] {271210628, 132774626, 237634998, 16548023, 759251, 612473});
	    ts.addListener(userTracker);	        
	    ts.filter(query);

 	    /*
	    ts.addListener(statusTracker);
	    String tags[] = {"trump"};
	    ts.filter(new FilterQuery(tags));
 	    */
 	    
		}catch(Exception ex) {
			System.out.println(ex.toString());
			System.out.println(ex.getMessage());
		}
	}	
	
	public void run() {
		try{
		for (int i = 0; i < streamTweets.size(); i++) {
			Status status1 = twitter.showStatus(streamTweets.get(i).getId());
		}
		}catch (Exception e) {
			
		}
	}
	
	public static String getMediaURL(String findURL) {
		String[] foundURLArray = findURL.split(" ");
		String foundURL = null;
		for (int i = 0; i < foundURLArray.length; i++) {
			if (foundURLArray[i].contains("mediaURL=")) {
				foundURL = foundURLArray[i];
				foundURL = foundURL.substring(9, foundURL.length()-1);
			}
		}
		return foundURL;
	}
	
	public static String getPermalink(String getLink) {
		String[] permalinkArray = getLink.split(" ");
		String permalink = null;
		for (int i = 0; i < permalinkArray.length; i++) {
			if (permalinkArray[i].contains("expandedURL=")) {
				permalink = permalinkArray[i];
				permalink = permalink.substring(12, permalink.length()-1);
			}
		}
		return permalink;
	}
}