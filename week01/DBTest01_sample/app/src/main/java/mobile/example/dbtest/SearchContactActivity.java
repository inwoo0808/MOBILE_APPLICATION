package mobile.example.dbtest;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SearchContactActivity extends Activity {

	ContactDBHelper helper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_contact);
		
		helper = new ContactDBHelper(this);
	}

	
	public void onClick(View v) {

	}

	
}
