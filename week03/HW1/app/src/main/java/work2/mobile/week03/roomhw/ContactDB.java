package work2.mobile.week03.roomhw;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Contact.class}, version = 1)
public abstract class ContactDB extends RoomDatabase {
    public abstract ContactDAO contactDao();

    private static volatile ContactDB INSTANCE;

    static ContactDB getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (ContactDB.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ContactDB.class, "contact_db.db").build();
                }
            }
        }
        return INSTANCE;
    }
}
