package in.nearfox.nearfox.authentication.gplus;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;

/**
 * Created by tangbang on 6/17/2015.
 */
public class GPlusLogoutMoitor implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    String TAG = this.getClass().getName() ;
    Context context ;
    static GoogleApiClient mGoogleApiClient ;

    public GPlusLogoutMoitor(Context context, GoogleApiClient mGooApliClient){
        this.context = context ;
        this.mGoogleApiClient = mGooApliClient ;

        if (mGooApliClient == null){
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).addApi(Plus.API, Plus.PlusOptions.builder().build())
                    .addScope(Plus.SCOPE_PLUS_LOGIN).build();
        }

        mGoogleApiClient.connect() ;
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d(TAG, "on connection failed") ;
        if (!result.hasResolution()) {
            Log.d(TAG, "!if") ;
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), (Activity) context,
                    0).show();
            return;
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }


    public void signOutFromGplus() {
        Log.d(TAG, "sign out") ;
        if (mGoogleApiClient.isConnected()) {
            Log.d(TAG, "is connected") ;
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }
        else if(mGoogleApiClient.isConnecting()){
            Log.d(TAG, "is connecting") ;
        }
        else{
            Log.d(TAG, "none of those") ;
        }
    }

    private void revokeGplusAccess() {
        Log.d(TAG, "revoke") ;
        if (mGoogleApiClient.isConnected()) {
            Log.d(TAG, "mgoogleapi") ;
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.e(TAG, "User access revoked!");
                            mGoogleApiClient.connect();
                        }

                    });
        }
    }
}
