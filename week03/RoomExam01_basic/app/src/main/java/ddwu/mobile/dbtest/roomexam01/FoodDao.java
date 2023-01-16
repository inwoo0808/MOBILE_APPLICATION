package ddwu.mobile.dbtest.roomexam01;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FoodDao {

    @Query("SELECT * FROM food_table")
    List<Food> getAllFoods();

    @Insert
    long insertFood(Food food);

    @Update
    int updateFood(Food food);

    @Delete
    int deleteFood(Food food);

    @Query("SELECT * FROM food_table WHERE nation = :nation")
    List<Food> getFoodByNation(String nation);

}
