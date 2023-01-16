package ddwu.mobile.dbtest.roomexam01;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Food.class}, version=1)
public abstract class FoodDB extends RoomDatabase {
    public abstract FoodDao foodDao();

    private static volatile FoodDB INSTANCE;

    static FoodDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FoodDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    FoodDB.class, "food_db.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
