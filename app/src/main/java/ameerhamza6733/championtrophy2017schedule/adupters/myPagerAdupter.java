package ameerhamza6733.championtrophy2017schedule.adupters;

/**
 * Created by AmeerHamza on 1/18/2017.
 */

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ameerhamza6733.championtrophy2017schedule.R;
import ameerhamza6733.championtrophy2017schedule.fragments.FavrateVideos;
import ameerhamza6733.championtrophy2017schedule.fragments.RecylerviewFragment;
import ameerhamza6733.championtrophy2017schedule.fragments.ScroeChoiseFragment;
import ameerhamza6733.championtrophy2017schedule.fragments.liveScrFramgment;
import ameerhamza6733.championtrophy2017schedule.fragments.shaduleFragment;
import ameerhamza6733.championtrophy2017schedule.fragments.webViewFragment;

/**
 * Created by DELL 3542 on 7/21/2016.
 */
public  class myPagerAdupter extends FragmentStatePagerAdapter {
    public String[] getTabs() {
        return tabs;
    }

    private final String [] tabs;
    private Context c;

    public myPagerAdupter(FragmentManager fm , Context c) {

        super(fm);
        this.c=c;
        tabs = c.getResources().getStringArray(R.array.tabs);
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        return tabs[position];
//    }


    @Override
    public android.support.v4.app.Fragment getItem(int position) {

        android.support.v4.app.Fragment  fragment = new android.support.v4.app.Fragment();
        if(position ==0)
        {
            fragment=new ScroeChoiseFragment();
        }

        else if(position==1)
        {
            fragment = new shaduleFragment();

        }else if (position==2)
        {
            fragment=new RecylerviewFragment();
        }else if(position==3)
        {
            fragment=new FavrateVideos();
        }

        return fragment;
        //return shaduleFragment.newInstance(position + 1);
    }


    @Override
    public int getCount() {
        return 4;
    }




}