package in.nearfox.nearfox.gcm;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;

import in.nearfox.nearfox.MainActivity;
import in.nearfox.nearfox.R;
import in.nearfox.nearfox.fragments.NewsFragment;
import in.nearfox.nearfox.models.DataModels;
import in.nearfox.nearfox.utilities.LocationHelper;
import in.nearfox.nearfox.utilities.Preference;
import in.nearfox.nearfox.utilities.RestClient;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GCMHandler {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static String TAG = "MyDebug: ";
    private static GoogleCloudMessaging gcm;
    private static Context context;
    private static String regid;
    retrofit.Callback<DataModels.RegistedUserResult> cb;
    private String name;
    private String lastName;
    private String link;
    private String email;
    private String image_url;
    private String google_or_fb;
    private LocationHelper locationHelper;
    private RelativeLayout loaderContainer;
    private ProgressDialog progressMessage;

    public GCMHandler(Context context, String name, String lastName, String link, String email, String image_url, String google_or_fb, LocationHelper locationHelper) {
        GCMHandler.context = context;
        this.name = name;
        this.lastName = lastName;
        this.link = link;
        this.email = email;
        this.image_url = image_url;
        this.google_or_fb = google_or_fb;
        this.locationHelper = locationHelper;
        loaderContainer = (RelativeLayout) ((Activity) context).findViewById(R.id.loader_container);

        if (this.locationHelper == null)
            this.locationHelper = new LocationHelper(context);

        if (this.checkPlayServices()) {

            gcm = GoogleCloudMessaging.getInstance(context);
            regid = getRegistrationId(context);
            if (regid.isEmpty() || regid.equals(""))
                registerInBackground();
            else {
                sendRegistrationIdToBackend();
            }
        } else {
            Log.d(TAG, "No valid Google Play Services APK found.");
            Toast.makeText(context, "Google Play Services Not Found\nNotification, Inapp map won't work.", Toast.LENGTH_LONG).show();
        }
    }

    public static String getRegistrationId(Context context) {

        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(context.getString(R.string.GCM_REG_ID), "");

        if (registrationId.isEmpty()) {
            Log.d(TAG, "Registration not found.");
            return "";
        }

        int registeredVersion = prefs.getInt(context.getString(R.string.GCM_APP_VERSION), Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);

        if (registeredVersion != currentVersion) {
            Log.d(TAG, "App version changed.");
            return "";
        }

        return registrationId;
    }

    private static SharedPreferences getGCMPreferences(Context context) {

        return context.getSharedPreferences(context.getString(R.string.GCM_INFO),
                Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("could not get package name: " + e);
        }
    }

    private void registerInBackground() {

        Log.d(TAG, "regestering");

        new AsyncTask<Void, Void, Boolean>() {
//            private ProgressDialog progressMessage;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressMessage = new ProgressDialog(context);
                progressMessage.setMessage("Regestering...");
                progressMessage.setIndeterminate(false);
                progressMessage.setCancelable(false);
                progressMessage.show();
            }

            @Override
            protected Boolean doInBackground(Void[] params) {
                Log.d(TAG, "in doinbackground");
                String msg;
                boolean registeredOnGCM;
                try {
                    if (gcm == null) {
                        Log.d(TAG, "gcm null");
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    Log.d(TAG, "after if");
                    regid = gcm.register(context.getResources().getString(R.string.SENDER_ID));
                    msg = "Device registered, registration ID=" + regid;
                    Log.d(TAG, "new registeration: " + msg);
                    registeredOnGCM = true;


                } catch (IOException ex) {
                    Log.d(TAG, "exception: " + ex.toString());
                    registeredOnGCM = false;
                }

                if (registeredOnGCM)
                    sendRegistrationIdToBackend();

                return registeredOnGCM;
            }

            @Override
            protected void onPostExecute(Boolean msg) {
                if (progressMessage != null && progressMessage.isShowing()) {
                    progressMessage.dismiss();
                }
            }
        }.execute(null, null, null);
    }

    private boolean sendRegistrationIdToBackend() {

        RestClient restClient = new RestClient();

        cb = new retrofit.Callback<DataModels.RegistedUserResult>() {

            @Override
            public void success(DataModels.RegistedUserResult registedUserResult, Response response) {
                boolean registered = registedUserResult.getRegistered();
                Log.d(TAG, "reg: " + registered);
                if (registered) {
                    Log.d(TAG, "registered on our server");
                    storeRegistrationId();
                    new Preference(context).setLoggedInEmail(email);
                    NewsFragment.deleteLastTimeStamp(context);
                    startAnyActivity(false);
                } else {
                    Log.d(TAG, "server error");
                    errorMessage();
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(TAG, "error: " + retrofitError);
                Log.d(TAG, retrofitError.getUrl());
                errorMessage();
            }
        };

        LatLng currentLocation= locationHelper.getCurrentLocation();
        Double latitude = currentLocation.latitude;
        Double longitude = currentLocation.longitude;

        if (latitude == null || longitude == null) {
            Log.d(TAG, "lat long not found");
            restClient.getApiService1().addUser(name, lastName, link, email, image_url, google_or_fb, "oauth_id", regid, MainActivity.getDeviceId(context), cb);
        } else {
            Log.d(TAG, "lat long found");
            try {
                Log.d(TAG, name + " " + link + " " + email + " " + image_url + " " + google_or_fb + " " + regid + " " + latitude + " " + longitude + " " + MainActivity.getDeviceId(context));
                restClient.getApiService1().addUser(name, lastName, link, email, image_url, google_or_fb, "oauth_id", regid, latitude, longitude, MainActivity.getDeviceId(context), cb);
            } catch (Exception e) {
                Log.d(TAG, "error: " + e.toString());
                e.printStackTrace();
            }
        }

        return false;
    }

    private void storeRegistrationId() {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.d(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getResources().getString(R.string.GCM_REG_ID), regid);
        editor.putInt(context.getResources().getString(R.string.GCM_APP_VERSION), appVersion);
        editor.apply();
    }

    private boolean checkPlayServices() {

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) context,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                ((Activity) context).finish();
            }
            return false;
        }

        return true;
    }

    public void startAnyActivity(boolean which) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(context.getResources().getString(R.string.USER_NAME), name);
        intent.putExtra(context.getResources().getString(R.string.USER_EMAIL), image_url);
        intent.putExtra(context.getResources().getString(R.string.USER_IMAGE_URL), link);
        intent.putExtra(context.getResources().getString(R.string.USER_PROFILE), email);
        if (which) {
            intent.putExtra(context.getResources().getString(R.string.GOOGLE_OR_FACEBOOK), context.getResources().getString(R.string.SELECTED_GOOGLE));
        } else {
            intent.putExtra(context.getResources().getString(R.string.GOOGLE_OR_FACEBOOK), context.getResources().getString(R.string.SELECTED_FACEBOOK));
        }
        ((Activity) context).findViewById(R.id.loader_container).setVisibility(View.GONE);
        context.startActivity(intent);
    }

    public void errorMessage() {

        Toast.makeText(context, "Unable to register on NearFox Server.", Toast.LENGTH_LONG).show();
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.GOOGLE_PLUS_INFO), Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences1 = context.getSharedPreferences(context.getResources().getString(R.string.FACEBOOK_INFO), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();

        editor.clear();
        editor1.clear();

        editor.apply();
        editor1.apply();
        loaderContainer.setVisibility(View.GONE);
//        ((Activity)context).findViewById(R.id.loader_container).setVisibility(View.GONE) ;
    }

    /*public void onPause(){
        if (progressMessage != null && progressMessage.isShowing())
            progressMessage.cancel();
    }*/
}