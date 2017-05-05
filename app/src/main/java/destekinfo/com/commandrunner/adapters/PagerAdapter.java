package destekinfo.com.commandrunner.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import destekinfo.com.commandrunner.fragment.DeployFragment;
import destekinfo.com.commandrunner.fragment.ServiceFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ServiceFragment tab1 = new ServiceFragment();
                return tab1;
            case 1:
                DeployFragment tab2 = new DeployFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}