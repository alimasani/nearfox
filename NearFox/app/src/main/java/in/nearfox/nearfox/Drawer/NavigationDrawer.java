package in.nearfox.nearfox.Drawer;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import in.nearfox.nearfox.ChangeCurrentLocationActivity;
import in.nearfox.nearfox.NoActionActivity;
import in.nearfox.nearfox.R;
import in.nearfox.nearfox.models.NavDrawerItem;
import in.nearfox.nearfox.models.NavDrawerListAdapter;

/**
 * Created by dell on 6/7/15.
 */
public class NavigationDrawer {

    private String TAG = "MyDebug";
    View lastSelected = null;

    private Context context;
    private FragmentActivity activity;

    //    for navigation drawer
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    private RelativeLayout drawerContainer;

    boolean signedIn;


    LogoutListener logoutListener;

    public NavigationDrawer(Context context, boolean signedIn, Bundle savedInstanceState) {

        this.context = context;
        this.activity = (FragmentActivity) context;
        this.signedIn = signedIn;

        try {
            logoutListener = (LogoutListener) activity;
            Log.d(TAG, "done logoutlistener");
        } catch (ClassCastException e) {
            Log.d(TAG, e.toString());
        }

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {

        mTitle = mDrawerTitle = (activity).getTitle();
        navMenuTitles = context.getResources().getStringArray(R.array.nav_drawer_items);

        navMenuIcons = context.getResources()
                .obtainTypedArray(R.array.nav_drawer_icons_unseleted);

        mDrawerLayout = (DrawerLayout) activity.findViewById(R.id.root);
        mDrawerList = (ListView) activity.findViewById(R.id.list_slidermenu);
        drawerContainer = (RelativeLayout) activity.findViewById(R.id.drawer_container);

        navDrawerItems = new ArrayList<>();
        addItems();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
//         setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(activity.getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);

        mDrawerToggle = new ActionBarDrawerToggle(activity, mDrawerLayout,
                R.drawable.ic_menu_white_24dp, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                activity.invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                activity.invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);


    }


    public void setSelectedIndex(int position) {
        mDrawerList.performItemClick(mDrawerList.getChildAt(position), position, position);
    }

    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//          display view for selected nav drawer item
            onItemSelected(position, view);
        }
    }

    private void onItemSelected(int position, View view) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        setSelected(position, view);
        switch (position) {
            case 0: {

                /**
                 * Send current location
                 */
                sendCurrentLocation();
                break;
            }
            case 1: {

                /**
                 * About Us
                 */
                startNewActivity(1);
                break;
            }
            case 2: {

                /**
                 * Terms & policies
                 */
                startNewActivity(2);
                break;
            }
            case 3: {

                /**
                 * Login / logout
                 */
                if (signedIn)
                    logoutListener.onLogout();
                else
                    logoutListener.onLogin();
                break;
            }
            default: {
                break;
            }
        }

//       Log.d(TAG, "position: " + position) ;
        mDrawerList.setItemChecked(position, true);
        mDrawerList.setSelection(position);

//        mDrawerLayout.closeDrawer(mDrawerList);
        mDrawerLayout.closeDrawer(drawerContainer);
    }

    public void onPrepareOptionsMenu() {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(drawerContainer);
    }

    public void setTitle(CharSequence title) {
        mTitle = title;
        activity.getActionBar().setTitle(mTitle);
    }

    public void onPostCreate() {
        mDrawerToggle.syncState();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return false;
    }

    public interface LogoutListener {
        public void onLogout();

        public void onLogin();
    }

    public void addItem(String title, int icon) {
        navDrawerItems.add(new NavDrawerItem(title, icon));
    }

    public void addItems() {

        for (int i = 0; i < navMenuTitles.length; i++) {
            addItem(navMenuTitles[i], navMenuIcons.getResourceId(i, 0));
        }

//        if signed in
        if (signedIn) {
            addItem("Logout", navMenuIcons.getResourceId(3, 0));
            Log.d(TAG, "adding logout");
        } else {
            addItem("Login", navMenuIcons.getResourceId(4, 0));
            Log.d(TAG, "adding login");
        }

        navMenuIcons.recycle();
    }

    private void sendCurrentLocation() {
        {
            Intent intent = new Intent(context, ChangeCurrentLocationActivity.class);
            intent.putExtra("isSignedIn", signedIn);
            context.startActivity(intent);

        }
    }

    /**
     *
     * @param whichActivity
     */
    private void startNewActivity(int whichActivity) {

        Intent intent = new Intent(context, NoActionActivity.class);


        switch (whichActivity) {
            case 1:
                intent = intent.putExtra(NoActionActivity.LAYOUT_RESOURCE_ID, R.layout.activity_no_action_about_us);
                break;
            case 2:
                intent = intent.putExtra(NoActionActivity.LAYOUT_RESOURCE_ID, R.layout.activity_no_action_termsandcondition);
                break;
            default:
                break;
        }

        context.startActivity(intent);
    }

    private void setSelected(int position, View view) {

        if (lastSelected != null) {
            Log.d(TAG, "last selected not null");
            ImageView imageView = (ImageView) lastSelected.findViewById(R.id.icon);
            imageView.setImageResource(R.drawable.ic_action_location_2_grey);
            TextView textView = (TextView) lastSelected.findViewById(R.id.title);
            textView.setTextColor(context.getResources().getColor(R.color.counter_text_bg));
            Log.d(TAG, textView.getText().toString());
        }

        if (view != null) {
            Log.d(TAG, "view selected not null");
            ImageView imageView = (ImageView) view.findViewById(R.id.icon);
            imageView.setImageResource(R.drawable.ic_action_location_2);
            TextView textView = (TextView) view.findViewById(R.id.title);
            textView.setTextColor(context.getResources().getColor(R.color.fbutton_color_belize_hole));
            lastSelected = view;
        }
    }

    public boolean onBackPressed(){
        if (mDrawerLayout != null && drawerContainer != null) {
            if ( mDrawerLayout.isDrawerOpen(drawerContainer) ) {
                mDrawerLayout.closeDrawers();
                return true ;
            }
            else
                return false ;

        }else{
            return false ;
        }
    }
}