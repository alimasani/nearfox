package in.nearfox.nearfox.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.util.Date;

import in.nearfox.nearfox.R;

public class LocationHelper implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final long INTERVAL = 100000;
    private static final long FASTEST_INTERVAL = 50000;
    String TAG = "MyDebug";
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    Context context;
    String mLastUpdateTime;
    private Boolean isHomeLocation = null;

    public LocationHelper(Context context) {
        this.context = context;
        //createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    protected void createLocationRequest() {
        Log.d(TAG, "creating location request");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        Log.d(TAG, "done creating request");
    }

    public void onPause() {
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        if (mGoogleApiClient.isConnecting()) {
            Log.d(TAG, "Still connecting");
        } else if (mGoogleApiClient.isConnected()) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(
//                    mGoogleApiClient, this);
            Log.d(TAG, "Location update stopped .......................");
        }
    }

    public void onResume() {
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, (Activity) context, 0).show();
            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
//        LocationServices.FusedLocationApi.requestLocationUpdates(
//                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

        /*Callback<DataModels.HomeLocation> cb = new Callback<DataModels.HomeLocation>() {
            @Override
            public void success(DataModels.HomeLocation homeLocation, Response response) {
                Log.d(TAG, "home location post request success");
                boolean success = homeLocation.isSuccess();
                if (success) {
                    Log.d(TAG, "home location set");
                    markHomeLocationSet();
                } else {
                    Log.d(TAG, "home location set");
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(TAG, "retrofit error, location not marked");
                Log.d(TAG, retrofitError.getUrl());
            }
        };*/

        if (!isHomeLocationSet()) {
            /*RestClient restClient = new RestClient();
            restClient.getApiService().postLocation(String.valueOf(getCurrentLatitude()), String.valueOf(getCurrentLongitude()), MainActivity.getDeviceId(context), cb);*/
            markHomeLocationSet();
        }

        setCurrentLocation();
    }

    public Double getCurrentLatitude() {
        if (mCurrentLocation == null)
            return null;
        else
            return mCurrentLocation.getLatitude();
    }

    public Double getCurrentLongitude() {
        if (mCurrentLocation == null)
            return null;
        else
            return mCurrentLocation.getLongitude();
    }

    public boolean isHomeLocationSet() {
        if (isHomeLocation == null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.HOME_LOCATION), Context.MODE_PRIVATE);
            boolean ifhome = sharedPreferences.getBoolean(context.getResources().getString(R.string.IF_HOME_LOCATION_SET), false);
            isHomeLocation = ifhome;
            return ifhome;
        } else {
            return isHomeLocation;
        }
    }

    public void markHomeLocationSet() {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.HOME_LOCATION), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            if (!isHomeLocation) {
                editor.putBoolean(context.getResources().getString(R.string.IF_HOME_LOCATION_SET), true);
                isHomeLocation = true;
                editor.putString(context.getString(R.string.home_latitude), String.valueOf(mCurrentLocation.getLatitude()));
                editor.putString(context.getString(R.string.home_longitude), String.valueOf(mCurrentLocation.getLongitude()));
                editor.apply();
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    public LatLng getCurrentLocation() {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.HOME_LOCATION), Context.MODE_PRIVATE);
            return new LatLng(Double.parseDouble(sharedPreferences.getString(context.getString(R.string.current_latitude), "19.0544718")), Double.parseDouble(sharedPreferences.getString(context.getString(R.string.current_longitude), "72.8475496")));
        } catch (Exception e) {
            Log.d(TAG, e.toString());
            return null;
        }
    }

    public LatLng getHomeLocation() {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.HOME_LOCATION), Context.MODE_PRIVATE);
            return new LatLng(Double.parseDouble(sharedPreferences.getString(context.getString(R.string.home_latitude), "19.0544718")), Double.parseDouble(sharedPreferences.getString(context.getString(R.string.home_longitude), "72.8475496")));
        } catch (Exception e) {
            Log.d(TAG, e.toString());
            return null;
        }
    }

    public void setCurrentLocation() {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.HOME_LOCATION), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(context.getString(R.string.current_latitude), String.valueOf(mCurrentLocation.getLatitude()));
            editor.putString(context.getString(R.string.current_longitude), String.valueOf(mCurrentLocation.getLongitude()));

            editor.apply();
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }
}