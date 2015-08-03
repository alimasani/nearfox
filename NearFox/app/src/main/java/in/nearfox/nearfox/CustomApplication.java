package in.nearfox.nearfox;

import android.app.Application;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * Created by dell on 10/7/15.
 */
public class CustomApplication extends Application{

    @Override
    public void onCreate(){

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheOnDisc(true).cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY).resetViewBeforeLoading(true)
                .displayer(new FadeInBitmapDisplayer(0)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).defaultDisplayImageOptions(defaultOptions).memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();


        ImageLoader.getInstance().init(config);
    }
}
