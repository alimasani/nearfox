package in.nearfox.nearfox.utilities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

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
        builder.setTitle("Information");
        builder.setMessage(msg);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
        return dialog;
    }
}
