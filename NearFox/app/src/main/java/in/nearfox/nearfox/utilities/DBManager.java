package in.nearfox.nearfox.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import in.nearfox.nearfox.models.DataModels;

public class DBManager {

    String TAG = "DBManager";
    DBHelper dbHelper;
    SQLiteDatabase database;

    public DBManager(Context context) {
        if (dbHelper == null || database == null) {
            dbHelper = DBHelper.getInstance(context);
            try {
                Log.d(TAG, "calling ");
                database = dbHelper.getWritableDatabase();

            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
        }
    }

    public void insertFavorite(String title, String favoritId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.TITLE, title);
        contentValues.put(DBHelper.FAVORITE_ID, favoritId);
        database.insert(DBHelper.FAVORITE_TABLE_NAME, null, contentValues);
    }

    public int getFavorite(String title, String favoritId) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + DBHelper.FAVORITE_TABLE_NAME + " where " + DBHelper.TITLE + "='" + title + "' and " + DBHelper.FAVORITE_ID + "='" + favoritId + "'", null);
        cursor.moveToFirst();
        return cursor.getCount();
    }

    public void deleteFavorite(String title, String favoritId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.TITLE, title);
        contentValues.put(DBHelper.FAVORITE_ID, favoritId);
        database.delete(DBHelper.FAVORITE_TABLE_NAME, String.format("%s='%s' and %s='%s'", DBHelper.TITLE, title, DBHelper.FAVORITE_ID, favoritId), null);
    }

    public void insertPreviousLocation(String location) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.PRE_LOCATION, location);
        database.insert(DBHelper.LOCATION_TABLE_NAME, null, contentValues);
    }

    public ArrayList<String> getPreviousLocations() {
        Cursor cursor = database.rawQuery("SELECT " + DBHelper.PRE_LOCATION + " FROM " + DBHelper.LOCATION_TABLE_NAME, null);
        cursor.moveToFirst();
        ArrayList<String> arrayList = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return arrayList;
    }

    public int getPreviousLocations(String location) {
        Cursor cursor = database.rawQuery("SELECT " + DBHelper.PRE_LOCATION + " FROM " + DBHelper.LOCATION_TABLE_NAME +
                " where " + DBHelper.PRE_LOCATION + " ='" + location + "'", null);
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            return cursor.getCount();
        }

        return 0;
    }



    public Cursor getEventList() {
        return database.rawQuery("SELECT * FROM " + DBHelper.EVENTS_TABLE_NAME + " ;", null);
    }

    public Cursor getParticularEvent(String id) {
        return database.rawQuery("SELECT * FROM " + DBHelper.EVENTS_TABLE_NAME + " WHERE " + DBHelper.EVENT_ID + " = " + id + " ;", null);
    }

    public Cursor getEventListParticularCategory(String category) {
        dbHelper.checkEventsParticularTable(category);
        return database.rawQuery("SELECT * FROM " + DBHelper.PARTICULAR_EVENTS_TABLE_NAME + "_" + category + " ;", null);
    }

    //    Events section
    public void insertEventList(ArrayList<DataModels.Event> eventObjects) {

        for (DataModels.Event eventObject : eventObjects) {

            ContentValues contentValues = new ContentValues();

            contentValues.put(DBHelper.TITLE, eventObject.getTitle());
            contentValues.put(DBHelper.LAT, eventObject.getLat());
            contentValues.put(DBHelper.LON, eventObject.getLon());
            contentValues.put(DBHelper.START_TIME, eventObject.getStart_time());
            contentValues.put(DBHelper.END_TIME, eventObject.getEnd_time());
            contentValues.put(DBHelper.IMAGE_UR, eventObject.getImage_url());
            contentValues.put(DBHelper.START_DATE, eventObject.getStart_date());
            contentValues.put(DBHelper.CATEGORIES, eventObject.getCategories());
            contentValues.put(DBHelper.END_DATE, eventObject.getEnd_date());
            contentValues.put(DBHelper.ADDRESS_LINE1, eventObject.getAddress_line1());
            contentValues.put(DBHelper.ADDRESS_LINE2, eventObject.getAddress_line2());
            contentValues.put(DBHelper.LANDMARK, eventObject.getLandmark());
            contentValues.put(DBHelper.EVENT_DETAILS, eventObject.getEvent_details());
            contentValues.put(DBHelper.FEES, eventObject.getFees());
            contentValues.put(DBHelper.EVENT_ID, eventObject.getEvent_id());
            contentValues.put(DBHelper.CONTACT_PHONE, eventObject.getContact());
            contentValues.put(DBHelper.WEBSITE, eventObject.getWebsite());
            contentValues.put(DBHelper.DAY, eventObject.getDay());
            contentValues.put(DBHelper.SHORT_ADDRESS, eventObject.getVenue() + ", " + eventObject.getLocality());
            contentValues.put(DBHelper.FULLSTARTDATE, eventObject.getStart_date());
            contentValues.put(DBHelper.ALL_DAY, eventObject.isAll_day());

            database.insert(DBHelper.EVENTS_TABLE_NAME, null, contentValues);
        }
    }

    public void emptyEventsParticularCategory(String category) {
        Log.d(TAG, "deleting particular events list");
        database.delete(DBHelper.PARTICULAR_EVENTS_TABLE_NAME + "_" + category, null, null);
    }

    public void insertEventsParticalarCategory(ArrayList<DataModels.Event> eventObjects, String category) {

        dbHelper.checkEventsParticularTable(category);

        for (DataModels.Event eventObject : eventObjects) {

            ContentValues contentValues = new ContentValues();

            contentValues.put(DBHelper.TITLE, eventObject.getTitle());
            contentValues.put(DBHelper.LAT, eventObject.getLat());
            contentValues.put(DBHelper.LON, eventObject.getLon());
            contentValues.put(DBHelper.START_TIME, eventObject.getStart_time());
            contentValues.put(DBHelper.END_TIME, eventObject.getEnd_time());
            contentValues.put(DBHelper.IMAGE_UR, eventObject.getImage_url());
            contentValues.put(DBHelper.START_DATE, eventObject.getStart_date());
            contentValues.put(DBHelper.CATEGORIES, eventObject.getCategories());
            contentValues.put(DBHelper.END_DATE, eventObject.getEnd_date());
            contentValues.put(DBHelper.ADDRESS_LINE1, eventObject.getAddress_line1());
            contentValues.put(DBHelper.ADDRESS_LINE2, eventObject.getAddress_line2());
            contentValues.put(DBHelper.LANDMARK, eventObject.getLandmark());
            contentValues.put(DBHelper.EVENT_DETAILS, eventObject.getEvent_details());
            contentValues.put(DBHelper.FEES, eventObject.getFees());
            contentValues.put(DBHelper.EVENT_ID, eventObject.getEvent_id());
            contentValues.put(DBHelper.CONTACT_PHONE, eventObject.getContact());
            contentValues.put(DBHelper.WEBSITE, eventObject.getWebsite());
            contentValues.put(DBHelper.DAY, eventObject.getDay());
            contentValues.put(DBHelper.SHORT_ADDRESS, eventObject.getVenue() + ", " + eventObject.getLocality());
            contentValues.put(DBHelper.FULLSTARTDATE, eventObject.getStart_date());
            contentValues.put(DBHelper.ALL_DAY, eventObject.isAll_day());

            database.insert(DBHelper.PARTICULAR_EVENTS_TABLE_NAME + "_" + category, null, contentValues);
        }
    }


    public void emptyEventsTable() {
        database.delete(DBHelper.EVENTS_TABLE_NAME, null, null);
    }


    //    news section
    public Cursor getNewsList() {
        if (database == null)
            Log.d(TAG, "null database");
        else
            Log.d(TAG, "not null database");
        return database.rawQuery("SELECT * FROM " + DBHelper.NEWS_TABLE_NAME + " ;", null);
    }

    public void insertNewsAbstractList(ArrayList<DataModels.singleNewsAbstract> newsArray) {
        ContentValues contentValues = new ContentValues();
        Log.d(TAG, "inside insertnews");

        for (DataModels.singleNewsAbstract news : newsArray) {
            contentValues.put(DBHelper.NEWS_ID, news.getNews_id());
            contentValues.put(DBHelper.TITLE, news.getTitle());
            contentValues.put(DBHelper.CATEGORY, news.getCategory());
            contentValues.put(DBHelper.IMAGE_URL, news.getImage_url());
            contentValues.put(DBHelper.PERIOD, news.getDatetime());
            contentValues.put(DBHelper.NEWS_EXCERPT, news.getNews_excerpt());
            contentValues.put(DBHelper.SOURCE_NAME, news.getSource_name());

            Log.d(TAG, "title: " + news.getTitle());

            database.insert(DBHelper.NEWS_TABLE_NAME, null, contentValues);
        }
    }


    public Cursor getAYNQuestionsList() {
        return database.rawQuery("SELECT * FROM " + DBHelper.AYN_QUESTIONS_TABLE_NAME + " ;", null);
    }

    public void insertAYNQuestions(ArrayList<DataModels.AYNSingleQuestionAbstract> aynQuestionsArray) {
        ContentValues contentValues = new ContentValues();

        for (DataModels.AYNSingleQuestionAbstract aynSingleQuestionAbstract : aynQuestionsArray) {
            contentValues.put(DBHelper.AYN_QUESTION_ID, aynSingleQuestionAbstract.getAyn_question_id());
            contentValues.put(DBHelper.CONTENT, aynSingleQuestionAbstract.getContent());
            contentValues.put(DBHelper.QUESTION_AUTHOR, aynSingleQuestionAbstract.getQuestion_author());
            contentValues.put(DBHelper.PERIOD, aynSingleQuestionAbstract.getPeriod());
            contentValues.put(DBHelper.THUMBNAIL_URL, aynSingleQuestionAbstract.getThumbnail_url());
//            contentValues.put(DBHelper.IMAGEURL, aynSingleQuestionAbstract.getImageURL());
            contentValues.put(DBHelper.USER_UP_VOTED, aynSingleQuestionAbstract.isUser_up_voted());
            contentValues.put(DBHelper.UP_VOTED, aynSingleQuestionAbstract.getUp_voted());
            contentValues.put(DBHelper.USER_DOWN_VOTED, aynSingleQuestionAbstract.isUser_down_voted());
            contentValues.put(DBHelper.DOWN_VOTED, aynSingleQuestionAbstract.getDown_voted());
            contentValues.put(DBHelper.ANSWERED, aynSingleQuestionAbstract.isAnswered());
            contentValues.put(DBHelper.ANSWERS, aynSingleQuestionAbstract.getAnswers());
            contentValues.put(DBHelper.SHARED, aynSingleQuestionAbstract.isShared());
            contentValues.put(DBHelper.SHARES, aynSingleQuestionAbstract.getShares());

            database.insert(DBHelper.AYN_QUESTIONS_TABLE_NAME, null, contentValues);
        }
    }

    public void emptyAYNQustionstable() {
        database.delete(DBHelper.AYN_QUESTIONS_TABLE_NAME, null, null);
    }

    public int updateAYNQuestionUpvoteDownvote(String AYNQuestionId, int upCount, int downCount, boolean whetherUpvoted, boolean whetherDownVoted) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.UP_VOTED, upCount);
        contentValues.put(DBHelper.DOWN_VOTED, downCount);
        contentValues.put(DBHelper.USER_UP_VOTED, whetherUpvoted);
        contentValues.put(DBHelper.USER_DOWN_VOTED, whetherDownVoted);
        int rowsAffected = database.update(DBHelper.AYN_QUESTIONS_TABLE_NAME, contentValues, DBHelper.AYN_QUESTION_ID + " = ?", new String[]{AYNQuestionId});
        Log.d(TAG, "rows affected: " + rowsAffected);
        return rowsAffected;
    }

    public void incrementAnswerCount(String questionId) {

        int count = getAnswerCount(questionId);
        count++;
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.ANSWERS, count);

        database.update(DBHelper.AYN_QUESTIONS_TABLE_NAME, contentValues, DBHelper.AYN_QUESTION_ID + " = ?", new String[]{questionId});
    }

    public int getAnswerCount(String questionId) {
        Cursor cursor = database.rawQuery("SELECT * from " + DBHelper.AYN_QUESTIONS_TABLE_NAME + " WHERE " + DBHelper.AYN_QUESTION_ID + " = " + questionId + " ;", null);
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex(DBHelper.ANSWERS));
    }

    public void emptyNewsTable() {
        database.delete(DBHelper.NEWS_TABLE_NAME, null, null);
    }

}