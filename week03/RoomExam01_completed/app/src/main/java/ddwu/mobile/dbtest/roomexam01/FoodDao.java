package ddwu.mobile.dbtest.roomexam01;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface FoodDao {
    @Query("SELECT * FROM food_table")
    Flowable<List<Food>> getAllFoods();

    @Insert
    Single<Long> insertFood(Food food);

    @Update
    Completable updateFood(Food food);

    @Delete
    Completable deleteFood(Food food);

    @Query("SELECT * FROM food_table WHERE nation = :nation")
    Flowable<List<Food>> getFoodByNation(String nation);
}

