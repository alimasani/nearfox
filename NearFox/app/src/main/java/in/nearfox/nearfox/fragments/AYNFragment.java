package in.nearfox.nearfox.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import in.nearfox.nearfox.R;

public class AYNFragment extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.single_answer);

        ImageLoader imageLoader = ImageLoader.getInstance();

        ImageView imageView = (ImageView)findViewById(R.id.ayn_comment_user_image);

        imageLoader.displayImage("drawable://" + R.drawable.default_person,
                imageView,
                new DisplayImageOptions
                        .Builder()
                        .displayer(new RoundedBitmapDisplayer(50))
                        .build());
    }
}