package in.nearfox.nearfox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import in.nearfox.nearfox.models.WalkThroughAdapter;
import in.nearfox.nearfox.utilities.Preference;

/**
 * Created by R3 on 7/25/2015.
 */
public class WalkThroughActivity extends Activity  {
    private WalkThroughAdapter walkThroughAdapter;
    private ViewPager mViewPager;
    private ImageView dots;
    int[] mResources = {
            R.drawable.walk_through_image_1,
            R.drawable.walk_through_2,
            R.drawable.walk_through_image_3,

    };

    int[] mdots = {
            R.drawable.dot1,
            R.drawable.dot2,
            R.drawable.dot3,

    };


    String [] msg = {
            "Discover everyday stories\nand quirky happenings\naround you",
            "Have a Question! Just ask\nYour Neighbourhood :-)",
            "Your Locality, Your Word\nSubmit News and Events",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Preference preference = new Preference(this);
        preference.setCompleteWalkThrough();
        setContentView(R.layout.walkthrough);

        walkThroughAdapter = new WalkThroughAdapter(this, mResources, msg);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(walkThroughAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                dots = (ImageView) findViewById(R.id.imgDots);
                dots.setImageResource(mdots[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        findViewById(R.id.btnGetStarted).setOnClickListener(getStarted);
    }

    View.OnClickListener getStarted = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(WalkThroughActivity.this, ChooseLocationActivity.class));
            finish();
        }
    };
}
