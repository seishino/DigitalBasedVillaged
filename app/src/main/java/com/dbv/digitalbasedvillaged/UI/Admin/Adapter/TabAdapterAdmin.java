package com.dbv.digitalbasedvillaged.UI.Admin.Adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dbv.digitalbasedvillaged.UI.Admin.ListPendudukLayout;
import com.dbv.digitalbasedvillaged.UI.Admin.Profile;
import com.dbv.digitalbasedvillaged.UI.Admin.VerificationLayout;


public class TabAdapterAdmin extends FragmentStatePagerAdapter {
    int noOfTabs;
    public TabAdapterAdmin(FragmentManager fragmentManager, int noOfTabs)
    {
        super(fragmentManager);
        this.noOfTabs = noOfTabs;
    }
    @Override
    public Fragment getItem(int i) {
        switch(i)
        {
            case 0:
                return new VerificationLayout();
            case 1:
                return new Profile();
            case 2:
                return new ListPendudukLayout();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}
