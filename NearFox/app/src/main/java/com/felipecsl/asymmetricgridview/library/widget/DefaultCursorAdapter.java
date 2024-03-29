package com.felipecsl.asymmetricgridview.library.widget;

import android.content.Context;
import android.database.sqlite.SQLiteCursor;
import android.support.v4.widget.SimpleCursorAdapter;
import com.felipecsl.asymmetricgridview.library.model.DemoItem;
import java.util.List;
import in.nearfox.nearfox.R;

public class DefaultCursorAdapter extends SimpleCursorAdapter implements DemoAdapter {

  private final Context context;

  public DefaultCursorAdapter(Context context, List<DemoItem> items) {
    super(context, R.layout.adapter_item,
          new SampleDbAdapter(context).open().deleteAllData().seedDatabase(items).fetchAllData(),
          new String[]{SampleDbAdapter.KEY_TEXT}, new int[]{R.id.text}, 0);

    this.context = context;
  }

  public DefaultCursorAdapter(Context context) {
    super(context, R.layout.adapter_item, new SampleDbAdapter(context).open().fetchAllData(),
          new String[]{SampleDbAdapter.KEY_TEXT}, new int[]{R.id.text}, 0);

    this.context = context;
  }

  @Override public Object getItem(int position) {
    return new CursorAdapterItem((SQLiteCursor) super.getItem(position));
  }

  @Override public void appendItems(List<DemoItem> newItems) {
    SampleDbAdapter sampleDbAdapter = new SampleDbAdapter(context).open();

    for (DemoItem item : newItems) {
      sampleDbAdapter.createItem(String.valueOf(item.getPosition()), item.getRowSpan(),
                                 item.getColumnSpan());
    }

    swapCursor(sampleDbAdapter.fetchAllData());
  }

  @Override public void setItems(List<DemoItem> moreItems) {
    swapCursor(new SampleDbAdapter(context).open().deleteAllData().seedDatabase(moreItems)
                   .fetchAllData());
  }

  public static class CursorAdapterItem extends DemoItem {

    public CursorAdapterItem(SQLiteCursor cursor) {
      super(cursor.getInt(5), cursor.getInt(4), cursor.getInt(3), cursor.getInt(2), cursor.getString(1));
    }
  }
}
