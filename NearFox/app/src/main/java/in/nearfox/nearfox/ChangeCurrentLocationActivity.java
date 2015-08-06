package in.nearfox.nearfox;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

import in.nearfox.nearfox.models.DataModels;
import in.nearfox.nearfox.utilities.ApplicationHelper;
import in.nearfox.nearfox.utilities.DBManager;
import in.nearfox.nearfox.utilities.Preference;
import in.nearfox.nearfox.utilities.RestClient;
import in.nearfox.nearfox.view.PlaceAutoComplete;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Uss on 8/3/2015.
 */
public class ChangeCurrentLocationActivity extends AppCompatActivity {

    private Toolbar mToolbarView;

    private PlaceAutoComplete placeAutoComplete;
    private TextView currentLocation;
    private LocationManager locManager;
    private LocationListener locListener;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_current_location_activity);


        /**
         * Main init starts
         */
        initToolbar();
        placeAutoComplete = (PlaceAutoComplete) findViewById(R.id.location);

        currentLocation = (TextView) findViewById(R.id.txtCurrentLocation);



        findViewById(R.id.btnLetGo).setOnClickListener(letsGoListener);


        placeAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                placeAutoComplete.click = true;
                placeAutoComplete.setSelection(0,0);

            }
        });


        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Preference preference = new Preference(ChangeCurrentLocationActivity.this);
                if (currentLocation.getText().toString().trim().length() > 0)
                    preference.setCurrentLocation(currentLocation.getText().toString());
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangeCurrentLocationActivity.this);
                    builder.setTitle("Attention!");
                    builder.setMessage("Please set your location");
                    builder.create().show();
                    return;
                }
                if(preference.getCurrentLocation().trim().toLowerCase().contains("mumbai")) {
                    LatLng location = new ApplicationHelper(ChangeCurrentLocationActivity.this).getLocationFromAddress(preference.getCurrentLocation());
                    preference.setCurrentLocation(location);
                    preference.setHomeLocation(location);
                    if (getIntent().getBooleanExtra("isSignedIn", false))
                        setCurrentLocation(preference.getCurrentLocation());
                    else {
                        finish();
                        Intent intent = new Intent(ChangeCurrentLocationActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }

                } else {
                    preference.setCurrentLocation("");
                    new ApplicationHelper(ChangeCurrentLocationActivity.this).showMessageDialog("Please select your Location within Mumbai city");
                }
            }
        });

    }

    public void setCurrentLocation(String location) {
        RestClient restClient = new RestClient(RestClient.LOCATION_BASE_URL1);
        Preference preference = new Preference(ChangeCurrentLocationActivity.this);
        restClient.getApiService().setLocation(preference.getLoggedInEmail(), location, callback);
    }

    retrofit.Callback<DataModels.Location> callback = new retrofit.Callback<DataModels.Location>() {
        @Override
        public void success(DataModels.Location news, Response response) {
            if(news.isSuccess()) {
                new ApplicationHelper(ChangeCurrentLocationActivity.this).showMessageDialog("Your location updated successfully").setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        finish();
                        Intent intent = new Intent(ChangeCurrentLocationActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }
                });

            } else {
                new ApplicationHelper(ChangeCurrentLocationActivity.this).showMessageDialog(news.getMessage());
                findViewById(R.id.btnLetGo).setEnabled(true);
                findViewById(R.id.btnLetGo).setClickable(true);
            }
        }

        @Override
        public void failure(RetrofitError error) {
            new ApplicationHelper(ChangeCurrentLocationActivity.this).showMessageDialog(error.toString());
            findViewById(R.id.btnLetGo).setEnabled(true);
            findViewById(R.id.btnLetGo).setClickable(true);
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locListener = new MyLocationListener();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Location location = getLastKnownLoaction(true);
                if (location != null) {
                    currentLocation.setText(getCompleteAddressString(location.getLatitude(), location.getLongitude()));
                }
                if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    //Ask the user to enable GPS
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangeCurrentLocationActivity.this);
                    builder.setTitle("Location Manager");
                    builder.setMessage("Would you like to enable GPS?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Launch settings, allowing user to make a change
                            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(i);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //No location service, no Activity
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                } else {

                    boolean gps_enabled = false, network_enabled = false;
                    try {
                        gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    } catch (Exception ex) {
                    }
                    try {
                        network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                    } catch (Exception ex) {
                    }

                    // don't start listeners if no provider is enabled
                    if (!gps_enabled && !network_enabled) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChangeCurrentLocationActivity.this);
                        builder.setTitle("Attention!");
                        builder.setMessage("Sorry, location is not determined. Please enable location providers");
                        builder.create().show();
                    }

                    if (gps_enabled) {
                        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
                    }
                    if (network_enabled) {
                        locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
                    }
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locManager.removeUpdates(locListener);
    }

    class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                // This needs to stop getting the location data and save the battery power.
                locManager.removeUpdates(locListener);
                currentLocation.setText(getCompleteAddressString(location.getLatitude(), location.getLongitude()));
                currentLocation.invalidate();
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current", "" + strReturnedAddress.toString());
            } else {
                Log.w("My Current", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current", "Cannot get Address!");
        }
        return strAdd;
    }

    private Location getLastKnownLoaction(boolean enabledProvidersOnly) {

        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Location location = null;

        List<String> providers = manager.getProviders(enabledProvidersOnly);

        for (String provider : providers) {
            location = manager.getLastKnownLocation(provider);
            if (location != null) return location;
        }

        return null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){

            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private View.OnClickListener letsGoListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Preference preference = new Preference(ChangeCurrentLocationActivity.this);
            if (placeAutoComplete.getText().toString().trim().length() > 0 && placeAutoComplete.click)
                preference.setCurrentLocation(placeAutoComplete.getText().toString());
            else if (currentLocation.getText().toString().trim().length() > 0)
                preference.setCurrentLocation(currentLocation.getText().toString());
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChangeCurrentLocationActivity.this);
                builder.setTitle("Attention!");
                builder.setMessage("Please set your location");
                builder.create().show();
                return;
            }
            view.setEnabled(false);
            view.setClickable(false);
            if(preference.getCurrentLocation().trim().toLowerCase().contains("mumbai")) {
                LatLng location = new ApplicationHelper(ChangeCurrentLocationActivity.this).getLocationFromAddress(preference.getCurrentLocation());
                preference.setCurrentLocation(location);
                preference.setHomeLocation(location);
                if (getIntent().getBooleanExtra("isSignedIn", false))
                    setCurrentLocation(preference.getCurrentLocation());
                else {
                    Intent intent = new Intent(ChangeCurrentLocationActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();
                    view.setEnabled(true);
                    view.setClickable(true);
                }

            } else {
                preference.setCurrentLocation("");
                new ApplicationHelper(ChangeCurrentLocationActivity.this).showMessageDialog("Please select your Location within Mumbai city");
                view.setEnabled(true);
                view.setClickable(true);
            }

        }
    };

    private void initToolbar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        getSupportActionBar().setHomeButtonEnabled(true);

        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        mToolbarView.setNavigationIcon(R.drawable.ic_chevron_left_white_36dp);
        mToolbarView.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

            
        });

    }


}
