package in.nearfox.nearfox;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import in.nearfox.nearfox.Drawer.NavigationDrawer;
import in.nearfox.nearfox.Drawer.SubmitNavigationAdapter;
import in.nearfox.nearfox.authentication.LoginActivity;
import in.nearfox.nearfox.authentication.gplus.GPlusLogoutMoitor;
import in.nearfox.nearfox.fragments.AYNFragment;
import in.nearfox.nearfox.utilities.LocationHelper;
import in.nearfox.nearfox.utilities.Preference;
import io.fabric.sdk.android.Fabric;

import static in.nearfox.nearfox.authentication.gplus.GPlusMonitor.mGoogleApiClient;


public class SubmitActivity extends AppCompatActivity implements NavigationDrawer.LogoutListener {

    private ViewPager mviewPager;
    private SubmitNavigationAdapter mNavigationAdapter;
    private Toolbar mtoolbar;
   // private NavigationDrawer mNavigationDrawer;
    private Context context;
    public  boolean signedIn;
    private String TAG = "MyDebug";
    private LocationHelper locationHelper;
    private SlidingTabLayout mSlidingTabLayout;

    //    user profile information
    private static String GOOGLE_OR_FACEBOOK = null;
    CallbackManager callbackManager;

    //    Gplus Logout monitor
    GPlusLogoutMoitor gPlusLogoutMoitor;


    //    MixPanel variables
    MixpanelAPI mixpanelAPI;
    String mixPanelToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submitactivity);
       new Preference(SubmitActivity.this).setSubmitClicked(false);

        /**
         * Main init starts
         */
        initToolbar();



        /*Tracker t = (new GoogleAnalyticsApp(MainActivity.this)).getTracker(GoogleAnalyticsApp.TrackerName.APP_TRACKER);
        t.setScreenName("MainActivity1");
        t.send(new HitBuilders.AppViewBuilder().build());*/

        locationHelper = new LocationHelper(this);


//        starting crashlytics

//        super.onCreate(savedInstanceState) ;
        Fabric.with(this, new Crashlytics());
        context = this;


        /**
         /*  initializing mix pannel
         **/
        mixPanelToken = getResources().getString(R.string.mix_panel_token);
        mixpanelAPI = MixpanelAPI.getInstance(this, mixPanelToken);

        /**
         /*  initializing Facebook and google
         **/
        signedIn = getUser(this);


        if (signedIn) {
            FacebookSdk.sdkInitialize(getApplicationContext());
            callbackManager = CallbackManager.Factory.create();
            gPlusLogoutMoitor = new GPlusLogoutMoitor(this, mGoogleApiClient);
        } else {
        }



        Log.d(TAG, "adding navigation dawer");
        //mNavigationDrawer = new NavigationDrawer(this, signedIn, savedInstanceState);
        initViewPager();
    }

    private void initViewPager() {

        mviewPager = (ViewPager) findViewById(R.id.pager);
//        mpagerTabStrip = (PagerTabStrip)findViewById(R.id.sliding_tabs) ;

        mNavigationAdapter = new SubmitNavigationAdapter(getSupportFragmentManager());

        mviewPager.setAdapter(mNavigationAdapter);
//        mpagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.fbutton_color_belize_hole));

        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.fbutton_default_shadow_color));
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mviewPager);

        mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
//                setting the current screen in sharedprferences for
//                next time app opens on the last used screen
                setLastUsed(context, i);
//                propagateToolbarState(toolbarIsShown());
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        mviewPager.setOffscreenPageLimit(3);
        mviewPager.setCurrentItem(getIntent().getIntExtra("index", 0), false);
    }

    private void initToolbar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));



        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        mtoolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_36dp);
        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){

            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();

//        if (mNavigationDrawer.onOptionsItemSelected(item)) {
//            return true;
//        }

//        noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }


    public void on_click_event(View v) {
        TextView event_id = (TextView) v.findViewById(R.id.event_id);
        TextView title = (TextView) v.findViewById(R.id.title);
        TextView lat = (TextView) v.findViewById(R.id.event_lat);
        TextView lon = (TextView) v.findViewById(R.id.event_lon);
        TextView time = (TextView) v.findViewById(R.id.event_time);
        TextView image_url = (TextView) v.findViewById(R.id.event_image_url);
        TextView date = (TextView) v.findViewById(R.id.event_date);
        TextView categories = (TextView) v.findViewById(R.id.event_category);
        TextView address = (TextView) v.findViewById(R.id.event_address);
        TextView event_details = (TextView) v.findViewById(R.id.event_details);
        TextView fees = (TextView) v.findViewById(R.id.event_fees);
        TextView all_day = (TextView) v.findViewById(R.id.event_all_day);
        TextView contact = (TextView) v.findViewById(R.id.event_contact);
        TextView website = (TextView) v.findViewById(R.id.event_website);
        TextView day = (TextView) v.findViewById(R.id.event_day);
        TextView address_line1 = (TextView) findViewById(R.id.event_address_line1);
        TextView address_line2 = (TextView) findViewById(R.id.event_address_line2);
        TextView landmark = (TextView) findViewById(R.id.event_landmark);
        TextView start_date = (TextView) findViewById(R.id.event_start_date);
        TextView end_date = (TextView) findViewById(R.id.event_end_date);
        TextView start_time = (TextView) findViewById(R.id.event_start_time);
        TextView end_time = (TextView) findViewById(R.id.event_end_time);
        TextView full_start_time = (TextView) findViewById(R.id.event_full_start_date);


        String id = event_id.getText().toString();
        String event_title = title.getText().toString();
        String event_lat = lat.getText().toString();
        String event_lon = lon.getText().toString();
        String event_time = time.getText().toString();
        String event_image_url = image_url.getText().toString();
//        Log.d(TAG, "main: " + event_image_url) ;
        String event_date = date.getText().toString();
        String event_categories = categories.getText().toString();
        String event_address = address.getText().toString();
        String event_event_details = event_details.getText().toString();
        String event_fees = fees.getText().toString();
        String event_all_day = all_day.getText().toString();
        String event_contact = contact.getText().toString();
        String event_website = website.getText().toString();
        String event_day = day.getText().toString();
        String event_address_line1 = address_line1.getText().toString();
        String event_address_line2 = address_line2.getText().toString();
        String event_landmark = landmark.getText().toString();
        String event_start_date = start_date.getText().toString();
        String event_end_date = end_date.getText().toString();
        String event_start_time = start_time.getText().toString();
        String event_end_time = end_time.getText().toString();
        String event_full_start_time = full_start_time.getText().toString();

        Intent intent = new Intent(this, SingleEvent.class);
        intent.putExtra(SingleEvent.ID, id);
        intent.putExtra(SingleEvent.TITLE, event_title);
        intent.putExtra(SingleEvent.LAT, event_lat);
        intent.putExtra(SingleEvent.LON, event_lon);
        intent.putExtra(SingleEvent.TIME, event_time);
        intent.putExtra(SingleEvent.IMAGE_URL, event_image_url);
        intent.putExtra(SingleEvent.DATE, event_date);
        intent.putExtra(SingleEvent.CATEGORIES, event_categories);
        intent.putExtra(SingleEvent.ADDRESS, event_address);
        intent.putExtra(SingleEvent.EVENT_DETAILS, event_event_details);
        intent.putExtra(SingleEvent.FEES, event_fees);
        intent.putExtra(SingleEvent.ALL_DAY, event_all_day);
        intent.putExtra(SingleEvent.CONTACT, event_contact);
        intent.putExtra(SingleEvent.WEBSITE, event_website);
        intent.putExtra(SingleEvent.DAY, event_day);
        intent.putExtra(SingleEvent.ADDRESS_LINE1, event_address_line1);
        intent.putExtra(SingleEvent.ADDRESS_LINE2, event_address_line2);
        intent.putExtra(SingleEvent.LANDMARK, event_landmark);
        intent.putExtra(SingleEvent.START_TIME, event_start_time);
        intent.putExtra(SingleEvent.END_TIME, event_end_time);
        intent.putExtra(SingleEvent.START_DATE, event_start_date);
        intent.putExtra(SingleEvent.END_DATE, event_end_date);
        intent.putExtra(SingleEvent.FULL_START_DATE, event_full_start_time);

        startActivity(intent);
    }

    public static String getDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public void on_click_news(View view) {
        TextView imageSrc = (TextView) view.findViewById(R.id.news_image_src);
        TextView id = (TextView) view.findViewById(R.id.news_id);
        TextView category = (TextView) view.findViewById(R.id.news_category);
        TextView title = (TextView) view.findViewById(R.id.news_title);
        TextView description = (TextView) view.findViewById(R.id.news_description);

        Intent intent = new Intent(this, SingleNews.class);
        intent.putExtra(SingleNews.CATEGORY, category.getText().toString());
        intent.putExtra(SingleNews.ID, id.getText().toString());
        intent.putExtra(SingleNews.TITLE, title.getText().toString());
        intent.putExtra(SingleNews.IMAGE_SRC, imageSrc.getText().toString());
        intent.putExtra(SingleNews.DESCRIPTION, description.getText().toString());

        startActivity(intent);
    }

    public static Boolean checkLoggedIn(Context context) {
        return SubmitActivity.getUser(context);
    }

    private static boolean getUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.FACEBOOK_INFO), MODE_PRIVATE);
        String userName = sharedPreferences.getString(context.getString(R.string.USER_NAME), null);
        String userLink = sharedPreferences.getString(context.getString(R.string.USER_PROFILE), null);
        String userImageURL = sharedPreferences.getString(context.getString(R.string.USER_IMAGE_URL), null);
        String userEmail = sharedPreferences.getString(context.getString(R.string.USER_EMAIL), null);

        if (userName != null && userLink != null && userEmail != null && userImageURL != null) {
            GOOGLE_OR_FACEBOOK = context.getString(R.string.SELECTED_FACEBOOK);
            return true;
        } else {
            sharedPreferences = context.getSharedPreferences(context.getString(R.string.GOOGLE_PLUS_INFO), MODE_PRIVATE);
            userName = sharedPreferences.getString(context.getString(R.string.USER_NAME), null);
            userLink = sharedPreferences.getString(context.getString(R.string.USER_PROFILE), null);
            userImageURL = sharedPreferences.getString(context.getString(R.string.USER_IMAGE_URL), null);
            userEmail = sharedPreferences.getString(context.getString(R.string.USER_EMAIL), null);

            if (userName != null && userLink != null && userEmail != null && userImageURL != null) {
                GOOGLE_OR_FACEBOOK = context.getString(R.string.SELECTED_GOOGLE);
                return true;
            } else {
                GOOGLE_OR_FACEBOOK = context.getString(R.string.SELECTED_GUEST);
                return false;
            }
        }
    }


    public void on_click_comment(View view) {
        on_click_ayn_post((LinearLayout) view.getParent().getParent().getParent().getParent());
    }

    public void on_click_ayn_post(View view) {
        Intent intent = new Intent(this, SingleAynQuestion.class);
        intent.putExtra(SingleAynQuestion.ID, ((TextView) (view.findViewById(R.id.ayn_id))).getText().toString());
        intent.putExtra(SingleAynQuestion.TITLE, ((TextView) (view.findViewById(R.id.ayn_title))).getText().toString());
        intent.putExtra(SingleAynQuestion.USERNAME, ((TextView) (view.findViewById(R.id.ayn_user_name))).getText().toString());
        intent.putExtra(SingleAynQuestion.PERIOD, ((TextView) (view.findViewById(R.id.ayn_period))).getText().toString());
        intent.putExtra(SingleAynQuestion.USERIMAGEURL, ((TextView) (view.findViewById(R.id.ayn_user_image_url))).getText().toString());
//        Log.d(TAG, "main: " + ((TextView)(view.findViewById(R.id.ayn_user_image_url))).getText().toString()) ;
        intent.putExtra(SingleAynQuestion.IMAGEURL, ((TextView) (view.findViewById(R.id.ayn_image_url))).getText().toString());
        intent.putExtra(SingleAynQuestion.UPVOTED, ((TextView) (view.findViewById(R.id.ayn_whether_upvoted))).getText().toString());
        intent.putExtra(SingleAynQuestion.UPVOTECOUNT, ((TextView) (view.findViewById(R.id.ayn_upvote_count))).getText().toString());
        intent.putExtra(SingleAynQuestion.DOWNVOTED, ((TextView) (view.findViewById(R.id.ayn_whether_downvoted))).getText().toString());
        intent.putExtra(SingleAynQuestion.DOWNVOTEDCOUNT, ((TextView) (view.findViewById(R.id.ayn_downvote_count))).getText().toString());
        intent.putExtra(SingleAynQuestion.COMMENTED, ((TextView) (view.findViewById(R.id.ayn_whether_commented))).getText().toString());
        intent.putExtra(SingleAynQuestion.COMMENTCOUNT, ((TextView) (view.findViewById(R.id.ayn_comment_count))).getText().toString());
        startActivity(intent);
    }

    @Override
    public void onLogout() {
        logout();
    }

    @Override
    public void onLogin() {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public void logout() {
//        Log.d(TAG, "logging out from: " + GOOGLE_OR_FACEBOOK ) ;
        if (GOOGLE_OR_FACEBOOK.equals(stringFromResource(R.string.SELECTED_FACEBOOK))) {
            SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.FACEBOOK_INFO), MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            LoginManager.getInstance().logOut();
            LoginActivity.deleteGcmRegistration(this);

//            Log.d(TAG, "logging out from: fb" ) ;
        } else if (GOOGLE_OR_FACEBOOK.equals(stringFromResource(R.string.SELECTED_GOOGLE))) {

            gPlusLogoutMoitor.signOutFromGplus();
            SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.GOOGLE_PLUS_INFO), MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            LoginActivity.deleteGcmRegistration(this);
            editor.clear();
            editor.apply();
//            Log.d(TAG, "logging out from google") ;
        }
        finish();
    }

    private String stringFromResource(int id) {
        return getResources().getString(id);
    }

    public void setLastUsed(Context context, int lastUsed) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.LAST_USED_SCREEN_KEY), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(context.getResources().getString(R.string.LAST_USED_SCREEN), lastUsed);
        editor.apply();
    }

    public int getLastUsed(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.LAST_USED_SCREEN_KEY), Context.MODE_PRIVATE);
        return sharedPreferences.getInt(context.getResources().getString(R.string.LAST_USED_SCREEN), context.getResources().getInteger(R.integer.NEWS_SCREEN));
    }
}