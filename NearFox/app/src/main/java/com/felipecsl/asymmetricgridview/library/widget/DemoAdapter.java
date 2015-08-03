package com.felipecsl.asymmetricgridview.library.widget;

import android.widget.ListAdapter;

import com.felipecsl.asymmetricgridview.library.model.DemoItem;

import java.util.List;

public interface DemoAdapter extends ListAdapter {

  void appendItems(List<DemoItem> newItems);

  void setItems(List<DemoItem> moreItems);
}
