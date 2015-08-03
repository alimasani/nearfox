package in.nearfox.nearfox.Drawer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import in.nearfox.nearfox.fragments.SubmitAYNFragment;
import in.nearfox.nearfox.fragments.SubmitEventFragment;
import in.nearfox.nearfox.fragments.SubmitNewsFragment;

/**
 * Created by dell on 9/7/15.
 */
public class SubmitNavigationAdapter extends FragmentStatePagerAdapter {

    String[] TITLES = {"News", "Events", "Ask"} ;
    FragmentManager fm ;

    public SubmitNavigationAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm ;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0: return new SubmitNewsFragment() ;
            case 1: return new SubmitEventFragment();
            case 2: return new SubmitAYNFragment() ;
        }

        return null;
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position){
        return TITLES[position] ;
    }
}