package mobile.database.dbtest02;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class AllContactsActivity extends AppCompatActivity {
	
	ListView lvContacts = null;
	ContactDBHelper helper;
	Cursor cursor;
	SimpleCursorAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_contacts);
		lvContacts = (ListView)findViewById(R.id.lvContacts);

		helper = new ContactDBHelper(this);

//		  SimpleCursorAdapter 객체 생성
//        adapter = new SimpleCursorAdapter ( /* 매개변수 설정*/ );

		lvContacts.setAdapter(adapter);

//		리스트 뷰 클릭 처리
//        lvContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
//
//           return true;
//            }
//        });

//		리스트 뷰 롱클릭 처리
//		lvContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//			@Override
//			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//				return true;
//			}
//		});


	}

	@Override
	protected void onResume() {
		super.onResume();
//        DB에서 데이터를 읽어와 Adapter에 설정
        SQLiteDatabase db = helper.getReadableDatabase();
        cursor = db.rawQuery("select * from " + ContactDBHelper.TABLE_NAME, null);

        adapter.changeCursor(cursor);
        helper.close();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//        cursor 사용 종료
		if (cursor != null) cursor.close();
	}

}




