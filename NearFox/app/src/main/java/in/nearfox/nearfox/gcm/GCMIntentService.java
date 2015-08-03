package in.nearfox.nearfox.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import in.nearfox.nearfox.MainActivity;
import in.nearfox.nearfox.R;

/**
 * Created by tangbang on 6/7/2015.
 */
public class GCMIntentService extends IntentService {
    private static String TAG = "MyDebug" ;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    public GCMIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "message received") ;
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
//                sendNotification("Send error: " + extras.toString(), "Error");
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
//                sendNotification("Deleted messages on server: " +
//                        extras.toString(), "Error");
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {

//                sendNotification(intent.getStringExtra("price")) ;
                Log.i(TAG, "Received: " + extras.toString()) ;
                Log.i(TAG, "intent was" + intent.toString()) ;
                handleMessage(intent.getStringExtra("message")) ;
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String type, String id, String title, String image, String category, String period, String news_details) {
        Log.d(TAG,"id: " + id) ;
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra(getString(R.string.MESSAGE_TYPE), getString(R.string.NEWS_NOTIFICATION)) ;
        resultIntent.putExtra(getString(R.string.ID), id) ;
        resultIntent.putExtra(getString(R.string.TITLE), title) ;
        resultIntent.putExtra(getString(R.string.NEWS_IMAGE), image) ;
        resultIntent.putExtra(getString(R.string.CATEGORY), category) ;
        resultIntent.putExtra(getString(R.string.PERIOD), period) ;
        resultIntent.putExtra(getString(R.string.NEWS_DETAILS), news_details) ;

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] pattern = {0,500,110,500,110,450,110,200,110,170,40,450,110,200,110,170,40,500};
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.app_icon)
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(period))
                        .setContentText(period)
                        .setContentIntent(resultPendingIntent)
                        .setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL)
                        .setSound(alarmSound)
                        .setVibrate(pattern);

        mNotificationManager.notify(Integer.parseInt(id), mBuilder.build());
    }

    private void handleMessage(String msg){

        Log.d(TAG, "message received handlemeassage") ;
        Log.d(TAG, "received message: " + msg) ;

        try {
            Log.d(TAG, "in Handle message") ;
            JSONObject jsonObject = new JSONObject(msg);
            String messageType = jsonObject.getString(getString(R.string.MESSAGE_TYPE)) ;
            Log.d(TAG,"message type: " + messageType) ;
            Log.d(TAG,"jsonstr: " + jsonObject.toString()) ;
            if (messageType.toUpperCase().equals( getString(R.string.NEWS_NOTIFICATION).toUpperCase() )){

                String newsContent = jsonObject.getString(getString(R.string.NEWS_BODY)) ;
                JSONObject jsonObject1 = new JSONObject(newsContent) ;

                String id = jsonObject1.getString(getString(R.string.ID)) ;
                String title = jsonObject1.getString(getString(R.string.TITLE)) ;
                String image = jsonObject1.getString(getString(R.string.NEWS_IMAGE)) ;
                String category = jsonObject1.getString(getString(R.string.CATEGORY)) ;
                String period = jsonObject1.getString(getString(R.string.PERIOD)) ;
                String news_details = jsonObject1.getString(getString(R.string.NEWS_DETAILS)) ;

                sendNotification(messageType, id, title, image, category, period, news_details);
            }
            else{
                Log.d(TAG, "Invalid Type");
            }

        }catch(JSONException e){
            Log.d("MyDebug","Error");
            e.printStackTrace();
        }
    }
}