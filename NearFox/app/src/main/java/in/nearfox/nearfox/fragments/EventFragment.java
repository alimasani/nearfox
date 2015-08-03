package in.nearfox.nearfox.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import in.nearfox.nearfox.EventFilter;
import in.nearfox.nearfox.MainActivity;
import in.nearfox.nearfox.R;
import in.nearfox.nearfox.models.DataModels;
import in.nearfox.nearfox.models.EventsCursorAdapter;
import in.nearfox.nearfox.utilities.DBHelper;
import in.nearfox.nearfox.utilities.DBManager;
import in.nearfox.nearfox.utilities.MyCallback;
import in.nearfox.nearfox.utilities.RestClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EventFragment extends android.support.v4.app.Fragment implements MyCallback.CallAdapter {

    String TAG = this.getClass().getName();
    ListView listView;
    DBManager dbManager;
    ProgressDialog progressDialog;
    MyCallback.LocationListener mLocationListener;

    public EventFragment() {
        // Required empty public constructor
    }

    public static void startProgressDialog(ProgressDialog progressDialog) {

        if (progressDialog != null) {
            progressDialog.show();
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading....");
            progressDialog.setCancelable(false);
        }
    }

    public static void stopProgressDialog(ProgressDialog progressDialog) {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.cancel();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new DBManager(getActivity());
        setHasOptionsMenu(true);

        try {
            mLocationListener = (MyCallback.LocationListener) getActivity();
        } catch (ClassCastException e) {
            Log.d(TAG, e.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_event, container, false);

        listView = (ListView) rootView.findViewById(R.id.list_view);

        Callback<DataModels.Events> cb = new Callback<DataModels.Events>() {
            @Override
            public void success(final DataModels.Events events, Response response) {
                Log.d(this.getClass().getName(), "successfull response");

                String currentTimeStamp = events.getCurrent_time_stamp();
                setCurrentTimeStamp(currentTimeStamp);

                boolean dataChanged = events.isData_changed();
                Log.d(TAG, "datachanged for evnets: " + dataChanged);
                /**
                 * to be removed when integrating
                 */
//                dataChanged = true;
                if (dataChanged) {
                    mThread newThread = new mThread(EventFragment.this, events);
                    newThread.start();
                } else {
                    Log.d(TAG, "not calling");
                }

                /**
                 * stopping progress dialog
                 */
                stopProgressDialog(progressDialog);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(this.getClass().getName(), error.toString());

                /**
                 * stopping progressDialog
                 */
                stopProgressDialog(progressDialog);
            }
        };

        /**
         * start showing events already in the database
         */
        startShowingEventsFromDB();


        /**
         * showing progressbar
         */
        progressDialog = new ProgressDialog(getActivity());
        startProgressDialog(progressDialog);

        /**
         * Meanwhile send the request to backend for loading new events
         * and checking if events order has changed
         * if it has, we will
         * empty the Db and start afresh
         */
        RestClient restClient = new RestClient();
//        String lastTimeStamp = getLastTimeStamp();

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
            latitude = 19.0544718;
            longitude = 72.8475496;
            restClient.getApiService1().getEventsNotSignedIn(emailId, latitude, longitude, getLastTimeStamp(), cb);
        } else {
            Log.d(TAG, "AYN for signed in calling server");

            /**
             * hardcoding the email id
             */
            emailId = "nikunj.sharma9299@gmail.com";
            restClient.getApiService1().getEvents(emailId, getLastTimeStamp(), cb);
        }

        rootView.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity)getActivity()).submitClick(1);
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

    @Override
    public void callAdapter() {
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    EventsCursorAdapter eventsCursorAdapter = new EventsCursorAdapter(getActivity(), dbManager.getEventList());
                    listView.setAdapter(eventsCursorAdapter);
                }
            });
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_event_filter, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.event_filter:
                Intent intent = new Intent(getActivity(), EventFilter.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setCurrentTimeStamp(String currentTimeStamp) {
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getActivity().getString(R.string.CURRENT_TIME_STAMP), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(getActivity().getString(R.string.CURRENT_TIME_STAMP_EVENTS), currentTimeStamp);
            editor.apply();
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    private String getLastTimeStamp() {
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getActivity().getString(R.string.CURRENT_TIME_STAMP), Context.MODE_PRIVATE);
            return sharedPreferences.getString(getActivity().getString(R.string.CURRENT_TIME_STAMP_EVENTS), "0.0");
        }catch (Exception e){
            return "0.0";
        }
    }

    private void startShowingEventsFromDB() {
        EventsCursorAdapter eventsCursorAdapter = new EventsCursorAdapter(getActivity(), dbManager.getEventList());
        listView.setAdapter(eventsCursorAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.cancel();
    }

    public class mThread extends Thread {
        MyCallback.CallAdapter mCallback;

        DataModels.Events mEvents;

        public mThread(MyCallback.CallAdapter callback, DataModels.Events events) {
            this.mCallback = callback;
            this.mEvents = events;
        }

        @Override
        public void run() {
            dbManager.emptyEventsTable();
            dbManager.insertEventList(mEvents.getEvents());
            mCallback.callAdapter();
        }
    }
}