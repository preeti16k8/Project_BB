package com.kre8tives.bareboneneww.Fragments;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kre8tives.bareboneneww.Adapter.ViewPagerAdapter;
import com.kre8tives.bareboneneww.R;
public class ItemSettingsFragment extends Fragment {

    private static TabLayout tabLayout;
    private static ViewPager viewPager;
    private static ViewPagerAdapter adapter;
    public ItemSettingsFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_settings, container, false);
        /*Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");*/
        getActivity().setTitle("Menu");
        getIDs(view);
        setEvents();
        addPage();

        return view;
    }


    private void getIDs(View view)
    {
        viewPager = (ViewPager) view.findViewById(R.id.item_viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.item_tab_layout);
        adapter = new ViewPagerAdapter(getChildFragmentManager(), getActivity(), viewPager, tabLayout);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);

    }

    int selectedTabPosition;

    private void setEvents() {
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                CharSequence titles[] = {"Liquor","Food", "Mixer"};
                viewPager.setCurrentItem(tab.getPosition());
                selectedTabPosition = viewPager.getCurrentItem();
                Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
                toolbar.setTitle(titles[tab.getPosition()]);
                Log.d("Selected", "Selected " + tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
                Log.d("Unselected", "Unselected " + tab.getPosition());
            }
        });
    }
    public static void addPage() {
        adapter.addFrag(new CategoryFragment(), "Liquor");
        adapter.addFrag(new FoodCategoryFragment(), "Food");
        adapter.addFrag(new NewMixerFragment(),"Mixer");

        adapter.notifyDataSetChanged();
        if (adapter.getCount() > 0) tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onResume() {
        super.onResume();
    }




}