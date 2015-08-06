package in.nearfox.nearfox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import in.nearfox.nearfox.utilities.ApiServices;
import in.nearfox.nearfox.utilities.DBManager;


public class SingleNews extends Activity {

    public static final String TITLE = "title";
    public static final String CATEGORY = "category";
    public static final String IMAGE_SRC = "image_src";
    public static final String ID = "id";
    public static final String DESCRIPTION = "description";
    public static final String PERIOD = "period";
    public static final String SOURCE = "source";

    public final String TAG = this.getClass().getName();
    public Context context;

    private String id;
    private String title;
    private String imageSrc;
    private String period;
    private String source;
    private String details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circular_loader_layout);
        context = this;
        Intent intent = getIntent();
        this.id = intent.getStringExtra(ID);
        this.title = intent.getStringExtra(TITLE);
        this.imageSrc = intent.getStringExtra(IMAGE_SRC);
        this.details = intent.getStringExtra(DESCRIPTION);
        this.period = intent.getStringExtra(PERIOD);
        this.source = intent.getStringExtra(SOURCE);



        populateSingleNews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    private void populateSingleNews() {

        setContentView(R.layout.activity_single_news);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView periodSource = (TextView) findViewById(R.id.single_news_period_source);
        ImageView imageView = (ImageView) findViewById(R.id.single_news_image_view);
        TextView titleView = (TextView) findViewById(R.id.single_newst_title);
        TextView descriptionView = (TextView) findViewById(R.id.single_news_description);

        toolbar.setTitleTextColor(Color.argb(255, 255, 255, 255));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_right);
        periodSource.setText(period + " / " + source);
        titleView.setText(title);
        descriptionView.setText(details);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(imageSrc, imageView);

        toolbar.setBackgroundColor(Color.parseColor("#00000000"));

        findViewById(R.id.share_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, SingleNews.this.title + "\n\n" + details);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "NearFox NEWS");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });


    }


}