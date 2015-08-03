package in.nearfox.nearfox.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import in.nearfox.nearfox.EventCategory;
import in.nearfox.nearfox.MainActivity;
import in.nearfox.nearfox.R;
import in.nearfox.nearfox.models.DataModels;
import in.nearfox.nearfox.models.EventsCursorAdapter;
import in.nearfox.nearfox.utilities.DBManager;
import in.nearfox.nearfox.utilities.LocationHelper;
import in.nearfox.nearfox.utilities.MyCallback;
import in.nearfox.nearfox.utilities.RestClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EventCategoryBody extends Fragment implements MyCallback.CallAdapter {

    String TAG = this.getClass().getName();
    ListView listView;
    DBManager dbManager;
    LocationHelper locationHelper;
    String category;

    public EventCategoryBody() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new DBManager(getActivity());
        locationHelper = new LocationHelper(getActivity());
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_event, container, false);

        EventCategory activity = (EventCategory) getActivity();
        category = activity.getCategory();

        listView = (ListView) rootView.findViewById(R.id.list_view);

        Callback<DataModels.Events> cb = new Callback<DataModels.Events>() {
            @Override
            public void success(final DataModels.Events events, Response response) {
                Log.d(this.getClass().getName(), "successfull response");

                String currentTimeStamp = events.getCurrent_time_stamp();
                setCurrentTimeStamp(currentTimeStamp);

                boolean dataChanged = events.isData_changed();

                if (dataChanged) {
                    mThread newThread = new mThread(EventCategoryBody.this, events);
                    newThread.start();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(this.getClass().getName(), error.toString());
            }
        };



        /**
         * start showing events already in the database
         */
        startShowingEventsFromDB();



        RestClient restClient = new RestClient();
        Log.d(TAG, "calling");


        String emailId = AYNFragment1. onlyCheckUser(getActivity());
        if (emailId == null) {
            Log.d(TAG, "AYN for not signed in calling server");
            emailId = "";
            LatLng currentLatLng = locationHelper.getCurrentLocation();

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
            restClient.getApiService1().getEventsParticularCategoryNotSignedIn(emailId, latitude, longitude, getLastTimeStamp(), category, cb);
        } else {
            Log.d(TAG, "AYN for signed in calling server");

            /**
             * hardcoding the email id
             */
            emailId = "nikunj.sharma9299@gmail.com";
            restClient.getApiService1().getEventsParticularCategory(emailId, getLastTimeStamp(), category, cb);
        }

        return rootView;
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
            dbManager.emptyEventsParticularCategory(category);
            dbManager.insertEventsParticalarCategory(mEvents.getEvents(), category);
            mCallback.callAdapter();
        }
    }

    @Override
    public void callAdapter() {
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    EventsCursorAdapter eventsCursorAdapter = new EventsCursorAdapter(getActivity(), dbManager.getEventListParticularCategory(category));
                    listView.setAdapter(eventsCursorAdapter);
                }
            });
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    private void setCurrentTimeStamp(String currentTimeStamp) {
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getActivity().getString(R.string.CURRENT_TIME_STAMP), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(getActivity().getString(R.string.CURRENT_TIME_STAMP_EVENTS) + category, currentTimeStamp);
            editor.apply();
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    private String getLastTimeStamp() {
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getActivity().getString(R.string.CURRENT_TIME_STAMP), Context.MODE_PRIVATE);
            return sharedPreferences.getString(getActivity().getString(R.string.CURRENT_TIME_STAMP_EVENTS) + category, "0.0");
        }catch (Exception e){
            return "0.0";
        }
    }


    private void startShowingEventsFromDB() {
        EventsCursorAdapter eventsCursorAdapter = new EventsCursorAdapter(getActivity(), dbManager.getEventListParticularCategory(category));
        listView.setAdapter(eventsCursorAdapter);
    }
}