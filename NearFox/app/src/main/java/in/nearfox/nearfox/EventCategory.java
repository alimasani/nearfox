package in.nearfox.nearfox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import in.nearfox.nearfox.fragments.EventCategoryBody;
import in.nearfox.nearfox.fragments.EventFragment;
import in.nearfox.nearfox.utilities.LocationHelper;
import in.nearfox.nearfox.utilities.MyCallback;

public class EventCategory extends ActionBarActivity implements MyCallback.LocationListener{

    public static final String CATEGORY = "category";
    String category;
    LocationHelper locationHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationHelper = new LocationHelper(this);
        setContentView(R.layout.activity_event_category);

        Intent intent = getIntent();
        String category = intent.getStringExtra(CATEGORY);
        this.category = category;

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        android.support.v4.app.Fragment fragment;

        if (this.category.toLowerCase().equals("all")) {
            fragment = new EventFragment();
        } else {
            fragment = new EventCategoryBody();
        }

        Bundle bundle = new Bundle();
        bundle.putString(CATEGORY, category);
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();

        setTitle(category);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void on_click_event(View v) {

        TextView event_id = (TextView) v.findViewById(R.id.event_id);

        String id = event_id.getText().toString();
        Intent intent = new Intent(this, SingleEvent.class);
        intent.putExtra(SingleEvent.ID, id);

        startActivity(intent);
    }

    @Override
    public LatLng getCurrentLocation() {
        if (locationHelper == null)
            return null;
        return locationHelper.getCurrentLocation();
    }

    @Override
    public LatLng getHomeLocation() {
        if (locationHelper == null)
            return null;
        return locationHelper.getHomeLocation();
    }

    public String getCategory() {
        return category;
    }
}