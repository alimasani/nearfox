package in.nearfox.nearfox.models;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import in.nearfox.nearfox.R;

/**
 * Created by R3 on 7/25/2015.
 */
public class WalkThroughAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    int[] mResources;
    private WalkThroughCallBack walkThroughCallBack = null;

    public WalkThroughAdapter(Context context, int[] mResources) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mResources = mResources;
    }

    public void setWalkThroughCallBack(WalkThroughCallBack callBack) {
        walkThroughCallBack = callBack;
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

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        imageView.setImageResource(mResources[position]);
        showButton(position, itemView);
        container.addView(itemView);

        itemView.setTag(position);

        itemView.findViewById(R.id.imgWTNext3).setTag(position);
        itemView.findViewById(R.id.imgWTNext2).setTag(position);
        itemView.findViewById(R.id.imgWTNext1).setTag(position);
        itemView.findViewById(R.id.imgWTBack).setTag(position);

        itemView.findViewById(R.id.imgWTNext3).setOnClickListener(onClickListener);
        itemView.findViewById(R.id.imgWTNext2).setOnClickListener(onClickListener);
        itemView.findViewById(R.id.imgWTNext1).setOnClickListener(onClickListener);
        itemView.findViewById(R.id.imgWTBack).setOnClickListener(onClickListener);
        return itemView;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = (int) view.getTag();
            if (view.getId() == R.id.imgWTNext1 || view.getId() == R.id.imgWTNext2 || view.getId() == R.id.imgWTNext3) {
                position++;
            } else {
                position--;
            }
            if (walkThroughCallBack != null)
                walkThroughCallBack.changedPosition(position);
        }
    };

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    private void showButton(int position, View rootView) {
        switch (position) {
            case 0:
                rootView.findViewById(R.id.imgWTBack).setVisibility(View.GONE);
                rootView.findViewById(R.id.imgWTNext2).setVisibility(View.GONE);
                rootView.findViewById(R.id.imgWTNext3).setVisibility(View.GONE);
                rootView.findViewById(R.id.imgWTNext1).setVisibility(View.VISIBLE);
                break;
            case 1:
                rootView.findViewById(R.id.imgWTNext3).setVisibility(View.GONE);
                rootView.findViewById(R.id.imgWTNext1).setVisibility(View.GONE);
                rootView.findViewById(R.id.imgWTBack).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.imgWTNext2).setVisibility(View.VISIBLE);
                break;
            case 2:
                rootView.findViewById(R.id.imgWTNext1).setVisibility(View.GONE);
                rootView.findViewById(R.id.imgWTNext2).setVisibility(View.GONE);
                rootView.findViewById(R.id.imgWTBack).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.imgWTNext3).setVisibility(View.VISIBLE);
                break;
        }
    }


    public interface WalkThroughCallBack {
        public void changedPosition(int position);
    }

}
