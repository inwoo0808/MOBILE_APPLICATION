package work2.mobile.week03.roomhw;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class InsertActivity extends AppCompatActivity {

    final static String TAG = "InsertContactActivity";

    ContactDB contactDB;
    ContactDAO contactDAO;

    EditText etName;
    EditText etPhone;
    EditText etCategory;

    private final CompositeDisposable mDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        etName = findViewById(R.id.editText1);
        etPhone = findViewById(R.id.editText2);
        etCategory = findViewById(R.id.editText3);

        contactDB = ContactDB.getDatabase(this);
        contactDAO = contactDB.contactDao();
    }

    public void onClick(View v) {
        final String name = etName.getText().toString();
        final String phone = etPhone.getText().toString();
        final String category = etCategory.getText().toString();

        switch (v.getId()) {
            case R.id.btnNewContactSave:
                Single<Long> insertResult = contactDAO.insertContact(new Contact(name, phone, category));
                mDisposable.add(
                        insertResult.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(result -> Log.d(TAG, "insert success" + result),
                                        throwable -> Log.d(TAG, "error"))
                );
                break;
            case R.id.btnNewContactClose:

                break;
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }
}