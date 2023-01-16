package mobile.example.dbtest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactDBHelper extends SQLiteOpenHelper {

	private final String TAG = "ContactDBHelper";

	private final static String DB_NAME = "contact_db"; 
	public final static String TABLE_NAME = "contact_table";
	public final static String COL_NAME = "name";
	public final static String COL_PHONE = "phone";
	public final static String COL_CAT = "category";

	public ContactDBHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
}


