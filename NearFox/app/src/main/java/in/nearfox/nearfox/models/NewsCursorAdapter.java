package in.nearfox.nearfox.models;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import in.nearfox.nearfox.R;
import in.nearfox.nearfox.utilities.DBHelper;
import in.nearfox.nearfox.utilities.GeneralHelper;

/**
 * Created by dell on 10/7/15.
 */
public class NewsCursorAdapter extends CursorAdapter {

    int news_id;
    int title;
    int category;
    int image_url;
    int period;
    int news_excerpt;
    int source_name;
    int lat;
    int lon;
    int locality;
    int author;
    int source_image_url;
    int website;
    int localDBId;

    LayoutInflater mInflater;
    Context mContext;
    ImageLoader mImageLoader;

    public NewsCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, false);
        this.mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        this.mContext = context;
        this.mImageLoader = ImageLoader.getInstance();

        this.news_id = cursor.getColumnIndex(DBHelper.NEWS_ID);
        this.title = cursor.getColumnIndex(DBHelper.TITLE);
        this.category = cursor.getColumnIndex(DBHelper.CATEGORY);
        this.image_url = cursor.getColumnIndex(DBHelper.IMAGE_URL);
        this.period = cursor.getColumnIndex(DBHelper.PERIOD);
        this.news_excerpt = cursor.getColumnIndex(DBHelper.NEWS_EXCERPT);
        this.source_name = cursor.getColumnIndex(DBHelper.SOURCE_NAME);
        this.lat = cursor.getColumnIndex(DBHelper.LAT);
        this.lon = cursor.getColumnIndex(DBHelper.LON);
        this.locality = cursor.getColumnIndex(DBHelper.LOCALITY);
        this.author = cursor.getColumnIndex(DBHelper.AUTHOR);
        this.source_image_url = cursor.getColumnIndex(DBHelper.SOURCE_IMAGE_URL);
        this.website = cursor.getColumnIndex(DBHelper.WEBSITE);
        this.localDBId = cursor.getColumnIndex(DBHelper.KEY_ID);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        ViewHolderNews viewHolderNews = new ViewHolderNews();

        View convertView = mInflater.inflate(R.layout.news_list_row_new, null);


        viewHolderNews.title = (TextView) convertView.findViewById(R.id.news_title);
        viewHolderNews.image = (ImageView) convertView.findViewById(R.id.news_imageView);
        viewHolderNews.id = (TextView) convertView.findViewById(R.id.news_id);
        viewHolderNews.category = (TextView) convertView.findViewById(R.id.news_category);
        viewHolderNews.image_src = (TextView) convertView.findViewById(R.id.news_image_src);
//            viewHolderNews.sourceImage = (ImageView)convertView.findViewById(R.id.source_image) ;
        viewHolderNews.newsPeriod = (TextView) convertView.findViewById(R.id.news_period);
        viewHolderNews.newsDescription = (TextView) convertView.findViewById(R.id.news_description);
        viewHolderNews.source = (TextView) convertView.findViewById(R.id.news_source);
        viewHolderNews.localDBId = (TextView) convertView.findViewById(R.id.news_local_db_id);

        convertView.setTag(viewHolderNews);
        return convertView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolderNews viewHolderNews = (ViewHolderNews) view.getTag();

        mImageLoader.displayImage(cursor.getString(image_url),
                viewHolderNews.image,
                new DisplayImageOptions
                        .Builder()
                        .cacheOnDisc(true)
                        .cacheInMemory(true)
                        .showStubImage(R.drawable.default_image_3)
                        .showImageForEmptyUri(R.drawable.default_image_3)
                        .showImageOnFail(R.drawable.default_image_3)
                        .build()
        );

        viewHolderNews.title.setText(cursor.getString(title));
        viewHolderNews.id.setText(cursor.getString(news_id));
        viewHolderNews.category.setText(cursor.getString(category));
        viewHolderNews.image_src.setText(cursor.getString(image_url));
//        Log.d(TAG, "news image: " + row_pos.getImage()) ;
        viewHolderNews.newsPeriod.setText( GeneralHelper.getBeautifulDate(cursor.getString(period)) );
        viewHolderNews.newsDescription.setText(cursor.getString(news_excerpt));
        viewHolderNews.source.setText(cursor.getString(source_name));
        viewHolderNews.localDBId.setText(cursor.getString(localDBId));

    }

    class ViewHolderNews {
        public TextView title;
        public ImageView image;
        public TextView id;
        public TextView category;
        public TextView image_src;
        //    public ImageView sourceImage ;
        public TextView newsPeriod;
        public TextView newsDescription;
        public TextView source;
        public TextView localDBId;
    }
}