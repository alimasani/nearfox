package in.nearfox.nearfox.models;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

import in.nearfox.nearfox.fragments.AYNFragment1;
import in.nearfox.nearfox.fragments.EventFragment;
import in.nearfox.nearfox.fragments.NewsFragment;

/**
 * Created by dell on 9/7/15.
 */
public class NavigationAdapter extends FragmentStatePagerAdapter {

    String[] TITLES = {"News", "Events", "Ask"};
    FragmentManager fm;
    Map<Integer, String> mFragmentTags;
    int mScrollY;

    public NavigationAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
        mFragmentTags = new HashMap<>();
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new NewsFragment();
            case 1:
                return new EventFragment();
            case 2:
                return new AYNFragment1();
        }

        return null;
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    public Fragment getItemAt(int position) {
        String tag = mFragmentTags.get(position);
        if (tag == null)
            return null;
        return fm.findFragmentByTag(tag);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object object = super.instantiateItem(container, position);

        if (object instanceof Fragment) {
            // record the fragment tag here.
            Fragment f = (Fragment) object;
            String tag = f.getTag();
            mFragmentTags.put(position, tag);
        }
        return object;
    }

    public void setScrollY(int scrollY) {
        mScrollY = scrollY;
    }
}