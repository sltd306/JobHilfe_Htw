package com.example.luongtiendat.jobhilfe;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Luong Tien Dat on 07.01.2018.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:
                NotifiticationFragment requestsFragment = new NotifiticationFragment();
                return requestsFragment;

            case 1:
                ChatFragment chatsFragment = new ChatFragment();
                return  chatsFragment;

            case 2:
                KontaktFragment friendsFragment = new KontaktFragment();
                return friendsFragment;

            default:
                return  null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position){

        switch (position) {
            case 0:
                return "NOTIFICATION";

            case 1:
                return "CHATS";

            case 2:
                return "KONTAKT";

            default:
                return null;
        }

    }
}
