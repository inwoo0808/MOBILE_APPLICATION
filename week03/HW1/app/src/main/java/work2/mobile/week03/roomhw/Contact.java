package work2.mobile.week03.roomhw;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contact_table")
public class Contact {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "phone")
    public String phone;

    @ColumnInfo(name = "category")
    public String category;

    public Contact(String name, String phone, String category) {
        this.name = name;
        this.phone = phone;
        this.category = category;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
