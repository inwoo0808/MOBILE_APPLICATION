package ddwu.mobile.dbtest.roomexam01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final static String TAG = "MainActivity";

    EditText etFood;
    EditText etNation;
    ListView listView;

    ArrayAdapter<Food> adapter;

    FoodDB foodDB;
    FoodDao foodDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etFood = findViewById(R.id.etFood);
        etNation = findViewById(R.id.etNation);
        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<Food>(this, android.R.layout.simple_list_item_1, new ArrayList<Food>());

        foodDB = Room.databaseBuilder(getApplicationContext(), FoodDB.class, "food_db.db").build();
        foodDao = foodDB.foodDao();
    }


    public void onClick(View v) {
        final String food = etFood.getText().toString();
        final String nation = etNation.getText().toString();
        switch (v.getId()) {
            case R.id.btnInsert:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        long result = foodDao.insertFood(new Food(food, nation));
                        Log.d(TAG, "Insert Result: " + result);
                    }
                }).start();

                break;
            case R.id.btnUpdate:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Food food = new Food("김치찌개", "한국");
                        food.id = 1;
                        long result = foodDao.updateFood(food);
                        Log.d(TAG, "Update Result: " + result);
                    }
                }).start();

                break;
            case R.id.btnDelete:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Food food = new Food("김치찌개", "한국");
                        food.id = 2;
                        long result = foodDao.deleteFood(food);
                        Log.d(TAG, "Delete Result: " + result);
                    }
                }).start();
                break;
            case R.id.btnShow:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                       List<Food> foods = foodDao.getAllFoods();
                       for (Food food : foods) {
                           Log.d(TAG, food.toString());
                       }
                    }
                }).start();
                break;
        }
    }
}