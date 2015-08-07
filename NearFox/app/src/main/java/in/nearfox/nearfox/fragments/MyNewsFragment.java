package in.nearfox.nearfox.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.google.android.gms.maps.model.LatLng;

import in.nearfox.nearfox.MainActivity;
import in.nearfox.nearfox.R;
import in.nearfox.nearfox.models.DataModels;
import in.nearfox.nearfox.models.NewsCursorAdapter;
import in.nearfox.nearfox.utilities.DBManager;
import in.nearfox.nearfox.utilities.MyCallback;
import in.nearfox.nearfox.utilities.RestClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyNewsFragment extends android.support.v4.app.Fragment implements MyCallback.PopulateNewsInAdapter {

    public static final String ARG_INITIAL_POSITION = "ARG_INITIAL_POSITION";
    String TAG = this.getClass().getName();
    ObservableListView listView;
    NewsCursorAdapter mNewsCursorAdapter;
    Cursor mCurrentCursor;
    boolean eod = false;
    MyCallback.LocationListener mLocationListener;
    Callback<DataModels.News> cb1 = new Callback<DataModels.News>() {
        @Override
        public void success(DataModels.News news, Response response) {
            Log.d(TAG, "success 2nd");
            eod = news.isEod();
            String currentTimeStamp = news.getCurrent_time_stamp();

            boolean dataChanged = news.isData_changed();
            Log.d(TAG, "news Changed: " + dataChanged);

            /**
             * saving current timestamp in sharedpreference
             */
            saveCurrentTimeStamp(currentTimeStamp);

            /**
             * start inserting in db
             */
            MThread mThread = new MThread(MyNewsFragment.this, news, false);
            mThread.start();
        }

        @Override
        public void failure(RetrofitError error) {

        }
    };
    boolean loading = false;
    int currentPage = 0;
    int itemsAtATime = 10;
    View rootView;
    boolean firstCommitDone = false;
    DBManager dbManager;
    int lastPageCalled;
    ProgressDialog progressDialog;
    /**
     * Callback after new news fetched from the server
     */
    Callback<DataModels.News> cb = new Callback<DataModels.News>() {

        @Override
        public void success(DataModels.News news, Response response) {
            Log.d(TAG, "callback success 1st");
            eod = news.isEod();
            String currentTimeStamp = news.getCurrent_time_stamp();
            boolean dataChanged = news.isData_changed();

            /**
             * to be commented later
             * when integrating with the backend
             */
//                dataChanged = true ;
            Log.d(TAG, "changed data: " + dataChanged);

            /**
             * saving current timestamp in sharedpreference
             */
            saveCurrentTimeStamp(currentTimeStamp);
            //dataChanged = true;
            if (dataChanged) {
                /**
                 * start inserting in db only if data on server changed
                 */
                MThread mThread = new MThread(MyNewsFragment.this, news, true);
                mThread.start();
            } else {
                firstCommitDone = true;
            }

            /**
             * stopping progressDialog
             */
            EventFragment.stopProgressDialog(progressDialog);
        }

        @Override
        public void failure(RetrofitError error) {
            Log.d(TAG, error.toString());

            /**
             * stopping progressDialog
             */
            EventFragment.stopProgressDialog(progressDialog);
        }
    };

    public MyNewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new DBManager(getActivity());
        progressDialog = new ProgressDialog(getActivity());

        try {
            mLocationListener = (MyCallback.LocationListener) getActivity();
        } catch (ClassCastException e) {
            Log.d(TAG, e.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "oncreateview");
        rootView = inflater.inflate(R.layout.fragment_news,
                container,
                false);

        listView = (ObservableListView) rootView.findViewById(R.id.list_view);
        listView.setOnScrollListener(new EndlessScroll(2));

        /**
         * start showing news already in the database
         */
        startShowingNewsFromDB();

        /**
         * showing progressbar
         */
        EventFragment.startProgressDialog(progressDialog);

        /**
         * Meawhile send the request to backend for loading new news
         * and checking if news order has changed
         * if news order has changed we will
         * empty the Db and start afresh
         */
        Log.d(TAG, "making first request");
        RestClient restClient = new RestClient();

        String emailId = AYNFragment1. onlyCheckUser(getActivity());
        if (emailId == null) {
            Log.d(TAG, "AYN for not signed in calling server");
            emailId = "";
            LatLng currentLatLng = mLocationListener.getCurrentLocation();
            Double latitude = 0.0;
            Double longitude = 0.0;
            if (currentLatLng != null) {
                latitude = currentLatLng.latitude;
                longitude = currentLatLng.longitude;
            }

            /**
             * hard coding latitude and longitude
             */
//            latitude = 19.0544718;
//            longitude = 72.8475496;
            restClient.getApiService1().getRecentNewsNotSignedIn(0, itemsAtATime, emailId, latitude, longitude, getLastTimeStamp(), cb);
        } else {
            Log.d(TAG, "AYN for signed in calling server");

            /**
             * hardcoding the email id
             */
//            emailId = "nikunj.sharma9299@gmail.com";
            restClient.getApiService1().getRecentNews(0, itemsAtATime, emailId, getLastTimeStamp(), cb);
        }
        rootView.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity)getActivity()).submitClick(0);
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void saveCurrentTimeStamp(String currentTimeStamp) {
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getActivity().getString(R.string.CURRENT_TIME_STAMP), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(getActivity().getString(R.string.CURRENT_TIME_STAMP_NEWS), currentTimeStamp);
            editor.apply();
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    private String getLastTimeStamp() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getActivity().getString(R.string.CURRENT_TIME_STAMP), Context.MODE_PRIVATE);
        return sharedPreferences.getString(getActivity().getString(R.string.CURRENT_TIME_STAMP_NEWS), "0.0");
    }

    public static void deleteLastTimeStamp(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.CURRENT_TIME_STAMP), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    private void startShowingNewsFromDB() {
        Log.d(TAG, "started showing from DB");
        DBManager dbManager = new DBManager(getActivity());
        mCurrentCursor = dbManager.getNewsList();
        mNewsCursorAdapter = new NewsCursorAdapter(getActivity(), mCurrentCursor);
        listView.setAdapter(mNewsCursorAdapter);

        if (getActivity() instanceof ObservableScrollViewCallbacks) {
            // Scroll to the specified position after layout
            Bundle args = getArguments();
            if (args != null && args.containsKey(ARG_INITIAL_POSITION)) {
                final int initialPosition = args.getInt(ARG_INITIAL_POSITION, 0);
                ScrollUtils.addOnGlobalLayoutListener(listView, new Runnable() {
                    @Override
                    public void run() {
                        // scrollTo() doesn't work, should use setSelection()
                        listView.setSelection(initialPosition);
                    }
                });
            }
            listView.setTouchInterceptionViewGroup((ViewGroup) getActivity().findViewById(R.id.root));
            listView.setScrollViewCallbacks((ObservableScrollViewCallbacks) getActivity());
            listView.setOnScrollListener(new EndlessScroll(2));
        }
    }

    @Override
    public void callAdapter() {
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "call adapter");
                    mCurrentCursor = dbManager.getNewsList();
                    mNewsCursorAdapter.swapCursor(mCurrentCursor);
                    mNewsCursorAdapter.notifyDataSetChanged();
                    firstCommitDone = true;
//                    currentPage = mNewsCursorAdapter.getCount();
                    currentPage += itemsAtATime;
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getActivity().getResources().getString(R.string.CURRENT_NEWS_OFFSET_KEY), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(getActivity().getString(R.string.CURRENT_NEWS_OFFSET), currentPage);
                    editor.apply();
                }
            });
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.cancel();
    }

    /**
     * MThread for inserting news in DB
     */
    public class MThread extends Thread {

        MyCallback.PopulateNewsInAdapter mCallback;
        DataModels.News mNews;
        boolean dataChanged;

        public MThread(MyCallback.PopulateNewsInAdapter callback, DataModels.News news, boolean dataChanged) {
            this.mCallback = callback;
            this.mNews = news;
            this.dataChanged = dataChanged;
        }

        @Override
        public UncaughtExceptionHandler getUncaughtExceptionHandler() {
            return super.getUncaughtExceptionHandler();
        }

        @Override
        public void run() {

            Log.d(TAG, "run called with: " + dataChanged);
            /**
             * if data(basically data order)
             * is changed on server then start afresh
             * first empty the DB
             */
            if (dataChanged)
                dbManager.emptyNewsTable();

            /**
             * inserting new news in DB
             */
            dbManager.insertNewsAbstractList(mNews.getNews_all());

            /**
             * calling CallAdapter to refresh the adapter
             */
            mCallback.callAdapter();
        }
    }

    class EndlessScroll implements AbsListView.OnScrollListener {

        RestClient restClient;
        private int visibleThreshold = 5;
        private int previousTotal = 0;

        public EndlessScroll(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
            this.restClient = new RestClient();
        }

        @Override
        public void onScroll(AbsListView view, final int firstVisibleItem,
                             final int visibleItemCount, final int totalItemCount) {

            /*new Runnable() {
                public void run(){*/
            if (mCurrentCursor != null && firstCommitDone) {
                if (!eod) {

                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            //                    Log.d(TAG, "total: " + totalItemCount) ;
                            loading = false;
                            previousTotal = totalItemCount;
                            currentPage += itemsAtATime;
                        }
                    }

                    if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getActivity().getResources().getString(R.string.CURRENT_NEWS_OFFSET_KEY), Context.MODE_PRIVATE);
                        currentPage = sharedPreferences.getInt(getActivity().getString(R.string.CURRENT_NEWS_OFFSET), 0);
                        if (currentPage != lastPageCalled) {
                            Log.d(TAG, "started loadin more items" + currentPage);

                            String emailId = AYNFragment1. onlyCheckUser(getActivity());
                            if (emailId == null) {
                                Log.d(TAG, "AYN for not signed in calling server");
                                emailId = "";
                                LatLng currentLatLng = mLocationListener.getCurrentLocation();
                                Double latitude = 0.0;
                                Double longitude = 0.0;
                                if (currentLatLng != null) {
                                    latitude = currentLatLng.latitude;
                                    longitude = currentLatLng.longitude;
                                }

                                /**
                                 * hard coding latitude and longitude
                                 */
//                                latitude = 19.0544718;
//                                longitude = 72.8475496;
                                restClient.getApiService1().getRecentNewsNotSignedIn(currentPage, itemsAtATime, emailId, latitude, longitude, getLastTimeStamp(), cb1);
                            } else {
                                Log.d(TAG, "AYN for signed in calling server");

                                /**
                                 * hardcoding the email id
                                 */
//                                emailId = "nikunj.sharma9299@gmail.com";
                                restClient.getApiService1().getRecentNews(currentPage, itemsAtATime, emailId, getLastTimeStamp(), cb1);
                            }
                            lastPageCalled = currentPage;
                        }
                    }
                } else {
                    Log.d(TAG, "tob be filled");
//                    rootView.findViewById(R.id.loader_for_more_items).setVisibility(View.GONE);
                }
            }
                /*}
            };*/
            Log.d(TAG, "on scroll");
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
    }
}