package in.nearfox.nearfox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.felipecsl.asymmetricgridview.library.Utils;
import com.felipecsl.asymmetricgridview.library.model.DemoItem;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;
import com.felipecsl.asymmetricgridview.library.widget.DefaultCursorAdapter;
import com.felipecsl.asymmetricgridview.library.widget.DefaultListAdapter;
import com.felipecsl.asymmetricgridview.library.widget.DemoAdapter;

import java.util.ArrayList;
import java.util.List;

import in.nearfox.nearfox.models.EventCategoriesData;

public class EventFilter extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "Mydebug";
    AsymmetricGridView asymmetricGridView;
    DemoAdapter adapter;
    private static final boolean USE_CURSOR_ADAPTER = false;
    private static int numberOfCategories;
    private ArrayList<Integer> images;
    private ArrayList<String> titles;
    private int currentOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_filter);

        // setting up button in actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // getting categories data
        EventCategoriesData categoriesData = new EventCategoriesData();
        EventCategoriesData.Categories categories = categoriesData.getEventCategories();
        images = categories.getImages();
        titles = categories.getTitles();
        numberOfCategories = images.size();

        asymmetricGridView = (AsymmetricGridView) findViewById(R.id.listView);

        if (USE_CURSOR_ADAPTER) {
            if (savedInstanceState == null) {
                adapter = new DefaultCursorAdapter(this, getMoreItems(numberOfCategories));
            } else {
                adapter = new DefaultCursorAdapter(this);
            }
        } else {
            if (savedInstanceState == null) {
                adapter = new DefaultListAdapter(this, getMoreItems(numberOfCategories));
            } else {
                adapter = new DefaultListAdapter(this);
            }
        }

        asymmetricGridView.setRequestedColumnWidth(Utils.dpToPx(this, 150));
        asymmetricGridView.setRequestedColumnCount(3);
        asymmetricGridView.setRequestedHorizontalSpacing(Utils.dpToPx(this, 0));
        asymmetricGridView.setDividerHeight(5);
        asymmetricGridView.setVerticalScrollBarEnabled(false);
        asymmetricGridView.setAllowReordering(true);
        asymmetricGridView.setAdapter(getNewAdapter());
        asymmetricGridView.setDebugging(true);
        asymmetricGridView.setOnItemClickListener(this);
    }

    private AsymmetricGridViewAdapter<?> getNewAdapter() {
        return new AsymmetricGridViewAdapter<>(this, asymmetricGridView, adapter);
    }

    private List<DemoItem> getMoreItems(int qty) {
        List<DemoItem> items = new ArrayList<>();
        int temp = 2;
        for (int i = 0; i < qty; i++) {

            int colSpan = 1, rowSpan = 1;

            /*if (temp == 2) {
            colSpan = 1;
            rowSpan = 1;
            temp = 0;
            } else {
            temp++;
            rowSpan = 1;
            colSpan = 1;
            }*/

            DemoItem item = new DemoItem(colSpan, rowSpan, currentOffset + i, images.get(i), titles.get(i));

            items.add(item);
        }

        currentOffset += qty;
        return items;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
        Intent intent = new Intent(this, EventCategory.class);
        TextView titleView = (TextView) view.findViewById(R.id.text_view_event_filter_title);
        String title = titleView.getText().toString();
        intent.putExtra(EventCategory.CATEGORY, title);
        startActivity(intent);
    }
}