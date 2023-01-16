package mobile.database.dbtest02;

import android.content.Context;
import android.database.Cursor;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

public class MyCursorAdapter  extends CursorAdapter {

    final static String TAG = "MyCursorAdapter";

    LayoutInflater inflater;
    int layout;

    public MyCursorAdapter(Context context, int layout, Cursor c) {
        super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

//        Tag에 저장한 ViewHolder 추출 후 view 를 생성하였는지 확인
        ViewHolder holder = (ViewHolder) view.getTag();

        if (holder.tvContactName == null) {
            holder.tvContactName = view.findViewById(R.id.tvContactName);
            holder.tvContactPhone = view.findViewById(R.id.tvContactPhone);
            Log.d(TAG, "The holder is filled");
        }

        holder.tvContactName.setText(cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_NAME)));
        holder.tvContactPhone.setText(cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_PHONE)));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View listItemLayout = inflater.inflate(layout, parent, false);

//        ViewHolder 사용을 위해 ViewHolder 를 생성한 View 의 Tag 에 추가
        ViewHolder holder = new ViewHolder();
        listItemLayout.setTag(holder);

        return listItemLayout;
    }


    static class ViewHolder {

        public ViewHolder() {
            Log.d(TAG, "A Holder is created");
            tvContactName = null;
            tvContactPhone = null;
        }

        TextView tvContactName;
        TextView tvContactPhone;
    }

}
