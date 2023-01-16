package work2.mobile.week03.roomhw;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface ContactDAO {
    @Query("SELECT * FROM contact_table")
    Flowable<List<Contact>> getAllList();

    @Insert
    Single<Long> insertContact(Contact contact);

    // 입력받은 name, phone, category로 변경
    @Query("UPDATE contact_table SET name =:name , phone =:phone , category =:category WHERE id=:id")
    Completable dataUpdate(long id, String name, String phone, String category);

    @Delete
    Completable deleteContact(Contact contact);
}
