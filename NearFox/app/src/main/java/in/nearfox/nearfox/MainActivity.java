package in.nearfox.nearfox;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.Scrollable;
import com.google.android.gms.maps.model.LatLng;
import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import in.nearfox.nearfox.Drawer.NavigationDrawer;
import in.nearfox.nearfox.authentication.LoginActivity;
import in.nearfox.nearfox.authentication.gplus.GPlusLogoutMoitor;
import in.nearfox.nearfox.fragments.AYNFragment1;
import in.nearfox.nearfox.fragments.NewsFragment;
import in.nearfox.nearfox.models.NavigationAdapter;
import in.nearfox.nearfox.utilities.LocationHelper;
import in.nearfox.nearfox.utilities.MyCallback;
import io.fabric.sdk.android.Fabric;

import static in.nearfox.nearfox.authentication.gplus.GPlusMonitor.mGoogleApiClient;

public class MainActivity extends AppCompatActivity implements NavigationDrawer.LogoutListener, ObservableScrollViewCallbacks, MyCallback.LocationListener {

    //    user profile information
    private static String GOOGLE_OR_FACEBOOK = null;
    ViewPager mViewPager;
    View mHeaderView;
    NavigationAdapter mNavigationAdapter;
    Toolbar mToolbarView;
    NavigationDrawer mNavigationDrawer;
    Context context;
    boolean signedIn;
    String TAG = this.getClass().getName();
    LocationHelper locationHelper;
    SlidingTabLayout mSlidingTabLayout;

    // for storing state of scrolling
    int offset = 0;
    int currentScrolled = 0;
    int totalScrolled = 0;
    Direction direction;

    ;
    CallbackManager callbackManager;
    //    Gplus Logout monitor
    GPlusLogoutMoitor gPlusLogoutMoitor;
    //    MixPanel variables
    MixpanelAPI mixpanelAPI;
    String mixPanelToken;
    private int mBaseTranslationY;

    public static String getDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public static String checkLoggedIn(Context context) {
        return MainActivity.getUser(context);
    }

    private static String getUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.FACEBOOK_INFO), MODE_PRIVATE);
        String userName = sharedPreferences.getString(context.getString(R.string.USER_NAME), null);
        String userLink = sharedPreferences.getString(context.getString(R.string.USER_PROFILE), null);
        String userImageURL = sharedPreferences.getString(context.getString(R.string.USER_IMAGE_URL), null);
        String userEmail = sharedPreferences.getString(context.getString(R.string.USER_EMAIL), null);

        if (userName != null && userLink != null && userEmail != null && userImageURL != null) {
            GOOGLE_OR_FACEBOOK = context.getString(R.string.SELECTED_FACEBOOK);
            return userEmail;
        } else {
            sharedPreferences = context.getSharedPreferences(context.getString(R.string.GOOGLE_PLUS_INFO), MODE_PRIVATE);
            userName = sharedPreferences.getString(context.getString(R.string.USER_NAME), null);
            userLink = sharedPreferences.getString(context.getString(R.string.USER_PROFILE), null);
            userImageURL = sharedPreferences.getString(context.getString(R.string.USER_IMAGE_URL), null);
            userEmail = sharedPreferences.getString(context.getString(R.string.USER_EMAIL), null);

            if (userName != null && userLink != null && userEmail != null && userImageURL != null) {
                GOOGLE_OR_FACEBOOK = context.getString(R.string.SELECTED_GOOGLE);
                return userEmail;
            } else {
                GOOGLE_OR_FACEBOOK = context.getString(R.string.SELECTED_GUEST);
                return null;
            }
        }
    }

    public static String getUserName(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.FACEBOOK_INFO), MODE_PRIVATE);
        String userName = sharedPreferences.getString(context.getString(R.string.USER_NAME), null);

        if (userName != null && userName != "")
            return  userName;

        sharedPreferences = context.getSharedPreferences(context.getString(R.string.GOOGLE_PLUS_INFO), MODE_PRIVATE);
        userName = sharedPreferences.getString(context.getString(R.string.USER_NAME), null);

        if (userName != null && userName != "")
            return userName;

        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /**
         * Main init starts
         */
        initToolbar();


        /**
         * Initiating location helper for
         * getting users location
         */
        locationHelper = new LocationHelper(this);


        /**
         * starting crashlytics
         */
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
        signedIn = getUser(this) != null;

        if (signedIn) {
            FacebookSdk.sdkInitialize(getApplicationContext());
            callbackManager = CallbackManager.Factory.create();
            gPlusLogoutMoitor = new GPlusLogoutMoitor(this, mGoogleApiClient);
        } else {
        }


        /**
         * Initiating navigation drawer
         */
        mNavigationDrawer = new NavigationDrawer(this, signedIn, savedInstanceState);


        /**
         * inititiating view pager
         */
        initViewPager();
    }

    private void initViewPager() {

        mViewPager = (ViewPager) findViewById(R.id.pager);
//        mpagerTabStrip = (PagerTabStrip)findViewById(R.id.sliding_tabs) ;

        mNavigationAdapter = new NavigationAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mNavigationAdapter);
//        mpagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.fbutton_color_belize_hole));

        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.fbutton_default_shadow_color));
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mViewPager);

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

        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(getLastUsed(this), false);
    }

    private void initToolbar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        getSupportActionBar().setHomeButtonEnabled(true);

        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        mToolbarView.setNavigationIcon(R.drawable.ic_menu_white_24dp);

        mHeaderView = findViewById(R.id.header);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mNavigationDrawer.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void on_click_event(View v) {

        TextView event_id = (TextView) v.findViewById(R.id.event_id);
        String id = event_id.getText().toString();
        Intent intent = new Intent(this, SingleEvent.class);
        intent.putExtra(SingleEvent.ID, id);
        Log.d(TAG, "id from main: " + id);
        startActivity(intent);
    }

    public void on_click_news(View view) {
        TextView imageSrc = (TextView) view.findViewById(R.id.news_image_src);
        TextView id = (TextView) view.findViewById(R.id.news_id);
        TextView category = (TextView) view.findViewById(R.id.news_category);
        TextView title = (TextView) view.findViewById(R.id.news_title);
        TextView description = (TextView) view.findViewById(R.id.news_description);
        TextView period = (TextView) view.findViewById(R.id.news_period);
        TextView source = (TextView) view.findViewById(R.id.news_source);

        Intent intent = new Intent(this, SingleNews.class);
        intent.putExtra(SingleNews.CATEGORY, category.getText().toString());
        intent.putExtra(SingleNews.ID, id.getText().toString());
        intent.putExtra(SingleNews.TITLE, title.getText().toString());
        intent.putExtra(SingleNews.IMAGE_SRC, imageSrc.getText().toString());
        intent.putExtra(SingleNews.DESCRIPTION, description.getText().toString());
        intent.putExtra(SingleNews.SOURCE, source.getText().toString());
        intent.putExtra(SingleNews.PERIOD, period.getText().toString());

        startActivity(intent);
    }

    public void up_voted_by_user(View view) {
        AYNFragment1.onClickUpVote(view, this);
    }

    public void down_voted_by_user(View view) {
        AYNFragment1.onClickDownVote(view, this);
    }


    public void on_click_share(View view){
        // share clicked from list
    }

    public void on_click_comment(View view) {
        on_click_ayn_post((LinearLayout) view.getParent().getParent());
    }

    public void submitClick(int index) {
        if(checkLoggedIn(this) != null) {

            Intent intent = new Intent(this, SubmitActivity.class);
            intent.putExtra("index", index);
            startActivity(intent);
            finish();
        }
        else
        {
            mNavigationDrawer.setSelectedIndex(3);
        }
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
        intent.putExtra(SingleAynQuestion.SHARED, ((TextView)view.findViewById(R.id.ayn_whether_shared)).getText().toString());
        intent.putExtra(SingleAynQuestion.SHARECOUNT, ((TextView)view.findViewById(R.id.ayn_share_count)).getText().toString());
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
            NewsFragment.deleteLastTimeStamp(this);

        } else if (GOOGLE_OR_FACEBOOK.equals(stringFromResource(R.string.SELECTED_GOOGLE))) {

            gPlusLogoutMoitor.signOutFromGplus();
            SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.GOOGLE_PLUS_INFO), MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            LoginActivity.deleteGcmRegistration(this);
            NewsFragment.deleteLastTimeStamp(this);
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
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

    @Override
    public void onBackPressed() {
        if (mNavigationDrawer != null) {
            if (!mNavigationDrawer.onBackPressed())
                finish();
        } else {
            finish();
        }
    }

    public void onclick_terms_and_policies(View view) {
        Intent intent = new Intent(context, NoActionActivity.class);
        intent = intent.putExtra(NoActionActivity.LAYOUT_RESOURCE_ID, R.layout.activity_no_action_termsandcondition);
        startActivity(intent);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        Log.d(TAG, "onscrollchanged");
        if (dragging) {
            Log.d(TAG, "dragging");
            int toolbarHeight = mToolbarView.getHeight();
            float currentHeaderTranslationY = ViewHelper.getTranslationY(mHeaderView);
            if (firstScroll) {
                offset = scrollY;
                currentScrolled = 0;
                if (scrollY > totalScrolled)
                    direction = Direction.INCREASING;
                else
                    direction = Direction.DECREASING;

                totalScrolled = scrollY;
                if (-toolbarHeight < currentHeaderTranslationY) {
                    mBaseTranslationY = scrollY;
                } else {
                }
            } else {
                // if moving down
                if (direction == Direction.DECREASING) {
                    // still moving down
                    if (scrollY <= totalScrolled) {
                        totalScrolled = scrollY;
                        currentScrolled = offset - scrollY;
                    } else {
                        offset = totalScrolled;
                        direction = Direction.INCREASING;
                        currentScrolled = scrollY - totalScrolled;
                        totalScrolled = scrollY;
                    }
                } else {
                    // still increasing or not
                    if (scrollY >= totalScrolled) {
                        totalScrolled = scrollY;
                        currentScrolled = scrollY - offset;
                    } else {
                        offset = totalScrolled;
                        direction = Direction.DECREASING;
                        currentScrolled = totalScrolled - scrollY;
                        totalScrolled = scrollY;
                    }
                }
//                Log.d(TAG, String.valueOf(currentScrolled) + " " + totalScrolled + " " + offset + " " + direction) ;
            }
            /*float headerTranslationY = ScrollUtils.getFloat(-(scrollY - mBaseTranslationY), -toolbarHeight, 0);
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewHelper.setTranslationY(mHeaderView, headerTranslationY);*/
            /*mPager.*/
//            Log.d(TAG, "out") ;
        }
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        mBaseTranslationY = 0;
        Log.d(TAG, "onuporcancel");
        Fragment fragment = getCurrentFragment();
        if (fragment == null) {
            return;
        }
        View view = fragment.getView();
        if (view == null) {
            return;
        }

        // ObservableXxxViews have same API
        // but currently they don't have any common interfaces.
        adjustToolbar(scrollState, view);
    }

    private Fragment getCurrentFragment() {
        return mNavigationAdapter.getItemAt(mViewPager.getCurrentItem());
    }

    private void adjustToolbar(ScrollState scrollState, View view) {
        Log.d(TAG, "adjust");
        int toolbarHeight = mToolbarView.getHeight();
        final Scrollable scrollView = (Scrollable) view.findViewById(R.id.scroll);
        if (scrollView == null) {
            return;
        }
        int scrollY = scrollView.getCurrentScrollY();
        if (scrollState == ScrollState.DOWN) {
            if ((direction != null && direction == Direction.DECREASING && currentScrolled > toolbarHeight / 3) || scrollY <= toolbarHeight)
                showToolbar();
            Log.d(TAG, "down");
        } else if (scrollState == ScrollState.UP) {
            Log.d(TAG, "up");
            if (toolbarHeight <= scrollY) {
                hideToolbar();
                Log.d(TAG, "hiding");
            } else {
                showToolbar();
                Log.d(TAG, "showing");
            }
        } else {
            // Even if onScrollChanged occurs without scrollY changing, toolbar should be adjusted
            if (toolbarIsShown() || toolbarIsHidden()) {
                // Toolbar is completely moved, so just keep its state
                // and propagate it to other pages
                propagateToolbarState(toolbarIsShown());
            } else {
                // Toolbar is moving but doesn't know which to move:
                // you can change this to hideToolbar()
                showToolbar();
            }
        }
    }

    private void propagateToolbarState(boolean isShown) {
        Log.d(TAG, "propagate");
        int toolbarHeight = mToolbarView.getHeight();

        // Set scrollY for the fragments that are not created yet
        mNavigationAdapter.setScrollY(isShown ? 0 : toolbarHeight);

        // Set scrollY for the active fragments
        for (int i = 0; i < mNavigationAdapter.getCount(); i++) {
            // Skip current item
            if (i == mViewPager.getCurrentItem()) {
                continue;
            }

            // Skip destroyed or not created item
            Fragment f = mNavigationAdapter.getItemAt(i);
            if (f == null) {
                continue;
            }

            View view = f.getView();
            if (view == null) {
                continue;
            }
            propagateToolbarState(isShown, view, toolbarHeight);
        }
    }

    private void propagateToolbarState(boolean isShown, View view, int toolbarHeight) {
        Log.d(TAG, "propagate 3 variables");
        Scrollable scrollView = (Scrollable) view.findViewById(R.id.scroll);
        if (scrollView == null) {
            return;
        }
        if (isShown) {
            // Scroll up
            if (0 < scrollView.getCurrentScrollY()) {
                scrollView.scrollVerticallyTo(0);
            }
        } else {
            // Scroll down (to hide padding)
            /*if (scrollView.getCurrentScrollY() < toolbarHeight) {
                Log.d(TAG, "scrolling down") ;
                scrollView.scrollVerticallyTo(toolbarHeight);
            }*/
        }
    }

    private boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(mHeaderView) == 0;
    }

    private boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(mHeaderView) == -mToolbarView.getHeight();
    }

    private void showToolbar() {
        Log.d(TAG, "showtoolbar");
        float headerTranslationY = ViewHelper.getTranslationY(mHeaderView);
        if (headerTranslationY != 0) {
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewPropertyAnimator.animate(mHeaderView).translationY(0).setDuration(200).start();
            ViewPropertyAnimator.animate(mViewPager).cancel();
            ViewPropertyAnimator.animate(mViewPager).translationY(0).setDuration(200).start();
        }
        propagateToolbarState(true);
    }

    private void hideToolbar() {
        Log.d(TAG, "hidetoolbar");
        float headerTranslationY = ViewHelper.getTranslationY(mHeaderView);
        int toolbarHeight = mToolbarView.getHeight();
        if (headerTranslationY != -toolbarHeight) {
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewPropertyAnimator.animate(mHeaderView).translationY(-toolbarHeight).setDuration(200).start();
            /*float scale = getResources().getDisplayMetrics().density;
            int dpAsPixels = (int) (48*scale + 0.5f);*/
//            mPager.setPadding();
            ViewPropertyAnimator.animate(mViewPager).cancel();
            ViewPropertyAnimator.animate(mViewPager).translationY(-toolbarHeight).setDuration(200).start();
        }
        propagateToolbarState(false);
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

    enum Direction {INCREASING, DECREASING}
}