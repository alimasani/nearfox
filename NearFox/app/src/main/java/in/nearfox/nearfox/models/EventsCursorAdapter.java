package in.nearfox.nearfox.models;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Point;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import in.nearfox.nearfox.R;
import in.nearfox.nearfox.utilities.DBHelper;
import in.nearfox.nearfox.utilities.GeneralHelper;


public class EventsCursorAdapter extends CursorAdapter {

    LayoutInflater mInflater;
    ImageLoader imageLoader;
    int title;
    int lat;
    int lon;
    int start_time;
    int end_time;
    int image_ur;
    int start_date;
    int categories;
    int end_date;
    int address_line1;
    int address_line2;
    int landmark;
    int event_details;
    int fees;
    int event_id;
    int contact_phone;
    int website;
    int day;
    int short_address;
    int locality;
    int fullstartdate;
    int all_day;
    int ttl;
    int deviceHeight;
    int deviceWidth;
    int desiredHeight;
    private String TAG = this.getClass().getName();

    public EventsCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, false);
        this.mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        imageLoader = ImageLoader.getInstance();

        title = cursor.getColumnIndex(DBHelper.TITLE);
        lat = cursor.getColumnIndex(DBHelper.LAT);
        lon = cursor.getColumnIndex(DBHelper.LON);
        start_time = cursor.getColumnIndex(DBHelper.START_TIME);
        end_time = cursor.getColumnIndex(DBHelper.END_TIME);
        image_ur = cursor.getColumnIndex(DBHelper.IMAGE_UR);
        start_date = cursor.getColumnIndex(DBHelper.START_DATE);
        categories = cursor.getColumnIndex(DBHelper.CATEGORIES);
        end_date = cursor.getColumnIndex(DBHelper.END_DATE);
        address_line1 = cursor.getColumnIndex(DBHelper.ADDRESS_LINE1);
        address_line2 = cursor.getColumnIndex(DBHelper.ADDRESS_LINE2);
        landmark = cursor.getColumnIndex(DBHelper.LANDMARK);
        event_details = cursor.getColumnIndex(DBHelper.EVENT_DETAILS);
        fees = cursor.getColumnIndex(DBHelper.FEES);
        event_id = cursor.getColumnIndex(DBHelper.EVENT_ID);
        contact_phone = cursor.getColumnIndex(DBHelper.CONTACT_PHONE);
        website = cursor.getColumnIndex(DBHelper.WEBSITE);
        day = cursor.getColumnIndex(DBHelper.DAY);
        short_address = cursor.getColumnIndex(DBHelper.SHORT_ADDRESS);
        locality = cursor.getColumnIndex(DBHelper.LOCALITY);
        fullstartdate = cursor.getColumnIndex(DBHelper.FULLSTARTDATE);
        all_day = cursor.getColumnIndex(DBHelper.ALL_DAY);
        ttl = cursor.getColumnIndex(DBHelper.TTL);

        Point point = new Point();
        ((Activity) context).getWindowManager().getDefaultDisplay().getSize(point);
        deviceWidth = point.x;
        deviceHeight = point.y;
        desiredHeight = (int) (0.45 * deviceHeight);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder();

        View convertView = mInflater.inflate(R.layout.important_events_lsit_row, null);

        viewHolder.title = (TextView) convertView.findViewById(R.id.title);
        viewHolder.image = (ImageView) convertView.findViewById(R.id.imageView);
        viewHolder.id = (TextView) convertView.findViewById(R.id.event_id);
        viewHolder.category = (TextView) convertView.findViewById(R.id.event_category);
        viewHolder.address = (TextView) convertView.findViewById(R.id.event_address);
        viewHolder.date = (TextView) convertView.findViewById(R.id.event_date);
        viewHolder.time = (TextView) convertView.findViewById(R.id.event_time);
        viewHolder.event_lat = (TextView) convertView.findViewById(R.id.event_lat);
        viewHolder.event_lon = (TextView) convertView.findViewById(R.id.event_lon);
        viewHolder.event_details = (TextView) convertView.findViewById(R.id.event_details);
        viewHolder.event_fees = (TextView) convertView.findViewById(R.id.event_fees);
        viewHolder.event_all_day = (TextView) convertView.findViewById(R.id.event_all_day);
        viewHolder.event_contact = (TextView) convertView.findViewById(R.id.event_contact);
        viewHolder.event_website = (TextView) convertView.findViewById(R.id.event_website);
        viewHolder.event_day = (TextView) convertView.findViewById(R.id.event_day);
        viewHolder.event_image_url = (TextView) convertView.findViewById(R.id.event_image_url);
        viewHolder.event_address_line1 = (TextView) convertView.findViewById(R.id.event_address_line1);
        viewHolder.event_address_line2 = (TextView) convertView.findViewById(R.id.event_address_line2);
        viewHolder.event_landmark = (TextView) convertView.findViewById(R.id.event_landmark);
        viewHolder.event_start_date = (TextView) convertView.findViewById(R.id.event_start_date);
        viewHolder.event_end_date = (TextView) convertView.findViewById(R.id.event_end_date);
        viewHolder.event_start_time = (TextView) convertView.findViewById(R.id.event_start_time);
        viewHolder.event_end_time = (TextView) convertView.findViewById(R.id.event_end_time);
        viewHolder.event_full_start_Date = (TextView) convertView.findViewById(R.id.event_full_start_date);


        convertView.setTag(viewHolder);
        return convertView;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.image.getLayoutParams();
        layoutParams.height = desiredHeight;
        holder.image.setLayoutParams(layoutParams);


        imageLoader.displayImage(cursor.getString(cursor.getColumnIndex(DBHelper.IMAGE_UR)),
                holder.image,
                new DisplayImageOptions
                        .Builder()
                        .showStubImage(R.drawable.default_event_image)
                        .showImageForEmptyUri(R.drawable.default_event_image)
                        .cacheOnDisc(true)
                        .cacheInMemory(true)
                        .showImageOnFail(R.drawable.default_event_image)
                        .build());

        holder.title.setText(cursor.getString(title));
        holder.id.setText(cursor.getString(event_id));

        holder.address.setText(cursor.getString(short_address));

        Pair p = GeneralHelper.getRealTime(cursor.getString(start_date), cursor.getString(start_time));

        holder.date.setText(p.first.toString());
        holder.category.setText(cursor.getString(categories));
        holder.time.setText(p.second.toString());
        holder.event_lat.setText(cursor.getString(lat));
        holder.event_lon.setText(cursor.getString(lon));
        holder.event_details.setText(cursor.getString(event_details));
        holder.event_fees.setText(cursor.getString(fees));
        if (cursor.getInt(all_day) > 0)
            holder.event_all_day.setText("TRUE");
        else
            holder.event_all_day.setText("FALSE");
        holder.event_contact.setText(cursor.getString(contact_phone));
        holder.event_website.setText(cursor.getString(website));
        holder.event_day.setText(cursor.getString(day));
//        Log.d(TAG, "adapter image: " +
        holder.event_image_url.setText(cursor.getString(image_ur));
        String address_line_1 = cursor.getString(address_line1);
        holder.event_address_line1.setText(address_line_1);
        holder.event_address_line2.setText(cursor.getString(address_line2));
        holder.event_landmark.setText(cursor.getString(landmark));
//        holder.event_start_date.setText(
        holder.event_start_date.setText(cursor.getString(start_date));
        holder.event_end_date.setText(cursor.getString(end_date));
        holder.event_start_time.setText(cursor.getString(start_time));
        holder.event_end_time.setText(cursor.getString(end_time));
        holder.event_full_start_Date.setText(cursor.getString(fullstartdate));

        Log.d(TAG, "id: " + cursor.getString(event_id) + "   startTime: " + cursor.getString(start_time));
    }

/*    public void stopImageLoading(){
        imageLoader.pause();
    }

    public void startImageLoading(){
        imageLoader.resume();
    }*/

    class ViewHolder {
        public TextView title;
        public ImageView image;
        public TextView id;
        public TextView category;
        public TextView address;
        public TextView date;
        public TextView time;
        public TextView event_lat;
        public TextView event_lon;
        public TextView event_details;
        public TextView event_fees;
        public TextView event_all_day;
        public TextView event_contact;
        public TextView event_website;
        public TextView event_day;
        public TextView event_image_url;
        public TextView event_address_line1;
        public TextView event_address_line2;
        public TextView event_landmark;
        public TextView event_start_date;
        public TextView event_end_date;
        public TextView event_start_time;
        public TextView event_end_time;
        public TextView event_full_start_Date;
    }
}