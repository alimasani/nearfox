package in.nearfox.nearfox.utilities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by dell on 8/7/15.
 */
public class DBHelper extends SQLiteOpenHelper {

    static String TAG = "MyDebug" ;
    public static final String EVENTS_TABLE_NAME = "events_table";
    public static final String NEWS_TABLE_NAME = "news_table";
    public static final String PARTICULAR_EVENTS_TABLE_NAME = "particular_events_table";
    public static final String AYN_QUESTIONS_TABLE_NAME = "ayn_table" ;

    public static final String FAVORITE_TABLE_NAME = "Favorite_table" ;
    public static final String LOCATION_TABLE_NAME = "Location_table" ;


    public static final String FAVORITE_ID = "Favorite_Id";
    public static final String PRE_LOCATION="Pre_Location";

    public static final String KEY_ID = "_id";
    public static final String TITLE = "title";
    public static final String LAT = "la";
    public static final String LON = "lon";
    public static final String START_TIME = "start_time";
    public static final String END_TIME = "end_time";
    public static final String IMAGE_UR = "image_ur";
    public static final String START_DATE = "start_date";
    public static final String CATEGORIES = "categories";
    public static final String END_DATE = "end_date";
    public static final String ADDRESS_LINE1 = "address_line1";
    public static final String ADDRESS_LINE2 = "address_line2";
    public static final String LANDMARK = "landmark";
    public static final String EVENT_DETAILS = "event_details";
    public static final String FEES = "fees";
    public static final String EVENT_ID = "event_id";
    public static final String CONTACT_PHONE = "contact_phone";
    public static final String WEBSITE = "website";
    public static final String DAY = "day";
    public static final String SHORT_ADDRESS = "venue";
    public static final String LOCALITY = "locality";
    public static final String FULLSTARTDATE = "fullStartDate";
    public static final String ALL_DAY = "all_day";
    public static final String TTL = "ttl";

    public static final String DBNAME = "NEARFOX_DB";


    public static final String NEWS_ID = "news_id";
    //    public static final String TITLE = "title" ;
//    public static final String LAT = "la" ;
//    public static final String LON = "lon" ;
//    public static final String WEBSITE = "website" ;
    public static final String CATEGORY = "category";
    public static final String IMAGE_URL = "image_url";
    public static final String NEWS_EXCERPT = "news_excerpt";
    public static final String LOCALIT = "localit";
    public static final String PERIOD = "period";
    public static final String AUTHOR = "author";
    public static final String SOURCE_NAME = "source_name";
    public static final String SOURCE_IMAGE_URL = "source_image_url";




    public static final String AYN_QUESTION_ID = "ayn_question_id";
    public static final String CONTENT = "content";
    public static final String QUESTION_AUTHOR = "question_author";
//    public static final String PERIOD = "period";
    public static final String THUMBNAIL_URL = "thumbnail_url";
    public static final String IMAGEURL = "imageURL";
    public static final String USER_UP_VOTED = "user_up_voted";
    public static final String UP_VOTED = "up_voted";
    public static final String USER_DOWN_VOTED = "user_down_voted";
    public static final String DOWN_VOTED = "down_voted";
    public static final String ANSWERED = "answered";
    public static final String ANSWERS = "answers";
    public static final String SHARED = "shared";
    public static final String SHARES = "shares";


    public static int version = 1;
    public static DBHelper dbHelper;


    private DBHelper(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbName, factory, version);
    }

    private DBHelper(Context context, String dbName) {
        this(context, dbName, null, 2);
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        database.execSQL("create table if not exists " + EVENTS_TABLE_NAME + " ( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + TITLE + " TEXT , " + LAT + " TEXT, "
                + LON + " TEXT , "
                + START_TIME + " TEXT , "
                + END_TIME + " TEXT , "
                + IMAGE_UR + " TEXT , "
                + START_DATE + " TEXT , "
                + CATEGORIES + " TEXT , "
                + END_DATE + " TEXT , "
                + ADDRESS_LINE1 + " TEXT , "
                + ADDRESS_LINE2 + " TEXT , "
                + LANDMARK + " TEXT , "
                + EVENT_DETAILS + " TEXT , "
                + FEES + " TEXT , "
                + EVENT_ID + " TEXT , "
                + CONTACT_PHONE + " TEXT , "
                + WEBSITE + " TEXT , "
                + DAY + " TEXT , "
                + SHORT_ADDRESS + " TEXT , "
                + LOCALITY + " TEXT , "
                + FULLSTARTDATE + " TEXT , "
                + ALL_DAY + " BOOLEAN ) ;");

        Log.d(TAG, "creating news table") ;
        database.execSQL("create table if not exists " + NEWS_TABLE_NAME + " ( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + NEWS_ID + " TEXT , "
                + TITLE + " TEXT , "
                + CATEGORY + " TEXT , "
                + IMAGE_URL + " TEXT , "
                + NEWS_EXCERPT + " TEXT , "
                + SOURCE_NAME + " TEXT , "
                + LAT + " TEXT , "
                + LON + " TEXT , "
                + LOCALIT + " TEXT , "
                + PERIOD + " TEXT , "
                + AUTHOR + " TEXT , "
                + SOURCE_IMAGE_URL + " TEXT , "
                + WEBSITE + " TEXT ) ;");
        Log.d(TAG, "created news table") ;

        /*database.execSQL("create table if not exists " + PARTICULAR_EVENTS_TABLE_NAME + " ( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + TITLE + " TEXT , " + LAT + " TEXT, "
                + LON + " TEXT , "
                + START_TIME + " TEXT , "
                + END_TIME + " TEXT , "
                + IMAGE_UR + " TEXT , "
                + START_DATE + " TEXT , "
                + CATEGORIES + " TEXT , "
                + END_DATE + " TEXT , "
                + ADDRESS_LINE1 + " TEXT , "
                + ADDRESS_LINE2 + " TEXT , "
                + LANDMARK + " TEXT , "
                + EVENT_DETAILS + " TEXT , "
                + FEES + " TEXT , "
                + EVENT_ID + " TEXT , "
                + CONTACT_PHONE + " TEXT , "
                + WEBSITE + " TEXT , "
                + DAY + " TEXT , "
                + SHORT_ADDRESS + " TEXT , "
                + LOCALITY + " TEXT , "
                + FULLSTARTDATE + " TEXT , "
                + ALL_DAY + " BOOLEAN ) ;");*/

        database.execSQL("create table if not exists " + AYN_QUESTIONS_TABLE_NAME + " ( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + AYN_QUESTION_ID + " TEXT , "
                + CONTENT + " TEXT , "
                + QUESTION_AUTHOR + " TEXT , "
                + PERIOD + " TEXT , "
                + THUMBNAIL_URL + " TEXT , "
                + IMAGEURL + " TEXT , "
                + USER_UP_VOTED + " BOOLEAN , "
                + UP_VOTED + " INTEGER , "
                + USER_DOWN_VOTED + " BOOLEAN , "
                + DOWN_VOTED + " INTEGER , "
                + SHARED + " BOOLEAN , "
                + SHARES + " INTEGER , "
                + ANSWERED + " BOOLEAN , "
                + ANSWERS + " INTEGER ) ;");

        database.execSQL("create table if not exists " + FAVORITE_TABLE_NAME + " ( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + TITLE + " TEXT , " + FAVORITE_ID + " TEXT ) ;");

        database.execSQL("create table if not exists " + LOCATION_TABLE_NAME + " ( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + PRE_LOCATION + " TEXT ) ;");
    }


    public static DBHelper getInstance(Context context) {

        if (dbHelper == null) {
            Log.d(TAG, "dbHelper new creating") ;
            dbHelper = new DBHelper(context, DBNAME);
        }

        return dbHelper;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void checkEventsParticularTable(String category){

        SQLiteDatabase database = this.getWritableDatabase();

        database.execSQL("create table if not exists " + PARTICULAR_EVENTS_TABLE_NAME + "_" + category + " ( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + TITLE + " TEXT , " + LAT + " TEXT, "
                + LON + " TEXT , "
                + START_TIME + " TEXT , "
                + END_TIME + " TEXT , "
                + IMAGE_UR + " TEXT , "
                + START_DATE + " TEXT , "
                + CATEGORIES + " TEXT , "
                + END_DATE + " TEXT , "
                + ADDRESS_LINE1 + " TEXT , "
                + ADDRESS_LINE2 + " TEXT , "
                + LANDMARK + " TEXT , "
                + EVENT_DETAILS + " TEXT , "
                + FEES + " TEXT , "
                + EVENT_ID + " TEXT , "
                + CONTACT_PHONE + " TEXT , "
                + WEBSITE + " TEXT , "
                + DAY + " TEXT , "
                + SHORT_ADDRESS + " TEXT , "
                + LOCALITY + " TEXT , "
                + FULLSTARTDATE + " TEXT , "
                + ALL_DAY + " BOOLEAN ) ;");
    }
}