package in.nearfox.nearfox.authentication.gplus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.io.InputStream;

import in.nearfox.nearfox.MainActivity;
import in.nearfox.nearfox.R;
import in.nearfox.nearfox.SubmitActivity;
import in.nearfox.nearfox.gcm.GCMHandler;
import in.nearfox.nearfox.utilities.LocationHelper;
import in.nearfox.nearfox.utilities.Preference;

/**
 * Created by tangbang on 6/13/2015.
 */
public class GPlusMonitor implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 0;
    private static final int PROFILE_PIC_SIZE = 400;
    public static GoogleApiClient mGoogleApiClient;
    //    google login variables
    static String gName;
    static String gEmail;
    static String gProfile;
    static String gImageURL;
    private static boolean gPlusLoggedIn = false;
    private static String TAG = "MyDebug";
    private static Context context;
    private static RelativeLayout loaderContainer;
    //    if already registered on gcm
    String regId;
    //    Handler for GCM Registration
    GCMHandler gcmHandler;
    //   location helper
    LocationHelper locationHelper;
    private SignInButton gPlusBtnSignIn;
    private Button gPlusBtnSignOut, gPlusBtnRevokeAccess;
    private ImageView gPlusImgProfilePic;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;

    public GPlusMonitor(Context context, String regId, LocationHelper locationHelper) {
        this.context = context;
        this.regId = regId;
        this.locationHelper = locationHelper;

        gPlusBtnSignIn = (SignInButton) ((Activity) context).findViewById(R.id.gplus_btn_sign_in);
//        gPlusBtnSignOut = (Button) ((Activity)context).findViewById(R.id.btn_sign_out);
//        gPlusBtnRevokeAccess = (Button)((Activity)context). findViewById(R.id.btn_revoke_access);
//        gPlusImgProfilePic = (ImageView) ((Activity)context).findViewById(R.id.imgProfilePic);

        gPlusBtnSignIn.setOnClickListener(this);
//        gPlusBtnSignOut.setOnClickListener(this);
//        gPlusBtnRevokeAccess.setOnClickListener(this);

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
        loaderContainer = (RelativeLayout) ((Activity) context).findViewById(R.id.loader_container);
    }


    public void onStart() {
        mGoogleApiClient.connect();
    }

    public void onStop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
            Log.d(TAG, "inside if");
        }
        loaderContainer.setVisibility(View.GONE);
    }

    private void resolveSignInError() {
        try {
            Log.d(TAG, "resolve o");
            if (mConnectionResult.hasResolution()) {
                try {
                    Log.d(TAG, "if");
                    mIntentInProgress = true;
                    mConnectionResult.startResolutionForResult((Activity) context, RC_SIGN_IN);
                } catch (IntentSender.SendIntentException e) {
                    Log.d(TAG, "catch");
                    mIntentInProgress = false;
                    mGoogleApiClient.connect();
                }
            }
        } catch (Exception e) {
            if (!mGoogleApiClient.isConnected()) {
                Log.d(TAG, "catch");
                mGoogleApiClient.connect();
                resolveSignInError(1);
            }
            e.printStackTrace();
        }
    }

    private void resolveSignInError(int i) {

        try {
            Log.d(TAG, "resolve 1");
            if (mConnectionResult.hasResolution()) {
                try {
                    Log.d(TAG, "if");
                    mIntentInProgress = true;
                    mConnectionResult.startResolutionForResult((Activity) context, RC_SIGN_IN);
                } catch (IntentSender.SendIntentException e) {
                    Log.d(TAG, "catch");
                    mIntentInProgress = false;
                    mGoogleApiClient.connect();
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "catch");
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d(TAG, "on connection failed");
        if (!result.hasResolution()) {
            Log.d(TAG, "!if");
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), (Activity) context,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            Log.d(TAG, "!min");
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                Log.d(TAG, "msignclicked");
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }

    public void onActivityResult(int requestCode, int responseCode,
                                 Intent intent) {

        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "if");
            if (responseCode != Activity.RESULT_OK) {
                Log.d(TAG, "if");
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                Log.d(TAG, "!m");
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        mSignInClicked = false;
        Log.d(TAG, "connected");
//        Toast.makeText(context, "User is connected!", Toast.LENGTH_LONG).show();
        // Get user's information
        boolean ifProfileInformation = getProfileInformation();

        // Update the UI after signin
        if (ifProfileInformation)
            updateUI(true);
        else {
            Toast.makeText(context, "cannot fetch person information", Toast.LENGTH_LONG).show();
            loaderContainer.setVisibility(View.GONE);
        }
        //startAnyActivity();
    }

    private void updateUI(boolean isSignedIn) {
        Log.d(TAG, "update ui");
        if (isSignedIn) {
            Log.d(TAG, "if");
            /*gPlusBtnSignIn.setVisibility(View.GONE);
            gPlusBtnSignOut.setVisibility(View.VISIBLE);
            gPlusBtnRevokeAccess.setVisibility(View.VISIBLE);*/
            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.GOOGLE_PLUS_INFO), Context.MODE_PRIVATE);
            String gName = sharedPreferences.getString(context.getResources().getString(R.string.USER_NAME), null);
            if (gName != null) {
                gPlusLoggedIn = true;
                startAnyActivity();
            } else {
                gPlusLoggedIn = false;
                Log.d(TAG, "signing out");
                signOutFromGplus();
            }
        } else {
            Log.d(TAG, "else");
            gPlusLoggedIn = false;
            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.GOOGLE_PLUS_INFO), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            gEmail = gProfile = gImageURL = gName = null;
            editor.clear();
            editor.commit();
        }
        if (!gPlusLoggedIn)
            loaderContainer.setVisibility(View.GONE);
    }

    private boolean getProfileInformation() {
        Log.d(TAG, "get profile");
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                Log.e(TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);


                /*txtName.setText(personName);
                txtEmail.setText(email);

                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X

                new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);
*/

                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + PROFILE_PIC_SIZE;

//                new LoadProfileImage(gPlusImgProfilePic).execute(personPhotoUrl);

                this.gEmail = email;
                this.gName = personName;
                this.gProfile = personGooglePlusProfile;
                this.gImageURL = personPhotoUrl;

                SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.GOOGLE_PLUS_INFO), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(context.getResources().getString(R.string.USER_EMAIL), email);
                editor.putString(context.getResources().getString(R.string.USER_IMAGE_URL), personPhotoUrl);
                editor.putString(context.getResources().getString(R.string.USER_PROFILE), personGooglePlusProfile);
                editor.putString(context.getResources().getString(R.string.USER_NAME), personName);
                Log.d(TAG, "gName: " + personName);
                editor.commit();
                return true;

            } else {
                Log.d(TAG, "else");
                Toast.makeText(context.getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
                SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.GOOGLE_PLUS_INFO), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                return false;
            }
        } catch (Exception e) {
            Log.d(TAG, "catch");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        Log.d(TAG, "on connection susspended");
        mGoogleApiClient.connect();
        updateUI(false);
    }

    @Override
    public void onClick(View v) {

        Log.d(TAG, "on click");
        switch (v.getId()) {
            case R.id.gplus_btn_sign_in:
                // Signin button clicked
                Log.d(TAG, "signin selected");
                signInWithGplus();
                break;
/*            case R.id.btn_sign_out:
                // Signout button clicked
                Log.d(TAG, "signout selected") ;
                signOutFromGplus();
                break;
            case R.id.btn_revoke_access:
                // Revoke access button clicked
                Log.d(TAG, "revoke selected") ;
                revokeGplusAccess();
                break;*/
            default:
                Log.d(TAG, "default");
                break;
        }
    }

    private void signInWithGplus() {
        Log.d(TAG, "sign in g+");
        if (!mGoogleApiClient.isConnecting()) {
            loaderContainer.setVisibility(View.VISIBLE);
            Log.d(TAG, "if");
            mSignInClicked = true;
            resolveSignInError();
        }
    }

    /**
     * Sign-out from google
     */
    private void signOutFromGplus() {
        Log.d(TAG, "sign out");
        if (mGoogleApiClient.isConnected()) {
            Log.d(TAG, "if");
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
//            mGoogleApiClient.connect();
            updateUI(false);
        }
    }

    /**
     * Revoking access from google
     */
    private void revokeGplusAccess() {
        Log.d(TAG, "revoke");
        if (mGoogleApiClient.isConnected()) {
            Log.d(TAG, "mgoogleapi");
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.e(TAG, "User access revoked!");
                            mGoogleApiClient.connect();
                            updateUI(false);
                        }

                    });
        }
    }

    public void startAnyActivity() {


        if (regId == null || regId.isEmpty() || regId.equals("")) {

            Log.d(TAG, "regid not found, registering on gcm3");
            gcmHandler = new GCMHandler(context, gName, "", gProfile, gEmail, gImageURL, context.getResources().getString(R.string.SELECTED_GOOGLE), locationHelper);
        } else {

//            Preference preference = new Preference(context);
//            if(preference.isSubmitClicked()){
////                preference.setSubmitClicked(false);
//                Intent intent = new Intent(context, SubmitActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                intent.putExtra("index", preference.getLastUsed());
//                context.startActivity(intent);
//                loaderContainer.setVisibility(View.VISIBLE);
//
//                return;
//            }
            Intent intent = new Intent(context, MainActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

            intent.putExtra(context.getResources().getString(R.string.GOOGLE_OR_FACEBOOK), context.getResources().getString(R.string.SELECTED_GOOGLE));
            intent.putExtra(context.getResources().getString(R.string.USER_NAME), gName);
            intent.putExtra(context.getResources().getString(R.string.USER_IMAGE_URL), gImageURL);
            intent.putExtra(context.getResources().getString(R.string.USER_PROFILE), gProfile);
            intent.putExtra(context.getResources().getString(R.string.USER_EMAIL), gEmail);

            loaderContainer.setVisibility(View.VISIBLE);
            context.startActivity(intent);

        }
    }

    /**
     * Background Async task to load user profile picture from url
     */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}