package com.felipecsl.asymmetricgridview.library;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.felipecsl.asymmetricgridview.library.model.DemoItem;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;
import com.felipecsl.asymmetricgridview.library.widget.DefaultCursorAdapter;
import com.felipecsl.asymmetricgridview.library.widget.DefaultListAdapter;
import com.felipecsl.asymmetricgridview.library.widget.DemoAdapter;

import java.util.ArrayList;
import java.util.List;

import in.nearfox.nearfox.R;

public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

  private static final String TAG = "MainActivity";
  private AsymmetricGridView listView;
  private DemoAdapter adapter;
  private int currentOffset;
  private static final boolean USE_CURSOR_ADAPTER = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_event_filter);
    listView = (AsymmetricGridView) findViewById(R.id.listView);

    if (USE_CURSOR_ADAPTER) {
      if (savedInstanceState == null) {
        adapter = new DefaultCursorAdapter(this, getMoreItems(50));
      } else {
        adapter = new DefaultCursorAdapter(this);
      }
    } else {
      if (savedInstanceState == null) {
        adapter = new DefaultListAdapter(this, getMoreItems(50));
      } else {
        adapter = new DefaultListAdapter(this);
      }
    }

    listView.setRequestedColumnCount(3);
    listView.setRequestedHorizontalSpacing(Utils.dpToPx(this, 3));
    listView.setAdapter(getNewAdapter());
    listView.setDebugging(true);
    listView.setOnItemClickListener(this);
  }

  private AsymmetricGridViewAdapter<?> getNewAdapter() {
    return new AsymmetricGridViewAdapter<>(this, listView, adapter);
  }

  private List<DemoItem> getMoreItems(int qty) {
    List<DemoItem> items = new ArrayList<>();

    for (int i = 0; i < qty; i++) {
      int colSpan = Math.random() < 0.2f ? 2 : 1;
      // Swap the next 2 lines to have items with variable
      // column/row span.
      // int rowSpan = Math.random() < 0.2f ? 2 : 1;
      int rowSpan = colSpan;
      DemoItem item = new DemoItem(colSpan, rowSpan, currentOffset + i , i,"title please");
      items.add(item);
    }

    currentOffset += qty;

    return items;
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt("currentOffset", currentOffset);
    outState.putInt("itemCount", adapter.getCount());
    for (int i = 0; i < adapter.getCount(); i++) {
      outState.putParcelable("item_" + i, (Parcelable) adapter.getItem(i));
    }
  }

  @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    currentOffset = savedInstanceState.getInt("currentOffset");
    int count = savedInstanceState.getInt("itemCount");
    List<DemoItem> items = new ArrayList<>(count);
    for (int i = 0; i < count; i++) {
      items.add((DemoItem) savedInstanceState.getParcelable("item_" + i));
    }
    adapter.setItems(items);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    return super.onOptionsItemSelected(item);
  }

  private void setNumColumns(int numColumns) {
    listView.setRequestedColumnCount(numColumns);
    listView.determineColumns();
    listView.setAdapter(getNewAdapter());
  }

  private void setColumnWidth(int columnWidth) {
    listView.setRequestedColumnWidth(Utils.dpToPx(this, columnWidth));
    listView.determineColumns();
    listView.setAdapter(getNewAdapter());
  }

  @Override public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
    Toast.makeText(this, "Item " + position + " clicked", Toast.LENGTH_SHORT).show();
  }
}
