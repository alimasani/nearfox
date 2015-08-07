package in.nearfox.nearfox.authentication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import in.nearfox.nearfox.MainActivity;
import in.nearfox.nearfox.NoActionActivity;
import in.nearfox.nearfox.R;
import in.nearfox.nearfox.SubmitActivity;
import in.nearfox.nearfox.authentication.gplus.GPlusMonitor;
import in.nearfox.nearfox.gcm.GCMHandler;
import in.nearfox.nearfox.utilities.LocationHelper;
import in.nearfox.nearfox.utilities.Preference;

public class LoginActivity extends Activity {

    int steps = 0 ;
    private final String TAG = this.getClass().getName();
    FacebookCallback<LoginResult> loginCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d(TAG, "onsuccess");
            Log.d(TAG, loginResult.toString());

            // App code
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            // Application code
                            Log.v(TAG, response.toString());
                            Log.d(TAG, object.toString());
                            try {
                                fEmail = object.getString("email");
                            }catch (JSONException e){
                                Log.d(TAG, e.toString());
                            }
                            steps++ ;
                            if (steps == 2)
                                startAnyActivity(false);
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            Log.d(TAG, "session cancel");
            Toast.makeText(context, "Canceled logging in", Toast.LENGTH_LONG).show();
            loaderContainer.setVisibility(View.GONE);
        }

        @Override
        public void onError(FacebookException exception) {
            Log.d(TAG, "session error");
            Toast.makeText(context, "Error logging in", Toast.LENGTH_LONG).show();
            loaderContainer.setVisibility(View.GONE);
        }
    };
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;
    //    fb login variables
    String fName;
    String fLastName;
    String fEmail;
    String fLink;
    String fImageURL;

    //   gmail login variables
    String gName;
    String gEmail;
    String gLink;
    String gImageURL;
    GPlusMonitor gPlusMonitor;
    //    if already registered on gcm
    String regId;
    //    Handler for GCM Registration
    GCMHandler gcmHandler;
    //    Handler for getting location
    LocationHelper locationHelper;
    private boolean facebookLoggedIn = false;
    private boolean gPlusLoggedIn = false;
    private Context context;
    //    getting view handles
    private RelativeLayout loaderContainer;

    public static void deleteGcmRegistration(Context context) {
        Log.d("LoginActivity", "deleted");
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.GCM_INFO), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        locationHelper = new LocationHelper(this) ;
        FacebookSdk.sdkInitialize(getApplicationContext());
//        deleteGcmRegistration();
        regId = GCMHandler.getRegistrationId(this);
        Log.d(TAG, "regid for gcm: " + regId);

        if (checkForFBLogin()) {
            startAsFBLoggedIn();
            facebookLoggedIn = true;
        }

        if (checkForGPlusLogIn()) {
            startAsGPlusLoggedIn();
            gPlusLoggedIn = true;
        }

        if (!facebookLoggedIn && !gPlusLoggedIn) {
            setContentView(R.layout.activity_login);
            gPlusMonitor = new GPlusMonitor(this, regId, locationHelper);
            context = this;
            Log.d(TAG, "oncreate");

            loaderContainer = (RelativeLayout) findViewById(R.id.loader_container);

//            fb
            callbackManager = CallbackManager.Factory.create();
            LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

            loginButton.setReadPermissions(Arrays.asList("public_profile",
                    "email"));

            loginButton.registerCallback(callbackManager, loginCallback);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loaderContainer.setVisibility(View.VISIBLE);

                }
            });

            accessTokenTracker = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(
                        AccessToken oldAccessToken,
                        AccessToken currentAccessToken) {
                    Log.d(TAG, "access token tracker current changed");
                    if (oldAccessToken != null) {
                        Log.d(TAG, oldAccessToken.toString());
                    }
                    if (currentAccessToken != null) {
                        Log.d(TAG, currentAccessToken.getUserId());
                        Log.d(TAG, currentAccessToken.toString());
                    }
                }
            };

            profileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(
                        Profile oldProfile,
                        Profile currentProfile) {

                    if (oldProfile != null) {
                        facebookLoggedIn = false;
                        Log.d(TAG, "old profile");
                        Log.d(TAG, oldProfile.getFirstName());
                        Log.d(TAG, oldProfile.getName());
                        Log.d(TAG, oldProfile.getProfilePictureUri(getResources().getInteger(R.integer.FACEBOOK_IMAGE_SIZE), getResources().getInteger(R.integer.FACEBOOK_IMAGE_SIZE)).toString());
                        SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.FACEBOOK_INFO), Context.MODE_PRIVATE);
                        sharedPreferences.edit().clear();
                        fName = fEmail = fLink = fImageURL = null;
                    }
                    if (currentProfile != null) {

                        Log.d(TAG, "current profile");
                        Log.d(TAG, currentProfile.getFirstName());
                        Log.d(TAG, currentProfile.getName());
                        Log.d(TAG, currentProfile.getProfilePictureUri(getResources().getInteger(R.integer.FACEBOOK_IMAGE_SIZE), getResources().getInteger(R.integer.FACEBOOK_IMAGE_SIZE)).toString());
                        Log.d(TAG, currentProfile.getId());
                        SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.FACEBOOK_INFO), Context.MODE_PRIVATE);
                        loaderContainer.setVisibility(View.GONE);
                        if (sharedPreferences != null) {
                            facebookLoggedIn = true;
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(getResources().getString(R.string.USER_NAME), currentProfile.getFirstName());
//                            editor.putString(getResources().getString(R.string.USER_EMAIL), currentProfile.getId());
                            editor.putString(getResources().getString(R.string.USER_IMAGE_URL), currentProfile.getProfilePictureUri(getResources().getInteger(R.integer.FACEBOOK_IMAGE_SIZE), getResources().getInteger(R.integer.FACEBOOK_IMAGE_SIZE)).toString());
                            editor.putString(getResources().getString(R.string.USER_PROFILE), currentProfile.getLinkUri().toString());
                            editor.apply();
                            fName = currentProfile.getFirstName();
//                            fEmail = currentProfile.getId();
                            fLastName = currentProfile.getLastName();
                            fImageURL = currentProfile.getProfilePictureUri(getResources().getInteger(R.integer.FACEBOOK_IMAGE_SIZE), getResources().getInteger(R.integer.FACEBOOK_IMAGE_SIZE)).toString();
                            fLink = currentProfile.getLinkUri().toString();

                            Log.d(TAG, fName + " " + fEmail + " " + fImageURL + " " + fLink);
//                            RestClient restClient = new RestClient() ;
//                            restClient.getApiService().addUser(fName, fLink, fLink, fImageURL, getResources().getString(R.string.SELECTED_FACEBOOK),);

                            steps++ ;
                            if (steps == 2)
                                startAnyActivity(false);
                        }
                    }
                }
            };

            AccessToken fb_token = AccessToken.getCurrentAccessToken();
            if (fb_token != null) {
                Log.d(TAG, "fb token not null");
                Log.d(TAG, fb_token.getUserId());
                Log.d(TAG, String.valueOf(fb_token.getExpires()));
                facebookLoggedIn = true;
                loaderContainer.setVisibility(View.VISIBLE);

            } else {
                Log.d(TAG, "fb token null");
                facebookLoggedIn = false;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationHelper != null)
            locationHelper.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (locationHelper != null)
            locationHelper.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onstart");
        if (gPlusMonitor != null)
            gPlusMonitor.onStart();
        Log.d(TAG, "onstart end");
    }

    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onstop");
        if (gPlusMonitor != null)
            gPlusMonitor.onStop();
        Log.d(TAG, "on stop end");
        if (accessTokenTracker != null)
            accessTokenTracker.stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);

        Log.d(TAG, "request: " + requestCode + "; response: " + responseCode);
        callbackManager.onActivityResult(requestCode, responseCode, intent);

        gPlusMonitor.onActivityResult(requestCode, responseCode, intent);
    }

    /**
     * Button on click listener
     */
    public void startAnyActivity(boolean which) {



        Intent intent = new Intent(this, MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        if (which) {

            if (regId.isEmpty() || regId.equals("")) {
                Log.d(TAG, "regid not found, registering on gcm1");
                gcmHandler = new GCMHandler(this, gName, "", gLink, gEmail, gImageURL, getResources().getString(R.string.SELECTED_GOOGLE), locationHelper);
            } else {
//                Preference preference = new Preference(LoginActivity.this);
//                if(preference.isSubmitClicked()){
//
//                    intent = new Intent(this, SubmitActivity.class);
//                    intent.putExtra("index", preference.getLastUsed());
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                    startActivity(intent);
//                    return;
//                }
                Log.d(TAG, "regid found during startActivity");
                intent.putExtra(getResources().getString(R.string.GOOGLE_OR_FACEBOOK), getResources().getString(R.string.SELECTED_GOOGLE));
                intent.putExtra(getResources().getString(R.string.USER_NAME), gName);
                intent.putExtra(getResources().getString(R.string.USER_IMAGE_URL), gImageURL);
                intent.putExtra(getResources().getString(R.string.USER_PROFILE), gLink);
                intent.putExtra(getResources().getString(R.string.USER_EMAIL), gEmail);
                intent.putExtra(context.getString(R.string.USER_LAST_NAME), fLastName);
                startActivity(intent);
                finish();
            }
        } else {
            if (regId.isEmpty() || regId.equals("")) {
                Log.d(TAG, "regid not found, registering on gcm2");
                gcmHandler = new GCMHandler(this, fName, fLastName, fLink, fEmail, fImageURL, getResources().getString(R.string.SELECTED_FACEBOOK), locationHelper);
            } else {
//                Preference preference = new Preference(context);
//                if(preference.isSubmitClicked()){
////            preference.setSubmitClicked(false);
//                    Intent intent = new Intent(context, SubmitActivity.class);
//                    intent.putExtra("index", preference.getLastUsed());
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                    ((Activity) context).findViewById(R.id.loader_container).setVisibility(View.GONE);
//                    context.startActivity(intent);
//                    return;
//                }
//

                intent.putExtra(getResources().getString(R.string.GOOGLE_OR_FACEBOOK), getResources().getString(R.string.SELECTED_FACEBOOK));
                intent.putExtra(getResources().getString(R.string.USER_NAME), fName);
                intent.putExtra(getResources().getString(R.string.USER_EMAIL), fEmail);
                intent.putExtra(getResources().getString(R.string.USER_IMAGE_URL), fImageURL);
                intent.putExtra(getResources().getString(R.string.USER_PROFILE), fLink);
                startActivity(intent);
            }
        }
    }

    public boolean checkForFBLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.FACEBOOK_INFO), Context.MODE_PRIVATE);
        fName = sharedPreferences.getString(getResources().getString(R.string.USER_NAME), null);
        fEmail = sharedPreferences.getString(getResources().getString(R.string.USER_EMAIL), null);
        fLink = sharedPreferences.getString(getResources().getString(R.string.USER_PROFILE), null);
        fImageURL = sharedPreferences.getString(getResources().getString(R.string.USER_IMAGE_URL), null);

        return (fName != null && fImageURL != null && fLink != null && fEmail != null) ;
    }

    public void startAsFBLoggedIn() {
        startAnyActivity(false);
    }

    public void startAsGPlusLoggedIn() {
        startAnyActivity(true);
    }

    public boolean checkForGPlusLogIn() {
        SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.GOOGLE_PLUS_INFO), Context.MODE_PRIVATE);
        gName = sharedPreferences.getString(getResources().getString(R.string.USER_NAME), null);
        gEmail = sharedPreferences.getString(getResources().getString(R.string.USER_EMAIL), null);
        gLink = sharedPreferences.getString(getResources().getString(R.string.USER_PROFILE), null);
        gImageURL = sharedPreferences.getString(getResources().getString(R.string.USER_IMAGE_URL), null);
        if (gName != null && gImageURL != null && gLink != null && gEmail != null) {
            Log.d(TAG, "gPlus. saved prefs found");
            return true;
        } else {
            Log.d(TAG, "gPlus. saved prefs not found");
            return false;
        }
    }

    public void onclick_terms_and_policies(View view) {
        Intent intent = new Intent(context, NoActionActivity.class);
        intent = intent.putExtra(NoActionActivity.LAYOUT_RESOURCE_ID, R.layout.activity_no_action_termsandcondition);
        startActivity(intent);
    }
}