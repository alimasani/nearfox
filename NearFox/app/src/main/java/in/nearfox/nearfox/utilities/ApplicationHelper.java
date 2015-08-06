package in.nearfox.nearfox.utilities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import in.nearfox.nearfox.R;

/**
 * Created by Uss on 7/25/2015.
 */
public class ApplicationHelper {

    private Context context;

    public ApplicationHelper(Context context){
        this.context = context;
    }

    public Dialog showMessageDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogLayout = View.inflate(context, R.layout.layout_custom_dialog, null);
        builder.setView(dialogLayout);
        Dialog dialog = builder.create();
        ((TextView) dialogLayout.findViewById(R.id.dialogTitle)).setText("Information");
        ((TextView) dialogLayout.findViewById(R.id.dialogMessage)).setText(msg);
        dialogLayout.findViewById(R.id.dialogOK).setTag(dialog);

        dialogLayout.findViewById(R.id.dialogOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = (Dialog) view.getTag();
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;

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
