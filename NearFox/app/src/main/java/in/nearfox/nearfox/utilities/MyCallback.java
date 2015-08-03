package in.nearfox.nearfox.utilities;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by dell on 10/7/15.
 */
public class MyCallback {

    public interface CallAdapter {
        public void callAdapter();
    }

    public interface PopulateNewsInAdapter {
        public void callAdapter();
    }

    public interface LocationListener {
        public LatLng getHomeLocation();
        public LatLng getCurrentLocation();
    }
}