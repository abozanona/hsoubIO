package com.rond.hsoub;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rond.hsoub.API.JsonLinks;

import java.util.ArrayList;
import java.util.List;


public class MainFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    public MainFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());

        Bundle bundle;
        PostsListFragment frag;

        bundle= new Bundle();
        bundle.putString("link", JsonLinks.mostPopular);
        frag= new PostsListFragment();
        frag.setArguments(bundle);
        adapter.addFragment(frag, getResources().getString(R.string.most_popular));

        bundle= new Bundle();
        bundle.putString("link", JsonLinks.recent);
        frag= new PostsListFragment();
        frag.setArguments(bundle);
        adapter.addFragment(frag, getResources().getString(R.string.recent));

        bundle= new Bundle();
        bundle.putString("link", JsonLinks.topDay);
        frag= new PostsListFragment();
        frag.setArguments(bundle);
        adapter.addFragment(frag, getResources().getString(R.string.top_day));

        bundle= new Bundle();
        bundle.putString("link", JsonLinks.topWeek);
        frag= new PostsListFragment();
        frag.setArguments(bundle);
        adapter.addFragment(frag, getResources().getString(R.string.top_week));

        bundle= new Bundle();
        bundle.putString("link", JsonLinks.topMonth);
        frag= new PostsListFragment();
        frag.setArguments(bundle);
        adapter.addFragment(frag, getResources().getString(R.string.top_month));

        bundle= new Bundle();
        bundle.putString("link", JsonLinks.topYear);
        frag= new PostsListFragment();
        frag.setArguments(bundle);
        adapter.addFragment(frag, getResources().getString(R.string.top_year));

        bundle= new Bundle();
        bundle.putString("link", JsonLinks.discover);
        frag= new PostsListFragment();
        frag.setArguments(bundle);
        adapter.addFragment(frag, getResources().getString(R.string.discover));


        viewPager.setAdapter(adapter);

        return rootView;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
