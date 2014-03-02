package matt.eveline.brandon.ntristed;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class Parser {
	private static String consumerKey = "@@@@@@@@@@";
	private static String consumerSecret = "@@@@@@@@@@";
	private static String accessToken = "@@@@@@@@@@";
	private static String accessSecret ="@@@@@@@@@@";
	private static ConfigurationBuilder cb;
	private static TwitterFactory factory;
	private static Twitter twitter;
	private static String user;
	public static ArrayList<Integer> months = new ArrayList<Integer>();
	public static ArrayList<Integer> days = new ArrayList<Integer>();
	
	public Parser(String twitterName){
		 cb = new ConfigurationBuilder();
	     cb.setDebugEnabled(true)
	           .setOAuthConsumerKey(consumerKey)
	          .setOAuthConsumerSecret(consumerSecret)
	            .setOAuthAccessToken(accessToken)
	          .setOAuthAccessTokenSecret(accessSecret);
	    factory = new TwitterFactory(cb.build());
		twitter = factory.getInstance();
		user = twitterName;
	}
	public ArrayList<Integer> getMonths(){
		return months;
	}
	
	public ArrayList<Integer> getDays(){
		return days;
	}
	public static boolean isValid (){
		Boolean valid = false;
		try{
			twitter.showUser(user);
			valid = true;
		}catch(TwitterException e){
			if(e.getStatusCode() == 404){
				valid = false;
			}
		}
		return valid;
	}
	
	

	public static Boolean isRelevant(Status tweet, String str){
		Date date = new Date();
		int twyr = tweet.getCreatedAt().getYear();
		int twmon = -1;
		int twday = -1;	
		Pattern p = Pattern.compile("(?i)(Jan|January|February|Feb|Mar|March|Apr|April|May|Jun|June|Jul|July|Aug|August|Sept|September|Oct|October|Nov|November|December|dec|Jun) ");
		Matcher m = p.matcher(str);
		if(m.find()){
			twmon = findMonth(m.group());
			Pattern p1 = Pattern.compile("([0-9][0-9])|[0-9]");
			m.reset();
			m.usePattern(p1);
			if(m.find())
			twday = Integer.parseInt(m.group());
		}else{
			p = Pattern.compile("([0-9])+/");
			m.reset();
			m = p.matcher(str);
			String mon  = null;
			if (m.find())
				mon = m.group().replaceFirst("/", "");
			twmon = Integer.parseInt(mon);
			p = Pattern.compile("/[0-9]+");
			m.reset();
			m = p.matcher(str);
			m.find();
			String day = m.group().replace("/", "");
			twday = Integer.parseInt(day);
		}
		
	if(twyr == date.getYear() && date.getMonth() <= twmon && date.getDate() < twday){
		months.add(twmon);
		days.add(twday);
		return true;
	}
	
	return false;
	}
	
	public static int findMonth(String str){
		Pattern p = Pattern.compile("(?i)(Jan|January)");
		Matcher m = p.matcher(str);
		if(m.find()){
			return 1;
		}
		p = Pattern.compile("(?i)(Feb|February)");
		m = p.matcher(str);
		if(m.find()){
			return 2;
		}
		p = Pattern.compile("(?i)(Mar|March)");
		m = p.matcher(str);
		if(m.find()){
			return 3;
		}
		p = Pattern.compile("(?i)(Apr|April)");
		m = p.matcher(str);
		if(m.find()){
			return 4;
		}
		p = Pattern.compile("(?i)(May)");
		m = p.matcher(str);
		if(m.find()){
			return 5;
		}
		p = Pattern.compile("(?i)(June)");
		m = p.matcher(str);
		if(m.find()){
			return 6;
		}
		p = Pattern.compile("(?i)(July)");
		m = p.matcher(str);
		if(m.find()){
			return 7;
		}
		p = Pattern.compile("(?i)(Aug|August)");
		m = p.matcher(str);
		if(m.find()){
			return 8;
		}
		p = Pattern.compile("(?i)(Sept|September)");
		m = p.matcher(str);
		if(m.find()){
			return 9;
		}
		p = Pattern.compile("(?i)(Oct|October)");
		m = p.matcher(str);
		if(m.find()){
			return 10;
		}
		p = Pattern.compile("(?i)(November|Nov)");
		m = p.matcher(str);
		if(m.find()){
			return 11;
		}
		p = Pattern.compile("(?i)(Dec|December)");
		m = p.matcher(str);
		if(m.find()){
			return 12;
		}
		
		return -1;
	}
	
	@SuppressWarnings("deprecation")
	public static Boolean isItTodayorTomorrow(Status tweet){
		Date date = new Date();
		int twdate = tweet.getCreatedAt().getDay();
		int twmonth = tweet.getCreatedAt().getMonth();
		if(twdate == date.getDay() && twmonth == date.getMonth()){
			months.add(twmonth+1);
			days.add(tweet.getCreatedAt().getDate());
			return true;
		}else if (twdate == date.getDay()+1 && twmonth == date.getMonth()){
			months.add(twmonth+1);
			days.add(tweet.getCreatedAt().getDate());
			return true;
		}else{
			return false;
		}
	}
	
	public static Boolean isEvent (Status s){
		Pattern dayPat = Pattern.compile("(?i)(today|tonight|tomorrow)");
		Pattern timePat = Pattern.compile("(?i)((1[0-2])|[1-9])(:[0-5][0-9])( |)(pm|am)|(((1[0-2])|[1-9])( |)(pm|am))");
		Matcher m = dayPat.matcher(s.getText());
		if(m.find()){
			m.reset();
			m.usePattern(timePat);
			if(m.find()){
				if(isItTodayorTomorrow((s))){
					return true;
				}
			}
		}else{
			//Matcher timeMatch  = timePat.matcher(s.getText());
			Pattern longDatePat = Pattern.compile("(?i)(Jan|January|February|Feb|Mar|March|Apr|April|May|June|July|Jul|Aug|August|Sept|September|Oct|October|Nov|November|December|dec|Jun) ([0-3][0-9]|[0-9])");
			//Matcher longDateMatch = longDatePat.matcher(s.getText());
			Pattern shortDatePat = Pattern.compile("(([0-1][0-9])|[0-9])/(([0-3][0-9])|[0-9])");
			//Matcher shortDateMatch = shortDatePat.matcher(s.getText());
			m.reset();
			m.usePattern(timePat);
			if(m.find()){
				m.reset();
				m.usePattern(longDatePat);
				if(m.find()){
					if(isRelevant(s,m.group())){
						return true;
					}
				}else {
					m.reset();
					m.usePattern(shortDatePat);
					if(m.find()){
						if(isRelevant(s,m.group())){
							return true;
						}
					}
				}
			}
			return false;
		}
		return false;
	}
	
	
	public ArrayList<String> tweets(){
		List<Status> status = null;
		ArrayList<String> accepted = new ArrayList<String>();
 		try{
			status = twitter.getUserTimeline(user, new Paging(1,30));
				for (Status s : status) {
            		if(isEvent(s)){
            		accepted.add(s.getText());
            		}
            	}
			
		}catch(TwitterException e){
			if(e.getStatusCode() == 404){
				return null;
			}else{
			e.printStackTrace();
			System.out.println("Failed to get time line:" + e.getMessage());
			System.exit(-1);
			}
		}
		return accepted;
	}
}
