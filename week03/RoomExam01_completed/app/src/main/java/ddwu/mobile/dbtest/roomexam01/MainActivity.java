package ddwu.mobile.dbtest.roomexam01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "MainActivity";

    EditText etFood;
    EditText etNation;
    ListView listView;

    ArrayAdapter<Food> adapter;

    FoodDB foodDB;
    FoodDao foodDao;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etFood = findViewById(R.id.etFood);
        etNation = findViewById(R.id.etNation);
        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<Food>(this, android.R.layout.simple_list_item_1, new ArrayList<Food>());
        listView.setAdapter(adapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "id: " + listView.getAdapter().getItem(i));

                Food deleteFood = (Food) listView.getAdapter().getItem(i);

                Completable deleteResult = foodDao.deleteFood(deleteFood);
                mDisposable.add(deleteResult
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> Log.d(TAG, "Delete success: "),
                                throwable -> Log.d(TAG, "error")) );

            }
        });

        foodDB = FoodDB.getDatabase(this);
        foodDao = foodDB.foodDao();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }


    public void onClick(View v) {
        final String food = etFood.getText().toString();
        final String nation = etNation.getText().toString();
        switch (v.getId()) {
            case R.id.btnInsert:

                Single<Long> insertResult = foodDao.insertFood(new Food(food, nation));

                mDisposable.add (
                        insertResult.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(result -> Log.d(TAG, "Insertion success: " + result),
                                throwable -> Log.d(TAG, "error"))   );


                break;
            case R.id.btnUpdate:
                Food updateFood = new Food("0000", "0000");
                updateFood.id = 4;

                Completable updateResult = foodDao.updateFood(updateFood);

                Disposable disposable = updateResult
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> Log.d(TAG, "Update success: "),
                                throwable -> Log.d(TAG, "error"));

                disposable.dispose();

                mDisposable.add(updateResult
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> Log.d(TAG, "Update success: "),
                            throwable -> Log.d(TAG, "error")) );

                break;
            case R.id.btnDelete:

                Food deleteFood = new Food("순두부찌개", "한국");
                deleteFood.id = 4;
                Completable deleteResult = foodDao.deleteFood(deleteFood);
                mDisposable.add(deleteResult
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> Log.d(TAG, "Delete success: "),
                                throwable -> Log.d(TAG, "error")) );

            break;
            case R.id.btnShow:

                Flowable<List<Food>> resultFoods = foodDao.getAllFoods();

                mDisposable.add( resultFoods
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(foods -> {
                                    for (Food aFood : foods) {
                                        Log.d(TAG, aFood.toString());
                                    }
                                    adapter.clear();
                                    adapter.addAll(foods);
                                },
                                throwable -> Log.d(TAG, "error", throwable)) );
                break;
        }
    }


    Consumer<Long> consumer = new Consumer<Long>() {
        @Override
        public void accept(Long result) {
            Log.d(TAG, "Insertioin success: " + result);
        }
    };


}