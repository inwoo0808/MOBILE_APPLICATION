package mobile.database.dbtest02;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class InsertContactActivity extends AppCompatActivity {

	EditText etName;
	EditText etPhone;
	EditText etCategory;

	ContactDBHelper helper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insert_contact);

		etName = findViewById(R.id.editText1);
		etPhone = findViewById(R.id.editText2);
		etCategory = findViewById(R.id.editText3);

		helper = new ContactDBHelper(this);
	}
	
	
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnAddNewContact:
//			DB 데이터 삽입 작업 수행


			helper.close();
			break;
		case R.id.btnAddNewContactClose:
//			DB 데이터 삽입 취소 수행

			finish();
			break;
		}
	}
	
	
	
	
	
}
