package in.nearfox.nearfox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Window;

import in.nearfox.nearfox.models.WalkThroughAdapter;
import in.nearfox.nearfox.utilities.Preference;

/**
 * Created by R3 on 7/25/2015.
 */
public class WalkThroughActivity extends Activity implements WalkThroughAdapter.WalkThroughCallBack {
    private WalkThroughAdapter walkThroughAdapter;
    private ViewPager mViewPager;

    int[] mResources = {
            R.drawable.walk_through_image_1,
            R.drawable.walk_through_2,
            R.drawable.walk_through_image_3,

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Preference preference = new Preference(this);
        if(preference.isWalkThroughComplete()){
            if(preference.isChooseLocatoinComplete()) {
                finish();
                startActivity(new Intent(this, MainActivity.class));
            } else {
                finish();
                startActivity(new Intent(this, ChooseLocationActivity.class));
            }
            return;
        }
        preference.setCompleteWalkThrough();
        setContentView(R.layout.walkthrough);

        walkThroughAdapter = new WalkThroughAdapter(this, mResources);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(walkThroughAdapter);
        walkThroughAdapter.setWalkThroughCallBack(this);
    }

    @Override
    public void changedPosition(int position) {
        if (position < mResources.length)
            mViewPager.setCurrentItem(position);
        else if(position == mResources.length) {
            startActivity(new Intent(this, ChooseLocationActivity.class));
            finish();

        }
    }
}
