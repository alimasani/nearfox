package in.nearfox.nearfox;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;


public class NoActionActivity extends Activity {

    public static final String LAYOUT_RESOURCE_ID = "layout_resource_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int layoutResourceId = intent.getIntExtra(LAYOUT_RESOURCE_ID, 1);
        setContentView(layoutResourceId);

        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (layoutResourceId == R.layout.activity_no_action_about_us)
            toolbar.setTitle("About Us");
        else
            toolbar.setTitle("Terms of Use");
        toolbar.setTitleTextColor(Color.argb(255, 0, 0, 0));
//        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_36dp);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_right);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setBackgroundColor(Color.parseColor("#00000000"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}