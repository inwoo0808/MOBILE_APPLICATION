package work2.mobile.week03.roomhw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class UpdateActivity extends AppCompatActivity {

    final static String TAG = "UpdateActivity";

    EditText etName;
    EditText etPhone;
    EditText etCategory;

    ContactDB contactDB;
    ContactDAO contactDAO;

    Intent intent;
    long itemId;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        etName = findViewById(R.id.etNameUpdate);
        etPhone = findViewById(R.id.etPhoneUpdate);
        etCategory = findViewById(R.id.etCategoryUpdate);

        contactDB = ContactDB.getDatabase(this);
        contactDAO = contactDB.contactDao();

        intent = getIntent();
        itemId = intent.getLongExtra("itemId", 1);
    }

    public void onClick(View v) {
        final String name = etName.getText().toString();
        final String phone = etPhone.getText().toString();
        final String category = etCategory.getText().toString();

        switch (v.getId()) {
            case R.id.btnUpdateContact:
                Completable updateResult = contactDAO.dataUpdate(itemId, name, phone, category);

                mDisposable.add(
                        updateResult.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> Log.d(TAG, "update success"),
                                        throwable -> Log.d(TAG, "error"))
                );
                break;
            case R.id.btnUpdateContactClose:

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