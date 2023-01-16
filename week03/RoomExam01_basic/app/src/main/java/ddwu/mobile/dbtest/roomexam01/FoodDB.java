package ddwu.mobile.dbtest.roomexam01;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Food.class}, version=1)
public abstract class FoodDB extends RoomDatabase {
    public abstract FoodDao foodDao();
}
