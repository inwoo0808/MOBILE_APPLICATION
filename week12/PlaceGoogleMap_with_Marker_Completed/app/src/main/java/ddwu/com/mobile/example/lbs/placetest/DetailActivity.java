package ddwu.com.mobile.example.lbs.placetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {

    EditText etName;
    EditText etPhone;
    EditText etAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        
        // 전달받은 intent 값 읽기
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String phone = intent.getStringExtra("phone");
        String address = intent.getStringExtra("address");

        // 화면에 출력
        etName.setText(name);
        etPhone.setText(phone);
        etAddress.setText(address);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                Toast.makeText(this, "Save Place information", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnClose:
                finish();
                break;
        }
    }
}
