package matt.eveline.brandon.ntristed;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.RadioGroup.LayoutParams;

public class Profile extends Activity {

	String events_text[];
	int months[];
	int days[];
	ArrayList<Button> events = new ArrayList<Button>();
	String handle;
	LinearLayout event_holder;
	public static final String PREFS_NAME = "MyPrefsFile";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_layout);
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

		event_holder = (LinearLayout) findViewById(R.id.single_wall);
		Intent tweeter = getIntent();
		events_text = tweeter.getExtras().getStringArray("events");
		months = tweeter.getExtras().getIntArray("months");
		days = tweeter.getExtras().getIntArray("days");
		handle = tweeter.getExtras().getString("name");
		TextView name = (TextView) findViewById(R.id.handle_name);
		name.setOnClickListener(setFavorite(handle));
		name.setText(handle);

		addEvents();

	}

	private void addEvents() {

		for (int i = 0; i < events_text.length; i++) {

			Button new_event = newEvent(handle, events_text[i]);
			int imageResource = getResources().getIdentifier("button",
					"drawable", getPackageName());
			new_event.setBackgroundResource(imageResource);
			new_event.setTextColor(Color.parseColor("#33DDDD"));
			new_event.setOnClickListener(select(events_text[i],months[i],days[i]));
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			params.setMargins(5, 0, 5, 5);
			new_event.setLayoutParams(params);
			events.add(new_event);
			event_holder.addView(new_event);

		}
	}

	private Button newEvent(String name, String info) {

		String content = name + "\n" + info;
		Button new_event = new Button(this);
		new_event.setText(content);

		return new_event;

	}

	public View.OnClickListener select(final String content,final int mon,final int day) {

		return new View.OnClickListener() {

			@Override
			public void onClick(final View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						v.getContext());
				builder.setTitle("Send Email or Set Calendar?");
				builder.setPositiveButton("Email Please!",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {

								dialog.dismiss();
								Intent intent = new Intent(Intent.ACTION_SEND);
								intent.setType("plain/text");
								intent.putExtra(Intent.EXTRA_SUBJECT, handle
										+ " event!");
								intent.putExtra(Intent.EXTRA_TEXT, content
										+ "\n\nBrought to you by NVited!");
								startActivity(Intent.createChooser(intent, ""));

							}
						});
				builder.setNegativeButton("Set Calendar!",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {

								dialog.dismiss();
								Calendar beginTime = Calendar.getInstance();
								int year = beginTime.get(Calendar.YEAR);
								beginTime.set(year, mon, day, 12, 00);
								Calendar endTime = Calendar.getInstance();
								endTime.set(year, mon, day, 12, 00);
								Intent intent = new Intent(Intent.ACTION_INSERT)
										.setData(Events.CONTENT_URI)
										.putExtra(
												CalendarContract.EXTRA_EVENT_BEGIN_TIME,
												beginTime.getTimeInMillis())
										.putExtra(
												CalendarContract.EXTRA_EVENT_END_TIME,
												endTime.getTimeInMillis())
										.putExtra(Events.TITLE,
												handle + " event!")
										.putExtra(
												Events.DESCRIPTION,
												content
														+ "\n\nBrought to you by NVited!")
										.putExtra(Events.EVENT_LOCATION,
												"See Tweet")
										.putExtra(Events.AVAILABILITY,
												Events.AVAILABILITY_BUSY);
								startActivity(intent);

							}
						});

				AlertDialog replaceDialog = builder.create();
				replaceDialog.show();

			}
		};
	}
	
	
	public View.OnClickListener setFavorite(final String who) {

		return new View.OnClickListener() {

			@Override
			public void onClick(final View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						v.getContext());
				builder.setTitle("Add "+who+" to favorites?");
				builder.setPositiveButton("Yea!",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {

								dialog.dismiss();
								SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
							    String favorites = settings.getString("favorite", "");

							    if(!favorites.contains(who)){
							    	
							    	favorites = favorites.concat(who);
							    	
							    }

							    SharedPreferences.Editor editor = settings.edit();
							    editor.putString("favorite", favorites);
							    editor.commit();

							}
						});
				builder.setNegativeButton("No!",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {

								dialog.dismiss();
								
							}
						});

				AlertDialog replaceDialog = builder.create();
				replaceDialog.show();

			}
		};
	}
}
