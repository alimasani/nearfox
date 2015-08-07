package in.nearfox.nearfox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import in.nearfox.nearfox.R;

/**
 * Created by dhaval on 5/4/2015.
 */
public class DialogActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_custom_dialog);


        TextView title = (TextView) findViewById(R.id.dialogTitle);
        title.setText(getIntent().getStringExtra("title"));



        TextView message = (TextView) findViewById(R.id.dialogMessage);
        message.setText(getIntent().getStringExtra("message"));

        TextView positive = (TextView) findViewById(R.id.dialogOK);

        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity(true);
            }
        });


    }

    private void finishActivity(boolean clickType) {
        Intent intent = new Intent();
        intent.putExtra("userClicked", clickType);
        setResult(RESULT_OK, intent);
        finish();
    }
}
