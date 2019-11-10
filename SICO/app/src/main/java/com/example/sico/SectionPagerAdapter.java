package com.example.sico;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.sico.UI.fragments.HistoryFragment;
import com.example.sico.UI.fragments.ShoppingFragment;

/**
 * Created by Diaa on 1/7/2018.
 */

public class SectionPagerAdapter extends FragmentPagerAdapter {

    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

       switch (position){
           case 0:
               ShoppingFragment chatFrgent=new ShoppingFragment();
               return chatFrgent;
           case 1:
           HistoryFragment requestFragment=new HistoryFragment();
           return requestFragment;

           default:

               ShoppingFragment chatFrgents=new ShoppingFragment();
               return chatFrgents;
    }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle (int position){
        switch (position) {
            case 0:
                return " Shopping";
            case 1:
                return "History";

        }
        return null;
    }

}
