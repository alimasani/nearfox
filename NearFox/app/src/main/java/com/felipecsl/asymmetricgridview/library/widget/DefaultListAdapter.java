package com.felipecsl.asymmetricgridview.library.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.felipecsl.asymmetricgridview.library.model.DemoItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import in.nearfox.nearfox.R;

/**
 * Sample adapter implementation extending from AsymmetricGridViewAdapter<DemoItem>
 * This is the easiest way to get started.
 */
public class DefaultListAdapter extends ArrayAdapter<DemoItem> implements DemoAdapter {

    String TAG = "MyDebug";
    private final LayoutInflater layoutInflater;
    private Context context;

    public DefaultListAdapter(Context context, List<DemoItem> items) {
        super(context, 0, items);
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public DefaultListAdapter(Context context) {
        super(context, 0);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout v;
        TextView t;
        ImageView im;

        DemoItem item = getItem(position);

        if (convertView == null) {
            v = (RelativeLayout) layoutInflater.inflate(R.layout.adapter_item, parent, false);
        } else {
            v = (RelativeLayout) convertView;
        }

        t = (TextView) v.findViewById(R.id.text_view_event_filter_title);
        im = (ImageView) v.findViewById(R.id.image_view_event_filter_categories);

        String title = item.getCategoryTitle();
        int imageUri = item.getImageUri();
        t.setText(title);

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage("drawable://" + imageUri,
                im,
                new DisplayImageOptions
                        .Builder()
                        .showImageForEmptyUri(R.drawable.default_event_image)
                        .showImageOnFail(R.drawable.default_event_image)
                        .showStubImage(R.drawable.default_event_image)
                        .cacheInMemory(true)
                        .cacheOnDisc(true)
                        .build());

        return v;
    }

    public void appendItems(List<DemoItem> newItems) {
        addAll(newItems);
        notifyDataSetChanged();
    }

    public void setItems(List<DemoItem> moreItems) {
        clear();
        appendItems(moreItems);
    }
}