package mobile.database.dbtest02;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateActivity extends AppCompatActivity {

    EditText etName;
    EditText etPhone;
    EditText etCategory;

    ContactDBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etCategory = findViewById(R.id.etCategory);

        helper = new ContactDBHelper(this);

    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnUpdateContact:
//                DB 데이터 업데이트 작업 수행


                break;
            case R.id.btnUpdateContactClose:
//                DB 데이터 업데이트 작업 취소


                finish();
                break;
        }
    }


}
