package in.nearfox.nearfox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.GregorianCalendar;
import java.util.List;

import in.nearfox.nearfox.models.DataModels;
import in.nearfox.nearfox.utilities.DBHelper;
import in.nearfox.nearfox.utilities.DBManager;
import retrofit.Callback;


public class SingleEvent extends Activity implements ObservableScrollViewCallbacks {

    String TAG = this.getClass().getName();
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String LAT = "lat";
    public static final String LON = "lon";
    public static final String TIME = "time";
    public static final String IMAGE_URL = "image_url";
    public static final String DATE = "date";
    public static final String CATEGORIES = "categories";
    public static final String ADDRESS = "address";
    public static final String EVENT_DETAILS = "event_details";
    public static final String FEES = "fees";
    public static final String ADDRESS_LINE1 = "address_line1";
    public static final String ADDRESS_LINE2 = "address_line2";
    public static final String LANDMARK = "landmark";
    public static final String START_TIME = "start_time";
    public static final String END_TIME = "end_time";
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    public static final String ALL_DAY = "all_day";
    public static final String CONTACT = "contact";
    public static final String WEBSITE = "website";
    public static final String DAY = "day";
    public static final String FULL_START_DATE = "full_start_date";

    Callback<DataModels.Event> cb;
    DBManager dbManager;

    double lat;
    double lon;
    String addressLine1;
    String addressLine2;
    String addressLine3;
    String contact;
    String startDateDay;
    String startDatemonth;
    String startDateYear;
    String website;
    String title;
    String eventdetails;

    // views
    LinearLayout imageContainer;
    Toolbar toolbar;
    int mParallaxImageHeight;
    ObservableScrollView mScrollView;

    private String event_id;

    ImageView favourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.circular_loader_layout);

        Intent intent = getIntent();
        event_id = intent.getStringExtra(ID);
        dbManager = new DBManager(this);


        populateSingleEvent(event_id);

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void populateSingleEvent(String event_id) {

        Cursor cursor = dbManager.getParticularEvent(event_id);
        cursor.moveToFirst();
        String title = cursor.getString(cursor.getColumnIndex(DBHelper.TITLE));
        String lat = cursor.getString(cursor.getColumnIndex(DBHelper.LAT));
        String lon = cursor.getString(cursor.getColumnIndex(DBHelper.LON));
        String start_time = cursor.getString(cursor.getColumnIndex(DBHelper.START_TIME));
        String end_time = cursor.getString(cursor.getColumnIndex(DBHelper.END_TIME));
        String start_date = cursor.getString(cursor.getColumnIndex(DBHelper.START_DATE));
        String end_date = cursor.getString(cursor.getColumnIndex(DBHelper.END_DATE));
        String address_line1 = cursor.getString(cursor.getColumnIndex(DBHelper.ADDRESS_LINE1));
        String address_line2 = cursor.getString(cursor.getColumnIndex(DBHelper.ADDRESS_LINE2));
        String address_line3 = cursor.getString(cursor.getColumnIndex(DBHelper.LANDMARK));
        final String event_details = cursor.getString(cursor.getColumnIndex(DBHelper.EVENT_DETAILS));
        String fees = cursor.getString(cursor.getColumnIndex(DBHelper.FEES));
        final String image = cursor.getString(cursor.getColumnIndex(DBHelper.IMAGE_UR));
        String id = cursor.getString(cursor.getColumnIndex(DBHelper.EVENT_ID));
        String contact = cursor.getString(cursor.getColumnIndex(DBHelper.CONTACT_PHONE));
        String website = cursor.getString(cursor.getColumnIndex(DBHelper.WEBSITE));
        String category = cursor.getString(cursor.getColumnIndex(DBHelper.CATEGORIES));
        String day = cursor.getString(cursor.getColumnIndex(DBHelper.DAY));
        String fullStartDate = cursor.getString(cursor.getColumnIndex(DBHelper.FULLSTARTDATE));

        if (fullStartDate != null && !fullStartDate.isEmpty()) {
            String[] splitedStartDate = fullStartDate.split("-");
            String start_date_day = splitedStartDate[2];
            String start_date_month = String.valueOf(Integer.parseInt(splitedStartDate[1]) - 1);
            String start_date_year = splitedStartDate[0];

            this.startDateDay = start_date_day;
            this.startDatemonth = start_date_month;
            this.startDateYear = start_date_year;
        }

        this.title = title;
        this.eventdetails = event_details;

        // converting lat/lon to integer
        this.lat = Double.parseDouble(lat);
        this.lon = Double.parseDouble(lon);

        // pushing address in global variable
        this.addressLine1 = address_line1;
        this.addressLine2 = address_line2;
        this.addressLine3 = address_line3;

        // pushing contact to global variable
        this.contact = contact;

        // pushing website to global variable
        this.website = website;

        // Log.d(TAG, "entered populate") ;
        final Context context = this;


        // setting the content view
        setContentView(R.layout.activity_single_event);
        //setSupportActionBar((Toolbar) findViewById(R.id.toolbar)) ;
        favourite=(ImageView)findViewById(R.id.favourite);
        // getting views from layout
        imageContainer = (LinearLayout) findViewById(R.id.image_container);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(
                ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.primary)));
        mScrollView = (ObservableScrollView) findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);
        mParallaxImageHeight = getResources().getDimensionPixelSize(
                R.dimen.parallax_image_height);
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(Color.argb(0, 255, 255, 255));
//        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_36dp);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_right);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setBackgroundColor(Color.parseColor("#00000000"));
        final ImageView imageViewImage = (ImageView) findViewById(R.id.single_event_image_view);

        TextView textViewTitle = (TextView) findViewById(R.id.single_event_title);
        TextView textViewEventDetails = (TextView) findViewById(R.id.single_event_details);
        TextView textViewEventDateTime = (TextView) findViewById(R.id.event_day_time);
        TextView textViewEventDate = (TextView) findViewById(R.id.single_event_text_view_date);
//        Button buttonEventCategory = (Button)findViewById(R.id.single_event_button_category) ;
        TextView textViewEventAddressLin1 = (TextView) findViewById(R.id.single_event_text_view_ad1);
        TextView textViewEventAddressLin2 = (TextView) findViewById(R.id.single_event_text_view_ad2);
        LinearLayout callContainer = (LinearLayout) findViewById(R.id.event_call_container);
        TextView textViewAddressLine1 = (TextView) findViewById(R.id.single_event_address_line1);
//        TextView textViewAddressLine2 = (TextView)findViewById(R.id.single_event_address_line2) ;

        Point point = new Point();
        ((Activity) context).getWindowManager().getDefaultDisplay().getSize(point);
        int deviceHeight = point.y;
        int targetHeight = (int) (0.45 * deviceHeight);
        Log.d(TAG, "targetHeight: " + targetHeight);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageViewImage.getLayoutParams();
        layoutParams.height = targetHeight;
        imageViewImage.setLayoutParams(layoutParams);

        imageViewImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Log.d(TAG, "onglobal");

                imageViewImage.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage(image, imageViewImage);
            }
        });

        textViewTitle.setText(title);
        textViewEventDetails.setText(event_details.trim());
        textViewEventDateTime.setText(day + ", " + start_time);
        textViewEventDate.setText(start_date);
        textViewEventAddressLin1.setText(address_line1);
        textViewEventAddressLin2.setText(address_line2 + ", " + address_line3);
        textViewAddressLine1.setText(address_line1);

        if (contact != null && !contact.isEmpty()) {

        } else {
            callContainer.setVisibility(View.GONE);
        }

        findViewById(R.id.share_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, SingleEvent.this.title + "\n\n" + event_details);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "NearFox Event");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(changeFavouriteIcon()) {
                    DBManager dbManager = new DBManager(SingleEvent.this);
                    dbManager.insertFavorite("Event", SingleEvent.this.event_id);
                } else {
                    DBManager dbManager = new DBManager(SingleEvent.this);
                    dbManager.deleteFavorite("Event", SingleEvent.this.event_id);
                }


            }
        });

        DBManager dbManager = new DBManager(SingleEvent.this);
        if(dbManager.getFavorite("Event", event_id) > 0) {
            changeFavouriteIcon();
        }
    }

    private boolean changeFavouriteIcon(){
        if(favourite.getTag()==null) {
            favourite.setImageResource(R.drawable.abc_btn_rating_star_on_mtrl_alpha);
            favourite.setTag("rt");
            return true;
        }
        else{
            favourite.setImageResource(R.drawable.abc_btn_rating_star_off_mtrl_alpha);
            favourite.setTag(null);
            return false;
        }
    }


    public void single_event_on_click_map(View view) {
//        Log.d(TAG, "location map clicked") ;
        String address = addressLine1 + "\n" + addressLine2 + addressLine3;
        String query = String.valueOf(lat) + "," + String.valueOf(lon) + "(" + address + ")";
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + query);

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        startActivity(mapIntent);
    }

    public void single_event_on_click_call(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + contact));
        startActivity(intent);
    }

    public void single_event_on_click_website(View view) {
        String url = website;
//        Log.d(TAG, "website: " + website) ;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }


    public void single_event_on_click_calendar(View view) {


        Intent calIntent = new Intent(Intent.ACTION_INSERT);
        calIntent.setData(CalendarContract.Events.CONTENT_URI);

        PackageManager manager = getPackageManager();
        List<ResolveInfo> infos = manager.queryIntentActivities(calIntent, 0);
        if (infos.size() > 0) {
            calIntent.putExtra(CalendarContract.Events.TITLE, title);
            calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, addressLine1 + " " + addressLine2);
            calIntent.putExtra(CalendarContract.Events.DESCRIPTION, eventdetails);
            if (startDateYear != null && !startDateYear.equals("") && startDatemonth != null && !startDatemonth.isEmpty() && startDateDay != null && !startDateDay.isEmpty()) {
                GregorianCalendar calDate = new GregorianCalendar(Integer.parseInt(startDateYear), Integer.parseInt(startDatemonth), Integer.parseInt(startDateDay));
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calDate.getTimeInMillis());
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calDate.getTimeInMillis());
                startActivity(calIntent);
            } else {
                Toast.makeText(this, "Date not present", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "No calendar app found on your phone.", Toast.LENGTH_LONG).show();
        }
    }

    public void single_event_on_click_go_back(View view) {
        finish();
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if (imageContainer != null)
            ViewHelper.setTranslationY(imageContainer, (float) (scrollY / (1.5)));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (mScrollView != null)
            onScrollChanged(mScrollView.getCurrentScrollY(), false, false);
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    @Override
    public void onDownMotionEvent() {
    }
}