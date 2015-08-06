package in.nearfox.nearfox.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import in.nearfox.nearfox.R;

/**
 * Created by Uss on 7/25/2015.
 */
public class Preference {
    private final String WALK_THROUGH = "WalkThrough";
    private final String NEAR_FOX = "NearFox";
    private final String CHOOSE_LOCATION = "Choose_Location";
    private final String CURRENT_LOCATION = "Current_Location";
    private final String CURRENT_LATITUDE = "Current_Latitude";
    private final String CURRENT_LONGITUDE = "Current_Longitude";
    private final String EMAIL = "Email";


    private Context context;

    public Preference(Context context) {
        this.context = context;
    }

    public void setCurrentLocation(String currentLocation) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NEAR_FOX, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CURRENT_LOCATION, currentLocation);
        editor.commit();
    }


    public String getCurrentLocation() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NEAR_FOX, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CURRENT_LOCATION, "");
    }


    public void setCompleteWalkThrough() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NEAR_FOX, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(WALK_THROUGH, true);
        editor.commit();
    }

    public void setCompleteLocation() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NEAR_FOX, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(CHOOSE_LOCATION, true);
        editor.commit();
    }

    public boolean isWalkThroughComplete() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NEAR_FOX, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(WALK_THROUGH, false);
    }

    public boolean isChooseLocatoinComplete() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NEAR_FOX, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(CHOOSE_LOCATION, false);
    }

    public void setLoggedInEmail(String email) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NEAR_FOX, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL, email);
        editor.commit();
    }

    public String getLoggedEmail() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NEAR_FOX, Context.MODE_PRIVATE);
        //return "nikunj.sharma92@gmail.com";
        return sharedPreferences.getString(EMAIL, "");
    }

    public String getLoggedInEmail() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NEAR_FOX, Context.MODE_PRIVATE);
        return sharedPreferences.getString(EMAIL, "");
        //return sharedPreferences.getString(EMAIL, "nikunj.sharma92@gmail.com");
    }

    public void setHomeLocation(LatLng homeLocation) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.HOME_LOCATION), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        editor.putBoolean(context.getResources().getString(R.string.IF_HOME_LOCATION_SET), true);
        editor.putString(context.getString(R.string.home_latitude), String.valueOf(homeLocation.latitude));
        editor.putString(context.getString(R.string.home_longitude), String.valueOf(homeLocation.longitude));
        editor.apply();
    }

    public void setCurrentLocation(LatLng mCurrentLocation) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.HOME_LOCATION), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(context.getString(R.string.current_latitude), String.valueOf(mCurrentLocation.latitude));
            editor.putString(context.getString(R.string.current_longitude), String.valueOf(mCurrentLocation.longitude));

            editor.apply();
        } catch (Exception e) {
            Log.d("PREFERENCE", e.toString());
        }
    }

    public LatLng getCurrentLocationLatLng() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.HOME_LOCATION), Context.MODE_PRIVATE);
        Double lat = Double.parseDouble(sharedPreferences.getString(context.getString(R.string.current_latitude), "0"));
        Double lng = Double.parseDouble(sharedPreferences.getString(context.getString(R.string.current_longitude), "0"));
        return new LatLng(lat, lng);
    }

    public LatLng getHomeLatLng() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.HOME_LOCATION), Context.MODE_PRIVATE);
        Double lat = Double.parseDouble(sharedPreferences.getString(context.getString(R.string.home_latitude), "0"));
        Double lng = Double.parseDouble(sharedPreferences.getString(context.getString(R.string.home_latitude), "0"));
        return new LatLng(lat, lng);
    }
}
