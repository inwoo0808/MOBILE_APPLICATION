package work2.mobile.week03.roomhw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AllContactsActivity extends AppCompatActivity {

    final static String TAG = "AllContactsActivity";

    ListView listView;
    ArrayAdapter<Contact> adapter;
    ContactDB contactDB;
    ContactDAO contactDAO;
    Intent intent;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_contacts);

        listView = findViewById(R.id.lvContacts);
        adapter = new ArrayAdapter<Contact>(this, android.R.layout.simple_list_item_1, new ArrayList<Contact>());
        listView.setAdapter(adapter);

        // 전체 목록 롱클릭 시 삭제
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Contact contact = (Contact) listView.getAdapter().getItem(i);
                Completable deleteResult = contactDAO.deleteContact(contact);
                // 위 2줄이 포인트.
                mDisposable.add(
                        deleteResult.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> Log.d(TAG, "delete success"),
                                        throwable -> Log.d(TAG, "error"))
                );
                return false;
            }
        });

        // 전체 목록 클릭 시 수정 액티비티로 이동 후 수정
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Contact contact = (Contact)listView.getAdapter().getItem(i);
                long itemId = contact.id;
                intent = new Intent(getApplicationContext(), UpdateActivity.class);
                intent.putExtra("itemId", itemId);
                if(intent != null) startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        contactDB = ContactDB.getDatabase(this);
        contactDAO = contactDB.contactDao();
        // 전체 목록 보기
        Flowable<List<Contact>> resultContacts = contactDAO.getAllList();

        mDisposable.add(
                resultContacts.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                contacts -> {
                                    for(Contact aContact : contacts) {
                                        Log.d(TAG, aContact.toString());
                                    }
                                    adapter.clear();
                                    adapter.addAll(contacts);
                                },
                                throwable -> Log.d(TAG, "error", throwable)
                        )
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }
}