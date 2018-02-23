import java.sql.Timestamp;

public class Tweet {
	private String name;
	private String text;
	private Long id;
	private int favorites;
	private int retweets;
	private String media;
	private String permalink;
	private Timestamp time;
	
	public Tweet(String getName, String getText, Long getId, int getFavorites, int getRetweets, String getMedia, String getPermalink) {
		name = getName;
		text = getText;
		id = getId;
		favorites = getFavorites;
		retweets = getRetweets;
		media = getMedia;
		permalink = getPermalink;
		time = new Timestamp(System.currentTimeMillis());		
	}
	
	public String getName() {
		return name;
	}
	
	public String getText() {
		return text;
	}
	
	public Long getId() {
		return id;
	}
	
	public int getFavorites() {
		return favorites;
	}
	
	public int getRetweets() {
		return retweets;
	}
	
	public String getMedia() {
		return media;
	}
	
	public String getPermalink() {
		return permalink;
	}
	
	public Timestamp getTime() {
		return time;
	}
	
	public String toString() {
		return name + " " + text + " " + id + " " + " " + favorites + " " + retweets + " " + media;
	}
}