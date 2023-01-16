package mobile.database.dbtest02;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateActivity extends AppCompatActivity {

    EditText etUpdateName;
    EditText etUpdatePhone;
    EditText etUpdateCategory;

    ContactDBHelper helper;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        etUpdateName = findViewById(R.id.etUpdateName);
        etUpdatePhone = findViewById(R.id.etUpdatePhone);
        etUpdateCategory = findViewById(R.id.etUpdateCategory);

        helper = new ContactDBHelper(this);

        id = getIntent().getLongExtra("id", 0);
    }


    @Override
    protected void onResume() {
        super.onResume();

        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery( "select * from " + ContactDBHelper.TABLE_NAME + " where " + ContactDBHelper.COL_ID + "=?", new String[] { String.valueOf(id) });
        while (cursor.moveToNext()) {
            etUpdateName.setText( cursor.getString( cursor.getColumnIndex(ContactDBHelper.COL_NAME) ) );
            etUpdatePhone.setText( cursor.getString( cursor.getColumnIndex(ContactDBHelper.COL_PHONE) ) );
            etUpdateCategory.setText( cursor.getString( cursor.getColumnIndex(ContactDBHelper.COL_CATEGORY) ) );
        }
        cursor.close();
        helper.close();

    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnUpdateContact:
                /*id 를 기준으로 화면의 값으로 DB 업데이트*/
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues row = new ContentValues();
                row.put(ContactDBHelper.COL_NAME, etUpdateName.getText().toString());
                row.put(ContactDBHelper.COL_PHONE, etUpdatePhone.getText().toString());
                row.put(ContactDBHelper.COL_CATEGORY, etUpdateCategory.getText().toString());
                String whereClause = ContactDBHelper.COL_ID + "=?";
                String[] whereArgs = new String[] { String.valueOf(id) };
                int result = db.update(ContactDBHelper.TABLE_NAME, row, whereClause, whereArgs);
                helper.close();
                String msg = result > 0 ? "Updated!" : "Failed!";
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);       // Intent 없이 실행결과 상태만 전달
                break;
            case R.id.btnUpdateContactClose:
                setResult(RESULT_CANCELED);
                break;
        }
        finish();
    }


}
