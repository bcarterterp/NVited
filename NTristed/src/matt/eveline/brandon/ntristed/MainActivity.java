package matt.eveline.brandon.ntristed;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	EditText eText;
	ArrayList<String> handles = new ArrayList<String>();
	public static final String PREFS_NAME = "MyPrefsFile";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_up);
		eText = (EditText) findViewById(R.id.search);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// gets text from textbox, sends to second view
	public void submit(View view) {
		if (!eText.getText().toString().equals("")) {
			String toSplit;
			String[] toSend;
			Intent intent = new Intent(this, EventWall.class);
			toSplit = eText.getText().toString().toUpperCase();
			toSend = toSplit.split(" ");
			if(toSend.length != 0){
				intent.putExtra("toSend", toSend);
				startActivity(intent);
			}else{
				Toast.makeText(getApplicationContext(), "No Handles Entered!", 
						   Toast.LENGTH_SHORT).show();
				eText.setText("");
				eText.setHint("Enter Twitter Handles Here");
			}
		}else{
			Toast.makeText(getApplicationContext(), "No Handles Entered!", 
					   Toast.LENGTH_SHORT).show();
		}
	}

	public void setFavorites(View view) {

		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		String favorites = settings.getString("favorite", "none");
		if (!favorites.equals("none")) {
			eText.setText(favorites);
		}

	}

	public void clear(View view) {

		eText.setText("");
		eText.setHint("Enter Twitter Handles Here");
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    String favorites = "";
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putString("favorite", favorites);
	    editor.commit();

	}

}