package matt.eveline.brandon.ntristed;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EventWall extends Activity {

	LinearLayout wall;
	ArrayList<Button> more_info = new ArrayList<Button>();
	String tweeters[];
	Intent single_feed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_wall);

		wall = (LinearLayout) findViewById(R.id.event_wall);

		Intent handles = getIntent();

		tweeters = handles.getExtras().getStringArray("toSend");

		single_feed  = new Intent(this, Profile.class);

		setupWall();
	}
	
	private class DoWork extends AsyncTask<String, Void, String> {
        
		ArrayList<String> findTweets;
		ArrayList<Integer> months;
		ArrayList<Integer> days;
		@Override
        protected String doInBackground(String... urls) {
            
			Parser person = new Parser(urls[0]);
        	findTweets = person.tweets();
        	months = person.getMonths();
        	days = person.getDays();
        	
        	return urls[0];
            
        }
        
        @Override
		protected void onPostExecute(String result) {
        	if (findTweets != null && findTweets.size() != 0) {

				for (int j = 0; j < findTweets.size(); j++) {

					Button new_button = newEvent(result, findTweets.get(j));
					new_button.setOnClickListener(clickedTweet(result,findTweets,months,days));
					int imageResource = getResources().getIdentifier("button", "drawable", getPackageName());
					new_button.setBackgroundResource(imageResource);
					new_button.setTextColor(Color.parseColor("#33DDDD"));
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					params.setMargins(5, 5, 5, 0);
					new_button.setLayoutParams(params);
					more_info.add(new_button);
					wall.addView(new_button);

				}
        	
       }

        
    }
        
	}


	private void setupWall() {

		for (int i = 0; i < tweeters.length; i++) {

			new DoWork().execute(tweeters[i]);
			
		}

	}

	private Button newEvent(String name, String info) {

		String content = name + "\n" + info;
		Button new_event = new Button(this);
		new_event.setText(content);

		return new_event;

	}
	
	private View.OnClickListener clickedTweet(final String name,
			final ArrayList<String> list,final ArrayList<Integer> list2,final ArrayList<Integer> list3) {

		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				single_feed.putExtra("name", name);
				String feed[] = new String[list.size()];
				int months[] = new int[list.size()];
				int days[]= new int[list.size()];
				for (int i = 0; i < list.size(); i++) {

					feed[i] = list.get(i);
					months[i] = list2.get(i);
					days[i] = list3.get(i);

				}
				single_feed.putExtra("events", feed);
				single_feed.putExtra("months", months);
				single_feed.putExtra("days", days);
				startActivity(single_feed);
			}

		};

	}
}
