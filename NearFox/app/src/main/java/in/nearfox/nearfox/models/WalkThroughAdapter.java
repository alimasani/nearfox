package in.nearfox.nearfox.models;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.nearfox.nearfox.R;
import in.nearfox.nearfox.WalkThroughActivity;

/**
 * Created by R3 on 7/25/2015.
 */
public class WalkThroughAdapter extends PagerAdapter {

    WalkThroughActivity mContext;
    LayoutInflater mLayoutInflater;
    int[] mResources;
    String [] msg;
    private TextView textView;


    public WalkThroughAdapter(WalkThroughActivity context, int[] mResources, String [] msg) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mResources = mResources;
        this.msg=msg;
    }


    @Override
    public int getCount() {
        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
         textView=(TextView) itemView.findViewById(R.id.txtMsg);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        imageView.setImageResource(mResources[position]);
        showButton(position, itemView);
        container.addView(itemView);

        itemView.setTag(position);
        return itemView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    private void showButton(int position, View rootView) {

              textView.setText(msg[position]);


    }




}
