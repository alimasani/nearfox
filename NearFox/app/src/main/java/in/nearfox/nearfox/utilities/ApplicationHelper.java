package in.nearfox.nearfox.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import in.nearfox.nearfox.DialogActivity;
import in.nearfox.nearfox.R;

/**
 * Created by Uss on 7/25/2015.
 */
public class ApplicationHelper {

    private Context context;

    public ApplicationHelper(Context context){
        this.context = context;
    }

//    public Dialog showMessageDialog(String msg) {
      public void showMessageDialog(String msg, int requestCode) {
        Intent intent = new Intent(context, DialogActivity.class);
        intent.putExtra("title", "NearFox");
        intent.putExtra("icon", android.R.drawable.ic_dialog_info);
        intent.putExtra("message", msg);
        intent.putExtra("positive", "OK");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(requestCode != -1)
            ((Activity)context).startActivityForResult(intent, requestCode);
        else
            context.startActivity(intent);
      //  return null;
    }

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng((location.getLatitude()),
                    (location.getLongitude()));
        } catch (Exception e) {
            p1 = new LatLng(19.0544718, 72.8475496);
        }
        return p1;
    }
}
