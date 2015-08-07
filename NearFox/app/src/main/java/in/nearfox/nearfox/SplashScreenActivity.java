package in.nearfox.nearfox;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;

import in.nearfox.nearfox.models.WalkThroughAdapter;
import in.nearfox.nearfox.utilities.Preference;

/**
 * Created by Uss on 8/5/2015.
 */
public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_screen);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Preference preference = new Preference(SplashScreenActivity.this);
                if(preference.isWalkThroughComplete()){
                    if(preference.isChooseLocatoinComplete()) {
                        finish();
                        startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    } else {
                        finish();
                        startActivity(new Intent(SplashScreenActivity.this, ChooseLocationActivity.class));
                    }
                    return;
                } else {
                    finish();
                    startActivity(new Intent(SplashScreenActivity.this, WalkThroughActivity.class));
                }
            }
        }, 2000);
    }
}
